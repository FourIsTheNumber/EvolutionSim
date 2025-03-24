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

    private int foodUsedLast;

    //TODO: this should not be artificially enforced
    final private int carryingCapacity = 100;

    // Determine migration ability
    final private ArrayList<Environment> neighbors = new ArrayList<>();

    public ArrayList<Creature> creatures = new ArrayList<>();

    public Environment(int temperature, int foodCapacity) {
        this.temperature = temperature;
        this.foodCapacity = foodCapacity;
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

    // For now, I'm going to model asexual reproduction, since parentage will be more complicated.

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

            // Try migration
            // TODO: Use local temperature
            if (rollPercent(ateFood ? 1 : 90)) {
                Environment n = neighbors.get(rollRange(0, neighbors.size()));
                n.creatures.add(c);
                deathQueue.add(c);
                continue;
            }

                // Simulate chance to die based on the difference between temperature and genetic optimal temperature
                // TODO: this should be a (more complex) function call passed to the creature
                int ageFactor = (int) Math.round(0.3F * (Math.pow(c.age, 2)) / 40);
                int deathRate = (Math.abs(temperature - c.getGene("temp")) + ageFactor);
                // Apply harsh penalty for starved creatures
                if (!ateFood) deathRate += 30;
                if (rollPercent(deathRate)) {
                    deathQueue.add(c);
                    continue;
                }

                // Do reproduction if creature is old enough and was able to eat this year
                if (c.age >= c.getGene("repAge") && ateFood) {
                    if (rollPercent(c.getGene("repRate"))) {
                        reproductiveQueue.add(new Creature(c));
                    }
                }

                // Increment age
                c.age += 1;
            }
            creatures.addAll(reproductiveQueue);
            creatures.removeAll(deathQueue);

        foodUsedLast = foodCapacity - food;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        //for (Species sp : species) {
        //    s.append(sp);
        //}
        s.append("\nFood used this year: ").append(foodUsedLast);
        return s.toString();
    }
}
