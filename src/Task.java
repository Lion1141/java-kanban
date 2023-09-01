public class Task {
    protected String name;
    protected int id;
    protected String description;
    protected String status;

    public Task(String name, String description) { //конструктор для создания объекта с новым id
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }

    public Task(String name, String description, int id) { //конструктор для обновления объекта без обновления статуса
        this.name = name;
        this.id = id;
        this.description = description;
    }

    public Task(String name, String description, int id, String status) { //конструктор для обновления объекта по ID
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public int getId() {

        return id;
    }
}

