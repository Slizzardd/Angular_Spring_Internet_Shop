package ua.com.alevel.service.impl;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.AuthorizationException;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.logger.InjectLog;
import ua.com.alevel.logger.LoggerLevel;
import ua.com.alevel.logger.LoggerService;
import ua.com.alevel.persistence.entity.User;
import ua.com.alevel.persistence.repository.UserRepository;
import ua.com.alevel.persistence.type.Role;
import ua.com.alevel.service.UserService;
import ua.com.alevel.util.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @InjectLog
    private final LoggerService loggerService;
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoggerService loggerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loggerService = loggerService;
    }

    @Override
    public User createUser(User user) throws EntityExistException {
        checkIfUserExistsByEmail(user.getEmail());
        checkIfUserExistsByPhoneNumber(user.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);
        loggerService.commit(LoggerLevel.INFO, "Create new User with ID= " + result.getId());
        return user;
    }

    @Override
    public User updateUser(User user, String actualAuthToken) throws AccessException, EntityExistException {
        User actualUser = findUserByToken(actualAuthToken);
        checkExist(user.getId());

        if (isAdmin(actualUser.getRole()) || Objects.equals(actualUser.getId(), user.getId())) {
            return  userRepository.save(user);
        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId() + " does not have access to update the user with ID=" + user.getId());
            throw new AccessException("You do not have permission for this operation");
        }
    }

    @Override
    public void deleteUser(Long id, String actualAuthToken) throws EntityNotFoundException, AccessException {
        User actualUser = findUserByToken(actualAuthToken);
        checkExist(id);

        if (isAdmin(actualUser.getRole()) || Objects.equals(actualUser.getId(), id)) {
            userRepository.deleteById(id);
            loggerService.commit(LoggerLevel.INFO, "User with ID= " + id + " was deleted by USER.ID= " + actualUser.getId());

        } else {
            loggerService.commit(LoggerLevel.WARN, "User with ID= " + actualUser.getId() + " does not have access to delete the user with ID=" + id);
            throw new AccessException("You do not have permission for this operation");
        }
    }

    @Override
    public User findUserById(Long id, String actualAuthToken) throws EntityNotFoundException {
        if(id == null){
            throw new EntityNotFoundException("WHAT???");
        }
        checkExist(id);
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findUserByEmail(String email) throws AccessException {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findUserByToken(String actualAuthToken) {
        try {
            String email = JwtUtil.extractUsername(actualAuthToken);
            return userRepository.findByEmail(email).orElse(null);
        } catch (MalformedJwtException e) {
            throw new JwtException(e.toString());
        }
    }

    @Override
    public List<User> findAllUsers(String actualAuthToken) throws AccessException {
        User actualUser = findUserByEmail(JwtUtil.extractUsername(actualAuthToken));

        if (isAdmin(actualUser.getRole())) {
            loggerService.commit(LoggerLevel.INFO, "Admin with ID= " + actualUser.getId() + " requested access to all users");
            return userRepository.findAll();
        } else {
            throw new AccessException("You do not have permission for this data");
        }
    }

    @Override
    public User addProductToUserBasket(User user, Long productId) {
        String currentProductsId = user.getProductsId();
        user.setProductsId(setProductIdForUser(currentProductsId, productId));
        loggerService.commit(LoggerLevel.INFO, "Product with ID= " + productId + " was added to the user basket with ID= " + user.getId());

        return user;
    }

    @Override
    public User deleteOneProductFromBasket(User user, Long productId) {
        String currentProductsId = user.getProductsId();
        List<Long> productList = stringToArrayLong(getElementsByString(currentProductsId));
        productList.remove(productId);
        user.setProductsId(arrayLongToString(productList));
        loggerService.commit(LoggerLevel.INFO, "Product with ID= " + productId + " was deleted from the user basket with ID=  " + user.getId());
        return user;
    }

    @Override
    public User deleteAllProductFromBasket(User user, Long productId) {
        String currentProductsId = user.getProductsId();
        List<Long> productList = stringToArrayLong(getElementsByString(currentProductsId));

        productList.removeIf(element -> Objects.equals(element, productId));
        user.setProductsId(arrayLongToString(productList));

        loggerService.commit(LoggerLevel.INFO, "All products with ID= " + productId + " were removed from the Basket of a user with an ID= " + user.getId());

        return user;
    }

    private void checkIfUserExistsByEmail(String email) throws EntityExistException {
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistException("A user with this email already exists");
        }
    }

    private static String[] getElementsByString(String currentProductsId) {
        if (currentProductsId != null) {
            return currentProductsId.replaceAll("\\s+", "").split(",");
        }else{
            return null;
        }
    }

    private static String arrayLongToString(List<Long> longList) {
        return longList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    private void checkIfUserExistsByPhoneNumber(String phoneNumber) throws EntityExistException {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new EntityExistException("A user with this phone number already exists");
        }
    }

    private Boolean isAdmin(Role role) {
        return role == Role.ADMIN;
    }

    private void checkExist(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("This entity is not found");
        }
    }

    private static String setProductIdForUser(String currentProductsId, Long targetProductId) {
        if (currentProductsId == null) {
            currentProductsId = "";
        }
        if (currentProductsId.isEmpty()) {
            return targetProductId.toString();
        } else {
            return currentProductsId + ", " + targetProductId;
        }
    }

    private static List<Long> stringToArrayLong(String[] elements) {
        List<Long> longList = new ArrayList<>();
        if (elements != null && !Objects.equals(elements[0], "")) {
            for (String element : elements) {
                longList.add(Long.parseLong(element));
            }
        }
        return longList;
    }
}
