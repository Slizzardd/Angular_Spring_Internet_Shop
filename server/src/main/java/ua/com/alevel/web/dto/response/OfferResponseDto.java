package ua.com.alevel.web.dto.response;

import ua.com.alevel.persistence.entity.Offer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OfferResponseDto extends ResponseDto {

    private String address;

    private UserResponseDto user;

    private String linkForPayment;

    private String status;

    public OfferResponseDto(Offer offer) {
        setId(offer.getId());
        setCreated(dateToString(offer.getCreated()));
        setUpdated(dateToString(offer.getUpdated()));
        this.address = offer.getAddress();
        this.user = new UserResponseDto(offer.getUser());
        this.linkForPayment = offer.getLinkForPayment();
        this.status = offer.getStatusOffer().toString();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }

    public String getLinkForPayment() {
        return linkForPayment;
    }

    public void setLinkForPayment(String linkForPayment) {
        this.linkForPayment = linkForPayment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }


    @Override
    public String toString() {
        return "OfferResponseDto{" +
                "address='" + address + '\'' +
                ", user=" + user +
                ", linkForPayment='" + linkForPayment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
