package ua.com.alevel.facade.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.exception.TimeForPaymentException;
import ua.com.alevel.facade.OfferFacade;
import ua.com.alevel.logger.InjectLog;
import ua.com.alevel.logger.LoggerLevel;
import ua.com.alevel.logger.LoggerService;
import ua.com.alevel.persistence.entity.Offer;
import ua.com.alevel.persistence.entity.User;
import ua.com.alevel.persistence.type.Role;
import ua.com.alevel.persistence.type.StatusOffer;
import ua.com.alevel.service.OfferService;
import ua.com.alevel.service.UserService;
import ua.com.alevel.util.ErrorMessageUtil;
import ua.com.alevel.web.dto.request.OfferRequestDto;
import ua.com.alevel.web.dto.response.OfferResponseDto;

import java.util.Objects;
@Service
public class OfferFacadeImpl implements OfferFacade {

    private final OfferService offerService;

    private final UserService userService;

    @InjectLog
    private final LoggerService loggerService;

    private static final Integer TIME_FOR_SLEEP = 10 * 60 * 1000; // 10 min in millis

    public OfferFacadeImpl(OfferService offerService, UserService userService, LoggerService loggerService) {
        this.offerService = offerService;
        this.userService = userService;
        this.loggerService = loggerService;
    }

    @Override
    public OfferResponseDto createOffer(OfferRequestDto offerRequestDto, String actualAuthToken) {
        User actualUser = userService.findUserByToken(actualAuthToken);

        User targetUser = userService.findUserById(offerRequestDto.getUserId(), actualAuthToken);

        if (isAdmin(actualUser) || Objects.equals(actualUser.getId(), targetUser.getId())) {

            Offer offer = setOfferMainInformation(offerRequestDto, targetUser);

            if (targetUser.getProductsId().isEmpty()) {
                throw new EntityNotFoundException(ErrorMessageUtil.EMPTY_BASKET_ERROR_MESSAGE);
            }


            Offer result = offerService.createOffer(offer);

            loggerService.commit(LoggerLevel.INFO, "User with ID= " + targetUser.getId() + " CREATE an offer with ID= " + result.getId());

            OfferResponseDto offerResponseDto = new OfferResponseDto(result);
            Thread thread = new Thread(() -> {
                try {
                    startPayment(offerResponseDto.getId());
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            });

            thread.start();
            return new OfferResponseDto(result);
        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId()
                    + " denied access to CREATE offer for user with ID= " + targetUser.getId());
            throw new AccessException(ErrorMessageUtil.ACCESS_DENIED_ERROR_MESSAGE);
        }
    }

    @Override
    public void successfulPayment(OfferRequestDto offerRequestDto, String actualAuthToken) {
        Offer offer = offerService.findOfferById(offerRequestDto.getId());
        if (offer.getStatusOffer() == StatusOffer.canceled){
            throw new TimeForPaymentException(ErrorMessageUtil.TIME_FOR_PAYMENT_ERROR_MESSAGE);
        }
        if(offer.getStatusOffer() == StatusOffer.paid){
            throw new EntityExistException(ErrorMessageUtil.ALREADY_PAID_ERROR_MESSAGE);
        }
        offer.setStatusOffer(StatusOffer.paid);
        offerService.updateOffer(offer);

        User user = userService.findUserById(offer.getUser().getId(), actualAuthToken);
        user.setProductsId("");
        userService.updateUser(user, actualAuthToken);

        loggerService.commit(LoggerLevel.INFO, "Offer with ID= " + offer.getId()
                + " was successfully paid by the user with ID= " + user.getId());
    }

    @Override
    public OfferResponseDto findOfferByLinkForPayment(String linkForPayment) throws EntityNotFoundException, NullPointerException {
        Offer offer = offerService.findOfferByLinkForPayment(linkForPayment);
        if (offer.getStatusOffer() == StatusOffer.awaitingPayment) {
            return new OfferResponseDto(offer);
        } else {
            throw new EntityNotFoundException(ErrorMessageUtil.OFFER_NOT_FOUND_ERROR_MESSAGE);
        }
    }

    private void startPayment(Long offerId) throws InterruptedException {
            Thread.sleep(TIME_FOR_SLEEP);

            Offer offer = offerService.findOfferById(offerId);

            if (offer.getStatusOffer() == StatusOffer.awaitingPayment) {
                offer.setStatusOffer(StatusOffer.canceled);
                loggerService.commit(LoggerLevel.INFO, "The order with ID= " + offer.getId()
                        + "was not paid by the user with ID= " + offer.getUser().getId());
            }

            offerService.updateOffer(offer);
    }


    private boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    private Offer setOfferMainInformation(OfferRequestDto offerRequestDto, User user){
        Offer offer = new Offer();
        offer.setUser(user);
        offer.setLinkForPayment(RandomStringUtils.randomAlphabetic(100));
        offer.setProductsId(user.getProductsId());
        offer.setAddress(offerRequestDto.getAddress());
        return offer;
    }
}
