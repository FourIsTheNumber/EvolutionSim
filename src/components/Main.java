package components;

import utils.GeneList;

import java.util.Scanner;

public class Main {

    public static final Scanner keyboard = new Scanner(System.in);

    public static void mainLoop(Environment env) {

        System.out.println(env);

        while (true) {
            System.out.println("Enter a number of years to simulate, or a number less than 1 to quit.");

            int years = keyboard.nextInt();
            if (years < 1) return;

            for (int i = 0; i < years; i++) {
                env.simulateYear();
            }
            System.out.println(env);
        }
    }
}