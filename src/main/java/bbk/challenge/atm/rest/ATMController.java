package bbk.challenge.atm.rest;

import bbk.challenge.atm.service.ATMService;
import bbk.challenge.atm.utils.InputInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ATMController {

    @Autowired ATMService atmService;

    @RequestMapping(value = "/atm/addCash", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCash(Map<String, Integer> denominatorToCount) {

        try {
            atmService.addCash("", denominatorToCount);
        } catch (InputInvalidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Cash added successful!", HttpStatus.OK);
    }

}
