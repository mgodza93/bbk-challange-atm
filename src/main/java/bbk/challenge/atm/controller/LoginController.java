package bbk.challenge.atm.controller;

import bbk.challenge.atm.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
public class LoginController {

    @Autowired private AuthenticationService authenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<String> login(@RequestParam("userName") String userName, @RequestParam("password") String password) {

        String jwt = null;
        try {
            jwt = authenticationService.authenticateUser(userName, password);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("user: " + userName + " failed to login");
        }

        if (jwt == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return ResponseEntity.ok(jwt);
        }
    }
}
