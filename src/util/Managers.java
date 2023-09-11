package util;

import controllers.HistoryManager;
import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;
import controllers.Manager;

public final class Managers {
    public static Manager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
