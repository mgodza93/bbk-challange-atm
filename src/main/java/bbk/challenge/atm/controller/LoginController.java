package bbk.challenge.atm.controller;

import bbk.challenge.atm.service.AuthenticationService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@Api(description = "The login controller", name = "Login service")
public class LoginController {

    @Autowired private AuthenticationService authenticationService;

    @ApiMethod(description = "Method that allows a user to login")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ApiResponseObject
    @ResponseBody
    ResponseEntity<String> login(

            @ApiQueryParam(name = "userName", description = "The name of the user") @RequestParam("userName")
                    String userName,

            @ApiQueryParam(name = "password", description = "The password") @RequestParam("password") String password) {

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