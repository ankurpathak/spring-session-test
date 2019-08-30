package com.github.ankurpathak.api.mb.listener.impl;

import com.github.ankurpathak.api.domain.model.Task;

public interface ITaskListener {
    void process(Task task);
}
