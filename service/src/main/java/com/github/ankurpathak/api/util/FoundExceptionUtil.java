package com.github.ankurpathak.api.util;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.exception.dto.FoundDto;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ValidationErrorDto;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class FoundExceptionUtil {

    private FoundExceptionUtil(){}

    public static final ApiCode processApiCode(FoundException ex){
       ApiCode code = ApiCode.FOUND;
        if(ex.hasOnlyFound()){
            FoundDto dto = ex.getFound();
            if(StringUtils.equalsIgnoreCase(ex.getEntity(), User.class.getSimpleName())){
                if(StringUtils.equalsIgnoreCase(dto.getProperty(), Model.User.Field.EMAIL_VALUE)){
                    code = ApiCode.EMAIL_FOUND;
                }else if(StringUtils.equalsIgnoreCase(dto.getProperty(), Model.User.Field.USERNAME)){
                    code = ApiCode.USERNAME_FOUND;
                }else if(StringUtils.equalsIgnoreCase(dto.getProperty(), Model.User.Field.PHONE_VALUE)){
                    code = ApiCode.PHONE_FOUND;
                }
            }else if(StringUtils.equalsIgnoreCase(ex.getEntity(), Product.class.getSimpleName())){
                if(StringUtils.equalsIgnoreCase(dto.getProperty(), Model.Product.Field.NAME)){
                }
            }
        }
        return code;
    }

    public static final ValidationErrorDto processFounds(FoundException ex){
        List<FoundDto> founds = ex.getFounds();

        ValidationErrorDto validationErrorDto = ValidationErrorDto.getInstance();
        for(FoundDto dto: founds){
            validationErrorDto.addError(dto.getProperty(), dto.getId());
        }
        return validationErrorDto;
    }
}
