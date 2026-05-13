package com.app.FinanceHelper.exceptions;

import com.app.FinanceHelper.payload.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler{

    private final MessageSource messageSource;

    public MyGlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName,message);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        APIResponse apiResponse = new APIResponse(e.getMessage(), false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        APIResponse apiResponse = new APIResponse(e.getMessage(), false);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse> handleSpringDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException e){
        String messageKey = "error.db.generic";
        if (e.getMostSpecificCause() != null) {
            String specificMessage = e.getMostSpecificCause().getMessage().toLowerCase();
            if (specificMessage.contains("duplicate") || specificMessage.contains("unique")) {
                messageKey = "error.db.duplicate";
            } else if (specificMessage.contains("foreign key") || specificMessage.contains("constraint") || specificMessage.contains("a foreign key constraint fails")) {
                messageKey = "error.db.foreignkey";
            } else if (specificMessage.contains("null")) {
                messageKey = "error.db.null";
            }
        }

        String localizedMessage = messageSource.getMessage(messageKey, null, messageKey, LocaleContextHolder.getLocale());
        APIResponse apiResponse = new APIResponse(localizedMessage, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(APIexception.class)
    public ResponseEntity<APIResponse> myAPIexception(APIexception e){
        // Tenta resolver a mensagem como chave de tradução, se não achar, exibe a original
        String localizedMessage = messageSource.getMessage(e.getMessage(), null, e.getMessage(), LocaleContextHolder.getLocale());
        APIResponse apiResponse = new APIResponse(localizedMessage, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<APIResponse> handleBadCredentialsException(BadCredentialsException e){
        String localizedMessage = messageSource.getMessage("error.badcredentials", null, "Email ou senha inválidos.", LocaleContextHolder.getLocale());
        APIResponse apiResponse = new APIResponse(localizedMessage, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse> handleAccessDeniedException(AccessDeniedException e){
        String localizedMessage = messageSource.getMessage("error.accessdenied", null, "Você não tem permissão para acessar este recurso.", LocaleContextHolder.getLocale());
        APIResponse apiResponse = new APIResponse(localizedMessage, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleGlobalException(Exception e){
        System.err.println(e.getMessage()); // Substituído printStackTrace() para não vazar logs na interface
        String localizedMessage = messageSource.getMessage("error.internal", null, "Ocorreu um erro interno no servidor.", LocaleContextHolder.getLocale());
        APIResponse apiResponse = new APIResponse(localizedMessage, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        String localizedMessage = messageSource.getMessage("error.badrequest", null, "O corpo da requisição é inválido ou está malformado.", LocaleContextHolder.getLocale());
        APIResponse apiResponse = new APIResponse(localizedMessage, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST); // 400
    }
}
