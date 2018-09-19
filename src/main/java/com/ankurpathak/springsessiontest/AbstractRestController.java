package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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

    public ResponseEntity<?> byId(ID id) {
        Optional<T> t = getService().findById(id);
        if (t.isPresent())
            return ResponseEntity.ok(t.get());
        else return ResponseEntity.notFound().build();
    }


    public ResponseEntity<?> all() {
        return ResponseEntity.ok().body(getService().findAll());
    }


    public ResponseEntity<?> paginated(int block, int size, String sort, HttpServletResponse response) {
        if (block < 1)
            throw new NotFoundException(String.valueOf(block), "block", Page.class.getSimpleName(), ApiCode.PAGE_NOT_FOUND);
        Pageable request = ControllerUtil.getPageable(block, size, sort);
        Page<T> page = getService().findPaginated(request);
        if (block > page.getTotalPages())
            throw new NotFoundException(String.valueOf(block), "block", Page.class.getSimpleName(), ApiCode.PAGE_NOT_FOUND);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return ResponseEntity.ok(page.getContent());
    }

    public ResponseEntity<?> createMany(DomainDtoList<T, ID, TDto> dtoList, BindingResult result, HttpServletRequest request) {
        return createMany(dtoList, result, request, DomainDto.Default.class);
    }

    public ResponseEntity<?> createMany(DomainDtoList<T, ID, TDto> dtoList, BindingResult result, HttpServletRequest request, Class<?> type) {
        ControllerUtil.processValidation(result, messageSource, request);
        Iterable<T> domains = getService().createAll(dtoList.getDtos().stream().map(dto -> dto.toDomain(type)).collect(Collectors.toList()));
        List<ID> ids = new ArrayList<>();
        domains.forEach(domain -> ids.add(domain.getId()));
        return ControllerUtil.processSuccess(messageSource, request, HttpStatus.CREATED, Map.of("ids", ids));
    }

    public T tryCreateOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, Class<?> type) {
        ControllerUtil.processValidation(result, messageSource, request);
        T t = getService().create(dto.toDomain(type));
        applicationEventPublisher.publishEvent(new DomainCreatedEvent<>(t, response));
        return t;
    }

    public ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return createOne(dto, result, request, response, DomainDto.Default.class);
    }

    public ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, Class<?> type) {
        try {
            T t = tryCreateOne(dto, result, request, response, type);
            return ControllerUtil.processSuccess(messageSource, request, HttpStatus.CREATED, Map.of("id", t.getId()));
        } catch (DuplicateKeyException ex) {
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }

    public void catchCreateOne(TDto dto, DuplicateKeyException ex, BindingResult result, HttpServletRequest request) {
        FoundException foundEx = ApplicationExceptionProcessor.processDuplicateKeyException(ex, dto, result);
        if (foundEx != null) {
            ControllerUtil.processValidationForFound(result, messageSource, request, foundEx);
        }
    }


    public ResponseEntity<?> delete(ID id, HttpServletRequest request) {
        Optional<T> domain = getService().findById(id);
        if (domain.isPresent()) {
            getService().delete(domain.get());
            return ControllerUtil.processSuccessNoContent();
        } else {
            throw new NotFoundException(String.valueOf(id), "id", getService().domainName(), ApiCode.NOT_FOUND);
        }
    }

    public ResponseEntity<?> update(TDto dto, ID id, Class<?> type, HttpServletRequest request) {
        Optional<T> domain = getService().findById(id);
        return update(dto, domain.orElse(null), id, type, request);
    }


    public ResponseEntity<?> update(TDto dto, T t, Class<?> type, HttpServletRequest request) {
        return update(dto, t, t.getId(), type, request);
    }


    public ResponseEntity<?> update(TDto dto, T t, ID id, Class<?> type, HttpServletRequest request) {
        if (t != null) {
            getService().update(dto.updateDomain(t, type));
            return ControllerUtil.processSuccess(messageSource, request);
        } else {
            throw new NotFoundException(String.valueOf(id), "id", dto.domainName(), ApiCode.NOT_FOUND);
        }
    }

    public List<T> search(String field, String value, int block, int size, String sort, Class<T> type, HttpServletResponse response) {
        ControllerUtil.pagePrecheck(block);
        Pageable pageable = ControllerUtil.getPageable(block, size, sort);
        String parsedValue = ControllerUtil.parseFieldValue(value);
        Page<T> page = getService().search(field, parsedValue, pageable, type);
        ControllerUtil.pagePostCheck(block, page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(page, response));
        return page.getContent();
    }

    public List<String> listField(String field, String value, int block, int size, String sort, Class<T> type) {
        ControllerUtil.pagePrecheck(block);
        Pageable pageable = ControllerUtil.getPageable(block, size, sort);
        String parsedValue = ControllerUtil.parseFieldValue(value);
        Page<String> page = getService().listField(field, parsedValue, pageable, type);
        ControllerUtil.pagePostCheck(block, page);
        return page.getContent();
    }


}
