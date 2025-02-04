import components.Environment;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static final Random rng = new Random();
    public static final Scanner keyboard = new Scanner(System.in);


    public static void main(String[] args) {
        Environment env = new Environment();
        env.temperature = 10;
        env.addSpecies(5);

        System.out.println(env.toString());

        while (true) {
            System.out.println("Enter a number of years to simulate, or a number less than 1 to quit.");

            int years = keyboard.nextInt();
            if (years < 1) return;

            for (int i = 0; i < years; i++) env.simulateYear();
            System.out.println(env.toString());
        }
    }
}