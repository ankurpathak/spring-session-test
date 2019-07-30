package com.github.ankurpathak.api.constant;

public interface CityEtlConstants {
    interface Property {
        String PATH = "city.path";
    }


    interface File {
    }


    interface Columns {
        String ID = "_id";
        String PINCODE = "pincode";
        String STATE = "statename";
        String DISTRICT = "Districtname";
    }

    interface Collection {
        String CITY = "city";
    }
}
