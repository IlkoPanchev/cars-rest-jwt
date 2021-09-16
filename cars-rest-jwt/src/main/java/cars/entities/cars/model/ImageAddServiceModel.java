package cars.entities.cars.model;

import cars.entities.cars.validation.ValidFile;
import org.springframework.web.multipart.MultipartFile;

public class ImageAddServiceModel {

    private MultipartFile imageUrl;

    @ValidFile(message = "Select valid image file. Allowed file extensions: .jpg .jpeg .png .gif .tif .tiff .bmp")
    public MultipartFile getImageUrl() {
        return imageUrl;
    }

    public ImageAddServiceModel setImageUrl(MultipartFile imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
