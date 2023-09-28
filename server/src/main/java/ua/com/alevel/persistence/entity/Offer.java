package ua.com.alevel.persistence.entity;

import ua.com.alevel.persistence.type.StatusOffer;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "offers")
public class Offer extends BaseEntity{

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name = "products_id")
    private String productsId;

    @Column(name = "payment_link")
    private String linkForPayment;

    @Column(name = "delivery_address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusOffer statusOffer;

    public Offer() {
        super();
        this.statusOffer = StatusOffer.awaitingPayment;
    }

    public StatusOffer getStatusOffer() {
        return statusOffer;
    }

    public void setStatusOffer(StatusOffer statusOffer) {
        this.statusOffer = statusOffer;
    }

    public String getProductsId() {
        return productsId;
    }

    public void setProductsId(String productsId) {
        this.productsId = productsId;
    }

    public String getLinkForPayment() {
        return linkForPayment;
    }

    public void setLinkForPayment(String linkForPayment) {
        this.linkForPayment = linkForPayment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
