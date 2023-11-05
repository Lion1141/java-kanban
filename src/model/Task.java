package model;
import java.time.Duration;
import java.time.LocalDateTime;

import static model.TaskType.TASK;

public class Task {
    protected String name;
    protected int id;
    protected String description;
    protected TaskStatus status;
    protected TaskType taskType = TASK;
    protected LocalDateTime startTime;  //Дата начала
    protected Duration duration;        //Продолжительность

    public Task(String name, String description) { //конструктор для создания объекта с новым id
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, int id) { //конструктор для обновления объекта без обновления статуса
        this.name = name;
        this.id = id;
        this.description = description;
    }

    public Task(String name, String description, int id, TaskStatus status) { //конструктор для обновления объекта по ID
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public Task(String name, int id, String description, TaskStatus status, TaskType taskType, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
        this.taskType = taskType;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, int id, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
        this.taskType = taskType;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    //Вычисление времени окончания задачи
    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null)
            return startTime.plusSeconds(duration.toSeconds());
        else
            return null;
    }
}