package components;

public enum GenePacket {
    // Gene Packets are the unchanging collection of values associated with each gene, NOT the mutable value.
    // Only one object is used to store immutable state and shared between all creatures.

    Temperature("temp", 1, 0, 10),
    ReproductiveAge("repAge", 0, 12, 30),
    ReproductiveRate("repRate", 0, 40, 60),
    Aquatic("aquatic", 0, 0, 10);

    public final String key;
    // Multiplied by value to determine effect on total food cost
    // TODO: this should be deviation from 5 for temperature. need more complex algorithm... can foodAssociation be a supplier?
    final int foodAssociation;

    final int min, max;

    GenePacket(String key, int foodAssociation) {
        this.key = key;
        this.foodAssociation = foodAssociation;
        min = 0;
        max = 100;
    }

    GenePacket(String key, int foodAssociation, int min, int max) {
        this.key = key;
        this.foodAssociation = foodAssociation;
        this.min = min;
        this.max = max;
    }
}
