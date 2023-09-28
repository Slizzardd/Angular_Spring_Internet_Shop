package ua.com.alevel.facade;

import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.web.dto.request.BasketRequestDto;
import ua.com.alevel.web.dto.request.ProductRequestDto;
import ua.com.alevel.web.dto.response.BasketResponseDto;
import ua.com.alevel.web.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductFacade extends BaseFacade<ProductRequestDto, ProductResponseDto> {

    ProductResponseDto create(ProductRequestDto req, String actualAuthToken) throws AccessException;

    ProductResponseDto update(ProductRequestDto req, String actualAuthToken) throws AccessException;

    void delete(Long productId, String actualAuthToken) throws AccessException;
    ProductResponseDto findById(Long productId) throws EntityNotFoundException;

    List<ProductResponseDto> findAll();

    List<BasketResponseDto> findAllProductsInUserBasket(String actualAuthToken, Long userId);

    void addProductToBasket(String actualAuthToken, BasketRequestDto basketRequestDto);

    void deleteOneProductFromBasket(String actualAuthToken, BasketRequestDto basketRequestDto);

    void deleteAllProductsFromBasket(String actualAuthToken, BasketRequestDto basketRequestDto);
}
