package com.github.ankurpathak.api.opencsv;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.lang3.StringUtils;

public class BooleanBeanField extends AbstractBeanField<Boolean>  {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if(StringUtils.equalsIgnoreCase(value, "Y"))
            return true;
        else
            return false;
    }
}
