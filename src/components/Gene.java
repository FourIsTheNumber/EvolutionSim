package components;

import java.util.Random;

public class Gene {
    public int value = 0;
    String key = "";

    private static final Random rng = new Random();

    public Gene(int value, String key) {
        this.value = value;
        this.key = key;
    }

    // Unlike most copy functions, this is an intentionally inaccurate copy - simulating genetic copy errors which cause
    // mutation! 90% of the time, the gene is copied accurately. 10% of the time, it shifts randomly up or down.
    public Gene copy() {
        int newvalue = value;
        if (rng.nextInt(100) >= 90) {
            newvalue += rng.nextBoolean() ? 1 : -1;
        }
        return new Gene(newvalue, key);
    }
}
