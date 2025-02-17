package gui;

import components.Environment;
import components.Main;
import utils.GeneList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainui extends JFrame {
    private JPanel contentPane;
    private JButton beginSimulationButton;
    private JTextField temperatureField;
    private JTextField foodField;
    private JTextField populationField;

    public mainui() {
        setContentPane(contentPane);
        setTitle("Evolution Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        beginSimulationButton.addActionListener(e -> {
            Environment env = new Environment(Integer.parseInt(temperatureField.getText()), Integer.parseInt(foodField.getText()));
            env.addSpecies(Integer.parseInt(populationField.getText()));

            Main.mainLoop(env);
        });
    }

    public static void main(String[] args) {
        GeneList.run();

        new mainui();
    }
}
