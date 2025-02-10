package components;

import java.util.ArrayList;

public class Species {
    ArrayList<Creature> individuals = new ArrayList<>();

    public void initialize(int population) {
        for (int i = 0; i < population; i++) {
            individuals.add(new Creature());
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder(" --- SPECIES START --- \n");
        for (Creature c : individuals) {
            s.append(c.toString()).append("\n\n");
        }
        s.append(" --- SPECIES END --- ");
        return s.toString();
    }
}
