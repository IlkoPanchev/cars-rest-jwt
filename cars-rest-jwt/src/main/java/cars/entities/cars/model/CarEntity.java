package cars.entities.cars.model;

import cars.entities.base.BaseEntity;
import cars.entities.users.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name = "cars")
public class CarEntity extends BaseEntity {

    private String brand;
    private String model;
    private String description;
    private int year;
    private String imageUrl;
    private BigDecimal price;
    private UserEntity owner;

    @Column(name = "brand", nullable = false)
    public String getBrand() {
        return brand;
    }

    public CarEntity setBrand(String brand) {
        this.brand = brand;
        return this;
    }
    @Column(name = "model", nullable = false)
    public String getModel() {
        return model;
    }

    public CarEntity setModel(String model) {
        this.model = model;
        return this;
    }
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public CarEntity setDescription(String description) {
        this.description = description;
        return this;
    }
    @Column(name = "year", nullable = false)
    @Min(value = 1950)
    public int getYear() {
        return year;
    }

    public CarEntity setYear(int year) {
        this.year = year;
        return this;
    }
    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public CarEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0")
    public BigDecimal getPrice() {
        return price;
    }

    public CarEntity setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="owner_id", nullable=false)
    public UserEntity getOwner() {
        return owner;
    }

    public CarEntity setOwner(UserEntity user) {
        this.owner = user;
        return this;
    }
}
