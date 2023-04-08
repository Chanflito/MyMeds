package MyMeds.jwt;

import MyMeds.App.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class JwtGeneratorImpl implements  JwtGenerator {
    //This class generates token.
    public static final SecretKey secretKey= Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${app.jwttoken.message}")
    private String message;

    @Override
    public Map<String, String> generateToken(User user) {
        String jwtToken="";
        jwtToken= Jwts.builder().
                setSubject(user.getUsername()).
                setIssuedAt(new Date()).
                claim("Role",user.getUserType()).
                claim("id",user.getPrimarykey()).
                signWith(secretKey).
                compact();

        Map<String,String> jwtTokenGen=new HashMap<>();
        jwtTokenGen.put("token",jwtToken);
        jwtTokenGen.put("message",message);
        return jwtTokenGen;
    }
}
