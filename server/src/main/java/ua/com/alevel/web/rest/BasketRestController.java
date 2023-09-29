package ua.com.alevel.web.rest;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.exception.QuantityOfProductException;
import ua.com.alevel.facade.ProductFacade;
import ua.com.alevel.util.ControllerUtil;
import ua.com.alevel.web.dto.request.BasketRequestDto;

/**
 * REST Controller for managing the user's basket.
 */
@RestController
@RequestMapping("/basket")
public class BasketRestController {

    private final ProductFacade productFacade;

    public BasketRestController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    /**
     * Add a product to the user's basket.
     *
     * @param actualAuthToken  The Bearer Token for authorization.
     * @param basketRequestDto The request body containing product and user information.
     */
    @PostMapping("/addProductToBasket")
    public ResponseEntity<?> addProductToBasket(@RequestHeader("Authorization") String actualAuthToken,
                                                @RequestBody BasketRequestDto basketRequestDto) {
        if (ControllerUtil.authCheck(actualAuthToken)) {
            try {
                productFacade.addProductToBasket(ControllerUtil.getToken(actualAuthToken), basketRequestDto);
                return ResponseEntity.ok().body(null);
            } catch (EntityNotFoundException | QuantityOfProductException e) {
                return ResponseEntity.status(404).body(e.getMessage());
            } catch (AccessException e) {
                return ResponseEntity.status(403).body(e.getMessage());
            } catch (JwtException e) {
                return ResponseEntity.status(401).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Delete one product from the user's basket.
     *
     * @param actualAuthToken  The Bearer Token for authorization.
     * @param basketRequestDto The request body containing product and user information.
     */
    @PostMapping("/deleteOneProductFromBasket")
    public ResponseEntity<?> deleteOneProductFromBasket(@RequestHeader("Authorization") String actualAuthToken,
                                                        @RequestBody BasketRequestDto basketRequestDto) {
        if (ControllerUtil.authCheck(actualAuthToken)) {
            try {
                productFacade.deleteOneProductFromBasket(ControllerUtil.getToken(actualAuthToken), basketRequestDto);
                return ResponseEntity.ok(null);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(404).body(e.getMessage());
            } catch (AccessException e) {
                return ResponseEntity.status(403).body(e.getMessage());
            } catch (JwtException e) {
                return ResponseEntity.status(401).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Delete all products from the user's basket.
     *
     * @param actualAuthToken  The Bearer Token for authorization.
     * @param basketRequestDto The request body containing user information.
     */
    @PostMapping("/deleteAllProductFromBasket")
    public ResponseEntity<?> deleteAllProductFromBasket(@RequestHeader("Authorization") String actualAuthToken,
                                                        @RequestBody BasketRequestDto basketRequestDto) {
        if (ControllerUtil.authCheck(actualAuthToken)) {
            try {
                productFacade.deleteAllProductsFromBasket(ControllerUtil.getToken(actualAuthToken), basketRequestDto);
                return ResponseEntity.ok(null);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(404).body(e.getMessage());
            } catch (AccessException e) {
                return ResponseEntity.status(403).body(e.getMessage());
            } catch (JwtException e) {
                return ResponseEntity.status(401).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Find all products in the user's basket by user ID.
     *
     * @param actualAuthToken The Bearer Token for authorization.
     * @param id              The user's ID.
     * @return ResponseEntity containing a list of products in the user's basket.
     */
    @GetMapping("/findAllProductByUserBasket/{id}")
    public ResponseEntity<?> findAllProductByUserBasket(@RequestHeader("Authorization") String actualAuthToken,
                                                        @PathVariable Long id) {
        if (ControllerUtil.authCheck(actualAuthToken)) {
            try {
                return ResponseEntity.ok(productFacade.findAllProductsInUserBasket(
                        ControllerUtil.getToken(actualAuthToken), id));
            } catch (EntityNotFoundException | NullPointerException e) {
                return ResponseEntity.status(404).body(e.getMessage());
            } catch (AccessException e) {
                return ResponseEntity.status(403).body(e.getMessage());
            } catch (JwtException e) {
                return ResponseEntity.status(401).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

