package com.github.ankurpathak.api.exception;

import com.github.ankurpathak.api.exception.dto.FoundDto;
import com.github.ankurpathak.api.util.MatcherUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class FoundException extends RuntimeException {
    private final List<FoundDto> fondDtos;
    private final DuplicateKeyException duplicateKeyException;
    private final Class<?> dtoType;

    public FoundException(DuplicateKeyException duplicateKeyException, List<FoundDto> foundDtos, Class<?> dtoType) {
        super(duplicateKeyException.getMessage(), duplicateKeyException.getCause());
        require(duplicateKeyException, notNullValue());
        require(foundDtos, MatcherUtil.notCollectionEmpty());
        require(dtoType, notNullValue());
        this.duplicateKeyException = duplicateKeyException;
        this.fondDtos = foundDtos;
        this.dtoType = dtoType;

    }

    public String getEntity(){
        return StringUtils.substringBefore(dtoType.getSimpleName(), "Dto");
    }

    public FoundDto getFound() {
        return fondDtos.get(0);
    }

    public boolean hasOnlyFound(){
        return fondDtos.size() == 1;
    }


    public List<FoundDto> getFounds() {
        return fondDtos;
    }


}
