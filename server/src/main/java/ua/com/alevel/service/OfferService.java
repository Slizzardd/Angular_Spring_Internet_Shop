package ua.com.alevel.service;

import ua.com.alevel.persistence.entity.Offer;

public interface OfferService extends BaseService<Offer> {

    Offer createOffer(Offer offer);
    Offer updateOffer(Offer offer);

    Offer findOfferById(Long offerId);

    Offer findOfferByLinkForPayment(String linkForPayment);
}
