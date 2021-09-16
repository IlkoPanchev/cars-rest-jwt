package cars.config;

import cars.utils.message.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;


@ControllerAdvice
public class GlobalAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalAdvice.class);

    @ExceptionHandler({EntityNotFoundException.class, ConstraintViolationException.class})
    public ResponseEntity<?> handleError(RuntimeException ex){

        LOGGER.error(ex.getMessage());

        return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
    }
}
