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
    private final List<BindingResult> bindingResults;

    public FoundException(DuplicateKeyException duplicateKeyException, List<BindingResult> bindingResults, List<FoundDto> foundDtos) {
        super(duplicateKeyException.getMessage(), duplicateKeyException.getCause());
        require(duplicateKeyException, notNullValue());
        require(bindingResults, MatcherUtil.notCollectionEmpty());
        require(foundDtos, MatcherUtil.notCollectionEmpty());
        this.duplicateKeyException = duplicateKeyException;
        this.bindingResults = bindingResults;
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

    public List<BindingResult> getBindingResults() {
        return bindingResults;
    }

    public boolean hasErrors(){
        return bindingResults.stream().allMatch(BindingResult::hasErrors);
    }
}
