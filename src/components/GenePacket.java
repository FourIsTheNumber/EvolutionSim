package components;

import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

public enum GenePacket {
    // Gene Packets are the unchanging collection of values associated with each gene, NOT the mutable value.
    // Only one object is used to store immutable state and shared between all creatures.

    Temperature("temp", x -> Math.abs(5 - x), 0, 10),
    ReproductiveAge("repAge", x -> 0, 12, 30),
    ReproductiveRate("repRate", x -> 0, 40, 60),
    Aquatic("aquatic", x -> 0, 0, 10);

    public final String key;
    // Applied with value to determine effect on total food cost
    final IntUnaryOperator foodAssociation;

    final int min, max;

    GenePacket(String key, IntUnaryOperator foodAssociation) {
        this.key = key;
        this.foodAssociation = foodAssociation;
        min = 0;
        max = 100;
    }

    GenePacket(String key, IntUnaryOperator foodAssociation, int min, int max) {
        this.key = key;
        this.foodAssociation = foodAssociation;
        this.min = min;
        this.max = max;
    }
}
