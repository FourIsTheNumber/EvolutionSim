package components;

import java.util.Random;

import static utils.RandomUtils.rollPercent;

public class Gene {
    public int value;
    String key;
    // Multiplied by value to determine effect on total food cost
    public int foodAssociation;

    private static final Random rng = new Random();

    public Gene(int value, String key, int foodAssociation) {
        this.value = value;
        this.key = key;
        this.foodAssociation = foodAssociation;
    }

    // Unlike most copy functions, this is an intentionally inaccurate copy - simulating genetic copy errors which cause
    // mutation! 90% of the time, the gene is copied accurately. 10% of the time, it shifts randomly up or down.
    public Gene copy() {
        int newvalue = value;
        if (rollPercent(90)) {
            newvalue += rng.nextBoolean() ? 1 : -1;
        }
        return new Gene(newvalue, key, foodAssociation);
    }
}
