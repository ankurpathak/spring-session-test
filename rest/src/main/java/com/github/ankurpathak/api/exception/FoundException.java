package com.github.ankurpathak.api.exception;

import com.github.ankurpathak.api.exception.dto.FoundDto;
import com.github.ankurpathak.api.util.MatcherUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.valid4j.Assertive.*;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class FoundException extends RuntimeException {
    private final List<FoundDto> fondDtos;
    private final DuplicateKeyException duplicateKeyException;
    private final BindingResult bindingResult;

    public FoundException(DuplicateKeyException duplicateKeyException, BindingResult bindingResult, List<FoundDto> foundDtos) {
        super(duplicateKeyException.getMessage(), duplicateKeyException.getCause());
        require(duplicateKeyException, notNullValue());
        require(bindingResult, notNullValue());
        require(foundDtos, MatcherUtil.notCollectionEmpty());
        this.duplicateKeyException = duplicateKeyException;
        this.bindingResult = bindingResult;
        this.fondDtos = foundDtos;

    }


    public FoundDto getFound() {
        return fondDtos.get(0);
    }


    public List<FoundDto> getFounds() {
        return fondDtos;
    }

    public DuplicateKeyException getDuplicateKeyException() {
        return duplicateKeyException;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
