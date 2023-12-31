package api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
    private final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDateTime.format(formatterWriter));
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
}
