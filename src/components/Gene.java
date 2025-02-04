package components;

import java.util.Random;

public class Gene {
    int value = 0;

    private static final Random rng = new Random();

    public Gene(int value) {
        this.value = value;
    }

    // Unlike most copy functions, this is an intentionally inaccurate copy - simulating genetic copy errors which cause
    // mutation! 90% of the time, the gene is copied accurately.
    public Gene copy() {
        int newvalue = value;
        if (rng.nextInt(100) >= 90) {
            newvalue += rng.nextBoolean() ? 1 : -1;
        }
        return new Gene(newvalue);
    }
}
