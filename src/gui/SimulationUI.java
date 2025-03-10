package gui;

import components.Creature;
import components.Environment;
import components.Gene;
import components.Species;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;

import static utils.GeneList.fullGeneList;

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

        // Initialize table columns
        columnNames.add("Age"); columnNames.add("Food Usage");
        for (Gene g: fullGeneList) {
            columnNames.add(g.data.key);
        }
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

        displaySpeciesTable(env.species.get(0));

        yearsLabel.setText("Years: " + totalYears);
        System.out.println(env);
    }

    private final ArrayList<String> columnNames = new ArrayList<>();

    private void displaySpeciesTable(Species s) {
        JFrame tableFrame = new JFrame();

        DefaultTableModel tableModel = new DefaultTableModel(columnNames.toArray(), 0);
        JTable speciesTable = new JTable(tableModel);
        speciesTable.setFillsViewportHeight(true);

        for (Creature c : s.individuals) {
            ArrayList<Integer> rowData = new ArrayList<>();
            rowData.add(c.age); rowData.add(c.foodUse);
            for (Gene g : c.genome.values()) rowData.add(g.value);
            tableModel.addRow(rowData.toArray());
        }

        JScrollPane tablePanel = new JScrollPane(speciesTable);

        tableFrame.setContentPane(tablePanel);
        tableFrame.setTitle("Species View");
        tableFrame.setSize(2000, 2000);
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setVisible(true);
    }
}
