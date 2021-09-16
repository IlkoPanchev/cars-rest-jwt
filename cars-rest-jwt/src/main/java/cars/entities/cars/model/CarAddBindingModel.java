package cars.entities.cars.model;

import cars.entities.users.model.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class CarAddBindingModel {

    private String brand;
    private String model;
    private String description;
    private int year;
    private BigDecimal price;

    @NotBlank
    public String getBrand() {
        return brand;
    }

    public CarAddBindingModel setBrand(String brand) {
        this.brand = brand;
        return this;
    }
    @NotBlank
    public String getModel() {
        return model;
    }

    public CarAddBindingModel setModel(String model) {
        this.model = model;
        return this;
    }
    @NotBlank
    public String getDescription() {
        return description;
    }

    public CarAddBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    @Min(value = 1950, message = "Year must be greater than 1950")
    public int getYear() {
        return year;
    }

    public CarAddBindingModel setYear(int year) {
        this.year = year;
        return this;
    }

    @DecimalMin(value = "0", message = "Price must be greater than 0")
    public BigDecimal getPrice() {
        return price;
    }

    public CarAddBindingModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
