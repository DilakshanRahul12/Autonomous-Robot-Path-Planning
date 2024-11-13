package Maze;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileInputTest {

    public static void main(String[] args) {
//        File inputFile = new File("Input_1001x1001.txt");
//        File inputFile = new File("Input_11x11.txt");
//        File inputFile = new File("Input_101x101.txt");
//        File inputFile = new File("Input_3501x3501.txt");
        File inputFile = new File("Input_10001x10001.txt");

        try {
            Scanner fileScanner = new Scanner(inputFile);
            int rows, cols, obstaclePercentage, startRow, startCol, goalRow, goalCol;
            String startDirection;

            // Read grid size
            rows = fileScanner.nextInt();
            cols = fileScanner.nextInt();
            Grid grid = new Grid(rows, cols);

            // Read obstacle percentage
            obstaclePercentage = fileScanner.nextInt();
            grid.generateObstacles((int) (rows * cols * obstaclePercentage / 100.0));

            // Read start point and direction
            startRow = fileScanner.nextInt();
            startCol = fileScanner.nextInt();
            startDirection = fileScanner.next().toUpperCase();

            // Read goal point
            goalRow = fileScanner.nextInt();
            goalCol = fileScanner.nextInt();

            // Create graph and robot
            Graph graph = new Graph(grid);
            Robot robot = new Robot(startRow, startCol, startDirection, goalRow, goalCol);

            // Find optimal path
            CustomLinkedList path = robot.findOptimalPath(graph, grid);
            robot.moveAlongOptimalPath(path, grid);
            robot.calculatePathDistance(path, grid);

            // Print final grid with optimal path to file
            robot.printGridWithOptimalPathToFile(grid, path, "GridOutputWithOptimalPath.txt");

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
        }
    }
}
