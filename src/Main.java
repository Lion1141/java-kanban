import api.HttpTaskServer;
import api.KVServer;
import controllers.Manager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

import java.io.IOException;

import static api.HttpTaskManager.loadFromServer;
import static model.TaskStatus.DONE;

public class Main {
        public static void main(String[] args) {
                try {
                        System.out.println("Стартуем KVServer");
                        new KVServer().start();

                        System.out.println("Стартуем HTTPTaskServer");
                        new HttpTaskServer().start();


                } catch (IOException e) {
                        System.out.println("Возникли проблемы в работе: " + e.getMessage());
                }

                Manager taskManager = Managers.getDefault("http://localhost", 8087);
                /*TaskManager taskManager = Managers.getDefault();*/

                taskManager.createTask(new Task("Task1", "TaskDesc1")); /*0*/
                taskManager.createTask(new Task("Task2", "TaskDesc2")); /*1*/

                taskManager.createEpic(new Epic("Epic1", "Epic1 desc")); /*2*/
                taskManager.createSubtask(new Subtask("Subtask1 Epic1", "Subtask1 Epic1 desc", 2)); /*3*/

                taskManager.createEpic(new Epic("Epic2", "Epic2 desc")); /*4*/
                taskManager.createSubtask(new Subtask("Subtask1 Epic2", "Subtask1 Epic2 desc", 4)); /*5*/
                taskManager.createSubtask(new Subtask("Subtask2 Epic2", "Subtask2 Epic2 desc", 4)); /*6*/

                System.out.println("=====CREATED=====");
                System.out.println("TASKS\n" + taskManager.getTasks());
                System.out.println("EPICS\n" + taskManager.getEpics());
                System.out.println("SUBTASKS\n" + taskManager.getSubtasks());

                taskManager.getTaskById(0);
                taskManager.getTaskById(0);

                taskManager.getEpicById(2);
                taskManager.getEpicById(2);
                taskManager.getEpicById(2);
                taskManager.getEpicById(4);

                taskManager.getSubtaskById(5);
                taskManager.getSubtaskById(6);
                taskManager.getSubtaskById(3);
                taskManager.getSubtaskById(3);
                taskManager.getSubtaskById(3);

                taskManager.getTaskById(0);
                taskManager.getTaskById(0);

                Manager taskManager1 = loadFromServer("http://localhost", 8087);

                System.out.println("=====HISTORY=====");
                System.out.println(taskManager.getHistory());

                taskManager.updateTask(new Task("Task1", "TaskDesc1", 1, DONE));
                taskManager.updateSubtask(new Subtask("Subtask1 Epic2", "Subtask1 Epic2 desc plus some more text", 5,
                        DONE, 4));
                taskManager.updateSubtask(new Subtask("Subtask1 Epic2", "Subtask1 Epic2 desc", 5,
                        DONE, 4));


/*
                Manager manager = Managers.getDefault();

                Task task1 = new Task("Задача 1", "Описание задачи 1"); //создаём задачи
                Integer taskId1 = manager.createTask(task1);
                Task task2 = new Task("Задача 2", "Описание задачи 2");
                Integer taskId2 = manager.createTask(task2);
                Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
                Integer epicId1 = manager.createEpic(epic1);
                Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
                Integer epicId2 = manager.createEpic(epic2);
                Subtask subtask21 = new Subtask("Сабтаск 2.1","Описание сабтаска 2.1", epic2.getId());
                Integer subtaskId21 = manager.createSubtask(subtask21);
                Subtask subtask22 = new Subtask("Сабтаск 2.2", "Описание сабтаска 2.2", epic2.getId());
                Integer subtaskId22 = manager.createSubtask(subtask22);
                Subtask subtask23 = new Subtask("Сабтаск 2.3", "Описание сабтаска 2.3", epic2.getId());
                Integer subtaskId23 = manager.createSubtask(subtask23);



                System.out.println("**************Список задач**************"); //выводим на консоль
                System.out.println(task1);
                System.out.println(task2);
                System.out.println();
                System.out.println("**************Список эпиков с подзадачами**************");
                System.out.println(epic1);
                System.out.println(epic2);
                System.out.println(subtask21);
                System.out.println(subtask22);
                System.out.println(subtask23);
                System.out.println();

                manager.getTaskById(0);
                manager.getTaskById(0);

                manager.getEpicById(3);
                manager.getEpicById(3);
                manager.getEpicById(3);

                manager.getSubtaskById(6);
                manager.getSubtaskById(4);
                manager.getSubtaskById(5);
                manager.getSubtaskById(5);

                manager.getTaskById(1);
                manager.getTaskById(1);

                manager.deleteSubtaskByID(subtask22.getId()); //удаляем сабтаск
                //System.out.println(manager.getSubtasks()); //проверяем удаление
                System.out.println("****************************");
                System.out.println(manager.getSubtasks());


                System.out.println("****************************");
                System.out.println(manager.getSubtasks());


                System.out.println("**************История задач**************");
                List<Task> history = manager.getHistory();
                System.out.println(history);
*/


        }
}