package ua.com.alevel.util;

public final class ControllerUtil {

    private ControllerUtil(){throw new IllegalStateException("This is utility class");}

    public static boolean authCheck(String token){
        return token != null && token.startsWith("Bearer ");
    }

    public static String getToken(String actualAuthToken){
        return actualAuthToken.substring(7); // Get token after "Bearer "
    }
}
