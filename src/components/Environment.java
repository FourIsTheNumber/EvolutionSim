package components;

import java.util.ArrayList;
import java.util.Random;

public class Environment {
    public int temperature;

    //TODO: consolidate all these randoms somewhere
    private static final Random rng = new Random();

    //TODO: should be a gene
    final private int reproductiveAge = 20;
    //TODO: should be a gene, should be chance-based. Currently unused
    final private int deathAge = 60;
    //TODO: this should not be artificially enforced
    final private int carryingCapacity = 100;

    public ArrayList<Species> species = new ArrayList<>();

    public void addSpecies(int population) {
        Species s = new Species();
        s.initialize(population);
        species.add(s);
    }

    // For now, I'm going to model asexual reproduction, since parentage will be more complicated.

    // Currently, a simulated year goes through three steps for every creature in the environment.
    // First, it finds a chance for that creature to die based on its fitness to this environment.
    // Next, it checks if the creature is of reproductive age and simulates reproduction if possible.
    // Finally, it prepares for the next year by incrementing age and other environment variables.
    public void simulateYear() {
        for (Species s : species) {
            s.deaths = 0; s.births = 0;
            ArrayList<Creature> reproductiveQueue = new ArrayList<>();
            ArrayList<Creature> deathQueue = new ArrayList<>();
            for (Creature c : s.individuals) {
                // Simulate chance to die based on the difference between temperature and genetic optimal temperature
                // Creature always has a 1% chance to die every year of "random" causes
                // TODO: this should be a (more complex) function call passed to the creature
                int deathRate = 1 + (1 * Math.abs(temperature - c.genome.get("temp").value));
                if (rng.nextInt(1, 101) <= deathRate) {
                    deathQueue.add(c);
                    s.deaths++;
                    continue;
                }

                // Do reproduction if creature is old enough
                if (c.age >= reproductiveAge && s.individuals.size() <= carryingCapacity) {
                    if (rng.nextInt(1, 100) <= 20) {
                        reproductiveQueue.add(new Creature(c));
                        s.births++;
                    }
                }

                // Increment age
                c.age += 1;
            }
            s.individuals.addAll(reproductiveQueue);
            s.individuals.removeAll(deathQueue);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Species sp : species) {
            s.append(sp.toString());
        }
        return s.toString();
    }
}
