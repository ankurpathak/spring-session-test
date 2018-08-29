package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizedUserRepository {
    Page<User> search(String field, String value, Pageable pageable);
}
