package model;

public class Task {
    protected String name;
    protected int id;
    protected String description;
    protected TaskStatus status;

    public Task(String name, String description) { //конструктор для создания объекта с новым id
        this.name = name;
        this.description = description;
        this.status = status;
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
}