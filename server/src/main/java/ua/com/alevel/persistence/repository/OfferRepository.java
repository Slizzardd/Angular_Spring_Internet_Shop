package ua.com.alevel.persistence.repository;

import org.springframework.stereotype.Repository;
import ua.com.alevel.persistence.entity.Offer;

@Repository
public interface OfferRepository extends BaseRepository<Offer> {

    Offer findOfferByLinkForPayment(String linkForPayment);
}
