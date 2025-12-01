package com.devwiki.leafy.global.common.exception.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import com.devwiki.leafy.Discord.service.DiscordService;
import com.devwiki.leafy.global.common.exception.type.BadRequestException;
import com.devwiki.leafy.global.common.exception.type.CustomException;
import com.devwiki.leafy.global.common.exception.type.ForbiddenException;
import com.devwiki.leafy.global.common.exception.type.NotFoundException;
import com.devwiki.leafy.global.common.exception.type.ServerErrorException;
import com.devwiki.leafy.global.common.exception.type.UnAuthorizedException;
import com.devwiki.leafy.global.common.response.bad.BadResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @Autowired
    private DiscordService discordService;

    //400 에러 핸들러
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadResponse> handle400Error(BadRequestException be,HttpServletRequest request){
        makeErrorLogs(be, request);
        sendToDiscord400Channel(be, request);
        return ResponseEntity
                .status(be.getBadStatusCode().getHttpStatus())
                .body(BadResponse.makeResponse(be.getBadStatusCode()));


    }



    //401 에러 핸들러
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<BadResponse> handle401Error(UnAuthorizedException ex,HttpServletRequest request){
        makeErrorLogs(ex, request);
        sendToDiscord400Channel(ex,request);
        return ResponseEntity
                .status(ex.getBadStatusCode().getHttpStatus())
                .body(BadResponse.makeResponse(ex.getBadStatusCode()));
    }

    //403 에러 핸들러
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<BadResponse> handle403Error (ForbiddenException ex , HttpServletRequest request){
        makeErrorLogs(ex, request);
        sendToDiscord400Channel(ex,request);
        return ResponseEntity
                .status(ex.getBadStatusCode().getHttpStatus())
                .body(BadResponse.makeResponse(ex.getBadStatusCode()));
    }

    //404 에러 핸들러
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BadResponse> handle404Error (NotFoundException ex,HttpServletRequest request){
        makeErrorLogs(ex, request);
        sendToDiscord400Channel(ex,request);
        return ResponseEntity
                .status(ex.getBadStatusCode().getHttpStatus())
                .body(BadResponse.makeResponse(ex.getBadStatusCode()));
    }
    //나머지 커스텀 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BadResponse> handleCustomError (CustomException ex, HttpServletRequest request){
        makeErrorLogs(ex,request);
        sendToDiscord400Channel(ex, request);
        log.info("Handled CustomException: {}", ex.getBadStatusCode().name());
        return ResponseEntity
                .status(ex.getBadStatusCode().getHttpStatus())
                .body(BadResponse.makeResponse(ex.getBadStatusCode()));
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    //500 에러 핸들러
    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<BadResponse> handle500Error (ServerErrorException ex,HttpServletRequest request){
        sendToDiscord500Channel(ex, request);
        return ResponseEntity
                .status(ex.getBadStatusCode().getHttpStatus())
                .body(BadResponse.makeResponse(ex.getBadStatusCode()));
    }




    // 나머지 5xx (예상치 못한 런타임 예외 등)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BadResponse> handle5xxError(Exception ex, HttpServletRequest request) {
        if (ex instanceof NoResourceFoundException && request.getRequestURI().equals("/favicon.ico")) {
            return ResponseEntity.notFound().build(); // 404로만 응답, 로그/디스코드 전송 X
        }
        HttpStatus st = HttpStatus.INTERNAL_SERVER_ERROR;

        log.error("""
                        
                        ┌─ {} {}
                        │ type    : {}
                        │ method  : {}
                        │ url     : {}
                        │ message : {}
                        └─ stack  : below""",
                st.value(), st.getReasonPhrase(),
                ex.getClass().getSimpleName(),
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );
        log.error("Stacktrace:", ex);


        String message = ex.getMessage();
        System.out.println("message = " + message);
        String stackTrace = ExceptionUtils.getStackTrace(ex);
        String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp != null && !clientIp.isBlank()) {
            clientIp = clientIp.split(",")[0].trim();
        } else {
            clientIp = request.getRemoteAddr();
        }


        discordService.send5xxNotification(message, stackTrace, clientIp, requestInfo);

        return ResponseEntity
                .status(st)
                .body(BadResponse.make5xxResponse("서버 내부 오류 입니다."));
    }

    // 스프링 자동 예외
    @ExceptionHandler({
            HttpMessageNotReadableException.class,           // JSON 파싱 실패
            MethodArgumentNotValidException.class,           // @Valid 검증 실패
            MissingServletRequestParameterException.class,   // 필수 파라미터 누락
            HttpRequestMethodNotSupportedException.class,    // 잘못된 HTTP 메서드
            HttpMediaTypeNotSupportedException.class,        // 잘못된 Content-Type
            MethodArgumentTypeMismatchException.class                      // 타입 변환 실패
    })
    public ResponseEntity<BadResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
        HttpStatus st = HttpStatus.BAD_REQUEST;

        log.error("""
        
        ┌─ {} {}
        │ type    : {}
        │ method  : {}
        │ url     : {}
        │ message : {}
        └─ stack  : below""",
                st.value(), st.getReasonPhrase(),
                ex.getClass().getSimpleName(),
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );
        log.error("Stacktrace:", ex);
        String message = "잘못된 요청 입니다";
        String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp != null && !clientIp.isBlank()) {
            clientIp = clientIp.split(",")[0].trim();
        } else {
            clientIp = request.getRemoteAddr();
        }
        discordService.send4xxNotification(message,requestInfo,clientIp);

        return ResponseEntity
                .status(st)
                .body(BadResponse.make4xxResponse("잘못된 요청 입니다."));
    }

    /*
    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
     */

    private static void makeErrorLogs(CustomException ex, HttpServletRequest req) {
        HttpStatus st = ex.getBadStatusCode().getHttpStatus();
        log.error("""
        
        ┌─ {} {}
        │ type    : {}
        │ code    : {}
        │ method  : {}
        │ url     : {}
        │ message : {}
        └─ stack  : below""",
                st.value(), st.getReasonPhrase(),      // "400 Bad Request" 분리
                ex.getClass().getSimpleName(),
                ex.getBadStatusCode().name(),
                req.getMethod(),
                req.getRequestURI(),
                ex.getMessage()
        );
        log.error("Stacktrace:", ex);
    }

    private void sendToDiscord400Channel(CustomException be, HttpServletRequest request) {
        String message = be.getMessage();
        String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp != null && !clientIp.isBlank()) {
            clientIp = clientIp.split(",")[0].trim();
        } else {
            clientIp = request.getRemoteAddr();
        }


        discordService.send4xxNotification(message,requestInfo,clientIp);
    }

    private void sendToDiscord500Channel(ServerErrorException ex, HttpServletRequest request) {
        makeErrorLogs(ex, request);
        String message = ex.getMessage();
        String stackTrace = ExceptionUtils.getStackTrace(ex);
        String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp != null && !clientIp.isBlank()) {
            clientIp = clientIp.split(",")[0].trim();
        } else {
            clientIp = request.getRemoteAddr();
        }

        discordService.send5xxNotification(message,stackTrace,clientIp,requestInfo);
    }




}
