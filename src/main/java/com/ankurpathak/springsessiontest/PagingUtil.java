package com.ankurpathak.springsessiontest;

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

import static com.ankurpathak.springsessiontest.Params.*;
import static com.ankurpathak.springsessiontest.Strings.ASTERISK;
import static com.ankurpathak.springsessiontest.Strings.COMMA;

public class PagingUtil {
    public static final String REGEX_ASTERISK = ".*";
    public static final String PATTERN_START_WITH = "^%s";
    public static final String PATTERN_END_WITH = "%s$";


    public static int fixPage(String page) {
        int value = PrimitiveUtils.toInteger(page);
        return value >= 0 ? value : 0;
    }

    public static int fixSize(String size) {
        int value = PrimitiveUtils.toInteger(size);
        return value >= 0 ? value : 0;
    }


    public static Pageable getPageable(int block, int size, String sort) {
        if (size > 200)
            size = 200;
        else if (size <= 0)
            size = 20;
        return PageRequest.of(block - 1, size, parseSort(sort));
    }

    private static Sort parseSort(String sort) {
        if (sort == null)
            sort = Strings.EMPTY;

        Iterable<String> tokens = Splitter.on(COMMA)
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
                    case ASC:
                    case DESC:
                        break;
                    case ASC_UPPERCASE:
                    case DESC_UPPERCASE:
                        tokenOrder = tokenOrder.toLowerCase();
                        break;
                    default:
                        tokenOrder = ASC;
                        break;
                }
                orders.add(Objects.equals(tokenOrder, ASC) ? Sort.Order.asc(tokenField) : Sort.Order.desc(tokenField));
            }
            return Sort.by(orders);
        } else {
            return Sort.unsorted();
        }

    }

    public static String parseFieldValue(String value) {
        if (!StringUtils.isEmpty(value)) {
            if (value.startsWith(ASTERISK) && value.endsWith(ASTERISK))
                return value.replace(ASTERISK, REGEX_ASTERISK);
            if (value.startsWith(ASTERISK) && value.length() > 1)
                return String.format(PATTERN_START_WITH, value.substring(1));
            else if (value.endsWith(ASTERISK) && value.length() > 1)
                return String.format(PATTERN_END_WITH, value.substring(0, value.length() - 2));
            else if (!value.contains(ASTERISK))
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
