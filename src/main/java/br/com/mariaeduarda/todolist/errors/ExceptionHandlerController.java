package br.com.mariaeduarda.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // defini classes globais no momento de tratamento de excessões
public class ExceptionHandlerController {

  @ExceptionHandler(HttpMessageNotReadableException.class) // defini qual o tipo de excessão
  public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    // exibe a mensagem de erro para o usuario
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
  }
}
