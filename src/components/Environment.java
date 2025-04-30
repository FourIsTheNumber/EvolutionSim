package components;

import utils.RandomUtils;

import java.util.ArrayList;

import static utils.RandomUtils.rollPercent;
import static utils.RandomUtils.rollRange;

public class Environment {
    private final int temperature;
    // Refreshed yearly - creatures must have food to survive.
    // Food rules: Creatures that go hungry NEVER reproduce. They have much higher chance to die.
    // TODO: model j curve: over-usage of food should lead to shortage and slow recovery
    private final int foodCapacity;

    private final Biome biome;

    private int foodUsedLast;

    private boolean isAquatic = false;

    // Determine migration ability
    final private ArrayList<Environment> neighbors = new ArrayList<>();

    public ArrayList<Creature> creatures = new ArrayList<>();

    public Environment(Biome biome) {
        this.biome = biome;
        this.temperature = Math.max(rollRange(biome.baseTemp - 1, biome.baseTemp + 2), 1);
        this.foodCapacity = biome.food;

        isAquatic = Biome.isAquaticBiome(biome);
    }

    // Deprecated
    public Environment(int temperature, int foodCapacity, Biome biome) {
        this.temperature = temperature;
        this.foodCapacity = foodCapacity;
        this.biome = biome;
    }

    public void addCreatures(int population) {
        for (int i = 0; i < population; i++) {
            creatures.add(new Creature());
        }
    }

    public void addNeighbor(Environment env) {
        neighbors.add(env);
    }

    public int getTemperature() {
        return temperature;
    }

    public Biome getBiome() { return biome; }

    public int getFoodCapacity() {
        return foodCapacity;
    }

    public int calculateAverageTolerance() {
        if (!creatures.isEmpty()) {
            int total = 0;
            for (Creature c : creatures) {
                total += c.getGene("temp");
            }
            return total / creatures.size();
        }
        return -1;
    }

    public boolean isSuitableFor(Creature c) {
        int aFactor = c.getGene("aquatic");
        int temp = c.getGene("temp");
        boolean aquaticCreature = aFactor <= 5;
        boolean landCreature = aFactor >= 5;

        boolean suitable = isAquatic && aquaticCreature || !isAquatic && landCreature;
        suitable = suitable && Math.abs(temperature - temp) <= 2;

        return suitable;
    }

    // Currently, a simulated year goes through three steps for every creature in the environment.
    // First, it finds a chance for that creature to die based on its fitness to this environment.
    // Next, it checks if the creature is of reproductive age and simulates reproduction if possible.
    // Finally, it prepares for the next year by incrementing age and other environment variables.
    public void simulateYear() {
        // Initialize the year's food
        int food = foodCapacity;

        ArrayList<Creature> reproductiveQueue = new ArrayList<>();
        ArrayList<Creature> deathQueue = new ArrayList<>();

        for (Creature c : creatures) {
            // Use food
            boolean ateFood = food >= c.foodUse;
            food -= c.foodUse;

            int aFactor = c.getGene("aquatic");
            boolean aquaticCreature = aFactor <= 5;
            boolean landCreature = aFactor >= 5;

            // Kill creatures in unsuitable biomes
            if (isAquatic && !aquaticCreature || !isAquatic && aquaticCreature) {
                deathQueue.add(c);
                continue;
            }

            // Try migration
            // TODO: Use local temperature
            if (rollPercent(ateFood ? 1 : 90)) {
                Environment n = neighbors.get(rollRange(0, neighbors.size()));
                // Do not migrate to unsuitable biomes
                if (n.isSuitableFor(c)) {
                    n.creatures.add(c);
                    deathQueue.add(c);
                    continue;
                }
            }

                // Simulate chance to die based on the difference between temperature and genetic optimal temperature
                // TODO: this should be a (more complex) function call passed to the creature
                int ageFactor = (int) Math.round(0.3F * (Math.pow(c.age, 2)) / 40);
                int deathRate = (Math.abs((temperature - c.getGene("temp")))* 3 + ageFactor);
                // Apply harsh penalty for starved creatures
                if (!ateFood) deathRate += 30;
                if (rollPercent(deathRate)) {
                    deathQueue.add(c);
                    continue;
                }

                // Do reproduction if creature is old enough and was able to eat this year
                if (c.age >= c.getGene("repAge") && ateFood) {
                    if (rollPercent(c.getGene("repRate"))) {
                        reproductiveQueue.add(c);
                    }
                }

                // Increment age
                c.age += 1;
            }
            while (reproductiveQueue.size() > 1) {
                Creature p1 = reproductiveQueue.remove(0);
                Creature p2 = reproductiveQueue.remove(rollRange(0, reproductiveQueue.size()));

                creatures.add(new Creature(p1, p2));
            }
            creatures.removeAll(deathQueue);

        foodUsedLast = foodCapacity - food;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\nFood used this year: ").append(foodUsedLast);
        return s.toString();
    }
}
