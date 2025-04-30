package components;
import java.util.HashMap;

import static utils.GeneList.fullGeneList;
import static utils.RandomUtils.rollBoolean;

public class Creature {
    public HashMap<String, Gene> genome = new HashMap<>();

    public int foodUse = 1;
    public int age;

    // Base creature initializer
    public Creature() {
        for (Gene g : fullGeneList) {
            genome.put(g.data.key, g);
            foodUse += (g.data.foodAssociation * g.value);
        }
        age = 0;
    }

    // Creature constructor which inherits genes
    public Creature(Creature parent) {
        for (Gene g : parent.genome.values()) {
            genome.put(g.data.key, g.copy());
            foodUse += (g.data.foodAssociation * g.value);
        }
        age = 0;
    }

    public Creature(Creature parent1, Creature parent2) {
        for (Gene g : parent1.genome.values())  {
            Gene newGene = rollBoolean() ? g.copy() : parent2.genome.get(g.data.key).copy();
            genome.put(newGene.data.key, newGene);
            foodUse += (newGene.data.foodAssociation * newGene.value);
        }
        age = 0;
    }

    public int getGene(String key) {
        return genome.get(key).value;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("    Creature\n");
        for (Gene g : genome.values()) {
            s.append(g.data.key).append(": ").append(g.value).append("\n");
        }
        s.append("Age: ").append(age);
        s.append("\nFood Usage: ").append(foodUse);
        return s.toString();
    }
}
