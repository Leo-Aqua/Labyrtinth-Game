import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import java.util.ArrayList;
import java.util.Random;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Pane screen;

    private Circle circle = new Circle();

    ArrayList<Line> walls = new ArrayList<Line>();
    Random r = new Random();

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
        draw(circle);

        for (Line i : walls) {
            draw(i);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        circle.setCenterX(100);
        circle.setCenterY(100);
        circle.setRadius(50);
        circle.setFill(Color.BLUE);

        generateWallGrid();
        dfs();
        drawAll(); // KEEP AT BOTTOM OF THIS METHOD!!!
    }

    void generateWallGrid() {
        int gridSize = Integer.parseInt(Settings.get("gridSize"));
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

        // Horizontal Lines
        for (int y = 0; y <= gridSize; y++) {          // rows: gridSize + 1 lines
            for (int x = 0; x < gridSize; x++) {       // columns: gridSize lines per row
                walls.add(new Line(x * maxLength, y * maxLength, (x + 1) * maxLength, y * maxLength));
            }
        }
        System.out.println("Walls after horizontal pass: " + walls.size());

        // Vertical Lines
        for (int x = 0; x <= gridSize; x++) {          // columns: gridSize + 1 lines
            for (int y = 0; y < gridSize; y++) {       // rows: gridSize lines per column
                walls.add(new Line(x * maxLength, y * maxLength, x * maxLength, (y + 1) * maxLength));
            }
        }
        System.out.println("Walls after vertical pass: " + walls.size());
    }

    void dfs() {

        // Refer to https://www.algosome.com/articles/maze-generation-depth-first.html for mor info.
        int gridSize = Integer.parseInt(Settings.get("gridSize"));

        ArrayList<Integer> Q = new ArrayList<Integer>();
        ArrayList<Integer> visited = new ArrayList<Integer>();

        // Randomly select a node (or cell) N. 
        
        int N = r.nextInt(gridSize*gridSize); // Thanks to Ivonet: https://stackoverflow.com/a/5271613 (low is 0)
        System.out.println("Start tile is: " + N);

        // Push the node N onto a queue Q.
        Q.add(N);

        // Mark the cell N as visited. 
        visited.add(N);

    }
    // Helper function for dfs()
    private ArrayList<Integer> getNeighbors(int i) {
        int gridSize = Integer.parseInt(Settings.get("gridSize"));
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

    private int selectRandomUnvisitedNeighborOrBacktrack(int N, ArrayList<Integer> Q, ArrayList<Integer> visited) { // This, is a Method. -Dear god! There's more. -No! It contains, a Method. -Dear god! There's more. -No! It contains, a Method. -Dear god! There's more. -No! It contains, a Method. -Dear god! There's more. -No! It contains, a Method. -Dear god! There's more. -No! It contains, a Method. -Dear god! There's more. -No! It contains, a Method.
        // Randomly select an adjacent cell A of node N that has not been visited.
        ArrayList<Integer> neighbors = getNeighbors(N);
        // Filter unvisited neighbors
        ArrayList<Integer> unvisited = new ArrayList<>();
        for (int neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                unvisited.add(neighbor);
            }
        }

        // Check if any unvisited neighbor exists
        if (!unvisited.isEmpty()) {
            // Return A
            return unvisited.get(r.nextInt(unvisited.size()));

        } else {
            // handle the case where all neighbors are visited
            Q.removeLast();
            return selectRandomUnvisitedNeighborOrBacktrack(Q.get(Q.size()-1), Q, visited);
        }
    }

}
