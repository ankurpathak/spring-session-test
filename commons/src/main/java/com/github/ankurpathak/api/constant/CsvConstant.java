package com.github.ankurpathak.api.constant;

public interface CsvConstant {

    String MIME_TEXT_CSV = "text/csv";

    interface Product {
        String AMOUNT = "AMOUNT";
        String NAME = "NAME";
        String TAX = "TAX";
        String VARIABLE = "VARIABLE(y/n)";
        String DESCRIPTION = "DESCRIPTION";
    }

    interface Customer {
        String NAME = "NAME";
        String EMAIL = "EMAIL";
        String PHONE = "PHONE";
        String ADDRESS = "ADDRESS";
        String STATE = "STATE";
        String CITY ="CITY";
        String PIN_CODE = "PIN CODE";

    }
}
