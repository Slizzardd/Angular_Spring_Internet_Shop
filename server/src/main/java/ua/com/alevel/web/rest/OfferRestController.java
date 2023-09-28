package ua.com.alevel.web.rest;

import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.exception.TimeForPaymentException;
import ua.com.alevel.facade.OfferFacade;
import ua.com.alevel.util.ControllerUtil;
import ua.com.alevel.web.dto.request.OfferRequestDto;

@RestController
@RequestMapping("/offers")
public class OfferRestController {

    private final OfferFacade offerFacade;

    public OfferRestController(OfferFacade offerFacade) {
        this.offerFacade = offerFacade;
    }

    /**
     * Create a new offer.
     *
     * @param actualAuthToken   User's authentication token.
     * @param offerRequestDto   DTO containing offer data.
     * @return ResponseEntity with the created offer or an error message.
     */
    @PostMapping("/createOffer")
    public ResponseEntity<?> createOffer(
            @RequestHeader("Authorization") String actualAuthToken,
            @RequestBody OfferRequestDto offerRequestDto) {
        try {
            return ResponseEntity.ok(offerFacade.createOffer(offerRequestDto, ControllerUtil.getToken(actualAuthToken)));
        } catch (AccessException e) {
            return ResponseEntity.status(403).body(e.toString());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.toString());
        } catch (JwtException e){
            return ResponseEntity.status(401).body(e.toString());
        }
    }

    /**
     * Start the payment process for an offer.
     *
     * @param actualAuthToken   User's authentication token.
     * @param offerRequestDto   DTO containing offer data.
     * @return ResponseEntity with the result of the payment initiation or an error message.
     */
    @PostMapping("/startPayment")
    public void startPayment(
            @RequestHeader("Authorization") String actualAuthToken,
            @RequestBody OfferRequestDto offerRequestDto) {
        try {
            offerFacade.startPayment(offerRequestDto, ControllerUtil.getToken(actualAuthToken));
        } catch (AccessException e) {
            ResponseEntity.status(403).body(e.toString());
        } catch (InterruptedException e) {
            ResponseEntity.status(500).body("Unexpected error, please try again");
        } catch (EntityNotFoundException e) {
            ResponseEntity.status(404).body(e.toString());
        }
    }

    /**
     * Complete the payment process for an offer.
     *
     * @param actualAuthToken   User's authentication token.
     * @param offerRequestDto   DTO containing offer data.
     * @return ResponseEntity with the result of the payment completion or an error message.
     */
    @PostMapping("/successfulPayment")
    public void successfulPayment(
            @RequestHeader("Authorization") String actualAuthToken,
            @RequestBody OfferRequestDto offerRequestDto) {
        try {
            offerFacade.successfulPayment(offerRequestDto, ControllerUtil.getToken(actualAuthToken));
        } catch (AccessException e) {
            ResponseEntity.status(403).body(e.toString());
        } catch (EntityNotFoundException e) {
            ResponseEntity.status(404).body(e.toString());
        } catch (EntityExistException e){
            ResponseEntity.status(409).body(e.toString());
        } catch (TimeForPaymentException e){
            ResponseEntity.status(402).body(e.toString());
        }
    }

    /**
     * Retrieve an offer by its payment link.
     *
     * @param linkForPayment Payment link for the offer.
     * @return ResponseEntity with the offer or an error message.
     */
    @GetMapping("/getOfferByLinkForPayment/{linkForPayment}")
    public ResponseEntity<?> findOfferByLinkForPayment(@PathVariable String linkForPayment) {
        try {
            return ResponseEntity.ok(offerFacade.findOfferByLinkForPayment(linkForPayment));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.toString());
        } catch (NullPointerException e) {
            return ResponseEntity.status(400).body(e.toString());
        }
    }
}
