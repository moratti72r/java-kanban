package historymanager;

import task.Epic;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomLinkedList {

    class Node<Task> {
        public Node<Task> prev;
        public Task data;
        public Node<Task> next;

        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }

    private Node<Task> head;
    private Node<Task> tail;
    private HashMap<Integer, Node<Task>> nodeMap = new HashMap<>();


    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        nodeMap.put(task.getId(), newNode);
        tail = newNode;
        if (oldTail == null) {
            tail = newNode;
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    public void removeNode(Node<Task> node) {
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
                nodeMap.remove(node.data.getId());
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
        if (nodeMap.get(id).data instanceof Epic) {
            for (Integer idSubtask : ((Epic) nodeMap.get(id).data).getSubtasks().keySet()) {
                if (nodeMap.containsKey(idSubtask)) {
                    removeNode(nodeMap.get(idSubtask));
                }
            }
        }
        removeNode(nodeMap.get(id));
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!nodeMap.isEmpty()) {
            Node<Task> node = head;
            tasks.add(node.data);
            while (node.next != null) {
                node = node.next;
                tasks.add(node.data);
            }
            return tasks;
        } else {
            return null;
        }
    }
}

