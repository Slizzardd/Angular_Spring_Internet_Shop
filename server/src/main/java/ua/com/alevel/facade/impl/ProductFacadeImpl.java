package ua.com.alevel.facade.impl;

import org.springframework.stereotype.Service;
import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.exception.QuantityOfProductException;
import ua.com.alevel.facade.ProductFacade;
import ua.com.alevel.logger.InjectLog;
import ua.com.alevel.logger.LoggerLevel;
import ua.com.alevel.logger.LoggerService;
import ua.com.alevel.persistence.entity.Product;
import ua.com.alevel.persistence.entity.User;
import ua.com.alevel.persistence.type.Role;
import ua.com.alevel.service.ProductService;
import ua.com.alevel.service.UserService;
import ua.com.alevel.web.dto.request.BasketRequestDto;
import ua.com.alevel.web.dto.request.ProductRequestDto;
import ua.com.alevel.web.dto.response.BasketResponseDto;
import ua.com.alevel.web.dto.response.ProductResponseDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;

    private final UserService userService;

    @InjectLog
    private final LoggerService loggerService;

    public ProductFacadeImpl(ProductService productService, UserService userService, LoggerService loggerService) {
        this.productService = productService;
        this.userService = userService;
        this.loggerService = loggerService;
    }

    @Override
    public ProductResponseDto create(ProductRequestDto requestDto, String authToken) throws AccessException {
        User actualUser = userService.findUserByToken(authToken);

        if (isAdmin(actualUser)) {
            Product product = new Product();
            setProductInformation(requestDto, product);
            product = productService.createProduct(product);

            loggerService.commit(LoggerLevel.INFO, "The product with ID= " + product.getId()
                    + " was CREATE by the administrator with ID= " + actualUser.getId());
            return new ProductResponseDto(product);
        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId() + " denied access to product CREATE");
            throw new AccessException("You do not have permission for this operation");
        }
    }

    @Override
    public ProductResponseDto update(ProductRequestDto requestDto, String authToken) throws AccessException {
        User actualUser = userService.findUserByToken(authToken);

        if (isAdmin(actualUser)) {
            Product product = productService.findProductById(requestDto.getId());
            setProductInformation(requestDto, product);
            product = productService.updateProduct(product);

            loggerService.commit(LoggerLevel.INFO, "The product with ID= " + product.getId()
                    + " was UPDATE by the administrator with ID= " + actualUser.getId());

            return new ProductResponseDto(product);
        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId() + " denied access to product UPDATE");
            throw new AccessException("You do not have permission for this operation");
        }
    }

    @Override
    public void delete(Long productId, String authToken) throws AccessException {
        User actualUser = userService.findUserByToken(authToken);

        if (isAdmin(userService.findUserByToken(authToken))) {
            deletingRelationWhenDeletingProduct(productId, authToken);

            productService.deleteProduct(productId);
            loggerService.commit(LoggerLevel.INFO, "The product with ID= " + productId
                    + " was DELETE by the administrator with ID= " + actualUser.getId());
        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId() + " denied access to product DELETE");
            throw new AccessException("You do not have permission for this operation");
        }
    }


    @Override
    public ProductResponseDto findById(Long productId) throws EntityNotFoundException {
        return new ProductResponseDto(productService.findProductById(productId));
    }

    @Override
    public List<ProductResponseDto> findAll() {
        List<Product> productList = productService.findAllProducts();

        return productList.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BasketResponseDto> findAllProductsInUserBasket(String authToken, Long userId) throws AccessException {
        User actualUser = userService.findUserByToken(authToken);
        User targetUser = userService.findUserById(userId, authToken);

        if (isAdminOrIsOwner(actualUser, targetUser)) {
            String productsId = targetUser.getProductsId();

            if (productsId.isEmpty()) {
                return new ArrayList<>();
            }

            Map<Long, Integer> productQuantityMap = countOccurrences(productsId);

            List<BasketResponseDto> basketResponseDtos = new ArrayList<>();

            for (Map.Entry<Long, Integer> entry : productQuantityMap.entrySet()) {
                basketResponseDtos.add(new BasketResponseDto(
                        productService.findProductById(entry.getKey()),
                        entry.getValue()
                ));
            }

            return basketResponseDtos;
        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId()
                    + " denied access to RECEIVE data about the basket of a user with ID= " + userId);
            throw new AccessException("You do not have permission for this data");
        }
    }

    @Override
    public void addProductToBasket(String authToken, BasketRequestDto basketRequestDto) {
        User actualUser = userService.findUserByToken(authToken);
        User targetUser = userService.findUserById(basketRequestDto.getUserId(), authToken);

        if (isAdminOrIsOwner(actualUser, targetUser)) {
            Product product = productService.findProductById(basketRequestDto.getProductId());

            if (product.getQuantity() >= 1L) {
                userService.updateUser(userService.addProductToUserBasket(targetUser,
                        basketRequestDto.getProductId()), authToken);
                product.setQuantity(removeQuantity(product.getQuantity(), 1L));
                productService.updateProduct(product);
            }else {
                throw new QuantityOfProductException("The product is out of stock");
            }

        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId()
                    + " denied access to ADD product to basket of user with ID= " + targetUser.getId());

            throw new AccessException("You do not have permission for this data");
        }
    }

    @Override
    public void deleteOneProductFromBasket(String authToken, BasketRequestDto basketRequestDto) throws AccessException, EntityNotFoundException {
        User actualUser = userService.findUserByToken(authToken);
        User targetUser = userService.findUserById(basketRequestDto.getUserId(), authToken);

        if (isAdminOrIsOwner(actualUser, targetUser)) {
            Product product = productService.findProductById(basketRequestDto.getProductId());
            product.setQuantity(addQuantity(product.getQuantity(), 1L));

            userService.updateUser(userService.deleteOneProductFromBasket(
                    targetUser, basketRequestDto.getProductId()), authToken);

            productService.updateProduct(product);
        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId()
                    + " denied access to DELETE product from basket of user with ID= " + targetUser.getId());
            throw new AccessException("You do not have permission for this data");
        }
    }


    @Override
    public void deleteAllProductsFromBasket(String authToken, BasketRequestDto basketRequestDto) {
        User actualUser = userService.findUserByToken(authToken);
        User targetUser = userService.findUserById(basketRequestDto.getUserId(), authToken);

        if (isAdminOrIsOwner(actualUser, targetUser)) {

            Product product = productService.findProductById(basketRequestDto.getProductId());

            String basket = targetUser.getProductsId();
            userService.updateUser(userService.deleteAllProductFromBasket(targetUser, basketRequestDto.getProductId()), authToken);

            product.setQuantity(addQuantity(
                    product.getQuantity(), countOccurrences(basket, basketRequestDto.getProductId().toString())));
            productService.updateProduct(product);
        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId() + " denied access to DELETE all products with ID= "
                    + basketRequestDto.getProductId() + " from basket of user with ID= " + targetUser.getId());
            throw new AccessException("You do not have permission for this operation");
        }
    }


    private static void setProductInformation(ProductRequestDto productRequestDto, Product product) {
        product.setTitle(productRequestDto.getTitle());
        product.setDescription(productRequestDto.getDescription());
        product.setImageData(productRequestDto.getImage());
        product.setPrice(productRequestDto.getPrice());
        product.setYear(productRequestDto.getYear());
        product.setProductCategory(productRequestDto.getCategory());
        product.setQuantity(productRequestDto.getQuantityInWarehouse());
    }

    private static Map<Long, Integer> countOccurrences(String input) {
        Map<Long, Integer> occurrenceMap = new HashMap<>();
        String[] numbers = input.split(",\\s*");

        for (String numStr : numbers) {
            Long num = Long.parseLong(numStr.trim());
            occurrenceMap.put(num, occurrenceMap.getOrDefault(num, 0) + 1);
        }

        return occurrenceMap;
    }

    private boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    private boolean isAdminOrIsOwner(User actualUser, User targetUser) {
        return isAdmin(actualUser) || Objects.equals(actualUser.getId(), targetUser.getId());
    }

    private void deletingRelationWhenDeletingProduct(Long productId, String actualAuthToken) {
        List<User> allUsersInDB = userService.findAllUsers(actualAuthToken);
        for (User user : allUsersInDB) {
            deleteAllProductsFromBasket(actualAuthToken, new BasketRequestDto(productId, user.getId()));
        }
    }

    private Long removeQuantity(Long quantity, Long num) {
        quantity = quantity - num;
        return quantity;
    }

    private Long addQuantity(Long quantity, Long num) {
        quantity = quantity + num;
        return quantity;
    }

    public static Long countOccurrences(String input, String target) {
        Long count = 0L;
        if (input == null) {
            return count;
        }
        String[] numbers = input.split(",\\s*");
        for (String number : numbers) {
            if (number.trim().equals(target)) {
                count++;
            }
        }
        return count;
    }
}
