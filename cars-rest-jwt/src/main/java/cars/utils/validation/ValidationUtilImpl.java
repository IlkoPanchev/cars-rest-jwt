package cars.utils.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidationUtilImpl implements ValidationUtil{

    private Validator validator;

    public ValidationUtilImpl() {

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <T> boolean isValid(T serviceModel) {
        return this.validator.validate(serviceModel).isEmpty();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> getViolations(T serviceModel) {
        return this.validator.validate(serviceModel);
    }
}
