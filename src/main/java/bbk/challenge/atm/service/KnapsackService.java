package bbk.challenge.atm.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KnapsackService {

    public Map<String, Integer> getDenominatorToCashOfWithdraw(Map<String, Integer> denominatorToCash,
            Long amount) {

        Map<String, Integer> denominatorToCashToRetrieve = new HashMap<>();
        List<Integer> sortedValues = getSortedValues(denominatorToCash);

        Long sum = 0L;
        for (Integer value : sortedValues) {

            if (sum > amount) {
                break;
            }

            if (value > amount) {
                continue;
            }

            Integer valueAmount = denominatorToCash.get(String.valueOf(value));
            int count = getCount(amount, sum, value, valueAmount);

            int c1 = count - 1;
            if (c1 != 0) {
                sum += value * (c1);
                denominatorToCashToRetrieve.put(String.valueOf(value), c1);
                removeMoney(denominatorToCash, value, valueAmount, c1);
            }
        }

        return denominatorToCashToRetrieve;
    }

    private void removeMoney(Map<String, Integer> denominatorToCash, Integer value, Integer valueAmount,
            int count) {
        if (valueAmount - count == 0) {
            denominatorToCash.remove(String.valueOf(value));
        } else {
            denominatorToCash.put(String.valueOf(value), valueAmount - count);
        }
    }

    private  int getCount(Long amount, Long sum, Integer value, Integer valueAmount) {
        int count = 1;
        while (count < valueAmount) {
            if (sum + count * value > amount) {
                break;
            }
            count++;
        }
        return count;
    }

    private List<Integer> getSortedValues(Map<String, Integer> denominatorToCash) {

        return denominatorToCash.keySet()
                .stream()
                .map(Integer::valueOf)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

}
