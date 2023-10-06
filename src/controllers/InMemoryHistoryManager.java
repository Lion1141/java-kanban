package controllers;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> taskIdToNode = new HashMap<>();

    Node head;
    Node tail;

    public List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.data);
            node = node.getNext();
        }
        return tasks;
    }

    @Override
    public void addTask(Task task) {
        int taskId = task.getId();

        if (taskIdToNode.containsKey(taskId)) {
            removeNode(taskIdToNode.get(taskId));
        }

        Node node = linkLast(task);
        taskIdToNode.put(taskId, node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int taskId) {
        Node node = taskIdToNode.get(taskId);
        removeNode(node);
        taskIdToNode.remove(taskId);
    }

        public Node linkLast(Task task) {
            final Node oldTail = tail;
            final Node newNode = new Node(oldTail, task, null);

            tail = newNode;

            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.setNext(newNode);
            }
            return newNode;
        }

            public void removeNode (Node node){
                Node prevNode = node.getPrev();
                Node nextNode = node.getNext();

                if (prevNode != null) {
                    prevNode.setNext(nextNode);
                } else {
                    head = nextNode;
                }

                if (nextNode != null) {
                    nextNode.setPrev(prevNode);
                } else {
                    tail = prevNode;
                }
            }

    private static class Node {
        private Task data;
        private Node next;
        private Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        public Task getData() {
            return data;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setData(Task data) {
            this.data = data;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}
