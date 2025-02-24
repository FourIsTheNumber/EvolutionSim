package gui;

import javax.swing.*;

public class SimulationUI extends JFrame {
    private JPanel contentPane;
    public JLabel yearsLabel;
    private JButton whatIfButtonWasButton;

    public SimulationUI() {
        setContentPane(contentPane);
        setTitle("Evolution Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
