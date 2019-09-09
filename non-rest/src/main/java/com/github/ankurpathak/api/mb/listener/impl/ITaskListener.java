package com.github.ankurpathak.api.mb.listener.impl;

import com.github.ankurpathak.api.domain.model.Task;

import java.util.Map;

public interface ITaskListener {
    void process(Map<String, String> request);
}
