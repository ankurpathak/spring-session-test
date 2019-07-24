package com.github.ankurpathak.api.domain.repository.mongo.custom.dto;

import java.io.Serializable;
import java.util.List;

public class BulkOperationResult<ID extends Serializable> {
    private final List<ID> ids;
    private final long count;

    public BulkOperationResult(List<ID> ids, long count) {
        this.ids = ids;
        this.count = count;
    }

    public List<ID> getIds() {
        return ids;
    }

    public long getCount() {
        return count;
    }
}
