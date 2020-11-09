package bbk.challenge.atm.controller;

import bbk.challenge.atm.service.ATMService;
import bbk.challenge.atm.utils.*;
import org.jsondoc.core.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(description = "The ATM controller", name = "ATM service")
public class ATMController {

    @Autowired
    ATMService atmService;

    @ApiMethod(description = "Method that allows a bank employee to add money to the ATM")
    @ApiHeaders(headers = {@ApiHeader(name = "authorization", allowedvalues = "", description = "")})
    @ApiResponseObject
    @ResponseBody
    @RequestMapping(value = "/atm/addCash", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCash(@RequestHeader("authorization") String authorization,
                                          @ApiBodyObject @RequestBody
                                                  Map<String, Integer> denominatorToCount) {

        try {
            atmService.addCash(authorization, denominatorToCount);
        } catch (InputInvalidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (AuthenticationException | AuthorizationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("Cash added successful!", HttpStatus.OK);
    }

    @ApiMethod(description = "Method that allows a card holder to withdraw money")
    @ApiHeaders(headers = {@ApiHeader(name = "authorization", allowedvalues = "", description = "")})
    @ApiResponseObject
    @ResponseBody
    @RequestMapping(value = "/atm/withdrawCash", method = RequestMethod.GET)
    public ResponseEntity<?> withdrawCash(@RequestHeader("authorization") String authorization,
                                          @ApiQueryParam(name = "amount", description = "The amount of cash to withdraw") @RequestParam("amount")
                                                  Long amount) {

        Map<String, Integer> denominatorToCash;

        try {
            denominatorToCash = atmService.withdrawCash(authorization, amount);
        } catch (AuthenticationException | AuthorizationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (MaxAmountExceededException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (PerformException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(denominatorToCash, HttpStatus.OK);
    }

}
