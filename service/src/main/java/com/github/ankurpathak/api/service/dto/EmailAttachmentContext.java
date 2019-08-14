package com.github.ankurpathak.api.service.dto;

import javax.activation.DataSource;
import java.io.Serializable;

public class EmailAttachmentContext implements Serializable {
    final private String originalFileName;
    final private DataSource dataSource;
    final private String mime;

    public String getMime() {
        return mime;
    }

    public EmailAttachmentContext(String originalFileName, DataSource dataSource, String mime) {
        this.originalFileName = originalFileName;
        this.dataSource = dataSource;
        this.mime = mime;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }


    public DataSource getDataSource() {
        return dataSource;
    }

}
