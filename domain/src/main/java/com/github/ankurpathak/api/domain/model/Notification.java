package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ankurpathak.api.constant.Model;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Document(collection = Model.Notification.NOTIFICATION)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Notification extends BusinessExtendedDomain<String> implements Serializable {
    private BigInteger actorId;
    private String entity;
    private String entityId;
    private String action;
    private Set<BigInteger> notifierIds;
    private Map<String, Object> params;

    public interface ActionType {

    }

    public Notification addBusinessId(BigInteger notifierId) {
        if (this.notifierIds == null)
            this.notifierIds = new HashSet<>();
        if (ObjectUtils.compare(BigInteger.ZERO,notifierId ) < 0)
            this.notifierIds.add(notifierId);
        return this;
    }

    public Notification removeBusinessId(BigInteger notifierId) {
        if (!CollectionUtils.isEmpty(this.notifierIds))
            if (ObjectUtils.compare(BigInteger.ZERO,notifierId ) < 0)
                this.notifierIds.remove(notifierId);
        return this;
    }



    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Notification entity(String entity) {
        this.entity = entity;
        return this;
    }

    public Notification entityId(String entityId) {
        this.entityId = entityId;
        return this;
    }

    public Notification action(String action) {
        this.action = action;
        return this;
    }


    public Notification params(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public BigInteger getActorId() {
        return actorId;
    }

    public void setActorId(BigInteger actorId) {
        this.actorId = actorId;
    }

    public Set<BigInteger> getNotifierIds() {
        return notifierIds;
    }

    public void setNotifierIds(Set<BigInteger> notifierIds) {
        this.notifierIds = notifierIds;
    }

    public Notification actorId(BigInteger actorId) {
        this.actorId = actorId;
        return this;
    }

    public Notification notifierIds(Set<BigInteger> notifierIds) {
        this.notifierIds = notifierIds;
        return this;
    }
}
