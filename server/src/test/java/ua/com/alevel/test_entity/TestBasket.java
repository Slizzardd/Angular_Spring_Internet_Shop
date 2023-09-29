package ua.com.alevel.test_entity;

public class TestBasket {

    private TestProduct product;

    private Long quantityProductOnBasket;

    public Long getQuantityProductOnBasket() {
        return quantityProductOnBasket;
    }

    public void setQuantityProductOnBasket(Long quantityProductOnBasket) {
        this.quantityProductOnBasket = quantityProductOnBasket;
    }

    public TestProduct getProduct() {
        return product;
    }

    public void setProduct(TestProduct product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "TestBasket{" +
                "product=" + product +
                ", quantityProductOnBasket=" + quantityProductOnBasket +
                '}';
    }
}
