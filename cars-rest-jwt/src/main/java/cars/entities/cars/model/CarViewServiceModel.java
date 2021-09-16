package cars.entities.cars.model;

import java.math.BigDecimal;

public class CarViewServiceModel {
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

    public CarViewServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public CarViewServiceModel setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public CarViewServiceModel setModel(String model) {
        this.model = model;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CarViewServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getYear() {
        return year;
    }

    public CarViewServiceModel setYear(int year) {
        this.year = year;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CarViewServiceModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CarViewServiceModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public CarViewServiceModel setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}
