package src;

public class LL<T> {
    private Node head = null;
    private Node end = null;
    private int size = 0;

    private class Node {
        private final T object;
        private Node prev = null;
        private Node next = null;

        Node(T object) {
            this.object = object;
        }
    }

    public void addLast(T in) {
        Node node = new Node(in);
        if (head == null) {
            head = node;
            end = node;
        } else {
            node.prev = end;
            end.next = node;
            end = node;
        }
        size++;
    }

    public void addFirst(T in) {
        Node node = new Node(in);
        if (end == null) {
            end = node;
            head = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    public void addAt(int index, T in) {
        Node prevNode = getNode(index - 1);
        Node nextNode = prevNode.next;
        Node newNode = new Node(in);
        newNode.prev = prevNode;
        newNode.next = nextNode;
        prevNode.next = newNode;
        nextNode.prev = newNode;
        size++;
    }

    public T getFirst() {
        return head.object;
    }

    public T getLast() {
        return end.object;
    }

    public T get(int index) {
        return getNode(index).object;
    }

    private Node getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index " + index + " is out of bounds");
        }
        Node currNode;
        if (index <= size - 1 - index) {
            currNode = head;
            for (int i = 0; i < index; i++) {
                currNode = currNode.next;
            }
        } else {
            currNode = end;
            for (int i = size - 1; i > index; i--) {
                currNode = currNode.prev;
            }
        }
        return currNode;
    }

    public void remove(int index) {
        Node node = getNode(index);
        Node prevNode = node.prev;
        Node nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        size--;
    }

    public int getSize() {
        return size;
    }
}
