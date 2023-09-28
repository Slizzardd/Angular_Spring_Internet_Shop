package ua.com.alevel.web.dto.response;

import ua.com.alevel.persistence.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserResponseDto extends ResponseDto {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean enabled;
    private String role;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public UserResponseDto(User user) {
        setId(user.getId());
        setCreated(dateToString(user.getCreated()));
        setUpdated(dateToString(user.getUpdated()));
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole().toString();
        this.enabled = user.isEnabled();
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String dateToString(Date date) {
        return dateFormat.format(date);
    }

    private String enabledToString(Boolean enabled) {
        return enabled ? "active" : "deactivate";
    }

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", enabled=" + enabled +
                ", role='" + role + '\'' +
                '}';
    }
}
