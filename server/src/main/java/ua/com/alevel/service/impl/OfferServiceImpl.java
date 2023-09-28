package ua.com.alevel.service.impl;

import org.springframework.stereotype.Service;
import ua.com.alevel.persistence.entity.Offer;
import ua.com.alevel.persistence.repository.OfferRepository;
import ua.com.alevel.service.OfferService;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public Offer updateOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public Offer findOfferById(Long offerId) {
        return offerRepository.findById(offerId).orElse(null);
    }

    @Override
    public Offer findOfferByLinkForPayment(String linkForPayment) {
        return offerRepository.findOfferByLinkForPayment(linkForPayment);
    }
}
