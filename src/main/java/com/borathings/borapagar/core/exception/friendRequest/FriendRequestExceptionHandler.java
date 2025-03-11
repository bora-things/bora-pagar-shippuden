package com.borathings.borapagar.core.exception.friendRequest;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(1)
public class FriendRequestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Trata exceções lançadas pela aplicação quando o usuário faz um pedido de amizade após ter um com menos de 7 dias.
     *
     * @param ex - FriendRequestCooldownException - Exceção lançada
     * @return ResponseEntity<Object> - Exceção serializada
     */
    @ExceptionHandler(FriendRequestCooldownException.class)
    private ResponseEntity<Object> handleFriendRequestCooldownException(FriendRequestCooldownException ex) {
        ApiException exception = new ApiException(HttpStatus.FORBIDDEN, ex);
        return ApiException.toResponseEntity(exception);
    }

    /**
     * Trata exceções lançadas pela aplicação quando o usuário faz um pedido de amizade quando já existe um pendente.
     *
     * @param ex - DuplicateFriendRequestException - Exceção lançada
     * @return ResponseEntity<Object> - Exceção serializada
     */
    @ExceptionHandler(DuplicateFriendRequestException.class)
    private ResponseEntity<Object> handleDuplicateFriendRequestException(DuplicateFriendRequestException ex) {
        ApiException exception = new ApiException(HttpStatus.FORBIDDEN, ex);
        return ApiException.toResponseEntity(exception);
    }

    /**
     * Trata exceções lançadas pela aplicação quando o usuário faz um pedido de amizade quando já é amigo.
     *
     * @param ex - DuplicateFriendRequestException - Exceção lançada
     * @return ResponseEntity<Object> - Exceção serializada
     */
    @ExceptionHandler(AlreadyFriendsException.class)
    private ResponseEntity<Object> handleAlreadyFriendsException(AlreadyFriendsException ex) {
        ApiException exception = new ApiException(HttpStatus.FORBIDDEN, ex);
        return ApiException.toResponseEntity(exception);
    }

    /**
     * Constrói a resposta da exceção lançada
     *
     * @param ex - ApiException - Exceção lançada
     * @return ResponseEntity<Object> - Exceção serializada
     */
}
