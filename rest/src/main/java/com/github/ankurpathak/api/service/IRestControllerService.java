package com.github.ankurpathak.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.updater.IUpdateDomain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controller.dto.converter.IToDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.service.callback.*;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

public interface IRestControllerService {

    ApplicationEventPublisher getApplicationEventPublisher();

    IRestControllerResponseService getRestControllerResponseService();

    ObjectMapper getObjectMapper();

    LocalValidatorFactoryBean getValidator();

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    byId(ID id, Class<T> type, IDomainService<T, ID> domainService);

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    all(IDomainService<T, ID> domainService);

    @Transactional
    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    createMany(IDomainService<T, ID> domainService, DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate);

    @Transactional
    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    createOne(IDomainService<T, ID> domainService, TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, IToDomain<T, ID, TDto> converter, IPreCreateOne<T, ID, TDto> preCreate, IPostCreateOne<T, ID, TDto> postCreate);

    @Transactional
    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    delete(IDomainService<T, ID> domainService, ID id);

    @Transactional
    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    update(IDomainService<T, ID> domainService, TDto dto, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate);

    @Transactional
    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    update(IDomainService<T, ID> domainService, TDto dto, T t, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate);

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> List<T>
    searchByField(IDomainService<T, ID> domainService, String field, String value, Pageable pageable, Class<T> type, HttpServletResponse response);

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> List<String>
    listField(IDomainService<T, ID> domainService, String field, String value, Pageable pageable, Class<T> type);

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    search(IDomainService<T, ID> domainService, String rsql, Pageable pageable, Class<T> type, HttpServletResponse response);

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    paginated(IDomainService<T, ID> domainService, Pageable pageable, HttpServletResponse response, Class<T> type);

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>, S extends T> ResponseEntity<?>
    paginated(IDomainService<T, ID> domainService, Pageable pageable, HttpServletResponse response, Class<S> type, String view);

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    patch(IDomainService<T, ID> domainService, JsonNode patch, ID id, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints);

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    patch(IDomainService<T, ID> domainService, JsonNode patch, T t, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints);

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    createManyByCsv(IDomainService<T, ID> domainService, DomainDtoList<T, ID, TDto> csvList, Class<TDto> dtoType, Class<T> type, HttpServletRequest request, IToDomain<T, ID, TDto> converter, Logger log, BindingResult result, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate, Class<?>... hints) throws CsvException;

    <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> ResponseEntity<?>
    createManyByCsvSubmit(User user, Business business, DomainDtoList<T, ID, TDto> csvList, BindingResult result, String type);
}
