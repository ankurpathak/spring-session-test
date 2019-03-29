package com.github.ankurpathak.app.service.dto;

import javax.activation.DataSource;
import java.io.Serializable;

public class EmailAttachmentContext implements Serializable {
    final private String orignalFileName;
    final private DataSource dataSource;


    public EmailAttachmentContext(String orignalFileName, DataSource dataSource) {
        this.orignalFileName = orignalFileName;
        this.dataSource = dataSource;
    }

    public String getOrignalFileName() {
        return orignalFileName;
    }


    public DataSource getDataSource() {
        return dataSource;
    }

}
