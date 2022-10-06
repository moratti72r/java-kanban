package historymanager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomLinkedList<T extends Task> {

    class Node<T extends Task> {
        public Node<T> prev;
        public T data;
        public Node<T> next;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size = 0;
    private HashMap<Integer, Node<T>> nodeMap = new HashMap<>();


    public void linkLast(T task) {
        if (nodeMap.isEmpty()) {
            final Node<T> newNode = new Node<>(null, task, null);
            nodeMap.put(task.getId(), newNode);
            head = newNode;
            tail = newNode;
            size++;
        } else if (nodeMap.containsKey(task.getId())) {
            Node<T> node = nodeMap.get(task.getId());
            if (node == head && tail != head) {
                node.next.prev = null;
                head = node.next;
                node.next = null;
                node.prev = tail;
                tail = node;
            } else if (node != tail) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.next = null;
                tail.next = node;
                node.prev = tail;
                tail = node;
            }
        } else {
            if (size > 9) {
                removeNode(head);
            }
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, task, null);
            nodeMap.put(task.getId(), newNode);
            tail = newNode;
            if (oldTail == null) {
                tail = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
        }
    }

    public void removeNode(Node<T> node) {
        if (nodeMap.containsKey(node.data.getId())) {
            if (node == head && tail != head) {
                head = node.next;
                node.next.prev = null;
                node.next = null;
            } else if (node == tail && tail != head) {
                tail = node.prev;
                node.prev.next = null;
                node.prev = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.next = null;
                node.prev = null;
            }
            nodeMap.remove(node.data.getId());
            size--;
        }
    }

    public void removeNode(Integer id) {
        if (nodeMap.containsKey(id)) {
            Node node = nodeMap.get(id);
            if (node == head && tail != head) {
                head = node.next;
                node.next.prev = null;
                node.next = null;
            } else if (node == tail && tail != head) {
                tail = node.prev;
                node.prev.next = null;
                node.prev = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.next = null;
                node.prev = null;
            }
            nodeMap.remove(id);
            size--;
        }
    }

    public ArrayList<T> getTasks() {
        ArrayList<T> tasks = new ArrayList<>();
        Node<T> node = head;
        tasks.add((T) node.data);
        while (node.next != null) {
            node = node.next;
            tasks.add(node.data);
        }
        return tasks;
    }
}

