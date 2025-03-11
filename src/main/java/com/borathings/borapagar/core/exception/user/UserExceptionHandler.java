package com.borathings.borapagar.core.exception.user;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(1)
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Trata exceções lançadas pela aplicação quando o usuário faz um pedido de amizade quando já é amigo.
     *
     * @param ex - UsersNotFriendsException - Exceção lançada
     * @return ResponseEntity<Object> - Exceção serializada
     */
    @ExceptionHandler(UsersNotFriendsException.class)
    private ResponseEntity<Object> handleUsersNotFriendsException(UsersNotFriendsException ex) {
        ApiException exception = new ApiException(HttpStatus.FORBIDDEN, ex);
        return ApiException.toResponseEntity(exception);
    }
}
