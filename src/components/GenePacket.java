package components;

public enum GenePacket {
    // Gene Packets are the unchanging collection of values associated with each gene, NOT the mutable value.
    // Only one object is used to store immutable state and shared between all creatures.

    Temperature("temp", 1),
    ReproductiveAge("repAge", 0),
    ReproductiveRate("repRate", 0);

    final String key;
    // Multiplied by value to determine effect on total food cost
    final int foodAssociation;

    GenePacket(String key, int foodAssociation) {
        this.key = key;
        this.foodAssociation = foodAssociation;
    }
}
