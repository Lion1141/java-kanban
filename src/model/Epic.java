package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static model.TaskType.EPIC;

public class Epic extends Task{
    private ArrayList<Subtask> subtaskslist;
    private LocalDateTime endTime;        //Окончание последней задачи

    public Epic(String name, String description) {
        super(name, description);
        subtaskslist = new ArrayList<>();
        super.setTaskType(EPIC);

    }
    public Epic(String name, String description, int id) {
        super(name, description, id);
        subtaskslist = new ArrayList<>();
        super.setTaskType(EPIC);
    }

    public Epic(String name, String description, int id, TaskStatus status, LocalDateTime startTime, Duration duration, LocalDateTime endTime) {
        super(name, description, id, status, startTime, duration);
        this.endTime = endTime;
    }


    //Добавление новой подзадачи к эпику
    public void addSubtask(Subtask subtask){
        subtaskslist.add(subtask);
        refreshDates();
    }

    //Обновление дат начала/окончания и продолжительности эпика
    public void refreshDates(){
        Duration sumDuration = null;
        LocalDateTime firstDate = null;
        LocalDateTime lastDate = null;

        if (subtaskslist != null){
            for (Subtask subtask : subtaskslist){
                if (subtask.getDuration()!=null && subtask.getStartTime()!=null){
                    if (firstDate == null || firstDate.isAfter(subtask.getStartTime()))
                        firstDate = subtask.getStartTime();
                    if(lastDate == null || lastDate.isBefore(subtask.getEndTime()))
                        lastDate = subtask.getEndTime();
                    if (sumDuration == null)
                        sumDuration = subtask.getDuration();
                    else
                        sumDuration = sumDuration.plus(subtask.getDuration());
                }
            }
        }
        duration = sumDuration;
        startTime = firstDate;
        endTime = lastDate;
    }

    public ArrayList<Subtask> getSubtaskslist() {
            return subtaskslist;
    }

    public void removeIdSubtasks() {
        subtaskslist.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", subtasks=" + subtaskslist +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
