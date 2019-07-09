package com.github.ankurpathak.api.service.dto;

import javax.activation.DataSource;
import java.io.Serializable;

public class EmailAttachmentContext implements Serializable {
    final private String originalFileName;
    final private DataSource dataSource;


    public EmailAttachmentContext(String originalFileName, DataSource dataSource) {
        this.originalFileName = originalFileName;
        this.dataSource = dataSource;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }


    public DataSource getDataSource() {
        return dataSource;
    }

}
