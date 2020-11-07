package bbk.challenge.atm.utils;

import java.util.concurrent.TimeUnit;

public class Constants {

    public static final Integer MAX_ADDITIONS = (int) Math.pow(10, 2);
    public static final Integer MAX_DENOMINATOR = (int) Math.pow(10, 3);
    public static final Integer MAX_COUNT = (int) Math.pow(10, 5);
    public static final Integer MAX_BILLS = (int) Math.pow(10, 5);

    public static final long TOKEN_DURATION = TimeUnit.MINUTES.toMillis(10);
}
