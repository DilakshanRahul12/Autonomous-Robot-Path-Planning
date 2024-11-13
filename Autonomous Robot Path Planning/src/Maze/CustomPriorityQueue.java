package Maze;

public class CustomPriorityQueue {
    private QNode[] heap;
    private int maxSize;
    private int currentSize;

    public CustomPriorityQueue(int maxSize) {
        this.maxSize = maxSize;
        heap = new QNode[maxSize];
        currentSize = 0;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public boolean isFull() {
        return currentSize == maxSize;
    }

    public void insert(QNode node) {
        if (isFull()) {
            System.out.println("Priority Queue is full. Cannot insert node.");
            return;
        }

        heap[currentSize] = node;
        trickleUp(currentSize++);
    }

    public QNode remove() {
        QNode root = heap[0];
        heap[0] = heap[--currentSize];
        trickleDown(0);
        return root;
    }
    private void trickleUp(int index) {
        int parent = (index - 1) / 2;
        QNode bottom = heap[index];

        while (index > 0 && heap[parent].priority > bottom.priority) {
            heap[index] = heap[parent];
            index = parent;
            parent = (parent - 1) / 2;
        }
        heap[index] = bottom;
    }

    private void trickleDown(int index) {
        int largerChild;
        QNode top = heap[index];
        while (index < currentSize / 2) {
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;

            if (rightChild < currentSize && heap[leftChild].priority > heap[rightChild].priority) {
                largerChild = rightChild;
            } else {
                largerChild = leftChild;
            }

            if (top.priority <= heap[largerChild].priority) {
                break;
            }

            heap[index] = heap[largerChild];
            index = largerChild;
        }
        heap[index] = top;
    }

    public void printQueue() {
        System.out.println("Priority Queue:");
        for (int i = 0; i < currentSize; i++) {
            System.out.println("Vertex: " + heap[i].vertex + ", Priority: " + heap[i].priority);
        }
    }

    public static class QNode {
        int vertex;
        int priority;

        public QNode(int vertex, int priority) {
            this.vertex = vertex;
            this.priority = priority;
        }
    }
}