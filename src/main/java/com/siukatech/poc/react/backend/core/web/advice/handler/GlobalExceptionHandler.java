package com.siukatech.poc.react.backend.core.web.advice.handler;

import com.siukatech.poc.react.backend.core.security.exception.NoSuchPermissionException;
import com.siukatech.poc.react.backend.core.web.advice.mapper.ProblemDetailExtMapper;
import com.siukatech.poc.react.backend.core.web.advice.model.ProblemDetailExt;
import com.siukatech.poc.react.backend.core.web.micrometer.CorrelationIdHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
//@Component
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    private final Tracer tracer;
    private final CorrelationIdHandler correlationIdHandler;
    private final ProblemDetailExtMapper problemDetailExtMapper;

    public GlobalExceptionHandler(
//            Tracer tracer
//            ,
            CorrelationIdHandler correlationIdHandler
            , ProblemDetailExtMapper problemDetailExtMapper
    ) {
//        this.tracer = tracer;
        this.correlationIdHandler = correlationIdHandler;
        this.problemDetailExtMapper = problemDetailExtMapper;
//        this.problemDetailExtMapper = Mappers.getMapper(ProblemDetailExtMapper.class);
    }


    /**
     * This is exception handler of IllegalArgumentException
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity handleIllegalArgument(RuntimeException ex, WebRequest request) {
//        return this.handleExceptionInternal(ex, "handle IllegalArgumentException", new HttpHeaders(), HttpStatus.CONFLICT, request);
        log.error("handleIllegalArgument - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "]");
        HttpStatus status = HttpStatus.CONFLICT;
        Object[] args = {};
        String defaultDetail = ex.getMessage();
        ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, args, request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    /**
     * This is the exception handler of Spring Data ObjectOptimisticLockingFailureException
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {ObjectOptimisticLockingFailureException.class})
    protected ResponseEntity handleObjectOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex, WebRequest request) {
//        return this.handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        log.error("handleObjectOptimisticLockingFailure - ex: [" + ex
                + "], ex.getIdentifier.getClass.getName: [" + ex.getIdentifier().getClass().getName()
                + "], ex.getIdentifier: [" + ex.getIdentifier()
                + "], ex.getPersistentClassName: [" + ex.getPersistentClassName()
                + "]");
        HttpStatus status = HttpStatus.CONFLICT;
        Object[] args = {ex.getIdentifier(), ex.getPersistentClassName()};
        String defaultDetail = "" + ex.getMessage()
                //+ "; Id: [" + args[0] + "], Type: [" + args[1] + "]"
                ;
        ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, args, request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers
            , HttpStatusCode status, WebRequest request) {
//        return handleExceptionInternal(ex, defaultDetailBuf.toString(), headers, status, request);
//ex.get
        String contextPath = request.getContextPath();
//        StringBuffer defaultDetailBuf = new StringBuffer();
//        ex.getBindingResult().getAllErrors().stream().forEach(objectError -> {
//            defaultDetailBuf
//                    .append("objectName: ").append(objectError.getObjectName())
//                    .append(", ").append("message: ").append(objectError.getDefaultMessage())
//                    .append(System.lineSeparator())
//            ;
//        });
        List<String> errorMessageList = new ArrayList<>();
        ex.getFieldErrors().stream().forEach(fieldError -> {
            String errorMessage = "";
            errorMessage += fieldError.getDefaultMessage();
            errorMessage += " : ";
            errorMessage += fieldError.getObjectName();
            errorMessage += ".";
            errorMessage += fieldError.getField();
            errorMessage += "=";
            errorMessage += fieldError.getRejectedValue();
            if (fieldError.getArguments() != null) {
//                errorMessage += " (";
//                errorMessage += String.join(",", Arrays.stream(fieldError.getArguments()).toArray(String[]::new));
//                errorMessage += ")";
                Arrays.stream(fieldError.getArguments()).forEach(arg -> {
                    log.debug("handleMethodArgumentNotValid - arg: [" + arg + "]");
                });
            }
//            errorMessage += System.lineSeparator();
            errorMessageList.add(errorMessage);
//            defaultDetailBuf
//                    .append("objectName: ").append(fieldError.getObjectName())
//                    .append(", ")
//                    .append("fieldError: ").append(fieldError.getField())
//                    .append(", ")
//                    .append("rejectedValue: ").append(fieldError.getRejectedValue())
//                    .append(", ")
//                    .append("message: ").append(fieldError.getDefaultMessage())
//                    .append(fieldError.getDefaultMessage())
//                    .append(" (")
//                    .append(fieldError.getObjectName())
//                    .append(".")
//                    .append(fieldError.getField())
//                    .append("=")
//                    .append(fieldError.getRejectedValue())
//                    .append(")")
//                    .append(System.lineSeparator())
//            ;
        });
//        String defaultDetail = defaultDetailBuf.toString();
        String defaultDetail = String.join("\n", errorMessageList.toArray(String[]::new));
        ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, null, request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error("handleHttpMessageNotReadable - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace());

        String defaultDetail = ex.getMessage();
        ProblemDetail body = createProblemDetail(ex, status
//                , "Failed to read request"
                , defaultDetail
                , null, null, request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error("handleHttpMessageNotWritable - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace());

        String defaultDetail = ex.getMessage();
        ProblemDetail body = createProblemDetail(ex, status
//                , "Failed to write request"
                , defaultDetail
                , null, null, request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * This is the exception handler of Jpa EntityNotFoundException
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
//        return this.handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        log.error("handleEntityNotFoundException - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace());

        HttpStatus status = HttpStatus.NOT_FOUND;
        String defaultDetail = "" + ex.getMessage();
        ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, null, request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(value = NoSuchPermissionException.class)
    protected ResponseEntity handleNoSuchPermissionException(NoSuchPermissionException ex, WebRequest request) {

        log.error("handleNoSuchPermissionException - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], request.getHeader: [" + request.getHeader("")
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace()
        );

        HttpStatus status = HttpStatus.FORBIDDEN;
        Object[] args = {ex.getClass().getName()};
        String defaultDetail = ""
//                + ex.getMessage()  // should not expose the exception detail
                ;
        ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, args, request);
        ProblemDetailExt bodyExt = createProblemDetailExt(body);
        return handleExceptionInternal(ex, bodyExt, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity handleRuntimeException(RuntimeException ex, WebRequest request) {

        log.error("handleRuntimeException - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Object[] args = {ex.getClass().getName()};
        String defaultDetail = "" + ex.getMessage()
                ;
        ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, args, request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    protected ProblemDetailExt createProblemDetailExt(ProblemDetail body) {
        ProblemDetailExt bodyExt = problemDetailExtMapper.convertProblemDetailToExtWithoutCorrelationId(body);
        bodyExt.setCorrelationId(this.correlationIdHandler.getCorrelationId());
        return bodyExt;
    }

}
