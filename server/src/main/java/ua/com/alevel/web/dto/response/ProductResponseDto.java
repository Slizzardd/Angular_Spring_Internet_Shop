package ua.com.alevel.web.dto.response;

import ua.com.alevel.persistence.entity.Product;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductResponseDto extends ResponseDto {

    private String title;
    private String description;
    private String image;
    private BigDecimal price;
    private Integer year;
    private String category;

    private Long quantityInWarehouse;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public ProductResponseDto(Product product) {
        setId(product.getId());
        setUpdated(dateToString(product.getUpdated()));
        setCreated(dateToString(product.getCreated()));
        this.title = product.getTitle();
        this.year = product.getYear();
        this.description = product.getDescription();
        this.category = product.getProductCategory();
        this.image = product.getImageData();
        this.price = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
        this.quantityInWarehouse = product.getQuantity();
    }

    public Long getQuantityInWarehouse() {
        return quantityInWarehouse;
    }

    public void setQuantityInWarehouse(Long quantityInWarehouse) {
        this.quantityInWarehouse = quantityInWarehouse;
    }

    private String dateToString(Date date) {
        synchronized (dateFormat) {
            return dateFormat.format(date);
        }
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ProductResponseDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", year=" + year +
                ", category='" + category + '\'' +
                ", id=" + getId() +
                ", created='" + getCreated() + '\'' +
                ", updated='" + getUpdated() + '\'' +
                '}';
    }
}
