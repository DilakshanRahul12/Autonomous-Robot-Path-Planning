package Maze;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int rows, cols;
        System.out.println();
        System.out.println("<-- Advice to use the range for the grid (3x3) to (3500x3500) -->");
        System.out.println();
        do {
            // Ask for grid size
            System.out.print("Enter number of rows: ");
            rows = scanner.nextInt();
            System.out.print("Enter number of columns: ");
            cols = scanner.nextInt();

            if (rows < 3 || cols < 3) {
                System.out.println("Grid size is too small. Please enter values greater than 3 for both ...");
                System.out.println();
            }
        } while (rows < 3 || cols < 3);


        // Create a grid
        Grid grid = new Grid(rows, cols);

        // Ask for the percentage of obstacles
        System.out.print("Enter the percentage of obstacles (0-100): ");
        int obstaclePercentage = scanner.nextInt();
        grid.generateObstacles((int) (rows * cols * obstaclePercentage / 100.0)); // Generate obstacles based on the percentage

        // Print the grid
        grid.printGrid();
        grid.outputToFile("GridOutput");

        // Create a graph from the grid

        Graph graph = new Graph(grid);

                                                //(Test to print the vertices)
//        System.out.println("Graph:");
//        graph.printGraph();

        // Ask for robot start point
        int startRow, startCol;
        boolean validStartPoint;
        do {
            System.out.print("Enter robot start row: ");
            startRow = scanner.nextInt();
            System.out.print("Enter robot start column: ");
            startCol = scanner.nextInt();
            validStartPoint = !grid.isCellOccupied(startRow, startCol);
            if (!validStartPoint) {
                System.out.println("Start point is an obstacle. Please enter another point.");
                System.out.println();
            }
        } while (!validStartPoint);
        System.out.print("Enter robot start direction (NORTH, SOUTH, EAST, WEST): ");
        String startDirection = scanner.next().toUpperCase();

        System.out.println();

        int goalRow, goalCol;
        do {
            System.out.print("Enter the goal row for the robot: ");
            goalRow = scanner.nextInt();
            System.out.print("Enter the goal column for the robot: ");
            goalCol = scanner.nextInt();

            if (grid.isCellOccupied(goalRow, goalCol)) {
                System.out.println("The end point cell is occupied. Please enter another point.");
                System.out.println();
            }
        } while (grid.isCellOccupied(goalRow, goalCol));


        // Create a robot
        Robot robot = new Robot(startRow, startCol, startDirection, goalRow, goalCol);
        robot.printGridWithRobot(grid);

        // Find the optimal path
        CustomLinkedList path = robot.findOptimalPath(graph, grid);

        robot.moveAlongOptimalPath(path,grid);
        robot.calculatePathDistance(path,grid);

        // Print the final grid with the robot's new position after moving along the optimal path
        robot.printGridWithOptimalPathToFile(grid, path, "GridOutputWithOptimalPath.txt");

        scanner.close();
    }
}
