package com.github.ankurpathak.api.event.util;

import com.github.ankurpathak.api.Strings;
import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rsql.CustomRSQLVisitor;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PagingUtil {
    public static final String REGEX_ASTERISK = ".*";
    public static final String PATTERN_START_WITH = "^%s";
    public static final String PATTERN_END_WITH = "%s$";


    public static int fixPage(String page) {
        int value = PrimitiveUtils.toInteger(page);
        return value >= 0 ? value : 0;
    }

    public static int fixSize(String size) {
        return fixSizeUpper(fixSizeLower(size));
    }

    public static int fixSizeLower(String size) {
        int value = PrimitiveUtils.toInteger(size);
        return value >= 1 ? value : 20;
    }

    public static int fixSizeUpper(int size) {
        return size <= 200 ? size : 200;
    }


    public static Pageable getPageable(int block, int size, String sort) {
        return PageRequest.of(block - 1, size, parseSort(sort));
    }

    private static Sort parseSort(String sort) {
        if (sort == null)
            sort = Strings.EMPTY;

        Iterable<String> tokens = Splitter.on(Strings.COMMA)
                .trimResults()
                .omitEmptyStrings()
                .split(sort);

        if (Iterables.size(tokens) >= 2) {
            Iterator<String> it = tokens.iterator();
            String tokenField = Strings.EMPTY;
            String tokenOrder = Strings.EMPTY;
            List<Sort.Order> orders = new ArrayList<>();
            while (it.hasNext()) {
                tokenField = it.next();
                if (it.hasNext())
                    tokenOrder = it.next();
                switch (tokenOrder) {
                    case Params.ASC:
                    case Params.DESC:
                        break;
                    case Params.ASC_UPPERCASE:
                    case Params.DESC_UPPERCASE:
                        tokenOrder = tokenOrder.toLowerCase();
                        break;
                    default:
                        tokenOrder = Params.ASC;
                        break;
                }
                orders.add(Objects.equals(tokenOrder, Params.ASC) ? Sort.Order.asc(tokenField) : Sort.Order.desc(tokenField));
            }
            return Sort.by(orders);
        } else {
            return Sort.unsorted();
        }

    }

    public static String parseFieldValue(String value) {
        if (!StringUtils.isEmpty(value)) {
            if (value.startsWith(Strings.ASTERISK) && value.endsWith(Strings.ASTERISK))
                return value.replace(Strings.ASTERISK, REGEX_ASTERISK);
            if (value.startsWith(Strings.ASTERISK) && value.length() > 1)
                return String.format(PATTERN_START_WITH, value.substring(1));
            else if (value.endsWith(Strings.ASTERISK) && value.length() > 1)
                return String.format(PATTERN_END_WITH, value.substring(0, value.length() - 2));
            else if (!value.contains(Strings.ASTERISK))
                return String.format(ControllerUtil.PATTERN_EXACT, value);
        }
        return value;
    }

    public static <T> void pagePostCheck(int block, Page<T> page) {
        if (block > page.getTotalPages())
            throw new NotFoundException(String.valueOf(block), Params.BLOCK, Page.class.getSimpleName(), ApiCode.PAGE_NOT_FOUND);
    }

    public static void pagePreCheck(int block) {
        if (block < 1)
            throw new NotFoundException(String.valueOf(block), Params.BLOCK, Page.class.getSimpleName(), ApiCode.PAGE_NOT_FOUND);

    }

    public static <T> Criteria parseRSQL(String rsql, Class<T> type) {
        Node rootNode = new RSQLParser().parse(rsql);
        return rootNode.accept(new CustomRSQLVisitor(type.getSimpleName()));
    }
}
