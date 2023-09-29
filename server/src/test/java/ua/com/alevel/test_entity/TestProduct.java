package ua.com.alevel.test_entity;

import java.math.BigDecimal;
import java.util.Date;

public class TestProduct {
    private Long id;
    private String created;
    private String updated;
    private String title;
    private String description;
    private String image;
    private BigDecimal price;
    private int year;
    private String category;
    private Long quantityInWarehouse;

    // Геттеры и сеттеры для всех полей

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getQuantityInWarehouse() {
        return quantityInWarehouse;
    }

    public void setQuantityInWarehouse(long quantityInWarehouse) {
        this.quantityInWarehouse = quantityInWarehouse;
    }
}
