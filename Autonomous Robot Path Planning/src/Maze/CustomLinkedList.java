package Maze;

public class CustomLinkedList {
    private LNode head;

    public void add(int value) {
        LNode newNode = new LNode(value);
        if (head == null) {
            head = newNode;
        } else {
            LNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public boolean contains(int value) {
        LNode current = head;
        while (current != null) {
            if (current.value == value) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void reverse() {
        LNode prev = null;
        LNode current = head;
        LNode next = null;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        head = prev;
    }

    public static class LNode {
        int value;
        LNode next;

        LNode(int value) {
            this.value = value;
        }
    }
    public LNode getHead() {
        return head;
    }

}
