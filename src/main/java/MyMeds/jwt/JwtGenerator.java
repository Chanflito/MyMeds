package MyMeds.jwt;

import MyMeds.App.User;

import java.util.Map;

public interface JwtGenerator {
    Map<String,String> generateToken(User user);
}
