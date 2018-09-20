package com.ankurpathak.springsessiontest;

import com.ankurpathak.springsessiontest.controller.InvalidTokenException;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.ankurpathak.springsessiontest.Params.*;

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

    public static ResponseEntity<?> processError(MessageSource messageSource, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponse.getInstance(
                                ApiCode.UNKNOWN,
                                MessageUtil.getMessage(messageSource, ApiMessages.UNKNOWN)
                        )
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
                throw new InvalidTokenException(
                        MessageUtil.getMessage(messageSource, ApiMessages.EXPIRED_TOKEN, token),
                        ApiCode.EXPIRED_TOKEN
                );
            case INVALID:
            default:
                throw new InvalidTokenException(
                        MessageUtil.getMessage(messageSource, ApiMessages.INVALID_TOKEN, token),
                        ApiCode.INVALID_TOKEN
                );
        }
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
            sort = "";

        Iterable<String> tokens = Splitter.on(",")
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
            if (value.startsWith("*") && value.endsWith("*"))
                return value.replace("*", ".*");
            if (value.startsWith("*") && value.length() > 1)
                return String.format("^%s", value.substring(1));
            else if (value.endsWith("*") && value.length() > 1)
                return String.format("%s$", value.substring(0, value.length() - 2));
            else if (!value.contains("*"))
                return String.format("^%s$", value);
        }
        return value;
    }

    public static<T> void pagePostCheck(int block, Page<T> page){
        if (block > page.getTotalPages())
            throw new NotFoundException(String.valueOf(block), "block", Page.class.getSimpleName(), ApiCode.PAGE_NOT_FOUND);
    }


    public static void pagePrecheck(int block){
        if (block < 1)
            throw new NotFoundException(String.valueOf(block), "block", Page.class.getSimpleName(), ApiCode.PAGE_NOT_FOUND);

    }

}
