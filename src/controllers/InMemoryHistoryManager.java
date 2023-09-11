package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_SIZE_HISTORY = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void addTask(Task task) {
        if (task != null) {
            if (history.size() > MAX_SIZE_HISTORY) {
                history.removeFirst();
                history.add(task);
            } else {
                history.add(task);
            }
        }
    }
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
