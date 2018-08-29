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
        Pageable request = null;
        int indexOfSort = -1;
        if (!StringUtils.isEmpty(sort) && (indexOfSort = sort.indexOf(",")) != -1) {
            sort = sort.trim();
            String field = sort.substring(0, indexOfSort);
            String order = sort.substring(indexOfSort);
            if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(order)) {
                field = field.trim();
                order = order.trim();
                if (Objects.equals(order, "asc")) {
                    request = PageRequest.of(block, size, Sort.by(Sort.Order.asc(field)));
                } else if (Objects.equals(order, "desc")) {
                    request = PageRequest.of(block, size, Sort.by(Sort.Order.desc(field)));
                }
            }

        }
        if (request == null)
            request = PageRequest.of(block, size);
        Page<T> page = getService().findPaginated(request);
        if (block > page.getTotalPages() - 1)
            throw new NotFoundException(String.valueOf(block), "block", Page.class.getSimpleName(), ApiCode.PAGE_NOT_FOUND);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Page<T>, T, ID>(page, response));
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
        applicationEventPublisher.publishEvent(new DomainCreatedEvent<T, ID>(t, response));
        return t;
    }

    public ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return createOne(dto, result, request, response, DomainDto.Default.class);
    }

    public ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, Class<?> type) {
        try{
            T t = tryCreateOne(dto, result, request, response, type);
            return ControllerUtil.processSuccess(messageSource, request, HttpStatus.CREATED, Map.of("id", t.getId()));
        } catch (DuplicateKeyException ex) {
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }

    public void catchCreateOne(TDto dto, DuplicateKeyException ex, BindingResult result, HttpServletRequest request){
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

    public ResponseEntity<?> update(TDto dto, ID id, HttpServletRequest request) {
        Optional<T> domain = getService().findById(id);
        if (domain.isPresent()) {
            getService().update(dto.updateDomain(domain.get()));
            return ControllerUtil.processSuccess(messageSource, request);
        } else {
            throw new NotFoundException(String.valueOf(id), "id", dto.domainName(), ApiCode.NOT_FOUND);
        }


    }

    public ResponseEntity<?> search(String field, String value, int block, int size, String sort, HttpServletResponse response){
        return null;
    }

}
