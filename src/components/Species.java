package components;

import java.util.ArrayList;

public class Species {
    ArrayList<Creature> individuals = new ArrayList<>();

    public void initialize(int population) {
        for (int i = 0; i < population; i++) {
            individuals.add(new Creature(5));
        }
    }
}
