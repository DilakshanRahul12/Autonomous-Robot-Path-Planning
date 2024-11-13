package Maze;

public class Graph {
    private int rows;
    private int cols;
    private CustomLinkedList[] adjacencyList;

    public Graph(Grid grid) {
        this.rows = grid.getRows();
        this.cols = grid.getCols();
        adjacencyList = new CustomLinkedList[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            adjacencyList[i] = new CustomLinkedList();
        }
        createGraph(grid);
    }

    public void addEdge(int src, int dest) {
        adjacencyList[src].add(dest);
    }

    public CustomLinkedList getAdjacencyList(int vertex) {
        return adjacencyList[vertex];
    }

    private void createGraph(Grid grid) {
        // Connect each cell with its adjacent cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int vertex = i * cols + j;
                if (!grid.getCell(i, j).isOccupied()) {
                    if (i > 0 && !grid.getCell(i - 1, j).isOccupied()) { // Connect with cell above
                        addEdge(vertex, (i - 1) * cols + j);
                    }
                    if (i < rows - 1 && !grid.getCell(i + 1, j).isOccupied()) { // Connect with cell below
                        addEdge(vertex, (i + 1) * cols + j);
                    }
                    if (j > 0 && !grid.getCell(i, j - 1).isOccupied()) { // Connect with cell to the left
                        addEdge(vertex, i * cols + (j - 1));
                    }
                    if (j < cols - 1 && !grid.getCell(i, j + 1).isOccupied()) { // Connect with cell to the right
                        addEdge(vertex, i * cols + (j + 1));
                    }
                }
            }
        }
    }

    public void printGraph() {
        for (int i = 0; i < adjacencyList.length; i++) {
            System.out.print("Vertex " + i + ": ");
            CustomLinkedList list = adjacencyList[i];
            CustomLinkedList.LNode current = list.getHead();
            while (current != null) {
                System.out.print(current.value + " ");
                current = current.next;
            }
            System.out.println();
        }
        System.out.println();
    }
}
