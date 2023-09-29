package ua.com.alevel.test_entity;

public class TestOffer {

    private Long id;

    private String address;

    private String linkForPayment;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public String toString() {
        return "TestOffer{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", linkForPayment='" + linkForPayment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
