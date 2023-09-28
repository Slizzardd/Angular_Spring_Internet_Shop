package ua.com.alevel.service;

import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.persistence.entity.User;

import java.util.List;

public interface UserService extends BaseService<User> {

    User createUser(User user) throws EntityExistException;

    User updateUser(User user, String actualAuthToken) throws EntityExistException, AccessException;

    void deleteUser(Long id, String actualAuthToken) throws EntityNotFoundException, AccessException;

    User findUserById(Long id, String actualAuthToken) throws EntityNotFoundException, AccessException;

    User findUserByEmail(String email) throws AccessException;

    User findUserByToken(String actualAuthToken);

    List<User> findAllUsers(String actualAuthToken) throws AccessException;

    User addProductToUserBasket(User user, Long productId);

    User deleteOneProductFromBasket(User user, Long productId);

    User deleteAllProductFromBasket(User user, Long productId);
}
