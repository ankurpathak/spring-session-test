package com.github.ankurpathak.api.constant;

public interface BankEtlConstants {
    interface Property {
        String PATH = "city.path";
    }




    interface File {
        String BANK_NAMES = "banknames";
        String BANK = "banks";
        String SUBLET = "sublet";
        String IFSC = "IFSC";
        String IFSC_LIST = "IFSC-list";
    }


    interface Columns {
        String ID = "_id";
        String IFSC = "bank";
        String STATE = "STATE";
        String DISTRICT = "DISTRICT";
    }
}
