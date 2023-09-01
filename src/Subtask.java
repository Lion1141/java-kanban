public class Subtask extends Task {
    private int idEpic; //ID эпика, к которому относится сабтаск

    public Subtask(String name, String description, int idEpic) { // конструктор при создании объекта (id генерируется первый свободный)
        super(name, description);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, int id, String status, int idEpic) { // конструктор при обновлении объекта по id (id известен)
        super(name, description, id, status);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }
}
