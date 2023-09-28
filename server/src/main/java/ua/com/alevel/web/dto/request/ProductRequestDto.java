package ua.com.alevel.web.dto.request;

import java.math.BigDecimal;

public class ProductRequestDto extends RequestDto{

    private Long id;
    private String title;
    private Integer year;
    private String description;
    private String image;
    private BigDecimal price;
    private String category;
    private Long quantityInWarehouse;

    public ProductRequestDto() { }

    public Long getQuantityInWarehouse() {
        return quantityInWarehouse;
    }

    public void setQuantityInWarehouse(Long quantityInWarehouse) {
        this.quantityInWarehouse = quantityInWarehouse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }
}
