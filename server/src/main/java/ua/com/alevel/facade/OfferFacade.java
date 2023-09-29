package ua.com.alevel.facade;

import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.web.dto.request.OfferRequestDto;
import ua.com.alevel.web.dto.response.OfferResponseDto;

public interface OfferFacade extends BaseFacade<OfferRequestDto, OfferResponseDto> {

    OfferResponseDto createOffer(OfferRequestDto offerRequestDto, String actualAuthToken) throws AccessException;
    void successfulPayment(OfferRequestDto offerRequestDto, String actualAuthToken);

    OfferResponseDto findOfferByLinkForPayment(String linkForPayment) throws EntityNotFoundException;
}
