package bbk.challenge.atm.authentication;

import bbk.challenge.atm.model.UserType;
import bbk.challenge.atm.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TokenGenerator {

    @Value("${token.generator.key}")
    private String tokenGeneratorKey;

    private Map<String, String> userNameToToken = ExpiringMap.builder().expiration(Constants.TOKEN_DURATION, TimeUnit.MILLISECONDS).build();

    private String getJWT(String userName, UserType userType) {

        String savedToken = userNameToToken.get(userName);
        if (savedToken != null) {
            return savedToken;
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenGeneratorKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Date exp = new Date(System.currentTimeMillis() + Constants.TOKEN_DURATION);

        JwtBuilder builder = Jwts.builder()
                .setSubject(userName)
                .setExpiration(exp)
                .setAudience(userType.toString())
                .signWith(signatureAlgorithm, signingKey);

        String jwt = builder.compact();

        System.out.println("JWT:" + jwt);

        userNameToToken.put(userName, jwt);

        return jwt;
    }

    public String authenticateUser(String userName, UserType userType) {

        return getJWT(userName, userType);
    }

    private Claims decodeJWT(String jwt) {

        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(tokenGeneratorKey))
                .parseClaimsJws(jwt).getBody();
    }

    public boolean authenticatedUser(String authenticatedJwt) {

        Claims claims = decodeJWT(authenticatedJwt);
        Date expirationDate = claims.getExpiration();
        return expirationDate.after(new Date());
    }

    public String getUserType(String authenticatedJwt) {

        return decodeJWT(authenticatedJwt).getAudience();
    }

    public String getUserName(String authenticatedJwt) {

        return decodeJWT(authenticatedJwt).getSubject();
    }
}
