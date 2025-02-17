package components;

import utils.StatUtils;

import java.util.ArrayList;

public class Species {
    public ArrayList<Creature> individuals = new ArrayList<>();

    // Logging variables
    public int births = 0;
    public int deaths = 0;

    public void initialize(int population) {
        for (int i = 0; i < population; i++) {
            individuals.add(new Creature());
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(" --- SPECIES START --- \n");
        for (Creature c : individuals) {
            s.append(c.toString()).append("\n\n");
        }
        s.append(births).append(" born. ").append(deaths).append( " died.\n");
        s.append("Total individuals: ").append(individuals.size()).append("\n");
        //TODO: temporarily pointless due to changes to the algorithm
        //s.append("Average population growth for the following cycle: ").append(StatUtils.calculateBirthRate(this)).append("%\n");
        //s.append("Average population loss for the following cycle: ").append(StatUtils.calculateDeathRate(this)).append("%\n");
        s.append(" --- SPECIES END --- ");
        return s.toString();
    }
}
