package com.github.ankurpathak.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.*;
import com.github.ankurpathak.api.config.RabbitConfig;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.MessageContext;
import com.github.ankurpathak.api.domain.updater.IUpdateDomain;
import com.github.ankurpathak.api.event.DomainCreatedEvent;
import com.github.ankurpathak.api.event.PaginatedResultsRetrievedEvent;
import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controller.dto.PageDto;
import com.github.ankurpathak.api.rest.controller.dto.converter.IToDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.security.dto.DomainContextHolder;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import com.github.ankurpathak.api.service.*;
import com.github.ankurpathak.api.service.callback.*;
import com.github.ankurpathak.api.service.impl.util.ControllerUtil;
import com.github.ankurpathak.api.service.impl.util.DuplicateKeyExceptionProcessor;
import com.github.ankurpathak.api.service.impl.util.PagingUtil;
import com.github.ankurpathak.api.util.LogUtil;
import com.github.ankurpathak.api.util.OpenCsvBeanReader;
import com.github.ankurpathak.api.util.WebUtil;
import com.google.common.collect.Maps;
import com.mongodb.bulk.BulkWriteResult;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RestControllerService implements IRestControllerService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final IRestControllerResponseService restControllerResponseService;
    private final ObjectMapper objectMapper;
    private final LocalValidatorFactoryBean validator;
    private final IFileService fileService;
    private final ITaskService taskService;
    private final IMessageSenderService messageSenderService;

    @Override
    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    @Override
    public IRestControllerResponseService getRestControllerResponseService() {
        return restControllerResponseService;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public LocalValidatorFactoryBean getValidator() {
        return validator;
    }

    public RestControllerService(ApplicationEventPublisher applicationEventPublisher, IRestControllerResponseService restControllerResponseService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator, IFileService fileService, ITaskService taskService, IMessageSenderService messageSenderService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.restControllerResponseService = restControllerResponseService;
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.fileService = fileService;
        this.taskService = taskService;
        this.messageSenderService = messageSenderService;
    }


    @Override
    public  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    byId(ID id, Class<T> type, IDomainService<T, ID> domainService) {
        return restControllerResponseService.processObject(domainService.findById(id).orElse(null), type, null, String.valueOf(id));
    }

    @Override
    public  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    all(IDomainService<T, ID> domainService) {
        return ResponseEntity.ok().body(domainService.findAll());
    }

    @Override
    @Transactional
    public  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    createMany(IDomainService<T, ID> domainService, DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate) {
        try {
            BulkWriteResult res = tryCreateMany(domainService, dtoList, type, result, request, converter, preCreate, postCreate);
            return restControllerResponseService.processSuccess(HttpStatus.CREATED, Map.of(Params.COUNT, res.getInsertedCount()));
        } catch (DuplicateKeyException ex) {
            catchCreateMany(dtoList, ex, result, request);
            throw ex;
        }
    }

    private   <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> void
    catchCreateMany(DomainDtoList<T, ID, TDto> dtoList, DuplicateKeyException ex, BindingResult result, HttpServletRequest request) {
        DuplicateKeyExceptionProcessor.processDuplicateKeyException(ex, dtoList.getDto().getClass()).ifPresent(e -> this.restControllerResponseService.processValidationForFound(e));
    }

    private  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> BulkWriteResult
    tryCreateMany(IDomainService<T, ID> domainService, DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate) {
        restControllerResponseService.processValidation(result);
        preCreate.doPreCreateMany(this, dtoList);
        List<T> ts = dtoList.getDtos().stream().map(dto -> dto.toDomain(converter)).collect(Collectors.toList());
        BulkWriteResult writeResult = domainService.bulkInsertMany(type, ts);
        postCreate.doPostCreateMany(this, dtoList, writeResult);
        //applicationEventPublisher.publishEvent(new DomainCreatedEvent<>(t, response));
        return writeResult;
    }

    private  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> T
    tryCreateOne(IDomainService<T, ID> domainService, TDto dto, BindingResult result, HttpServletResponse response, IToDomain<T, ID, TDto> converter, IPreCreateOne<T, ID, TDto> preCreate, IPostCreateOne<T, ID, TDto> postCreate) {
        restControllerResponseService.processValidation(result);
        preCreate.doPreCreateOne(this, dto);
        T t = domainService.create(dto.toDomain(converter));
        postCreate.doPostCreateOne(this, t, dto);
        applicationEventPublisher.publishEvent(new DomainCreatedEvent<>(t, response));
        return t;
    }

    @Override
    @Transactional
    public   <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    createOne(IDomainService<T, ID> domainService, TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, IToDomain<T, ID, TDto> converter, IPreCreateOne<T, ID, TDto> preCreate, IPostCreateOne<T, ID, TDto> postCreate) {
        try {
            T t = tryCreateOne(domainService, dto, result, response, converter, preCreate, postCreate);
            return restControllerResponseService.processSuccess(HttpStatus.CREATED, Map.of(Params.ID, t.getId()));
        } catch (DuplicateKeyException ex) {
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }

    private <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> void
    catchCreateOne(TDto dto, DuplicateKeyException ex, BindingResult result, HttpServletRequest request) {
        Optional<FoundException> res = DuplicateKeyExceptionProcessor.processDuplicateKeyException(ex, dto.getClass());
        res.ifPresent(e -> ControllerUtil.processValidationForFound(e));
    }

    @Override
    @Transactional
    public  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    delete(IDomainService<T, ID> domainService, ID id) {
        Optional<T> domain = domainService.findById(id);
        if (domain.isPresent()) {
            domainService.delete(domain.get());
            return ControllerUtil.processSuccessNoContent();
        } else {
            throw new NotFoundException(String.valueOf(id), Params.ID, domainService.domainName(), ApiCode.NOT_FOUND);
        }
    }


    private  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    update(IDomainService<T, ID> domainService, TDto dto, T t, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate, BindingResult result) {
        this.restControllerResponseService.processValidation(result);
        if (t != null) {
            try {
                preUpdate.doPreCreateOne(this, dto);
                T newT = domainService.update(dto.updateDomain(t, updater));
                postUpdate.doPostUpdateOne(this, newT, dto);
                return this.restControllerResponseService.processSuccessOk();
            } catch (DuplicateKeyException ex) {
                DuplicateKeyExceptionProcessor.processDuplicateKeyException(ex, dto.getClass()).ifPresent(e -> this.restControllerResponseService.processValidationForFound(e));
                throw ex;
            }
        } else {
            throw new NotFoundException(String.valueOf(id), Params.ID, dto.domainName(), ApiCode.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    update(IDomainService<T, ID> domainService, TDto dto, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate) {
        Optional<T> domain = domainService.findById(id);
        return this.update(domainService, dto, domain.orElse(null), id, updater, request, preUpdate, postUpdate, result);
    }

    @Override
    @Transactional
    public <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    update(IDomainService<T, ID> domainService, TDto dto, T t, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate) {
        return this.update(domainService, dto, t, null, updater, request, preUpdate, postUpdate, result);
    }

    @Override
    public <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> List<T>
    searchByField(IDomainService<T, ID> domainService, String field, String value, Pageable pageable, Class<T> type, HttpServletResponse response) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        String parsedValue = PagingUtil.parseFieldValue(value);
        Page<T> page = domainService.findByField(field, parsedValue, pageable, type);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return page.getContent();
    }

    @Override
    public <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> List<String>
    listField(IDomainService<T, ID> domainService, String field, String value, Pageable pageable, Class<T> type) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        String parsedValue = PagingUtil.parseFieldValue(value);
        Page<String> page = domainService.listField(field, parsedValue, pageable, type);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        return page.getContent();
    }

    @Override
    public <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    search(IDomainService<T, ID> domainService, String rsql, Pageable pageable, Class<T> type, HttpServletResponse response) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        Criteria criteria = PagingUtil.parseRSQL(rsql, type);
        Page<T> page = domainService.findByCriteriaPaginated(criteria, pageable, type);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        this.applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return this.restControllerResponseService.processSuccessOk(Map.of("data", PageDto.getInstance(page)));
    }

    @Override
    public <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    paginated(IDomainService<T, ID> domainService, Pageable pageable, HttpServletResponse response, Class<T> type) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        Page<T> page = domainService.findAllPaginated(pageable, type);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        this.applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return this.restControllerResponseService.processSuccessOk(Map.of("data", PageDto.getInstance(page)));
    }

    @Override
    public <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>, S extends T> ResponseEntity<?>
    paginated(IDomainService<T, ID> domainService, Pageable pageable, HttpServletResponse response, Class<S> type, String view) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        Page<S> page = domainService.findAllPaginated(pageable, type, view);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        this.applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return this.restControllerResponseService.processSuccessOk(Map.of("data", PageDto.getInstance(page)));

    }

    private  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>>
    ResponseEntity<?> patch(IDomainService<T, ID> domainService, T t, ID id, JsonNode patch, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints) {
        if (t != null) {
            try {
                TDto dto = t.toDto(converter);
                id = t.getId();
                JsonNode source = objectMapper.valueToTree(dto);
                EnumSet<CompatibilityFlags> flags = CompatibilityFlags.defaults();
                JsonNode target = JsonPatch.apply(patch, source, flags);
                EnumSet<DiffFlags> diffFlags = DiffFlags.dontNormalizeOpIntoMoveAndCopy();
                JsonNode diffPatch = JsonDiff.asJson(source, target, diffFlags);
                JsonPatch.applyInPlace(diffPatch, source);
                TDto dtoDiff = this.objectMapper.treeToValue(source, dtoType);
                BindException bindResult = new BindException(dtoDiff, dtoDiff.getClass().getSimpleName());
                this.validator.validate(dtoDiff, bindResult, (Object[]) hints);
                this.restControllerResponseService.processValidation(bindResult);
                T updatedT = dtoDiff.updateDomain(t, updater);
                domainService.update(updatedT);
                return this.restControllerResponseService.processSuccessOk();
            } catch (JsonProcessingException | InvalidJsonPatchException e) {
                throw new InvalidException(ApiCode.INVALID_PATCH, Params.PATCH, Params.JSON.toUpperCase());
            }
        } else {
            throw new NotFoundException(String.valueOf(id), Params.ID, domainService.domainName(), ApiCode.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public   <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    patch(IDomainService<T, ID> domainService, JsonNode patch, ID id, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints) {
        Optional<T> t = domainService.findById(id);
        return this.patch(domainService, t.orElse(null), id, patch, converter, updater, dtoType, hints);
    }

    @Override
    @Transactional
    public   <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    patch(IDomainService<T, ID> domainService, JsonNode patch, T t, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints) {
        return this.patch(domainService, t, null, patch, converter, updater, dtoType, hints);
    }

    private <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> DomainDtoList<T, ID, TDto>
    csvToDomainDtoList(MultipartFile csv, Class<TDto> dtoType, Logger log) throws CsvException {
        List<TDto> dtos = Collections.emptyList();
        try (OpenCsvBeanReader<TDto> reader = new OpenCsvBeanReader<>(csv.getResource(), dtoType)) {
            dtos = reader.readLines();
        } catch (IOException ex) {
            LogUtil.logStackTrace(log, ex);
        } catch (RuntimeException ex) {
            LogUtil.logStackTrace(log, ex);
            if(ex.getCause() instanceof CsvException) {
                WebUtil.getRequest().ifPresent((request -> {
                    request.setAttribute(MultipartFile.class.getName(), csv.getOriginalFilename());
                    request.setAttribute(RuntimeException.class.getName(), ex.getMessage());
                    request.setAttribute(CsvException.class.getName(), ex.getCause().getMessage());
                }));
                throw ((CsvException) ex.getCause());
            } else {
                throw ex;
            }

        }
        DomainDtoList<T, ID, TDto> dtoList = new DomainDtoList<>();
        return dtoList.dtos(dtos);
    }

    @Transactional
    @Override
    public  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    createManyByCsv(IDomainService<T, ID> domainService, DomainDtoList<T, ID, TDto> csvList, Class<TDto> dtoType, Class<T> type, HttpServletRequest request, IToDomain<T, ID, TDto> converter, Logger log, BindingResult result, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate, Class<?>... hints) throws CsvException {
        this.restControllerResponseService.processValidation(result);
        DomainDtoList<T, ID, TDto> list = csvToDomainDtoList(csvList.getCsv(), dtoType, log);
        BindException exception = new BindException(list, list.getClass().getSimpleName());
        validator.validate(list, exception, hints);
        return createMany(domainService, list, type, exception, request, converter, preCreate, postCreate);
    }


    @Override
    @Transactional
    public  <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    createManyByCsvSubmit(User user, Business business, DomainDtoList<T, ID, TDto> csvList, BindingResult result, String type) {
        this.restControllerResponseService.processValidation(result);
        String csvFileId = fileService.store(csvList.getCsv());
        Map<String, String> request  = Map.of(
                "fileId", csvFileId,
                "userId", String.valueOf(user.getId()),
                "businessId", String.valueOf(business.getId()),
                "requestedBusinessId", String.valueOf(SecurityUtil.getRequestedBusinessId().orElse(BigInteger.ZERO)),
                "taskType", type
        );
        Task task = Task.getInstance()
                .type(type)
                .status(Task.TaskStatus.ACCEPTED)
                .request((Map)request);
        task = this.taskService.create(task);
        request = Maps.newHashMap(request);
        request.put("taskId", task.getId());
        this.messageSenderService.send(new MessageContext(request, RabbitConfig.TASK_EXCHANGE, RabbitConfig.TASK_QUEUE));
        return this.restControllerResponseService.processSuccessAccepted(Map.of("obj", task));
    }
}
