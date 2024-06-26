package me.kupchenko.webfluxdemo.exception.handler;

import lombok.extern.slf4j.Slf4j;
import me.kupchenko.webfluxdemo.exception.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

  @ExceptionHandler(InvalidRequestException.class)
  public Mono<ResponseEntity<Void>> handleInvalidRequestException(InvalidRequestException invalidRequestException) {
    log.warn("Invalid request exception occurred", invalidRequestException);
    return Mono.just(ResponseEntity.badRequest().build());
  }
}
