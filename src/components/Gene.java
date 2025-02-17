package components;

import java.util.Random;

import static utils.RandomUtils.rollPercent;

public class Gene {
    public int value;
    public GenePacket data;

    private static final Random rng = new Random();

    public Gene(int value, GenePacket data) {
        this.value = value;
        this.data = data;
    }

    // Unlike most copy functions, this is an intentionally inaccurate copy - simulating genetic copy errors which cause
    // mutation! 90% of the time, the gene is copied accurately. 10% of the time, it shifts randomly up or down.
    public Gene copy() {
        int newvalue = value;
        if (rollPercent(90)) {
            newvalue += rng.nextBoolean() ? 1 : -1;
        }
        return new Gene(newvalue, data);
    }
}
