package bbk.challenge.atm.service;

import bbk.challenge.atm.authentication.PasswordEncrypt;
import bbk.challenge.atm.authentication.TokenGenerator;
import bbk.challenge.atm.model.User;
import bbk.challenge.atm.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private PasswordEncrypt passwordEncrypt;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGenerator tokenGenerator;

    public String authenticateUser(String userName, String password) throws NoSuchAlgorithmException {

        String encryptedPassword = passwordEncrypt.encryptPassword(password);
        Optional<User> userByName = userService.findUserByName(userName);

        if (userByName.isPresent()) {
            User user = userByName.get();
            if (Objects.equals(user.getPassword(), encryptedPassword)) {
                return tokenGenerator.authenticateUser(user.getName(), user.getUserType());
            }
        }

        return null;
    }

    public boolean authenticatedUser(String authenticationJwt) {

        return tokenGenerator.authenticatedUser(authenticationJwt);
    }

    public boolean hasWriteAccess(String authenticationJwt) {

        String userType = tokenGenerator.getUserType(authenticationJwt);
        return Objects.equals(userType, UserType.BANK_EMPLOYEE.toString());
    }

    public boolean hasReadAccess(String authenticationJwt) {

        String userType = tokenGenerator.getUserType(authenticationJwt);
        return userTypeWithReadPermissions(userType);
    }

    public boolean hasReadAccessToUser(String authenticationJwt, String user) {

        String userType = tokenGenerator.getUserType(authenticationJwt);
        String userName = tokenGenerator.getUserName(authenticationJwt);
        return userTypeWithReadPermissions(userType) || Objects.equals(user, userName);
    }

    private boolean userTypeWithReadPermissions(String userType) {

        return Objects.equals(userType, UserType.BANK_EMPLOYEE.toString()) || Objects.equals(userType, UserType.CARD_HOLDER.toString());
    }
}
