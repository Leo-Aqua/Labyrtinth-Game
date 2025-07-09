import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.Random;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Pane screen;

    private Circle circle = new Circle();
    private Rectangle goal = new Rectangle();

    ArrayList<Line> walls = new ArrayList<Line>();
    ArrayList<Boolean> removedWalls = new ArrayList<Boolean>();

    Random r = new Random();
    private int gridSize = Integer.parseInt(Settings.get("gridSize"));

    // DFS stuff
    ArrayList<Integer> Q = new ArrayList<Integer>();
    ArrayList<Integer> visited = new ArrayList<Integer>();

    private
    @FXML
    void keyPressed(KeyEvent event) {

    }

    @FXML
    void keyReleased(KeyEvent event) {

    }

    private void draw(Shape shape) {
        // Wrapper for screen.getChildren().add()
        // Just because it looks nicer
        screen.getChildren().add(shape);
    }

    private void drawAll() {

        // Define in which order to draw shapes
        screen.getChildren().add(circle);
        screen.getChildren().add(goal);
        for (int i = 0; i<walls.size(); i++) {
            if (!removedWalls.get(i)) {
                screen.getChildren().add(walls.get(i));
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        circle.setCenterX(100);
        circle.setCenterY(100);
        circle.setRadius(50);
        circle.setFill(Color.BLUE);


        System.out.println(getNeighbors(0));
        generateWallGrid();

        dfs();
        drawAll(); // KEEP AT BOTTOM OF THIS METHOD!!!
    }

    void generateWallGrid() {

        boolean debugLabels = Boolean.parseBoolean(Settings.get("dbgWalls"));

        System.out.println("Grid size: " + gridSize);
        System.out.println("MaxHeight: " + screen.getPrefHeight());
        System.out.println("MaxWidth: " + screen.getPrefWidth());
        double maxLength;
        if (screen.getPrefHeight() < screen.getPrefWidth()) {
            maxLength = screen.getPrefHeight() / (double) gridSize;
        } else {
            maxLength = screen.getPrefWidth() / (double) gridSize;
        }
        System.out.println("Max wall Length: " + maxLength);

        // Clear previous walls
        walls.clear();
        removedWalls.clear();
        // Horizontal Lines
        for (int y = 0; y <= gridSize; y++) {          // rows: gridSize + 1 lines
            for (int x = 0; x < gridSize; x++) {       // columns: gridSize lines per row
                walls.add(new Line(x * maxLength, y * maxLength, (x + 1) * maxLength, y * maxLength));
                removedWalls.add(false);
            }
        }
        System.out.println("Walls after horizontal pass: " + walls.size());

        // Vertical Lines
        for (int x = 0; x <= gridSize; x++) {          // columns: gridSize + 1 lines
            for (int y = 0; y < gridSize; y++) {       // rows: gridSize lines per column
                walls.add(new Line(x * maxLength, y * maxLength, x * maxLength, (y + 1) * maxLength));
                removedWalls.add(false);
            }
        }

        System.out.println("Walls after vertical pass: " + walls.size());
    }

    void dfs() {
        // https://www.algosome.com/articles/maze-generation-depth-first.html
        int gridSize = Integer.parseInt(Settings.get("gridSize"));
        double maxLength;
        if (screen.getPrefHeight() < screen.getPrefWidth()) {
            maxLength = screen.getPrefHeight() / (double) gridSize;
        } else {
            maxLength = screen.getPrefWidth() / (double) gridSize;
        }

        Q.clear();
        visited.clear();

        int start = r.nextInt(gridSize * gridSize);

        // Start
        double[] startpos = tileIndexToScreenCoords(start);
        circle.setCenterX(startpos[0] + maxLength/2);
        circle.setCenterY(startpos[1] + maxLength/2);
        circle.setRadius(maxLength/2);

        
        System.out.println("Start index: " + start);
        Q.add(start);
        visited.add(start);

        while (!Q.isEmpty()) {
            int N = Q.get(Q.size() - 1);
            ArrayList<Integer> neighbors = getNeighbors(N);
            ArrayList<Integer> unvisitedNeighbors = new ArrayList<>();

            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    unvisitedNeighbors.add(neighbor);
                }
            } // Works correctly

            if (unvisitedNeighbors.isEmpty()) { // if all neighbors have been visisted
                Q.remove(Q.size() - 1); // remove the last Node
            } else { // if there is at least one unvisited neighbor
                // Select a random one
                int A = unvisitedNeighbors.get(r.nextInt(unvisitedNeighbors.size()));

                // Remove wall between N and A
                removeWallBetweenTiles(N, A);

                visited.add(A);
                Q.add(A);
            }

            System.out.println("Goal index is: " + visited.get(visited.size()-1));
            goal.setWidth(maxLength);
            goal.setHeight(maxLength);
            double[] endpos = tileIndexToScreenCoords(visited.get(visited.size()-1));
            goal.setX(endpos[0]);
            goal.setY(endpos[1]);
            goal.setFill(Color.GREEN);
        }

    }

    // Helper function for dfs()
    private ArrayList<Integer> getNeighbors(int i) {

        int row = i / gridSize;
        int col = i % gridSize;
        int[][] offsets = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} }; // https://www.w3schools.com/java/java_arrays_multi.asp

        ArrayList<Integer> neighbors = new ArrayList<Integer>();
        for (int[] offset : offsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];

            if (newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize) {
                int neighborIndex = newRow * gridSize + newCol;
                neighbors.add(neighborIndex);
            }
        }

        return neighbors;
    }

    // Vielen Dank and das großartige Team von Anthropic die einen Chatbot zur Verfügung gestellt haben um diese Methode zu generieren                                                                                                                          ok wenigstens bin ich ehrlich 
    void removeWallBetweenTiles(int tile1, int tile2) {
        int row1 = tile1 / gridSize;
        int col1 = tile1 % gridSize;
        int row2 = tile2 / gridSize;
        int col2 = tile2 % gridSize;

        // Verify tiles are actually adjacent
        int rowDiff = Math.abs(row1 - row2);
        int colDiff = Math.abs(col1 - col2);
        if (!((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1))) {
            System.out.println("Error: Tiles " + tile1 + " and " + tile2 + " are not adjacent!");
            return;
        }

        // Determine if tiles are horizontally or vertically adjacent
        if (row1 == row2) { // Same row - vertical wall between them
            int wallCol = Math.max(col1, col2); // Wall is to the right of the leftmost tile
            int wallRow = row1;

            // Check bounds - make sure we're not trying to remove a border wall
            if (wallCol > gridSize || wallRow >= gridSize) {
                System.out.println("Warning: Attempted to remove border wall (vertical)");
                return;
            }

            // Calculate index in vertical walls section
            // Vertical walls start after all horizontal walls
            int horizontalWallCount = (gridSize + 1) * gridSize;
            int verticalWallIndex = horizontalWallCount + wallCol * gridSize + wallRow;

            if (verticalWallIndex >= 0 && verticalWallIndex < walls.size()) {
                removedWalls.set(verticalWallIndex, true); // Mark as removed instead of deleting
            } else {
                System.out.println("Error: Vertical wall index out of bounds: " + verticalWallIndex);
            }

        } else if (col1 == col2) { // Same column - horizontal wall between them
            int wallRow = Math.max(row1, row2); // Wall is below the topmost tile
            int wallCol = col1;

            // Check bounds - make sure we're not trying to remove a border wall
            if (wallRow > gridSize || wallCol >= gridSize) {
                System.out.println("Warning: Attempted to remove border wall (horizontal)");
                return;
            }

            // Calculate index in horizontal walls section
            int horizontalWallIndex = wallRow * gridSize + wallCol;

            if (horizontalWallIndex >= 0 && horizontalWallIndex < walls.size()) {
                removedWalls.set(horizontalWallIndex, true); // Mark as removed instead of deleting
            } else {
                System.out.println("Error: Horizontal wall index out of bounds: " + horizontalWallIndex);
            }
        }
    }

    private ArrayList<Integer> convert1Dto2DMap(int i) {
        int gridSize = Integer.parseInt(Settings.get("gridSize"));
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        int x = i % gridSize;
        int y = i / gridSize;
        coordinates.add(x);
        coordinates.add(y);
        return coordinates;
    }

    private double[] tileIndexToScreenCoords(int index) {
        int gridSize = Integer.parseInt(Settings.get("gridSize"));
        double maxLength;
        if (screen.getPrefHeight() < screen.getPrefWidth()) {
            maxLength = screen.getPrefHeight() / (double) gridSize;
        } else {
            maxLength = screen.getPrefWidth() / (double) gridSize;
        }

        int row = index / gridSize;
        int col = index % gridSize;

        double x = col * maxLength;
        double y = row * maxLength;

        return new double[] { x, y };
    }

}
