package com.ankurpathak.springsessiontest;

import com.ankurpathak.springsessiontest.controller.InvalidException;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.ankurpathak.springsessiontest.Params.*;
import static com.ankurpathak.springsessiontest.Strings.ASTERISK;
import static com.ankurpathak.springsessiontest.Strings.COMMA;

public class ControllerUtil {

    private static void processValidation(BindingResult result, MessageSource messageSource, HttpServletRequest request, ApiCode code, String message) {
        if (result.hasErrors()) {
            throw new ValidationException(
                    result,
                    MessageUtil.getMessage(messageSource, message),
                    code
            );
        }
    }


    public static void processValidation(BindingResult result, MessageSource messageSource, HttpServletRequest request) {
        processValidation(result, messageSource, request, ApiCode.VALIDATION, ApiMessages.VALIDATION);
    }


    public static void processValidationForFound(BindingResult result, MessageSource messageSource, HttpServletRequest request, FoundException ex) {
        if (result.hasErrors()) {
            throw new ValidationException(
                    result,
                    MessageUtil.getMessage(messageSource, ApiMessages.FOUND, ex.getEntity(), ex.getProperty(), ex.getId()),
                    ex.getCode()
            );
        }
    }

    public static ResponseEntity<?> processError(MessageSource messageSource, HttpServletRequest request, Map<String, Object> extras) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.getInstance(
                                ApiCode.UNKNOWN,
                                MessageUtil.getMessage(messageSource, ApiMessages.UNKNOWN)
                        )
                                .addExtras(extras)
                );

    }


    public static ResponseEntity<?> processSuccess(MessageSource messageSource, HttpServletRequest request, HttpStatus code, Map<String, Object> extras) {
        return ResponseEntity.status(code)
                .body(
                        ApiResponse.getInstance(
                                ApiCode.SUCCESS,
                                MessageUtil.getMessage(messageSource, ApiMessages.SUCCESS)
                        ).addExtras(extras)
                );
    }


    public static ResponseEntity<?> processSuccessNoContent() {
        return ResponseEntity.noContent().build();
    }


    public static ResponseEntity<?> processSuccess(MessageSource messageSource, HttpServletRequest request) {
        return processSuccess(messageSource, request, HttpStatus.OK, Collections.emptyMap());
    }

    public static ResponseEntity<?> processSuccess(MessageSource messageSource, HttpServletRequest request, Map<String, Object> extras) {
        return processSuccess(messageSource, request, HttpStatus.OK, extras);
    }

    public static ResponseEntity<?> processSuccessCreated(MessageSource messageSource, HttpServletRequest request) {
        return processSuccess(messageSource, request, HttpStatus.CREATED, Collections.emptyMap());
    }

    public static ResponseEntity<?> processSuccessCreated(MessageSource messageSource, HttpServletRequest request, Map<String, Object> extras) {
        return processSuccess(messageSource, request, HttpStatus.CREATED, extras);
    }


    public static ResponseEntity<?> processTokenStatus(Token.TokenStatus status, String token, MessageSource messageSource, HttpServletRequest request) {
        switch (status) {
            case VALID:
                return ControllerUtil.processSuccess(messageSource, request);

            case EXPIRED:
                return ControllerUtil.processExpiredToken(token, messageSource, request);
            case INVALID:
            default:
                throw new InvalidException(ApiCode.INVALID_TOKEN, Params.TOKEN, token);
        }
    }

    public static ResponseEntity<?> processExpiredToken(String token, MessageSource messageSource, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.getInstance(
                        ApiCode.EXPIRED_TOKEN,
                        MessageUtil.getMessage(messageSource, ApiMessages.EXPIRED_TOKEN, token)
                )
        );
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
                return String.format(PATTERN_EXACT, value);
        }
        return value;
    }

    public static final String REGEX_ASTERISK = ".*";
    public static final String PATTERN_START_WITH = "^%s";
    public static final String PATTERN_END_WITH = "%s$";
    public static final String PATTERN_EXACT = "^%s$";

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
