package ua.com.alevel.web.rest;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.util.ControllerUtil;
import ua.com.alevel.util.JwtUtil;
import ua.com.alevel.web.dto.request.UserRequestDto;
import ua.com.alevel.web.dto.response.JwtResponse;

/**
 * REST controller for user operations.
 */
@RestController
@RequestMapping("/auth/user")
public class UserRestController {

    private final UserFacade userFacade;
    private final AuthenticationManager authenticationManager;

    public UserRestController(UserFacade userFacade, AuthenticationManager authenticationManager) {
        this.userFacade = userFacade;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Create a new user.
     *
     * @param userRequestDto The user request DTO containing user details.
     * @return ResponseEntity with a success message or an error message.
     */
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            return ResponseEntity.ok(userFacade.createUser(userRequestDto));
        } catch (EntityExistException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    /**
     * Log in a user and generate an authentication token.
     *
     * @param userRequestDto The user request DTO containing email and password.
     * @return ResponseEntity with a JWT token and the username.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword())
            );

            String username = authentication.getName();
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(new JwtResponse(JwtUtil.generateJwtToken(username), username));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Get user information by the authentication token.
     *
     * @param actualAuthToken The user's authentication token.
     * @return ResponseEntity with user information or an error message.
     */
    @GetMapping("/getUserByToken")
    public ResponseEntity<?> getUserByToken(@RequestHeader("Authorization") String actualAuthToken) {
        if (ControllerUtil.authCheck(actualAuthToken)) {
            String token = ControllerUtil.getToken(actualAuthToken);
            try {
                return ResponseEntity.ok(userFacade.findUserByToken(token));
            } catch (JwtException e) {
                return ResponseEntity.status(401).body("User not authorized: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update user information.
     *
     * @param userRequestDto The user request DTO containing updated user details.
     * @return ResponseEntity with updated user information or an error message.
     */
    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto userRequestDto) {
        if (ControllerUtil.authCheck(userRequestDto.getAuthToken())) {
            userRequestDto.setAuthToken(ControllerUtil.getToken(userRequestDto.getAuthToken()));
            try {
                return ResponseEntity.ok(userFacade.updateUser(userRequestDto));
            } catch (JwtException e) {
                return ResponseEntity.status(401).body("User not authorized: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
