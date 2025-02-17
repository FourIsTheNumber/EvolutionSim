package components;
import java.util.HashMap;

import static utils.GeneList.fullGeneList;

public class Creature {
    public HashMap<String, Gene> genome = new HashMap<>();

    public int age = 20;

    // Base creature initializer
    public Creature() {
        for (Gene g : fullGeneList) {
            genome.put(g.key, g);
        }
        age = 0;
    }

    // Creature constructor which inherits genes
    public Creature(Creature parent) {
        for (Gene g : parent.genome.values()) {
            genome.put(g.key, g.copy());
        }
        age = 0;
    }

    public int getGene(String key) {
        return genome.get(key).value;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("    Creature\n");
        int i = 1;
        for (Gene g : genome.values()) {
            s.append(g.key).append(": ").append(g.value).append("\n");
        }
        s.append("Age: ").append(age);
        return s.toString();
    }
}
