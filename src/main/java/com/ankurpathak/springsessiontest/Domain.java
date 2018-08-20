package com.ankurpathak.springsessiontest;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

abstract public class Domain<T> implements Serializable {
    private  T id;


    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
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


    public Domain id(T id) {
        this.id = id;
        return this;
    }


    @CreatedDate
    private LocalDateTime created;


    @LastModifiedDate
    private LocalDateTime updated;

    private Set<String> tags;


    public Domain<T> addTag(String tag){
        if(tags == null)
            tags = new HashSet<>();
        if(!StringUtils.isEmpty(tag))
            tags.add(tag);
        return this;
    }

    public Domain<T> removeTag(String tag){
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
