package cars.entities.cars.model;

import java.math.BigDecimal;

public class CarViewBindingModel {

    private Long id;
    private String brand;
    private String model;
    private String description;
    private int year;
    private String imageUrl;
    private BigDecimal price;
    private Long ownerId;

    public Long getId() {
        return id;
    }

    public CarViewBindingModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public CarViewBindingModel setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public CarViewBindingModel setModel(String model) {
        this.model = model;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CarViewBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getYear() {
        return year;
    }

    public CarViewBindingModel setYear(int year) {
        this.year = year;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CarViewBindingModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CarViewBindingModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public CarViewBindingModel setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}
