package historymanager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomLinkedList {

    class Node {
        public Node prev;
        public Task data;
        public Node next;

        public Node(Node prev, Task data, Node next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }

    private Node head;
    private Node tail;
    private HashMap<Integer, Node> nodeMap = new HashMap<>();


    public void linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        nodeMap.put(task.getId(), newNode);
        tail = newNode;
        if (oldTail == null) {
            tail = newNode;
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    public void removeNode(Node node) {
        if (nodeMap.containsKey(node.data.getId())) {
            if (node == head && tail != head) {
                head = node.next;
                node.next.prev = null;
                node.next = null;
            } else if (node == tail && tail != head) {
                tail = node.prev;
                node.prev.next = null;
                node.prev = null;
            } else if (head == tail) {
                head = null;
                tail = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.next = null;
                node.prev = null;
            }
            nodeMap.remove(node.data.getId());
        }
    }

    public void removeNode(Integer id) {
        if (nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id));
        }
    }

    public boolean contains(Task task) {
        return nodeMap.containsKey(task.getId());
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (head!=null) {
            Node node = head;
            while (node != null) {
                tasks.add(node.data);
                node = node.next;
            }
        }
        return tasks;
    }
}

