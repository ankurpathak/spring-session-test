package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.updater.IUpdateDomain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controller.dto.converter.IToDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IRestControllerService;
import com.github.ankurpathak.api.service.callback.*;
import org.slf4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractRestController<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {
    protected final IRestControllerService restControllerService;
    protected AbstractRestController(IRestControllerService restControllerService) {
        this.restControllerService = restControllerService;
    }

    abstract public IDomainService<T, ID> getDomainService();

    protected ResponseEntity<?> byId(ID id, Class<T> type) {
        return restControllerService.byId(id, type, getDomainService());
    }

    protected ResponseEntity<?> all() {
        return restControllerService.all(getDomainService());
    }


    protected ResponseEntity<?> createMany(DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter) {
        return createMany(dtoList, type, result, request, converter, (rest, list) -> { }, (rest, list, count) -> { });
    }

    protected ResponseEntity<?> createMany(DomainDtoList<T, ID, TDto> dtoList, Class<T> type, BindingResult result, HttpServletRequest request, IToDomain<T, ID, TDto> converter, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate) {
        return this.restControllerService.createMany(getDomainService(), dtoList, type, result, request, converter, preCreate, postCreate);
    }

    protected ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, IToDomain<T, ID, TDto> converter) {
        return createOne(dto, result, request, response, converter, (rest, tDto) -> {}, (rest, tDto, t) -> {});
    }


    protected ResponseEntity<?> createOne(TDto dto, BindingResult result, HttpServletRequest request, HttpServletResponse response, IToDomain<T, ID, TDto> converter, IPreCreateOne<T, ID, TDto> preCreate, IPostCreateOne<T, ID, TDto> postCreate) {
        return this.restControllerService.createOne(getDomainService(), dto, result, request, response, converter, preCreate, postCreate);
    }


    protected ResponseEntity<?> delete(ID id) {
        return this.restControllerService.delete(getDomainService(), id);
    }


    protected ResponseEntity<?> update(TDto dto, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result) {
        return update(dto, id, updater, request, result, (rest, tDto) -> {}, (rest, t, tDto) -> {});
    }

    protected ResponseEntity<?> update(TDto dto, ID id, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate) {
        return this.restControllerService.update(getDomainService(), dto, id, updater, request, result, preUpdate, postUpdate);
    }

    protected ResponseEntity<?> update(TDto dto, T t, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result) {
        return update(dto, t, updater, request, result, (rest, tDto) -> {}, (rest, aT, tDto) -> {});
    }

    protected ResponseEntity<?> update(TDto dto, T t, IUpdateDomain<T, ID, TDto> updater, HttpServletRequest request, BindingResult result, IPreUpdateOne<T, ID, TDto> preUpdate, IPostUpdateOne<T, ID, TDto> postUpdate) {
        return this.restControllerService.update(getDomainService(), dto, t, updater, request, result, preUpdate, postUpdate);
    }


    protected List<T> searchByField(String field, String value, Pageable pageable, Class<T> type, HttpServletResponse response) {
        return this.restControllerService.searchByField(getDomainService(), field, value, pageable, type, response);
    }


    protected List<String> listField(String field, String value, Pageable pageable, Class<T> type) {
        return this.restControllerService.listField(getDomainService(), field, value, pageable, type);
    }


    protected ResponseEntity<?> search(String rsql, Pageable pageable, Class<T> type, HttpServletResponse response) {
        return this.restControllerService.search(getDomainService(), rsql, pageable, type, response);
    }


    protected ResponseEntity<?> paginated(Pageable pageable, HttpServletResponse response, Class<T> type) {
        return this.restControllerService.paginated(getDomainService(), pageable, response, type);
    }
    protected<S extends T> ResponseEntity<?> paginated(Pageable pageable, HttpServletResponse response, Class<S> type, String view) {
        return this.restControllerService.paginated(getDomainService(), pageable, response, type, view);
    }


    protected ResponseEntity<?> patch(JsonNode patch, ID id, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints) {
        return this.restControllerService.patch(getDomainService(), patch, id, converter, updater, dtoType, hints);
    }

    protected ResponseEntity<?> patch(JsonNode patch, T t, IToDto<T, ID, TDto> converter, IUpdateDomain<T, ID, TDto> updater, Class<TDto> dtoType, Class<?>... hints) {
        return this.restControllerService.patch(getDomainService(), patch, t, converter, updater, dtoType, hints);
    }

    protected ResponseEntity<?> createManyByCsv(DomainDtoList<T, ID, TDto> csvList, Class<TDto> dtoType, Class<T> type, HttpServletRequest request, IToDomain<T, ID, TDto> converter, Logger log, BindingResult result, IPreCreateMany<T, ID, TDto> preCreate, IPostCreateMany<T, ID, TDto> postCreate, Class<?>... hints) {
        return this.restControllerService.createManyByCsv(getDomainService(), csvList, dtoType, type, request, converter, log, result, preCreate, postCreate, hints);
    }

    protected ResponseEntity<?> createManyByCsvSubmit(User user, Business business, DomainDtoList<T, ID, TDto> csvList, BindingResult result, String taskType) {
        return this.restControllerService.createManyByCsvSubmit(user, business, csvList, result, taskType);
    }
}
