package cars.entities.cars.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MultipartFileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        if (multipartFile != null && multipartFile.getSize() != 0 && multipartFile.getOriginalFilename() != null) {

            List<String> pictureFileFormats = List.of(".jpg", ".jpeg", ".png", ".gif", ".tif", ".tiff", ".bmp");
            String fileName = multipartFile.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));

            return pictureFileFormats.contains(fileExtension);
        }

        return true;
    }
}
