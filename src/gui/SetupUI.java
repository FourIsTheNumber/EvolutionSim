package gui;

import components.Environment;
import components.Main;
import utils.GeneList;

import javax.swing.*;

public class SetupUI extends JFrame {
    private JPanel contentPane;
    private JButton beginSimulationButton;
    private JTextField temperatureField;
    private JTextField foodField;
    private JTextField populationField;

    public SetupUI() {
        setContentPane(contentPane);
        setTitle("Set Up Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        beginSimulationButton.addActionListener(e -> {
            Environment env = new Environment(Integer.parseInt(temperatureField.getText()), Integer.parseInt(foodField.getText()));
            env.addSpecies(Integer.parseInt(populationField.getText()));

            SimulationUI ui = new SimulationUI();
            setVisible(false);

            Main simulator = new Main();
            simulator.bindUI(ui);
            simulator.mainLoop(env);
        });
    }

    public static void main(String[] args) {
        GeneList.run();

        new SetupUI();
    }
}
