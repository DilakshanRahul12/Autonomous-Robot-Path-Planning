package Maze;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Grid {
    private int rows;
    private int cols;
    private Cell[][] cells;
    private Random random;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        this.random = new Random();
        // Initialize grid cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public boolean isCellOccupied(int row, int col) {
        return cells[row][col].isOccupied();
    }

    // Method to randomly generate obstacles within the grid
    public void generateObstacles(int numObstacles) {
        int generatedObstacles = 0;
        while (generatedObstacles < numObstacles) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (!cells[row][col].isOccupied()) {
                cells[row][col].setOccupied(true);
                generatedObstacles++;
            }
        }
    }
    // Method to print the grid with numbered rows and columns
    public void printGrid() {
        System.out.println("Grid with Obstacles");
        // Print column numbers
        System.out.print("   ");
        for (int j = 0; j < cols; j++) {
            if (j<10){
                System.out.print(" " + j + " ");
            }
            else {
                System.out.print(j + " ");
            }
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            // Print row number
            if (i<10) {
                System.out.print(i + "  ");
            }
            else {
                System.out.print(i + " ");
            }

            for (int j = 0; j < cols; j++) {
                if (cells[i][j].isOccupied()) {
                    System.out.print("[X]"); // Indicates obstacle
                } else {
                    System.out.print("[ ]"); // Empty cell
                }
            }
            System.out.println(); // Move to the next row
        }
        System.out.println();
    }

    public void outputToFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            // Write grid configuration to the file
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (cells[i][j].isOccupied()) {
                        writer.write("[X]"); // Indicates obstacle
                    } else {
                        writer.write("[ ]"); // Empty cell
                    }
                }
                writer.write("\n"); // Move to the next row
            }
            writer.close();
            System.out.println("Grid configuration has been written to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printGridWithOptimalPath(CustomLinkedList optimalPath) {
        System.out.println("Grid with Optimal Path");
        System.out.print("   ");
        for (int j = 0; j < cols; j++) {
            if (j<10){
                System.out.print(" " + j + " ");
            }
            else {
                System.out.print(j + " ");
            }
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            if (i<10) {
                System.out.print(i + "  ");
            }
            else {
                System.out.print(i + " ");
            }
            for (int j = 0; j < cols; j++) {
                if (isCellInOptimalPath(i, j, optimalPath)) {
                    System.out.print("[*]"); // Path cell
                } else if (cells[i][j].isOccupied()) {
                    System.out.print("[X]"); // Obstacle
                } else {
                    System.out.print("[ ]"); // Empty cell
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean isCellInOptimalPath(int row, int col, CustomLinkedList optimalPath) {
        CustomLinkedList.LNode node = optimalPath.getHead();
        while (node != null) {
            int vertex = node.value;
            int vertexRow = vertex / cols;
            int vertexCol = vertex % cols;
            if (vertexRow == row && vertexCol == col) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

}


