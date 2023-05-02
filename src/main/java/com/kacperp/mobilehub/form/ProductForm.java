package com.kacperp.mobilehub.form;

import com.kacperp.mobilehub.model.OrderProduct;
import com.kacperp.mobilehub.model.User;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public class ProductForm {
    private Long id;
    @NotEmpty(message = "Name field cannot be empty")
    private String name;
    @NotEmpty(message = "Description field cannot be empty")
    private String description;
    @DecimalMin(message = "Price needs to be bigger than 0", value = "0.00", inclusive = false)
    @Digits(message = "Price format is not correct or value is too big", integer = 10, fraction = 2)
    @NotNull(message = "Price field cannot be empty")
    private BigDecimal price;
    @NotNull(message = "Please select product category")
    @Positive(message = "Please select product category")
    private Long categoryId;
    private boolean active;
    private String imageFilename;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }
}
