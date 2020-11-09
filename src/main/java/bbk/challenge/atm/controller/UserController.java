package bbk.challenge.atm.controller;

import bbk.challenge.atm.model.User;
import bbk.challenge.atm.auth.AuthenticationService;
import bbk.challenge.atm.service.UserService;
import org.jsondoc.core.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Optional;

@RestController
@Api(description = "The users controller", name = "User service")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @ApiMethod(description = "Method that allows to add a new user")
    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ApiResponseObject
    @ResponseBody
    ResponseEntity<User> save(
            @ApiBodyObject @RequestBody User user
    ) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiMethod(description = "Method that return all the users")
    @ApiHeaders(headers = {@ApiHeader(name = "authorization", allowedvalues = "", description = "")})
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ApiResponseObject
    @ResponseBody
    ResponseEntity<Collection<User>> getAll(
            @RequestHeader("authorization") String authorization
    ) {

        if (!authenticationService.authenticatedUser(authorization)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!authenticationService.hasReadAccess(authorization)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return  ResponseEntity.ok(userService.getAllUsers());
    }

    @ApiMethod(description = "Method that return a user")
    @ApiHeaders(headers = {@ApiHeader(name = "authorization", allowedvalues = "", description = "")})
    @RequestMapping(value = "/users/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ApiResponseObject
    @ResponseBody
    ResponseEntity<User> getByName(
            @RequestHeader("authorization") String authorization,
            @ApiPathParam(name = "userName") @PathVariable(value = "userName") String userName
    ) {

        if (!authenticationService.authenticatedUser(authorization)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!authenticationService.hasReadAccessToUser(authorization, userName)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<User> userByName = userService.findUserByName(userName);
        return userByName.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
    }
}
