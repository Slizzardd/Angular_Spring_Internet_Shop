package ua.com.alevel.facade;

import ua.com.alevel.exception.AccessException;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.web.dto.request.UserRequestDto;
import ua.com.alevel.web.dto.response.UserResponseDto;

import java.util.List;

public interface UserFacade extends BaseFacade<UserRequestDto, UserResponseDto> {

    UserResponseDto createUser(UserRequestDto userRequestDto) throws EntityExistException;

    UserResponseDto updateUser(UserRequestDto userRequestDto) throws EntityExistException, AccessException;

    void deleteUser(Long id, String actualAuthToken) throws EntityNotFoundException, AccessException;

    UserResponseDto findUserById(Long id, String actualAuthToken) throws EntityNotFoundException, AccessException;

    UserResponseDto findUserByEmail(String email) throws AccessException;

    UserResponseDto findUserByToken(String actualAuthToken);

    List<UserResponseDto> findAllUsers(String actualAuthToken) throws AccessException;
}
