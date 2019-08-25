package com.github.ankurpathak.api.rest.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.ankurpathak.api.domain.model.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageDto<T> {
    private List<T> list;
    private PageInfo page;


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    public PageDto content(List<T> content) {
        this.list = content;
        return this;
    }

    public PageDto page(PageInfo page) {
        this.page = page;
        return this;
    }

    public static <T> PageDto<T> getInstance(){
        return new PageDto<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> PageDto<T> getInstance(Page<T> page){
        return getInstance()
                .content((List)page.getContent())
                .page(PageInfo.getInstance(page.getPageable(), page.getTotalElements()));
    }


    public static class PageInfo{
        private final Long page;
        private final Long size;
        private final Long count;

        @JsonCreator
        public PageInfo(@JsonProperty("page") Long page, @JsonProperty("size")Long size, @JsonProperty("count") Long count) {
            this.page = page;
            this.size = size;
            this.count = count;
        }

        public static PageInfo getInstance(Long page, Long size, Long count){
            return new PageInfo(page, size,count);
        }

        public static PageInfo getInstance(Pageable pageable, Long count){
            return new PageInfo(Integer.valueOf(pageable.getPageNumber()).longValue(), Integer.valueOf(pageable.getPageSize()).longValue(), count);
        }

        public Long getPage() {
            return page;
        }

        public Long getSize() {
            return size;
        }

        public Long getCount() {
            return count;
        }
    }
}
