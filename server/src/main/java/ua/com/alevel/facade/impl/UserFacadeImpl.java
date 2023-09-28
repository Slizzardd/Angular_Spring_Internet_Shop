package ua.com.alevel.facade.impl;

import org.springframework.stereotype.Service;
import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.persistence.entity.User;
import ua.com.alevel.service.UserService;
import ua.com.alevel.web.dto.request.UserRequestDto;
import ua.com.alevel.web.dto.response.UserResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    public UserFacadeImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) throws EntityExistException {
        User user = new User();

        setUserData(requestDto, user);

        user.setPassword(requestDto.getPassword());

        return new UserResponseDto(userService.createUser(user));
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto requestDto) throws EntityExistException, AccessException {
        User user = userService.findUserByEmail(requestDto.getEmail());
        setUserData(requestDto, user);

        user.setEnabled(requestDto.getEnabled());

        return new UserResponseDto(userService.updateUser(user, requestDto.getAuthToken()));
    }

    @Override
    public void deleteUser(Long id, String authToken) throws EntityNotFoundException, AccessException {
        userService.deleteUser(id, authToken);
    }

    @Override
    public UserResponseDto findUserById(Long id, String authToken) throws EntityNotFoundException, AccessException {
        return new UserResponseDto(userService.findUserById(id, authToken));
    }

    @Override
    public UserResponseDto findUserByEmail(String email) throws AccessException {
        return new UserResponseDto(userService.findUserByEmail(email));
    }

    @Override
    public UserResponseDto findUserByToken(String authToken) {
        return new UserResponseDto(userService.findUserByToken(authToken));
    }


    @Override
    public List<UserResponseDto> findAllUsers(String authToken) throws AccessException {
        List<User> users = userService.findAllUsers(authToken);

        return users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    private void setUserData(UserRequestDto userRequestDto, User user) {
        user.setEmail(userRequestDto.getEmail());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
    }
}
