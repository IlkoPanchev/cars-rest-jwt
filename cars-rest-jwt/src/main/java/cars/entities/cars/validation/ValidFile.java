package cars.entities.cars.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MultipartFileValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {

    String message() default "Not valid file";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
