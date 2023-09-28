package ua.com.alevel.web.dto.request;

public class BasketRequestDto {

    private Long productId;
    private Long userId;

    public BasketRequestDto() {
    }

    public BasketRequestDto(Long productId, Long userId) {
        this.userId = userId;
        this.productId = productId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
