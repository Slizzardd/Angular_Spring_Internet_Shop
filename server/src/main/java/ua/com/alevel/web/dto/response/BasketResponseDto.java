package ua.com.alevel.web.dto.response;

import ua.com.alevel.persistence.entity.Product;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasketResponseDto extends ResponseDto {

    private ProductResponseDto product;

    private Integer quantityProductOnBasket;

    public BasketResponseDto(Product product, Integer quantity) {
        setId(product.getId());
        setUpdated(dateToString(product.getUpdated()));
        setCreated(dateToString(product.getCreated()));
        this.product = new ProductResponseDto(product);
        this.quantityProductOnBasket = quantity;
    }

    private String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    public ProductResponseDto getProduct() {
        return product;
    }

    public void setProduct(ProductResponseDto product) {
        this.product = product;
    }

    public Integer getQuantityProductOnBasket() {
        return quantityProductOnBasket;
    }

    public void setQuantityProductOnBasket(Integer quantityProductOnBasket) {
        this.quantityProductOnBasket = quantityProductOnBasket;
    }
}
