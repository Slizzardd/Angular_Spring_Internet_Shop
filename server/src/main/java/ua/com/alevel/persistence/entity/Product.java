package ua.com.alevel.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private Integer year;

    @Column(name = "price", scale = 2)
    private BigDecimal price;
    @Column(name = "image_data")
    private String imageData;

    @Column(name = "description", length = 3000)
    private String description;

    @Column(name = "category")
    private String productCategory;

    @Column(name = "quantity")
    private Long quantity;


    public Product() {
        super();
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
