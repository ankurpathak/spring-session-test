package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.*;
import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.updater.IUpdateDomain;
import com.github.ankurpathak.api.event.DomainCreatedEvent;
import com.github.ankurpathak.api.event.PaginatedResultsRetrievedEvent;
import com.github.ankurpathak.api.event.util.PagingUtil;
import com.github.ankurpathak.api.exception.CsvException;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.callback.*;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controller.dto.PageDto;
import com.github.ankurpathak.api.rest.controller.dto.converter.IToDto;
import com.github.ankurpathak.api.rest.controller.util.DuplicateKeyExceptionProcessor;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.util.LogUtil;
import com.mongodb.bulk.BulkWriteResult;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRestController<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {


    abstract public IDomainService<T, ID> getDomainService();


    protected final ApplicationEventPublisher applicationEventPublisher;
    protected final IMessageService messageService;
    protected final ObjectMapper objectMapper;
    protected final LocalValidatorFactoryBean validator;

    public AbstractRestController(ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageService = messageService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }


    protected ResponseEntity<?> byId(ID id, Class<T> type) {
        return ControllerUtil.processOptional(getDomainService().findById(id), type, null, String.valueOf(id), messageService);
    }


    protected ResponseEntity<List<T>> all() {
        return ResponseEntity.ok().body(getDomainService().findAll());
    }


    protected ResponseEntity<?> createMany(DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter) {
        return createMany(dtoList, type, result, request, converter,
                (rest, list) -> { }, (rest, list, count) -> { }
        );
    }


    protected ResponseEntity<?> createMany(DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate) {
        try {
            BulkWriteResult res = tryCreateMany(dtoList, type, result, request, converter, preCreate, postCreate);
            return ControllerUtil.processSuccess(messageService, HttpStatus.CREATED, Map.of(Params.COUNT, res.getInsertedCount()));
        } catch (DuplicateKeyException ex) {
            catchCreateMany(dtoList, ex, result, request);
            throw ex;
        }
    }

    private BulkWriteResult tryCreateMany(DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate) {
        ControllerUtil.processValidation(result, messageService);
        preCreate.doPreCreateMany(this, dtoList);
        List<T> ts = dtoList.getDtos().stream().map(dto -> dto.toDomain(converter)).collect(Collectors.toList());
        BulkWriteResult writeResult = getDomainService().bulkInsertMany(type, ts);
        postCreate.doPostCreateMany(this, dtoList, writeResult);
        //applicationEventPublisher.publishEvent(new DomainCreatedEvent<>(t, response));
        return writeResult;
    }

    private T tryCreateOne(TDto dto, BindingResult result, HttpServletResponse response, IToDomain<T, ID, TDto> converter, IPreCreateOne<T, ID, TDto> preCreate, IPostCreateOne<T, ID, TDto> postCreate) {
        ControllerUtil.processValidation(result, messageService);
        preCreate.doPreCreateOne(this, dto);
        T t = getDomainService().create(dto.toDomain(converter));
        postCreate.doPostCreateOne(this, t, dto);
        applicationEventPublisher.publishEvent(new DomainCreatedEvent<>(t, response));
        return t;
    }


    protected ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, IToDomain<T, ID, TDto> converter) {
        return createOne(dto, result, request, response, converter, (rest, tDto) -> {
        }, (rest, tDto, t) -> {
        });
    }


    protected ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, IToDomain<T, ID, TDto> converter, IPreCreateOne<T, ID, TDto> preCreate, IPostCreateOne<T, ID, TDto> postCreate) {
        try {
            T t = tryCreateOne(dto, result, response, converter, preCreate, postCreate);
            return ControllerUtil.processSuccess(messageService, HttpStatus.CREATED, Map.of(Params.ID, t.getId()));
        } catch (DuplicateKeyException ex) {
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }


    private void catchCreateMany(DomainDtoList<T, ID, TDto> dtoList, DuplicateKeyException ex, BindingResult result, HttpServletRequest request) {
        DuplicateKeyExceptionProcessor.processDuplicateKeyException(ex, dtoList.getDto().getClass()).ifPresent(e -> ControllerUtil.processValidationForFound(messageService, e));
    }


    private void catchCreateOne(TDto dto, DuplicateKeyException ex, BindingResult result, HttpServletRequest request) {
        DuplicateKeyExceptionProcessor.processDuplicateKeyException(ex, dto.getClass()).ifPresent(e -> ControllerUtil.processValidationForFound(messageService, e));
    }


    protected ResponseEntity<?> delete(ID id) {
        Optional<T> domain = getDomainService().findById(id);
        if (domain.isPresent()) {
            getDomainService().delete(domain.get());
            return ControllerUtil.processSuccessNoContent();
        } else {
            throw new NotFoundException(String.valueOf(id), Params.ID, getDomainService().domainName(), ApiCode.NOT_FOUND);
        }
    }


    protected ResponseEntity<?> update(TDto dto, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result) {
        return update(dto, id, updater, request, result,
                (rest, tDto) -> {
                },
                (rest, t, tDto) -> {
                }
        );

    }


    protected ResponseEntity<?> update(TDto dto, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate) {
        Optional<T> domain = getDomainService().findById(id);
        return update(dto, domain.orElse(null), id, updater, request, preUpdate, postUpdate, result);
    }


    protected ResponseEntity<?> update(TDto dto, T t, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result) {
        return update(dto, t, updater, request, result,
                (rest, tDto) -> {
                },
                (rest, aT, tDto) -> {
                });
    }


    protected ResponseEntity<?> update(TDto dto, T t, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate) {
        return update(dto, t, null, updater, request, preUpdate, postUpdate, result);
    }


    private ResponseEntity<?> update(TDto dto, T t, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate, BindingResult result) {
        ControllerUtil.processValidation(result, messageService);
        if (t != null) {
            try {
                preUpdate.doPreCreateOne(this, dto);
                T newT = getDomainService().update(dto.updateDomain(t, updater));
                postUpdate.doPostUpdateOne(this, newT, dto);
                return ControllerUtil.processSuccess(messageService);
            } catch (DuplicateKeyException ex) {
                DuplicateKeyExceptionProcessor.processDuplicateKeyException(ex, dto.getClass()).ifPresent(e -> ControllerUtil.processValidationForFound(messageService, e));
                throw ex;
            }
        } else {
            throw new NotFoundException(String.valueOf(id), Params.ID, dto.domainName(), ApiCode.NOT_FOUND);
        }
    }


    protected List<T> searchByField(String field, String value, Pageable pageable, Class<T> type, HttpServletResponse response) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        String parsedValue = PagingUtil.parseFieldValue(value);
        Page<T> page = getDomainService().findByField(field, parsedValue, pageable, type);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return page.getContent();
    }


    protected List<String> listField(String field, String value, Pageable pageable, Class<T> type) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        String parsedValue = PagingUtil.parseFieldValue(value);
        Page<String> page = getDomainService().listField(field, parsedValue, pageable, type);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        return page.getContent();
    }


    protected ResponseEntity<?> search(String rsql, Pageable pageable, Class<T> type, HttpServletResponse response) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        Criteria criteria = PagingUtil.parseRSQL(rsql, type);
        Page<T> page = getDomainService().findByCriteriaPaginated(criteria, pageable, type);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return ControllerUtil.processSuccess(messageService, Map.of("data", PageDto.getInstance(page)));
    }


    protected ResponseEntity<?> paginated(Pageable pageable, HttpServletResponse response, Class<T> type) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        Page<T> page = getDomainService().findAllPaginated(pageable, type);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return ControllerUtil.processSuccess(messageService, Map.of("data", PageDto.getInstance(page)));

    }
    protected<S extends T> ResponseEntity<?> paginated(Pageable pageable, HttpServletResponse response, Class<S> type, String view) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        Page<S> page = getDomainService().findAllPaginated(pageable, type, view);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return ControllerUtil.processSuccess(messageService, Map.of("data", PageDto.getInstance(page)));

    }

    protected ResponseEntity<?> patch(T t, ID id, JsonNode patch, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints) {
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
                TDto dtoDiff = objectMapper.treeToValue(source, dtoType);
                BindException bindResult = new BindException(dtoDiff, dtoDiff.getClass().getSimpleName());
                validator.validate(dtoDiff, bindResult, (Object[]) hints);
                ControllerUtil.processValidation(bindResult, messageService);
                T updatedT = dtoDiff.updateDomain(t, updater);
                getDomainService().update(updatedT);
                return ControllerUtil.processSuccess(messageService);
            } catch (JsonProcessingException | InvalidJsonPatchException e) {
                throw new InvalidException(ApiCode.INVALID_PATCH, Params.PATCH, Params.JSON.toUpperCase());
            }
        } else {
            throw new NotFoundException(String.valueOf(id), Params.ID, getDomainService().domainName(), ApiCode.NOT_FOUND);
        }
    }

    protected ResponseEntity<?> patch(JsonNode patch, ID id, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints) {
        Optional<T> t = getDomainService().findById(id);
        return patch(t.orElse(null), id, patch, converter, updater, dtoType, hints);
    }

    protected ResponseEntity<?> patch(JsonNode patch, T t, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints) {
        return patch(t, null, patch, converter, updater, dtoType, hints);
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    public IMessageService getMessageService() {
        return messageService;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public LocalValidatorFactoryBean getValidator() {
        return validator;
    }

    public DomainDtoList<T, ID, TDto> csvToDomainDtoList(MultipartFile csv, Class<TDto> dtoType, Logger log) {
        HeaderColumnNameMappingStrategy<TDto> ms = new HeaderColumnNameMappingStrategy<>();
        ms.setType(dtoType);
        List<TDto> dtos = Collections.emptyList();
        try (CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(csv.getInputStream())))
        ) {
            CsvToBean<TDto> cb = new CsvToBeanBuilder<TDto>(reader)
                    .withType(dtoType)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(true)
                    .withMappingStrategy(ms)
                    .build();
            dtos = cb.parse();
        } catch (IOException ex) {
            LogUtil.logStackTrace(log, ex);
        } catch (RuntimeException ex) {
            LogUtil.logStackTrace(log, ex);
            throw new CsvException(ex.getMessage(), ex, csv.getOriginalFilename());
        }

        DomainDtoList<T, ID, TDto> dtoList = new DomainDtoList<>();
        return dtoList.dtos(dtos);
    }

    public ResponseEntity<?> createManyByCsv(DomainDtoList<T, ID, TDto> csvList, Class<TDto> dtoType, Class<T> type, HttpServletRequest request, IToDomain<T, ID, TDto> converter, Logger log, BindingResult result, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate, Class<?>... hints) {
        ControllerUtil.processValidation(result, messageService);
        DomainDtoList<T, ID, TDto> list = csvToDomainDtoList(csvList.getCsv(), dtoType, log);
        BindException exception = new BindException(list, list.getClass().getSimpleName());
        processValidation(list, exception, hints);
        return createMany(list, type, exception, request, converter, preCreate, postCreate);
    }

    @SafeVarargs
    private void processValidation(Object object, BindingResult result, Class<?>... hints) {
        validator.validate(object, result, hints);
    }

}
