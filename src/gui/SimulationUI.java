package gui;

import components.Biome;
import components.Creature;
import components.Environment;
import components.Gene;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import static utils.GeneList.fullGeneList;
import static utils.RandomUtils.rollRange;

public class SimulationUI extends JFrame {
    private JPanel contentPane;
    public JLabel yearsLabel;
    private JTextField passYearsTextField;
    private JButton passYearsButton;
    private JButton pauseButton;
    private JButton colorButton;

    private final Timer mainLoopTimer;

    private int totalYears;

    private boolean isPaused = false;

    private final int BOARD_LENGTH = 6;
    private final int BOARD_HEIGHT = 6;
    // This is filth code written by a goblin
    private final ArrayList<JButton> panelGrid = new ArrayList<>();
    private Environment[][] envGrid;
    private final HashMap<JButton, Environment> panelEnvMap = new HashMap<>();

    private final ImageIcon pauseIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./resources/pause.png")));
    private final ImageIcon playIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./resources/play.png")));

    private final int COLOR_TEMPERATURE = 1;
    private final int COLOR_POPULATION = 2;

    private int colorMode = 1;


    public SimulationUI(int population, int temperature, int food) {
        setContentPane(contentPane);
        setTitle("Evolution Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setVisible(true);

        mainLoopTimer = new Timer(1000, e -> passYears(1));
        mainLoopTimer.setRepeats(true);
        mainLoopTimer.start();

        JPanel envPanel = new JPanel(new GridLayout(BOARD_HEIGHT, BOARD_LENGTH, 1, 1));
        envGrid = new Environment[BOARD_LENGTH][BOARD_HEIGHT];

        for (int i = 0; i < BOARD_LENGTH * BOARD_HEIGHT; i++) {
            boolean isBaseGrid = i == (BOARD_LENGTH * BOARD_HEIGHT) / 2;

            JButton envTile = new JButton("");
            //envTile.setIcon(applyRGBFilter(baseGridImage, 255, 255, 0));
            envTile.addActionListener(e -> displayCreatureTable(envTile));
            panelGrid.add(envTile);
            Environment newEnv = new Environment(isBaseGrid ? temperature : rollRange(1, 11), food, Biome.Forest);
            //Put population into roughly the middle grid
            if (isBaseGrid) newEnv.addCreatures(population);
            envGrid[i % BOARD_LENGTH][i / BOARD_HEIGHT] = newEnv;
            panelEnvMap.put(envTile, newEnv);
            envPanel.add(envTile);

            envTile.setBackground(getColorFromTemperature(newEnv.getTemperature()));
            envTile.setPreferredSize(new Dimension(64, 64));
            envTile.setForeground(Color.WHITE);
        }

        envPanel.setVisible(true);
        contentPane.add(envPanel);

        assignNeighbors();

        // Initialize table columns
        columnNames.add("Age"); columnNames.add("Food Usage");
        for (Gene g: fullGeneList) {
            columnNames.add(g.data.key);
        }

        pauseButton.addActionListener(e -> {
            if (isPaused) {
                mainLoopTimer.start();
                pauseButton.setIcon(pauseIcon);
            } else {
                mainLoopTimer.stop();
                pauseButton.setIcon(playIcon);
            }
            isPaused = !isPaused;
        });

        colorButton.addActionListener(e -> {
            if (colorMode == COLOR_POPULATION) {
                colorMode = COLOR_TEMPERATURE;
                colorButton.setText("Color: Temperature");
            } else {
                colorMode = COLOR_POPULATION;
                colorButton.setText("Color: Population");
            }
        });

        passYearsButton.addActionListener(e -> passYears(Integer.parseInt(passYearsTextField.getText())));
    }

    private Color getColorFromTemperature(int temperature) {
        float normalizedValue = (float) (temperature - 1) / 9;

        int red = (int) (normalizedValue * 255);
        int blue = (int) ((1 - normalizedValue) * 255);

        return new Color(red, 0, blue);
    }

    private Color getColorFromPopulation(int population, int maxPopulation) {
        float normalizedValue = (float) (population) / maxPopulation;

        int green = (int) Math.min((normalizedValue * 255), 255);

        return new Color(0, green, 0);
    }

    // More filth code
    private void assignNeighbors() {
        for (int x = 0; x < BOARD_LENGTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                if (x != 0) envGrid[x][y].addNeighbor(envGrid[x-1][y]);
                if (x != BOARD_LENGTH - 1) envGrid[x][y].addNeighbor(envGrid[x+1][y]);
                if (y != 0) envGrid[x][y].addNeighbor(envGrid[x][y-1]);
                if (y != BOARD_HEIGHT - 1) envGrid[x][y].addNeighbor(envGrid[x][y+1]);
            }
        }
    }

    private void passYears(int years) {

        if (years < 1) return;

        int maxPopulation = 0;

        for (int i = 0; i < years; i++) {
            for (JButton p : panelGrid) {
                Environment env = panelEnvMap.get(p);
                env.simulateYear();
                if (env.creatures.size() > maxPopulation) maxPopulation = env.creatures.size();
                p.setText(Integer.toString(env.creatures.size()));
            }
            totalYears++;
        }

        for (JButton p : panelGrid) {
            Environment env = panelEnvMap.get(p);
            if (colorMode == COLOR_TEMPERATURE) {
                p.setBackground(getColorFromTemperature(env.getTemperature()));
            } else if (colorMode == COLOR_POPULATION) {
                p.setBackground(getColorFromPopulation(env.creatures.size(), maxPopulation));
            }
        }

        //displayCreatureTable(env.creatures);
        yearsLabel.setText("Years: " + totalYears);
    }

    private final ArrayList<String> columnNames = new ArrayList<>();

    private void displayCreatureTable(JButton envTile) {
        Collection<Creature> creatures = panelEnvMap.get(envTile).creatures;

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

        Graphics gr = bufferedImage.createGraphics();
        gr.drawImage(image, 0, 0, null);
        gr.dispose();

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int rgb = bufferedImage.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                r = Math.min(r + rFilter, 255);
                g = Math.min(g + gFilter, 255);
                b = Math.min(b + bFilter, 255);

                int newRGB = (r << 16) | (g << 8) | b;
                bufferedImage.setRGB(x, y, newRGB);
            }
        }

        return new ImageIcon(bufferedImage);
    }
}
