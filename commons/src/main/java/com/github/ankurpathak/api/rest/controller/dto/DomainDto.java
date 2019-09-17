package com.github.ankurpathak.api.rest.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.updater.IUpdateDomain;
import com.github.ankurpathak.api.util.CombinedUtils;
import com.opencsv.bean.CsvBindAndJoinByName;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.batch.item.ItemCountAware;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Map;

public class DomainDto<T extends Domain<ID>, ID extends Serializable> implements  Serializable, ItemCountAware {

    @Transient
    @JsonIgnore
    transient private int itemCount;

    @SuppressWarnings("unchecked")
    public  <TDto extends DomainDto<T, ID>> T toDomain(IToDomain<T, ID, TDto> converter){
        TDto dto = (TDto) this;
        return converter.toDomain(dto);
    }

    @SuppressWarnings("unchecked")
    public <TDto extends DomainDto<T, ID>> T updateDomain(T t, IUpdateDomain<T, ID, TDto> updater){
        TDto dto = (TDto) this;
        return updater.doUpdate(t, dto);
    }


    @CsvBindAndJoinByName(column = ".*", elementType = String.class, mapType = ArrayListValuedHashMap.class)
    private MultiValuedMap<String, String> fields;

    public String domainName(){
        String name = this.getClass().getSimpleName();
        name = name != null ? name : "";
        int index = name.indexOf('D');
        index = index > -1 ? index : 0;
        return name.substring(0, index);
    }



    public Map<String, String> fields(){
        return CombinedUtils.toMap(fields);
    }

    @Override
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemCount() {
        return itemCount;
    }

}

