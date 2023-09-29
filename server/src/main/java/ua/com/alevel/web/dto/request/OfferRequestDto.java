package ua.com.alevel.web.dto.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ua.com.alevel.persistence.entity.User;
import ua.com.alevel.persistence.type.StatusOffer;

public class OfferRequestDto extends RequestDto{

    private Long userId;

    private Long id;

    private StatusOffer status;

    private String address;

    public OfferRequestDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusOffer getStatus() {
        return status;
    }

    public void setStatus(StatusOffer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
