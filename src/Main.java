import components.Environment;

import java.util.Random;

public class Main {

    public static final Random rng = new Random();

    public static void main(String[] args) {
        Environment env = new Environment();
        env.temperature = 10;
        env.addSpecies(5);

        System.out.println(env.toString());
    }
}