package utils;

import components.Creature;
import components.Environment;
import components.Species;

public class StatUtils {
    // I am having some trouble tuning numbers and species dying before they can reach exponential growth, so
    // this class will add some utilities to show useful statistics for tuning that can eventually be printed to a csv!

    // Identified problem: Population growth drops massively once the initial generation dies off, due to the long gap
    // between the next generation of creatures of reproductive age. The curve for age-related death must be applied.

    /**
     * Calculate the average population growth accounting for creature's reproductive ages.
     */
    public static float calculateBirthRate(Species species) {
        //TODO: When this value is no longer static, this will need severe changes
        float adults = 0;
        for (Creature c : species.individuals) {
            if (c.age >= 20) adults++;
        }
        return 100F / ((float) species.individuals.size() / (adults / 5));
    }

    /**
     * Calculate the average population loss.
     */
    public static float calculateDeathRate(Species s) {
        float deathRate = 0;
        for (Creature c : s.individuals) {
            deathRate += 1 + (1 * Math.abs(10 - c.genome.get("temp").value));
        }
        return (deathRate / (float) s.individuals.size());
    }
}
