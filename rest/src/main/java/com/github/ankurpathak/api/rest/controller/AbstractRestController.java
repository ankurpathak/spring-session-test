package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.*;
import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.repository.mongo.custom.dto.BulkOperationResult;
import com.github.ankurpathak.api.domain.updater.IUpdateDomain;
import com.github.ankurpathak.api.event.DomainCreatedEvent;
import com.github.ankurpathak.api.event.PaginatedResultsRetrievedEvent;
import com.github.ankurpathak.api.event.util.PagingUtil;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.callback.IPostCreateOne;
import com.github.ankurpathak.api.rest.controller.callback.IPostUpdateOne;
import com.github.ankurpathak.api.rest.controller.callback.IPreCreateOne;
import com.github.ankurpathak.api.rest.controller.callback.IPreUpdateOne;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controller.dto.converter.IToDto;
import com.github.ankurpathak.api.rest.controller.util.DuplicateKeyExceptionProcessor;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    protected ResponseEntity<T> byId(ID id, Class<T> type) {
        return ControllerUtil.processOptional(getDomainService().findById(id), type, null, String.valueOf(id), messageService);
    }


    protected ResponseEntity<List<T>> all() {
        return ResponseEntity.ok().body(getDomainService().findAll());
    }


    protected ResponseEntity<?> createMany(DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter) {
        try {
            BulkOperationResult<ID> res = tryCreateMany(dtoList, type, result, request, converter);
            return ControllerUtil.processSuccess(messageService, HttpStatus.CREATED, Map.of(Params.ID, res.getIds(), Params.COUNT, res.getCount()));
        } catch (DuplicateKeyException ex) {
            catchCreateMany(dtoList, ex, result, request);
            throw ex;
        }
    }

    private BulkOperationResult<ID> tryCreateMany(DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter) {
        ControllerUtil.processValidation(result, messageService);
        List<T> ts = dtoList.getDtos().stream().map(dto -> dto.toDomain(converter)).collect(Collectors.toList());
        return getDomainService().bulkInsertMany(type, ts);
        //applicationEventPublisher.publishEvent(new DomainCreatedEvent<>(t, response));
    }

    private T tryCreateOne(TDto dto, BindingResult result, HttpServletResponse response, IToDomain<T, ID, TDto> converter) {
        ControllerUtil.processValidation(result, messageService);
        T t = getDomainService().create(dto.toDomain(converter));
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
            preCreate.doPreCreateOne(this, dto);
            T t = tryCreateOne(dto, result, response, converter);
            postCreate.doPostCreateOne(this, t, dto);
            return ControllerUtil.processSuccess(messageService, HttpStatus.CREATED, Map.of(Params.ID, t.getId()));
        } catch (DuplicateKeyException ex) {
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }


    private void catchCreateMany(DomainDtoList<T, ID, TDto> dtoList, DuplicateKeyException ex, BindingResult result, HttpServletRequest request) {
        DuplicateKeyExceptionProcessor.processDuplicateKeyException(ex, dtoList.getDto()).ifPresent(e -> ControllerUtil.processValidationForFound(messageService, e));
    }


    private void catchCreateOne(TDto dto, DuplicateKeyException ex, BindingResult result, HttpServletRequest request) {
        DuplicateKeyExceptionProcessor.processDuplicateKeyException(ex, dto).ifPresent(e -> ControllerUtil.processValidationForFound(messageService, e));
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
                (rest, tDto) -> { },
                (rest, t, tDto) -> { }
        );

    }


    protected ResponseEntity<?> update(TDto dto, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate) {
        Optional<T> domain = getDomainService().findById(id);
        return update(dto, domain.orElse(null), id, updater, request, preUpdate, postUpdate, result);
    }


    protected ResponseEntity<?> update(TDto dto, T t, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result) {
        return update(dto, t, updater, request, result,
                (rest, tDto) -> { },
                (rest, aT, tDto) -> { });
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
                DuplicateKeyExceptionProcessor.processDuplicateKeyException(ex, dto).ifPresent(e -> ControllerUtil.processValidationForFound(messageService, e));
                throw ex;
            }
        } else {
            throw new NotFoundException(String.valueOf(id), Params.ID, dto.domainName(), ApiCode.NOT_FOUND);
        }
    }

    /*

    protected List<T> searchByField(String field, String value, int block, int size, String sort, Class<T> type, HttpServletResponse response) {
        ControllerUtil.pagePreCheck(block);
        Pageable pageable = ControllerUtil.getPageable(block, size, sort);
        String parsedValue = ControllerUtil.parseFieldValue(value);
        Page<T> page = getDomainService().findByField(field, parsedValue, pageable, type);
        ControllerUtil.pagePostCheck(block, page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return page.getContent();
    }

    protected List<String> listField(String field, String value, int block, int size, String sort, Class<T> type) {
        ControllerUtil.pagePreCheck(block);
        Pageable pageable = ControllerUtil.getPageable(block, size, sort);
        String parsedValue = ControllerUtil.parseFieldValue(value);
        Page<String> page = getDomainService().listField(field, parsedValue, pageable, type);
        ControllerUtil.pagePostCheck(block, page);
        return page.getContent();
    }


    protected List<T> search(String rsql, int block, int size, String sort, Class<T> type, HttpServletResponse response){
        ControllerUtil.pagePreCheck(block);
        Criteria criteria = ControllerUtil.parseRSQL(rsql, type);
        Pageable pageable = ControllerUtil.getPageable(block, size, sort);
        Page<T> page = getDomainService().findByCriteriaPaginated(criteria, pageable, type);
        ControllerUtil.pagePostCheck(block, page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return page.getContent();
    }

    protected ResponseEntity<?> paginated(int block, int size, String sort, HttpServletResponse response) {
        ControllerUtil.pagePreCheck(block);
        Pageable request = ControllerUtil.getPageable(block, size, sort);
        Page<T> page = getDomainService().findAllPaginated(request);
        ControllerUtil.pagePostCheck(block, page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return ResponseEntity.ok(page.getContent());
    }

    */


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


    protected List<T> search(String rsql, Pageable pageable, Class<T> type, HttpServletResponse response) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        Criteria criteria = PagingUtil.parseRSQL(rsql, type);
        Page<T> page = getDomainService().findByCriteriaPaginated(criteria, pageable, type);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return page.getContent();
    }


    protected ResponseEntity<?> paginated(Pageable pageable, HttpServletResponse response) {
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        Page<T> page = getDomainService().findAllPaginated(pageable);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return ResponseEntity.ok(page.getContent());
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
}
