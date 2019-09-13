package com.github.ankurpathak.api.constant;

public interface SchemaConstants {
    interface File {
        String VIEW = "view.json";
        String VIEW_COMMAND = "view-command.js";
    }

    public static final String[] COLLECTIONS = {
            "Sequences",
            "JobInstance",
            "JobExecution",
            "ExecutionContext",
            "StepExecution"
    };
}
