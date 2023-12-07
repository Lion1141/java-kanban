package util;

import api.DurationAdapter;
import api.HttpTaskManager;
import api.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.HistoryManager;
import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;
import controllers.Manager;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Managers {
    public static Manager getDefault() {
        return new InMemoryTaskManager();
    }

    public static Manager getDefault(String host, int port) {
        return new HttpTaskManager(host, port);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGsonWithLocalDateTimeAdapter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());

        return gsonBuilder.create();
    }
}
