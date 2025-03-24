package gui;

import components.Creature;
import components.Environment;
import components.Gene;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static utils.GeneList.fullGeneList;

public class SimulationUI extends JFrame {
    private JPanel contentPane;
    public JLabel yearsLabel;
    private JTextField passYearsTextField;
    private JButton passYearsButton;

    private int totalYears;

    private final Environment env;

    private final int BOARD_LENGTH = 3;
    private final int BOARD_HEIGHT = 3;
    private final ArrayList<Environment> envGrid = new ArrayList<>();
    private final ArrayList<JButton> panelGrid = new ArrayList<>();

    public SimulationUI(Environment env) {
        setContentPane(contentPane);
        setTitle("Evolution Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel envPanel = new JPanel(new GridLayout(BOARD_HEIGHT, BOARD_LENGTH, 1, 1));

        for (int i = 0; i < BOARD_LENGTH * BOARD_HEIGHT; i++) {
            JButton envTile = new JButton("");
            envTile.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./resources/boxBase.png"))));
            panelGrid.add(envTile);
            envGrid.add(new Environment(10, 1000));
            envPanel.add(envTile);
        }
        envPanel.setVisible(true);
        contentPane.add(envPanel);


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

        displayCreatureTable(env.creatures);

        yearsLabel.setText("Years: " + totalYears);
        System.out.println(env);
    }

    private final ArrayList<String> columnNames = new ArrayList<>();

    private void displayCreatureTable(Collection<Creature> creatures) {
        JFrame tableFrame = new JFrame();

        DefaultTableModel tableModel = new DefaultTableModel(columnNames.toArray(), 0);
        JTable speciesTable = new JTable(tableModel);
        speciesTable.setFillsViewportHeight(true);

        for (Creature c : creatures) {
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

    public static ImageIcon applyRGBFilter(ImageIcon originalIcon, int rFilter, int gFilter, int bFilter) {
        Image image = originalIcon.getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int rgb = bufferedImage.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int gComp = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                r = clamp(r + rFilter, 0, 255);
                gComp = clamp(gComp + gFilter, 0, 255);
                b = clamp(b + bFilter, 0, 255);

                // Recombine the modified components
                int newRGB = (r << 16) | (gComp << 8) | b;
                bufferedImage.setRGB(x, y, newRGB);
            }
        }

        return new ImageIcon(bufferedImage);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
