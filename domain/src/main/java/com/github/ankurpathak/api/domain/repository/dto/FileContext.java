package com.github.ankurpathak.api.domain.repository.dto;

import java.nio.file.Path;
import java.util.Map;

public class FileContext {
    private Path path;
    private String fileName;
    private String mime;
    private Map<String, String> meta;


    public static FileContext getInstance(){
        return new FileContext();
    }



    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public FileContext path(Path path) {
        this.path = path;
        return this;
    }

    public FileContext fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public FileContext mime(String mime) {
        this.mime = mime;
        return this;
    }

    public FileContext meta(Map<String, String> meta) {
        this.meta = meta;
        return this;
    }
}
