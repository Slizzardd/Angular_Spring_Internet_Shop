package ua.com.alevel.web.rest;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.facade.ProductFacade;
import ua.com.alevel.util.ControllerUtil;
import ua.com.alevel.web.dto.request.ProductRequestDto;
import ua.com.alevel.web.dto.response.ProductResponseDto;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductFacade productFacade;

    public ProductRestController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    /**
     * Create a new product.
     *
     * @param productRequestDto The product request DTO containing product details.
     * @return ResponseEntity with the created product information.
     */
    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto productRequestDto,
                                                     @RequestHeader("Authorization") String actualAuthToken) {
        if (ControllerUtil.authCheck(actualAuthToken)) {
            try {
                return ResponseEntity.ok(productFacade.create(productRequestDto, ControllerUtil.getToken(actualAuthToken)));
            } catch (AccessException e) {
                return ResponseEntity.status(403).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update an existing product.
     *
     * @param productRequestDto The product request DTO containing updated product details.
     * @return ResponseEntity with the updated product information.
     */
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ProductRequestDto productRequestDto,
                                    @RequestHeader("Authorization") String actualAuthToken) {
        if (ControllerUtil.authCheck(actualAuthToken)) {
            try {
                return ResponseEntity.ok(productFacade.update(productRequestDto, ControllerUtil.getToken(actualAuthToken)));
            } catch (AccessException e) {
                return ResponseEntity.status(403).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Delete a product by its ID.
     *
     * @param productId The ID of the product to deleteUser.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestHeader("productId") Long productId,
                                    @RequestHeader("Authorization") String actualAuthToken) {
        if (ControllerUtil.authCheck(actualAuthToken)) {
            try {
                productFacade.delete(productId, ControllerUtil.getToken(actualAuthToken));
                return ResponseEntity.ok(null);
            } catch (AccessException e) {
                return ResponseEntity.status(403).body(e.toString());
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(404).body(e.toString());
            } catch (JwtException e) {
                return ResponseEntity.status(401).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Find a product by its ID.
     *
     * @param id The ID of the product to find.
     * @return ResponseEntity with the product information or an error message.
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productFacade.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Find all products.
     *
     * @return ResponseEntity with a list of all products.
     */
    @GetMapping("/findAll")
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        return ResponseEntity.ok(productFacade.findAll());
    }
}
