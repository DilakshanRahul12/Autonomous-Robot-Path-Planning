package Maze;

import java.io.FileWriter;
import java.io.IOException;

public class Robot {
    private int row;
    private int col;
    private String direction;
    private int goalRow; // New instance variable for goal row
    private int goalCol; // New instance variable for goal column

    public Robot(int row, int col, String direction, int goalRow, int goalCol) {
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getDirection() {
        return direction;
    }

    private void moveForward() {
        // Implement logic to move the robot forward based on its direction
        switch (direction) {
            case "NORTH":
                row--;
                break;
            case "SOUTH":
                row++;
                break;
            case "EAST":
                col++;
                break;
            case "WEST":
                col--;
                break;
        }
    }

    private void turnLeft() {
        // Implement logic to turn the robot left
        switch (direction) {
            case "NORTH":
                direction = "WEST";
                break;
            case "SOUTH":
                direction = "EAST";
                break;
            case "EAST":
                direction = "NORTH";
                break;
            case "WEST":
                direction = "SOUTH";
                break;
        }
    }

    public void printGridWithRobot(Grid grid) {
        System.out.println("Grid with Robot");
        // Print column numbers
        System.out.print("  ");
        for (int j = 0; j < grid.getCols(); j++) {
            if(j<10) {
                System.out.print(" " + j + " ");
            }
            else {
                System.out.print(j + " ");
            }
        }
        System.out.println();

        for (int i = 0; i < grid.getRows(); i++) {
            // Print row number
            System.out.print(i + " ");

            for (int j = 0; j < grid.getCols(); j++) {
                if (i == row && j == col) {
                    System.out.print("[R]"); // Indicates robot
                } else if (grid.getCell(i, j).isOccupied()) {
                    System.out.print("[X]"); // Indicates obstacle
                } else {
                    System.out.print("[ ]"); // Empty cell
                }
            }
            System.out.println(); // Move to the next row
        }
        System.out.println();
    }

    public CustomLinkedList findOptimalPath(Graph graph, Grid grid) {
        long startTime = System.nanoTime();
        int startVertex = row * grid.getCols() + col;
        int goalVertex = goalRow * grid.getCols() + goalCol; // Set the goal vertex based on the provided goal row and column

        CustomPriorityQueue priorityQueue = new CustomPriorityQueue(grid.getRows() * grid.getCols());
        int[] distances = new int[grid.getRows() * grid.getCols()];
        int[] previous = new int[grid.getRows() * grid.getCols()];
        boolean[] visited = new boolean[grid.getRows() * grid.getCols()];

        // Initialize distances and visited array
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
            previous[i] = -1;
            visited[i] = false;
        }
        distances[startVertex] = 0;

        priorityQueue.insert(new CustomPriorityQueue.QNode(startVertex, 0));

        while (!priorityQueue.isEmpty()) {
            CustomPriorityQueue.QNode current = priorityQueue.remove();
            int currentVertex = current.vertex;

            if (currentVertex == goalVertex) {
                break; // Stop when goal vertex is reached
            }

            if (visited[currentVertex]) {
                continue; // Skip if already visited
            }
            visited[currentVertex] = true;

            CustomLinkedList neighbors = graph.getAdjacencyList(currentVertex);
            CustomLinkedList.LNode neighborNode = neighbors.getHead();

            while (neighborNode != null) {
                int neighborVertex = neighborNode.value;

                // Calculate neighbor cell coordinates
                int neighborRow = neighborVertex / grid.getCols();
                int neighborCol = neighborVertex % grid.getCols();

                // Skip if neighbor cell is occupied by an obstacle
                if (grid.getCell(neighborRow, neighborCol).isOccupied()) {
                    neighborNode = neighborNode.next;
                    continue;
                }

                int distance = distances[currentVertex] + 1; // Each edge has a weight of 1
                int heuristic = calculateHeuristic(neighborRow, neighborCol, goalRow, goalCol); // Calculate heuristic (Manhattan distance)
                int totalCost = distance + heuristic; // Calculate total cost (f = g + h)

                if (totalCost < distances[neighborVertex]) {
                    distances[neighborVertex] = totalCost;
                    previous[neighborVertex] = currentVertex;
                    priorityQueue.insert(new CustomPriorityQueue.QNode(neighborVertex, totalCost));
                }
                neighborNode = neighborNode.next;
            }
        }

        // Find optimal path
        CustomLinkedList optimalPath = new CustomLinkedList();
        int currentVertex = goalVertex;
        while (currentVertex != -1) {
            optimalPath.add(currentVertex);
            currentVertex = previous[currentVertex];
        }

        // Reverse the optimal path to start from the robot's position
        optimalPath.reverse();

        // Check if a path to the goal vertex was found
        if (distances[goalVertex] == Integer.MAX_VALUE) {
            System.out.println("No path to the goal found.");
            return null; // Return null to indicate no path found
        }

        // Print optimal path
        System.out.println("Optimal Path:");
        CustomLinkedList.LNode pathNode = optimalPath.getHead();
        while (pathNode != null) {
            int vertex = pathNode.value;
            int vertexRow = vertex / grid.getCols();
            int vertexCol = vertex % grid.getCols();
            System.out.println("Row: " + vertexRow + ", Col: " + vertexCol);
            pathNode = pathNode.next;
        }
        System.out.println();
        grid.printGridWithOptimalPath(optimalPath);

        System.out.println();

        long endTime = System.nanoTime();
        long durationMilliseconds = (endTime - startTime) / 1000000;
        System.out.println("Time taken to find the optimal path: " + durationMilliseconds + " milliseconds");

        System.out.println();
        return optimalPath;
    }

    // Method to calculate Manhattan distance (heuristic)
    private int calculateHeuristic(int currentRow, int currentCol, int goalRow, int goalCol) {
        int edge_length = 1;
        return (Math.abs(goalRow - currentRow) + Math.abs(goalCol - currentCol))*edge_length;
    }

    public void moveAlongOptimalPath(CustomLinkedList optimalPath, Grid grid) {
        if (optimalPath == null){
            System.out.println("No path");
            System.exit(0);
        }

        CustomLinkedList.LNode currentNode = optimalPath.getHead();

        while (currentNode != null) {
            int vertex = currentNode.value;
            int vertexRow = vertex / grid.getCols();
            int vertexCol = vertex % grid.getCols();

            // Determine movement direction
            while (row != vertexRow || col != vertexCol) {
                if (vertexRow < row) {
                    // Move north
                    if (!direction.equals("NORTH")) {
                        while (!direction.equals("NORTH")) {
                            turnLeft();
                            System.out.println("Turned left. Current direction: " + direction);
                        }
                    }
                    moveForward();
                    System.out.println("Moved forward. Current position: (" + row + ", " + col + ")");
                } else if (vertexRow > row) {
                    // Move south
                    if (!direction.equals("SOUTH")) {
                        while (!direction.equals("SOUTH")) {
                            turnLeft();
                            System.out.println("Turned left. Current direction: " + direction);
                        }
                    }
                    moveForward();
                    System.out.println("Moved forward. Current position: (" + row + ", " + col + ")");
                } else if (vertexCol < col) {
                    // Move west
                    if (!direction.equals("WEST")) {
                        while (!direction.equals("WEST")) {
                            turnLeft();
                            System.out.println("Turned left. Current direction: " + direction);
                        }
                    }
                    moveForward();
                    System.out.println("Moved forward. Current position: (" + row + ", " + col + ")");
                } else if (vertexCol > col) {
                    // Move east
                    if (!direction.equals("EAST")) {
                        while (!direction.equals("EAST")) {
                            turnLeft();
                            System.out.println("Turned left. Current direction: " + direction);
                        }
                    }
                    moveForward();
                    System.out.println("Moved forward. Current position: (" + row + ", " + col + ")");
                }
            }

            currentNode = currentNode.next; // Move to the next node in the optimal path
        }
        System.out.println();
    }

    public int calculatePathDistance(CustomLinkedList optimalPath, Grid grid) {
        if (optimalPath == null) {
            System.out.println("No path found.");
            return -1;
        }

        int distance = 0;
        CustomLinkedList.LNode currentNode = optimalPath.getHead();

        while (currentNode != null && currentNode.next != null) {
            int currentVertex = currentNode.value;
            int nextVertex = currentNode.next.value;

            // Calculate row and column indices for the current and next vertices
            int currentRow = currentVertex / grid.getCols();
            int currentCol = currentVertex % grid.getCols();
            int nextRow = nextVertex / grid.getCols();
            int nextCol = nextVertex % grid.getCols();

            // Calculate Manhattan distance between current and next vertices
            int dx = Math.abs(nextCol - currentCol);
            int dy = Math.abs(nextRow - currentRow);

            // Update the total distance
            distance += dx + dy;

            currentNode = currentNode.next; // Move to the next node in the path
        }
        distance += 1;
        System.out.println("Path distance: " + distance);
        System.out.println();
        return distance;
    }

    public void printGridWithOptimalPathToFile(Grid grid, CustomLinkedList optimalPath, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);

            // Write grid configuration to the file
            for (int i = 0; i < grid.getRows(); i++) {
                for (int j = 0; j < grid.getCols(); j++) {
                    if (i == row && j == col) {
                        writer.write("[R]"); // Write robot position
                    } else if (optimalPath.contains(i * grid.getCols() + j)) {
                        writer.write("[*]"); // Write optimal path
                    } else {
                        writer.write("[ ]"); // Write empty cell
                    }
                }
                writer.write("\n"); // Move to the next row
            }

            writer.close();
            System.out.println("Grid configuration with optimal path has been written to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
