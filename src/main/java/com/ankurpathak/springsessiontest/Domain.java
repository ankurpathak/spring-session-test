package com.ankurpathak.springsessiontest;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

abstract public class Domain<ID extends Serializable> implements Serializable {


    private  ID id;


    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Domain() {
        created = updated = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Domain<?> domain = (Domain<?>) o;

        return id != null ? id.equals(domain.id) : domain.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public Domain id(ID id) {
        this.id = id;
        return this;
    }


    @CreatedDate
    private Instant created;


    @LastModifiedDate
    private Instant updated;

    private Set<String> tags;


    public Domain<ID> addTag(String tag){
        if(tags == null)
            tags = new HashSet<>();
        if(!StringUtils.isEmpty(tag))
            tags.add(tag);
        return this;
    }

    public Domain<ID> removeTag(String tag){
        if(!CollectionUtils.isEmpty(tags))
            tags.remove(tag);
        return this;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }


    public Domain created(Instant created) {
        this.created = created;
        return this;
    }

    public Domain updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public Domain tags(Set<String> tags) {
        this.tags = tags;
        return this;
    }
}
