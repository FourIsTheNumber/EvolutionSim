package gui;

import components.Biome;
import components.Environment;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.GeneList;
import utils.ImageUtils;
import utils.RandomUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

import static utils.RandomUtils.rollRange;

public class SetupUIJfx extends Application {

    @FXML private ChoiceBox<String> paintChoiceBox;
    @FXML private Button passTimeButton;
    @FXML private TextField passTimeTextField;

    @FXML private Label biomeLabel;
    @FXML private Label temperatureLabel;
    @FXML private Label foodLabel;
    @FXML private Label populationLabel;
    @FXML private Label speciesTemperatureLabel;

    @FXML private RadioButton defaultButton;
    @FXML private RadioButton popButton;
    @FXML private RadioButton tempButton;

    @FXML private StackPane stackPane;

    @FXML private ToggleGroup overlayGroup = new ToggleGroup();

    private final Timer timer = new Timer();

    private int totalYears = 0;

    private int selectedX = -1;
    private int selectedY = -1;

    TimerTask yearTask = new TimerTask()
    {
        public void run()
        {
            passYears(1);
        }
    };

    private int maxPopulation = 0;

    @FXML
    private void passTimeButtonPress() {
        passYears(Integer.parseInt(passTimeTextField.getText()));
    }

    private void passYears(int years) {

        if (years < 1) return;

        maxPopulation = 0;

        for (int i = 0; i < years; i++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                for (int y = 0; y < BOARD_HEIGHT; y++) {
                    envGrid[x][y].simulateYear();
                    if (envGrid[x][y].creatures.size() > maxPopulation) maxPopulation = envGrid[x][y].creatures.size();
                    //p.setText(Integer.toString(env.creatures.size()));
                }
            }
            totalYears++;
        }

        renderOverlay();

        Platform.runLater(this::updateLabels);

        //yearsLabel.setText("Years: " + totalYears);
    }

    private void renderOverlay() {
        switch (colorMode) {
            case 0 -> Platform.runLater(this::renderNoOverlay);
            case 1 -> Platform.runLater(() -> renderPopulationOverlay(maxPopulation));
            case 2 -> Platform.runLater(this::renderTemperatureOverlay);
        }
    }

    private void updateLabels() {
        if (selectedX != -1 && selectedY != -1) {
            Environment selectedEnv = envGrid[selectedX][selectedY];
            // Update environment labels
            biomeLabel.setText("Biome: " + selectedEnv.getBiome().name);
            temperatureLabel.setText("Temperature: " + selectedEnv.getTemperature());
            foodLabel.setText("Food Per Year: " + selectedEnv.getFoodCapacity());

            // Update creature labels
            populationLabel.setText("Population: " + selectedEnv.creatures.size());
            speciesTemperatureLabel.setText("Average Temperature Tolerance: " + selectedEnv.calculateAverageTolerance());
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/setupLayout.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.setTitle("Set Up Simulator");
        stage.show();
    }

    public static void main(String[] args) {
        GeneList.run();
        launch(args);
    }

    @FXML
    private void startSim(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        Scene scene = getSimScene();
        stage.setTitle("Simulation");
        stage.setScene(scene);
        stage.show();

        stage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private static final int BOARD_LENGTH = 20;
    private static final int BOARD_HEIGHT = 10;
    private static final int TILE_SIZE = 64;

    private final Environment[][] envGrid = new Environment[BOARD_LENGTH][BOARD_HEIGHT];
    private final Biome[][] biomeGrid = new Biome[BOARD_LENGTH][BOARD_HEIGHT];

    private Scene getSimScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/simulationLayout.fxml"));
        Parent root = loader.load();

        return new Scene(root);
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < BOARD_LENGTH && y < BOARD_HEIGHT;
    }

    // Used for painting
    private static final HashMap<String, Biome> nameToBiome = new HashMap<>();

    // I am using the same controller for both FXML files.
    // That means this method is called twice - on the first call, setupUI has initialized.
    // On the second call, simulationUI has initialized.
    // I check for the presence of stackPane to verify which one...
    @FXML
    private void initialize() {
        if (stackPane == null) {
            System.out.println("Initializing setup pane");
        } else {
            paintChoiceBox.getItems().addAll("None", "Creatures");
            for (Biome b : Biome.values()) {
                paintChoiceBox.getItems().add(b.name);
                nameToBiome.put(b.name, b);
            }

            System.out.println("Initializing simulation pane");

            int totalTiles = BOARD_HEIGHT * BOARD_LENGTH;

            int completedTiles = 0;

            // Randomly seed some random biomes throughout the board
            // If tile was already seeded, try again
            int seeds = totalTiles / 20;
            for (int i = 0; i < seeds; i++) {
                int rX = rollRange(0, BOARD_LENGTH);
                int rY = rollRange(0, BOARD_HEIGHT);
                if (biomeGrid[rX][rY] == null) {

                    biomeGrid[rX][rY] = Biome.getRandomBiome();
                    completedTiles++;
                }
                else i--;
            }

            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            List<int[]> directionsList = new ArrayList<>(Arrays.stream(directions).toList());
            while (completedTiles < totalTiles) {
                for (int y = 0; y < BOARD_HEIGHT; y++) {
                    for (int x = 0; x < BOARD_LENGTH; x++) {
                        if (biomeGrid[x][y] != null) {
                            Collections.shuffle(directionsList);
                            for (int[] dir : directionsList) {
                                int nX = x + dir[0];
                                int nY = y + dir[1];

                                if (isInBounds(nX, nY) && biomeGrid[nX][nY] == null) {
                                    Biome b = biomeGrid[x][y];
                                    if (RandomUtils.rollRange(0, 8) == 0) {
                                        List<Biome> similars = Biome.getSimilarBiomes(b);
                                        assert similars != null;
                                        int i = RandomUtils.rollRange(0, similars.size());
                                        b = similars.get(i);
                                    }

                                    biomeGrid[nX][nY] = b;
                                    completedTiles++;
                                }
                            }
                        }
                    }
                }
            }

            // Set up environments
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                for (int x = 0; x < BOARD_LENGTH; x++) {
                    envGrid[x][y] = new Environment(biomeGrid[x][y]);
                }
            }

            renderNoOverlay();

            assignNeighbors();
            envGrid[0][0].addCreatures(50);
            timer.scheduleAtFixedRate(yearTask, 1000, 1000);

            defaultButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    System.out.println("No overlay.");
                    renderNoOverlay();
                    colorMode = 0;
                }
            });

            popButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    System.out.println("Population overlay.");
                    renderPopulationOverlay(maxPopulation);
                    colorMode = 1;
                }
            });

            tempButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    System.out.println("Temperature overlay.");
                    renderTemperatureOverlay();
                    colorMode = 2;
                }
            });
        }
    }

    private int colorMode = 0;

    private void handleMouseClick(MouseEvent event) {
        int tileX = (int)(event.getX() / TILE_SIZE);
        int tileY = (int)(event.getY() / TILE_SIZE);
        selectedX = tileX;
        selectedY = tileY;
        System.out.println("Clicked tile at: " + tileX + ", " + tileY);

        String paint = paintChoiceBox.getValue();
        if (paint != null && !paint.equals("None")) {
            if (paint.equals("Creatures")) {
                envGrid[selectedX][selectedY].addCreatures(10);
            } else {
                biomeGrid[selectedX][selectedY] = nameToBiome.get(paint);
                envGrid[selectedX][selectedY] = new Environment(biomeGrid[selectedX][selectedY]);
            }
        }
        renderOverlay();
        updateLabels();
    }

    private void renderNoOverlay() {
        Canvas canvas = new Canvas(BOARD_LENGTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(this::handleMouseClick);

        gc.setImageSmoothing(false);

        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                Biome biome = biomeGrid[x][y];

                // Draw the base tile
                gc.drawImage(new Image(biome.getTileResourceLocation()), x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                // Special seeded random so that the decorations remain consistent
                Random rand = new Random(44 * x + 22 * y);

                // Draw decorative overlays
                int decorationsToDraw = rand.nextInt(1, 3);
                for (int i = 0; i < decorationsToDraw; i++) {
                    List<String> decorations = biome.getDecorationResources();
                    if (decorations.isEmpty()) continue;

                    Image decor = new Image(decorations.get(rand.nextInt(decorations.size())));

                    int decorSize = 12 + rand.nextInt(16);
                    int offsetX = x * TILE_SIZE + rand.nextInt(TILE_SIZE - decorSize);
                    int offsetY = y * TILE_SIZE + rand.nextInt(TILE_SIZE - decorSize);

                    gc.drawImage(decor, offsetX, offsetY, decorSize, decorSize);
                }

                // Draw creatures, max 10 per tile
                // Draw one image for every 50 creatures
                // Always draw at least 1 creature if there is any population
                if (envGrid[x][y].creatures.isEmpty()) continue;
                int creaturesToDraw = Math.max(Math.min(envGrid[x][y].creatures.size() / 50, 10), 1);
                for (int i = 0; i < creaturesToDraw; i++) {
                    Image decor = new Image("resources/creacher.png");

                    int offsetX = x * TILE_SIZE + rollRange(TILE_SIZE - 12);
                    int offsetY = y * TILE_SIZE + rollRange(TILE_SIZE - 12);

                    gc.drawImage(decor, offsetX, offsetY, 12, 12);
                }
            }
        }

        gc.drawImage(new Image("resources/target.png"), selectedX * TILE_SIZE, selectedY * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        stackPane.getChildren().clear();
        stackPane.getChildren().add(canvas);
    }

    private void renderPopulationOverlay(int maxPopulation) {
        Canvas canvas = new Canvas(BOARD_LENGTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(this::handleMouseClick);

        gc.setImageSmoothing(false);
        gc.setFont(new Font("Arial", 24));

        // First render pass, place tiles
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                int population = envGrid[x][y].creatures.size();

                java.awt.Color c = ImageUtils.getColorFromPopulation(population, maxPopulation);

                gc.setFill(Color.rgb(c.getRed(), c.getGreen(), c.getBlue()));
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Second render pass, write text
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                int population = envGrid[x][y].creatures.size();
                if (population > 0) {
                    gc.setFill(Color.WHITE);
                    gc.fillText(Integer.toString(population), x * TILE_SIZE, y * TILE_SIZE + 32);
                }
            }
        }

        gc.drawImage(new Image("resources/target.png"), selectedX * TILE_SIZE, selectedY * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        stackPane.getChildren().clear();
        stackPane.getChildren().add(canvas);
    }

    private void renderTemperatureOverlay() {
        Canvas canvas = new Canvas(BOARD_LENGTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(this::handleMouseClick);

        gc.setImageSmoothing(false);
        gc.setFont(new Font("Arial", 24));

        // First render pass, place tiles
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                java.awt.Color c = ImageUtils.getColorFromTemperature(envGrid[x][y].getTemperature());
                gc.setFill(Color.rgb(c.getRed(), c.getGreen(), c.getBlue()));
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Second render pass, write text
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                gc.setFill(Color.WHITE);
                gc.fillText(Integer.toString(envGrid[x][y].getTemperature()), x * TILE_SIZE, y * TILE_SIZE + 32);
            }
        }

        gc.drawImage(new Image("resources/target.png"), selectedX * TILE_SIZE, selectedY * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        stackPane.getChildren().clear();
        stackPane.getChildren().add(canvas);
    }

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
}