package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRestController<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {


    abstract public IDomainService<T, ID> getService();


    protected final ApplicationEventPublisher applicationEventPublisher;
    protected final MessageSource messageSource;

    public AbstractRestController(ApplicationEventPublisher applicationEventPublisher, MessageSource messageSource) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageSource = messageSource;
    }

    protected  ResponseEntity<?> byId(ID id, Class<T> type) {
        Optional<T> t = getService().findById(id);
        return t.<ResponseEntity<?>>map(ResponseEntity::ok).orElseThrow(() -> new NotFoundException(String.valueOf(id), Params.ID, type.getSimpleName(), ApiCode.NOT_FOUND));
    }


    protected ResponseEntity<?> all() {
        return ResponseEntity.ok().body(getService().findAll());
    }


    protected ResponseEntity<?> createMany(DomainDtoList<T, ID, TDto> dtoList, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter) {
        ControllerUtil.processValidation(result, messageSource, request);
        Iterable<T> domains = getService().createAll(dtoList.getDtos().stream().map(dto -> dto.toDomain(converter)).collect(Collectors.toList()));
        List<ID> ids = new ArrayList<>();
        domains.forEach(domain -> ids.add(domain.getId()));
        return ControllerUtil.processSuccess(messageSource, request, HttpStatus.CREATED, Map.of(Params.ID, ids));
    }

    protected T tryCreateOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, IToDomain<T, ID, TDto> converter) {
        ControllerUtil.processValidation(result, messageSource, request);
        T t = getService().create(dto.toDomain(converter));
        applicationEventPublisher.publishEvent(new DomainCreatedEvent<>(t, response));
        return t;
    }


    protected ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, IToDomain<T, ID, TDto> converter) {
        try {
            T t = tryCreateOne(dto, result, request, response, converter);
            return ControllerUtil.processSuccess(messageSource, request, HttpStatus.CREATED, Map.of("id", t.getId()));
        } catch (DuplicateKeyException ex) {
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }

    protected void catchCreateOne(TDto dto, DuplicateKeyException ex, BindingResult result, HttpServletRequest request) {
        FoundException foundEx = ApplicationExceptionProcessor.processDuplicateKeyException(ex, dto, result);
        if (foundEx != null) {
            ControllerUtil.processValidationForFound(result, messageSource, request, foundEx);
        }
    }


    protected ResponseEntity<?> delete(ID id) {
        Optional<T> domain = getService().findById(id);
        if (domain.isPresent()) {
            getService().delete(domain.get());
            return ControllerUtil.processSuccessNoContent();
        } else {
            throw new NotFoundException(String.valueOf(id), Params.ID, getService().domainName(), ApiCode.NOT_FOUND);
        }
    }

    protected ResponseEntity<?> update(TDto dto, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request) {
        Optional<T> domain = getService().findById(id);
        return update(dto, domain.orElse(null), id, updater, request);
    }


    protected ResponseEntity<?> update(TDto dto, T t, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request) {
        return update(dto, t, t.getId(), updater, request);
    }


    protected ResponseEntity<?> update(TDto dto, T t, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request) {
        if (t != null) {
            getService().update(dto.updateDomain(t, updater));
            return ControllerUtil.processSuccess(messageSource, request);
        } else {
            throw new NotFoundException(String.valueOf(id), Params.ID, dto.domainName(), ApiCode.NOT_FOUND);
        }
    }

    /*

    protected List<T> searchByField(String field, String value, int block, int size, String sort, Class<T> type, HttpServletResponse response) {
        ControllerUtil.pagePreCheck(block);
        Pageable pageable = ControllerUtil.getPageable(block, size, sort);
        String parsedValue = ControllerUtil.parseFieldValue(value);
        Page<T> page = getService().findByField(field, parsedValue, pageable, type);
        ControllerUtil.pagePostCheck(block, page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return page.getContent();
    }

    protected List<String> listField(String field, String value, int block, int size, String sort, Class<T> type) {
        ControllerUtil.pagePreCheck(block);
        Pageable pageable = ControllerUtil.getPageable(block, size, sort);
        String parsedValue = ControllerUtil.parseFieldValue(value);
        Page<String> page = getService().listField(field, parsedValue, pageable, type);
        ControllerUtil.pagePostCheck(block, page);
        return page.getContent();
    }


    protected List<T> search(String rsql, int block, int size, String sort, Class<T> type, HttpServletResponse response){
        ControllerUtil.pagePreCheck(block);
        Criteria criteria = ControllerUtil.parseRSQL(rsql, type);
        Pageable pageable = ControllerUtil.getPageable(block, size, sort);
        Page<T> page = getService().findByCriteria(criteria, pageable, type);
        ControllerUtil.pagePostCheck(block, page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return page.getContent();
    }

    protected ResponseEntity<?> paginated(int block, int size, String sort, HttpServletResponse response) {
        ControllerUtil.pagePreCheck(block);
        Pageable request = ControllerUtil.getPageable(block, size, sort);
        Page<T> page = getService().findPaginated(request);
        ControllerUtil.pagePostCheck(block, page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return ResponseEntity.ok(page.getContent());
    }

    */


    protected List<T> searchByField(String field, String value, Pageable pageable, Class<T> type, HttpServletResponse response) {
        ControllerUtil.pagePreCheck(pageable.getPageNumber());
        String parsedValue = ControllerUtil.parseFieldValue(value);
        Page<T> page = getService().findByField(field, parsedValue, pageable, type);
        ControllerUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return page.getContent();
    }



    protected List<String> listField(String field, String value, Pageable pageable, Class<T> type) {
        ControllerUtil.pagePreCheck(pageable.getPageNumber());
        String parsedValue = ControllerUtil.parseFieldValue(value);
        Page<String> page = getService().listField(field, parsedValue, pageable, type);
        ControllerUtil.pagePostCheck(pageable.getPageNumber(), page);
        return page.getContent();
    }


    protected List<T> search(String rsql, Pageable pageable, Class<T> type, HttpServletResponse response){
        ControllerUtil.pagePreCheck(pageable.getPageNumber());
        Criteria criteria = ControllerUtil.parseRSQL(rsql, type);
        Page<T> page = getService().findByCriteria(criteria, pageable, type);
        ControllerUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return page.getContent();
    }


    protected ResponseEntity<?> paginated(Pageable pageable, HttpServletResponse response) {
        ControllerUtil.pagePreCheck(pageable.getPageNumber());
        Page<T> page = getService().findPaginated(pageable);
        ControllerUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return ResponseEntity.ok(page.getContent());
    }

}
