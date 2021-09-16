package cars.entities.cars.model;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class CarAddServiceModel {

    private String brand;
    private String model;
    private String description;
    private int year;
    private BigDecimal price;

    @NotBlank
    public String getBrand() {
        return brand;
    }

    public CarAddServiceModel setBrand(String brand) {
        this.brand = brand;
        return this;
    }
    @NotBlank
    public String getModel() {
        return model;
    }

    public CarAddServiceModel setModel(String model) {
        this.model = model;
        return this;
    }
    @NotBlank
    public String getDescription() {
        return description;
    }

    public CarAddServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    @Min(value = 1950, message = "Year must be greater than 1950")
    public int getYear() {
        return year;
    }

    public CarAddServiceModel setYear(int year) {
        this.year = year;
        return this;
    }

    @DecimalMin(value = "0", message = "Price must be greater than 0")
    public BigDecimal getPrice() {
        return price;
    }

    public CarAddServiceModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
