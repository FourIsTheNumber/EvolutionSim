package utils;

public class MathUtils {

    public static int clamp(int val, int min, int max) {
        return Math.min(Math.max(val, min), max);
    }
}
