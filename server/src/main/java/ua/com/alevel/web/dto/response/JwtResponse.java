package ua.com.alevel.web.dto.response;

public class JwtResponse {

    private String jwtToken;

    public JwtResponse(String jwtToken, String username) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
