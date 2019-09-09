package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

@Document(collection = Model.Task.TASK)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Task extends Domain<String> implements Serializable {

    private TaskStatus status = TaskStatus.ACCEPTED;

    private String type;

    private Map<String, Object> request;

    private Map<String, Object> response;

    public Task status(TaskStatus status) {
        this.status = status;
        return this;
    }

    public Task type(String type) {
        this.type = type;
        return this;
    }

    public Task request(Map<String, Object> request) {
        this.request = request;
        return this;
    }

    public Task response(Map<String, Object> response) {
        this.response = response;
        return this;
    }

    public enum TaskStatus {
        RUNNING, COMPLETED, ACCEPTED, ERROR
    }

    public interface TaskType {
        String CSV_PRODUCT = "CSV_PRODUCT";
        String CSV_CUSTOMER = "CSV_CUSTOMER";
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String  getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }

    public static Task getInstance(){
        return new Task();
    }
}
