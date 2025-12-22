package com.presight.inventory.adapter.web;

import com.presight.common_lib.errorhandling.BusinessException;
import com.presight.common_lib.errorhandling.EntityNotFoundException;
import com.presight.common_lib.errorhandling.ErrorDTO;
import com.presight.common_lib.errorhandling.ValidationException;
import com.presight.common_lib.errorhandling.model.CommonErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class InventoryExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handlerBusinessException(BusinessException businessException){
        log.error("handling business exception");
        ErrorDTO errorDTO = businessException.getErrorDTO();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handlerBusinessException(EntityNotFoundException entityNotFoundException){
        log.error("handling enitity not found exception");
        ErrorDTO errorDTO = entityNotFoundException.getErrorDTO();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handlerBusinessException(Exception exception){
        log.error("handling general exception. ex: ",exception);
        ErrorDTO errorDTO = new ErrorDTO(CommonErrorType.GENERAL_EXCEPTION, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleBusiness(ValidationException exception) {
        log.error("handling general exception. ex: ",exception);
        ErrorDTO errorDTO = exception.getErrorDTO();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }
}
