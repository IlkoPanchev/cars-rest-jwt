package cars.utils.validation;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {

    <T> boolean isValid(T serviceModel);
    <T> Set<ConstraintViolation<T>> getViolations(T serviceModel);
}
