package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;

import java.io.Serializable;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class CsvTaskContext implements ITaskContext,  Serializable {
    private Task task;
    private User user;
    private Business business;
    private FileContext file;

    public void afterPropertiesSet(){
        require(task, notNullValue());
        require(user, notNullValue());
        require(business, notNullValue());
        require(file, notNullValue());
    }

    public static final CsvTaskContext getInstance(){
        return new CsvTaskContext();
    }




    public Optional<Task> getTask() {
        return Optional.ofNullable(task);
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Optional<Business> getBusiness() {
        return Optional.ofNullable(business);
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
}
