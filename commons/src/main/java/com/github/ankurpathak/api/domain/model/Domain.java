package com.github.ankurpathak.api.domain.model;


import com.fasterxml.jackson.annotation.JsonView;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controller.dto.View;
import com.github.ankurpathak.api.rest.controller.dto.converter.IToDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

abstract public class Domain<ID extends Serializable> implements Serializable {

    private  ID id;

    @JsonView({View.Public.class, View.Me.class})
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Domain() {
        //created = updated = Instant.now();
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
    @JsonView({View.Public.class, View.Me.class})
    private Instant created;


    @LastModifiedDate
    @JsonView({View.Public.class, View.Me.class})
    private Instant updated;

    private Set<String> tags;


    public Domain<ID> addTag(String tag){
        if(tags == null)
            tags = new HashSet<>();
        if(!StringUtils.isEmpty(tag))
            tags.add(tag);
        return this;
    }

    public Domain<ID> removeField(CustomField field){
        if(!org.apache.commons.collections4.CollectionUtils.isEmpty(fields))
            fields.add(field);
        return this;
    }

    public Domain<ID> addField(CustomField field){
        if(fields == null)
            fields = new HashSet<>();
        if(field!=null)
            fields.add(field);
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



    private Set<CustomField> fields;

    public Set<CustomField> getFields() {
        return fields;
    }

    public void setFields(Set<CustomField> fields) {
        this.fields = fields;
    }

    @SuppressWarnings("unchecked")
    public  <T extends  Domain<ID>, TDto extends DomainDto<T, ID>> TDto toDto(IToDto<T, ID, TDto> converter){
        T t = (T) this;
        return converter.toDto(t);
    }

    public Domain fields(Set<CustomField> fields) {
        this.fields = fields;
        return this;
    }
}
