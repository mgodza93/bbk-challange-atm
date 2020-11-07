package bbk.challenge.atm.service;

import bbk.challenge.atm.utils.Constants;
import bbk.challenge.atm.utils.InputInvalidException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InputValidationService {

    public void validateAddingCashInput(Map<String, Integer> denominatorToCount, Integer noBills)
            throws InputInvalidException {

        if (isGreater(denominatorToCount.size(), Constants.MAX_ADDITIONS)) {
            throw new InputInvalidException(
                    String.format("To many additions! Max allowed:%d, given:%d", Constants.MAX_ADDITIONS,
                            denominatorToCount.size()));
        }

        int addingNoBills = noBills;

        for (Map.Entry<String, Integer> entry : denominatorToCount.entrySet()) {
            int addingKey = Integer.parseInt(entry.getKey());
            Integer addingValue = entry.getValue();
            if (isGreater(addingKey, Constants.MAX_DENOMINATOR)) {
                throw new InputInvalidException(
                        String.format("Denominator to big! Max allowed:%d, given:%s", Constants.MAX_DENOMINATOR,
                                addingKey));
            }

            if (isGreater(addingValue, Constants.MAX_COUNT)) {
                throw new InputInvalidException(
                        String.format("Count to big! Max allowed:%d, given:%d", Constants.MAX_COUNT, addingValue));
            }

            addingNoBills += addingValue;

            if (isGreater(addingNoBills, Constants.MAX_BILLS)) {
                throw new InputInvalidException(
                        String.format("Bills will exceed the capacity! Max allowed:%d, given:%d", Constants.MAX_BILLS,
                                addingNoBills));
            }
        }

    }

    private boolean isGreater(int inputValue, Integer maxValue) {
        return inputValue >= maxValue;
    }
}
