package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.service.ITaskService;

import java.io.Serializable;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class CsvTaskContext implements ITaskContext,  Serializable {
    private Task task;
    private User user;
    private Business business;
    private FileContext file;
    private String requestedBusinessId;





    public void afterPropertiesSet(){
        ITaskContext.super.afterPropertiesSet();
        require(this.file, notNullValue());
    }

    public static final CsvTaskContext getInstance(){
        return new CsvTaskContext();
    }





    public void setTask(Task task) {
        this.task = task;
    }


    public void setUser(User user) {
        this.user = user;
    }




    public void setBusiness(Business business) {
        this.business = business;
    }

    public Optional<FileContext> getFile() {
        return Optional.ofNullable(file);
    }

    public void setFile(FileContext file) {
        this.file = file;
    }

    public CsvTaskContext task(Task task) {
        this.task = task;
        return this;
    }

    public CsvTaskContext user(User user) {
        this.user = user;
        return this;
    }

    public CsvTaskContext business(Business business) {
        this.business = business;
        return this;
    }

    public CsvTaskContext file(FileContext file) {
        this.file = file;
        return this;
    }

    @Override
    public Task getTask() {
        return this.task;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public Business getBusiness() {
        return this.business;
    }

    @Override
    public String getRequestedBusinessId() {
        return this.requestedBusinessId;
    }

    public void setRequestedBusinessId(String requestedBusinessId) {
        this.requestedBusinessId = requestedBusinessId;
    }

    public CsvTaskContext requestedBusinessId(String requestedBusinessId) {
        this.requestedBusinessId = requestedBusinessId;
        return this;
    }
}
