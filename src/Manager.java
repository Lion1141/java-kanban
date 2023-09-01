import java.util.HashMap;

public class Manager {
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public static int nextId = 0;

    private void setNextId(int countId) {
        nextId = countId;
    }
    public void createTask(Object obj) { //2.4
        String status = "NEW";
        int id = nextId;
        id++;
        String nameClass = String.valueOf(obj.getClass());

        if (nameClass.equals("class Task")) {
            Task task = (Task) obj;
            task.id = id;
            tasks.put(id, task);
        } else if (nameClass.equals("class Epic")) {
            Epic epic = (Epic) obj;
            epic.id = id;
            epics.put(id, epic);
        } else if (nameClass.equals("class Subtask")) {
            Subtask subtask = (Subtask) obj;
            subtask.id = id;
            // вызов функции для внесения id сабтаска, в список сабтасков его эпика
            if (!epics.containsKey(subtask.getIdEpic())) { // проверяем наличие Epic с введенным id
                System.out.println("Epic с id " + subtask.getIdEpic() + " не существует! Subtask не создан!");
                return;
            }
                addIdSubtaskToEpics(id, subtask.getIdEpic());
                subtasks.put(id,subtask);
                checkStatusOfSubtask(subtask.getIdEpic());
        }
        setNextId(id);
    }

    private void addIdSubtaskToEpics(int idSubtask, int idEpic) { // добавление id сабтаска в список сабтасков эпика
        Epic epic = epics.get(idEpic); // получаем объект Epic с указанным id
        epic.getIdSubtask().add(idSubtask); // добавляем id сабтаска в ArrayList Epic'а
    }

    private void removeIdSubtaskFromEpics(int idSubtask, int idEpic) {
        Epic epic = epics.get(idEpic); // получаем объект Epic с указанным id
        int position = 0;
        for (Integer valueId : epic.getIdSubtask()) {
            if (valueId == idSubtask) {
                break;
            }
            position++;
        }
        epic.getIdSubtask().remove(position);

    }

    public void printAllTask() { //2.1
        printTask();
        printEpic();
        printSubtask();
    }

    public void clearAllTasks() { //2.2
        tasks.clear();
        subtasks.clear();
        epics.clear();
        System.out.println("Все задачи удалены");
        System.out.println(tasks + "\n" + subtasks + "\n" + epics);
        }

    public Object getById(int id) { // 2.3 Получение по идентификатору
        if (tasks.containsKey(id)) { // проверка на наличие переданного ID
            for (Integer key : tasks.keySet()) {
                if (key == id) {
                    System.out.print(tasks.get(key).id + " ");
                    System.out.print(tasks.get(key).name + " ");
                    System.out.print(tasks.get(key).status + " ");
                    System.out.println();
                    return tasks.get(key);
                }
            }
        } else if (epics.containsKey(id)) {
            for (Integer key : epics.keySet()) {
                if (key == id) {
                    System.out.print(epics.get(key).id + " ");
                    System.out.print(epics.get(key).name + " ");
                    System.out.print(epics.get(key).status + " ");
                    System.out.println();
                    return epics.get(key);
                }
            }
        } else if (subtasks.containsKey(id)) {
            for (Integer key : subtasks.keySet()) {
                if (key == id) {
                    System.out.print(subtasks.get(key).id + " ");
                    System.out.print(subtasks.get(key).name + " ");
                    System.out.print(subtasks.get(key).status + " ");
                    System.out.println();
                    return subtasks.get(key);
                }
            }
        }
        return null;
    }
    // 2.5
    public void updateTask(Task task) { // Обновление Task
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Ошибка обновления! Данные у задачи №" + task.getId() + " не сохранены! id Таска не существует!");
        }
    }

    public void updateSubtask(Subtask subtask) { // Обновление Subtask
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getIdEpic())) {
            Subtask oldSubtask = subtasks.get(subtask.getId());
            subtasks.put(subtask.getId(), subtask);
            addIdSubtaskToEpics(subtask.getId(), subtask.getIdEpic()); // добавляем сабтакс в список нового эпика
            removeIdSubtaskFromEpics(subtask.getId(), oldSubtask.getIdEpic()); // удаляем сабтакс из списка старого эпика
            checkStatusOfSubtask(subtask.getIdEpic()); // обновляем статус у нового эпика
            checkStatusOfSubtask(oldSubtask.getIdEpic()); // обновляем статус у старого эпика
        } else {
            System.out.println("Ошибка обновления! Данные не сохранены! id Эпика / id Сабтаска не существует!");
        }
    }

    public void updateEpic(Epic epic) { // Обновление Epic
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            epic.setIdSubtask(oldEpic.getIdSubtask()); // сохраняем старый список сабтасков у эпика
            epics.put(epic.getId(), epic);
            checkStatusOfSubtask(epic.getId()); // обновляем статус у  эпика
        } else {
            System.out.println("Ошибка обновления! Данные у задачи №" + epic.getId() + " не сохранены! id Эпика не существует!");
        }
    }

    public void printEpicSubtask(int idEpic) { //Получение списка всех подзадач определённого эпика
        System.out.println("Все Subtask для Epic'a №" + idEpic + ":");
        for (Subtask value : subtasks.values()) {
            if (value.getIdEpic() == idEpic) {
                System.out.print(value.id + " \t");
                System.out.print(value.name + " \t");
                System.out.print(value.status + " \t");
                System.out.print(value.getIdEpic() + " \t");
                System.out.println();
            }
        }
    }

    public void deleteById(int id) { //2.6 Удаление по ID
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (int i = 0; i < epic.getIdSubtask().size(); i++) { //удаление всех сабтасков эпика
                deleteById(epic.getIdSubtask().get(i));
            }
            epics.remove(id); //удаляем эпик
        } else if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        } else {
            System.out.println("Ошибка удаления! Такого id не существует!");
        }
    }

    private void printTask() { // 2.1 Получение всех задач (Task)
        System.out.println("***********Task**********");
        System.out.println("=========================");
        System.out.println("id/name/status");
        System.out.println("=========================");
        for (Task value : tasks.values()) {
            System.out.print(value.id + " \t");
            System.out.print(value.name + " \t");
            System.out.print(value.status + " \t");
            System.out.println();
        }
        System.out.println();
    }

    private void printSubtask() { // 2.1 Получение всех задач (Subtask)
        System.out.println("*********Subtask*********");
        System.out.println("=========================");
        System.out.println("id/name/status/idEpic");
        System.out.println("=========================");
        for (Subtask value : subtasks.values()) {
            System.out.print(value.id + " \t");
            System.out.print(value.name + " \t");
            System.out.print(value.status + " \t");
            System.out.print(value.getIdEpic() + " \t");
            System.out.println();
        }
        System.out.println();
    }

    private void printEpic() { // 2.1 Получение всех задач (Epic)
        System.out.println("***********Epic**********");
        System.out.println("=========================");
        System.out.println("id/name/status/idSubtasks");
        System.out.println("=========================");
        for (Epic value : epics.values()) {
            System.out.print(value.id + " \t");
            System.out.print(value.name + " \t");
            System.out.print(value.status + " \t");
            System.out.print(value.getIdSubtask() + " \t");
            System.out.println();
        }
        System.out.println();
    }

    private void checkStatusOfSubtask(int idEpic) { // проверяем статусы всех сабтасков при обновлении каждого из них и меняем у эпика
        int countNew = 0;
        int countDone = 0;
        int countSubtasksOfEpic = 0;
        Epic epic = epics.get(idEpic); // берем из HashMap Epic с необходимым id
        for (Subtask value : subtasks.values()) {
            if (value.getIdEpic() == idEpic) {
                countSubtasksOfEpic++; // считаем количество сабтасков у определенного эпика
                if (value.status.equalsIgnoreCase("DONE")) {
                    countDone++;
                } else if (value.status.equalsIgnoreCase("NEW")) {
                    countNew++;
                }

            }
        }
        // Смена статуса у родительского эпика в зависимости от статусов его сабтасков
        if (countDone == countSubtasksOfEpic) { // DONE
            epic.status = "DONE";
        } else if (countNew == countSubtasksOfEpic) { // NEW
            epic.status = "NEW";
        } else {
            epic.status = "IN_PROGRESS";
        }
    }
}
