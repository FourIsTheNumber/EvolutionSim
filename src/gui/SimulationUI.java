package gui;

import components.Environment;
import components.Main;

import javax.swing.*;

public class SimulationUI extends JFrame {
    private JPanel contentPane;
    public JLabel yearsLabel;
    private JTextField passYearsTextField;
    private JButton passYearsButton;

    private int totalYears;

    private final Environment env;

    public SimulationUI(Environment env) {
        setContentPane(contentPane);
        setTitle("Evolution Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        this.env = env;

        passYearsButton.addActionListener(e -> passYears(Integer.parseInt(passYearsTextField.getText())));
    }

    private void passYears(int years) {

        System.out.println(env);

        if (years < 1) return;

        for (int i = 0; i < years; i++) {
            env.simulateYear();
            totalYears++;
        }

        yearsLabel.setText("Years: " + totalYears);
        System.out.println(env);
    }
}
