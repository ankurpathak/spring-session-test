package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Text;
import com.github.ankurpathak.api.domain.repository.mongo.ITextRepository;
import com.github.ankurpathak.api.service.ITextService;
import org.springframework.stereotype.Service;

@Service
public class TextService extends AbstractDomainService<Text, String> implements ITextService {

    private final ITextRepository dao;

    public TextService(ITextRepository dao) {
        super(dao);
        this.dao = dao;
    }


}
