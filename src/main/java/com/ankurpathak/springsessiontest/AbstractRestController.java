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

public abstract class AbstractRestController<T extends Domain<ID>, TDto extends DomainDto<ID>, ID extends Serializable> {


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

    public ResponseEntity<?> createMany(DomainDtoList<TDto, ID> dtoList, BindingResult result, HttpServletRequest request) {
        ControllerUtil.processValidaton(result, messageSource, request);
        List<ID> ids = createMany(dtoList.getDtos(), result);
        return ControllerUtil.processSuccess(messageSource, request, HttpStatus.CREATED, Map.of("ids", ids));

    }

    public ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        ControllerUtil.processValidaton(result, messageSource, request);
        ID id = createOne(dto, result, response);
        return ControllerUtil.processSuccess(messageSource, request, HttpStatus.CREATED, Map.of("id", id));
    }


    @SuppressWarnings("unchecked")
    private List<ID> createMany(List<TDto> dtos, BindingResult result) {
        Iterable<T> domains = getService().createAll(dtos.stream().map(dto -> ((T) dto.toDomain())).collect(Collectors.toList()));
        List<ID> ids = new ArrayList<>();
        domains.forEach(domain -> ids.add(domain.getId()));
        return ids;
    }

    @SuppressWarnings("unchecked")
    private ID createOne(TDto dto, BindingResult result, HttpServletResponse response) {
        try {
            T t = getService().create((T) dto.toDomain());
            applicationEventPublisher.publishEvent(new DomainCreatedEvent<T,ID>(t, response));
            return t.getId();
        } catch (DuplicateKeyException ex) {
            throw ApplicationExceptionTranslator.convertToFoundException(ex, dto);
        }
    }

    public ResponseEntity<?> delete(ID id, HttpServletRequest request){
        Optional<T> domain = getService().findById(id);
        if(domain.isPresent()){
            getService().delete(domain.get());
            return ControllerUtil.processSuccess(messageSource,  request, HttpStatus.NO_CONTENT);
        }else {
            throw new NotFoundException(String.valueOf(id), "id", getService().domainName(), ApiCode.NOT_FOUND);
        }
    }

    public ResponseEntity<?> update(TDto dto, ID id, HttpServletRequest request){
        Optional<T> domain = getService().findById(id);
        if(domain.isPresent()){
            dto.updateDomain(domain.get());
            getService().update(domain.get());
            return ControllerUtil.processSuccess(messageSource,  request);
        }else {
            throw new NotFoundException(String.valueOf(id), "id", dto.domainName(), ApiCode.NOT_FOUND);
        }


    }

}
