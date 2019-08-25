package com.github.ankurpathak.api.domain.model;

import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

@Document(collection = Model.Task.TASK)
public class Task extends Domain<String> implements Serializable {

    private TaskStatus status = TaskStatus.ACCEPTED;

    private TaskType type;

    private Map<String, Object> request;

    private Map<String, Object> response;

    public enum TaskStatus {
        RUNNING, COMPLETED, ACCEPTED, ERROR
    }

    private enum TaskType {
        CSV_PRODUCT,
        CSV_CUSTOMER
    }
}
