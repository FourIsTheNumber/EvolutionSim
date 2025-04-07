package utils;

import java.util.Random;

public class RandomUtils {

    // Performs all random operations.
    private static final Random rng = new Random();

    //TODO: This should most definitely support more precise float percentages
    /**
     * @param percentage Chance to succeed
     * @return True: Succeeded, False: Failed
     */
    public static boolean rollPercent(int percentage) {
        return rng.nextInt(1, 101) <= percentage;
    }

    public static boolean rollBoolean() {
        return rng.nextBoolean();
    }

    /**
     * This is just a wrapped call to nextInt. Minimum inclusive, maximum exclusive.
     */
    public static int rollRange(int min, int max) {
        return rng.nextInt(min, max);
    }

    /**
     * This is just a wrapped call to nextInt. Minimum 0, maximum exclusive.
     */
    public static int rollRange(int max) {
        return rng.nextInt(max);
    }
}
