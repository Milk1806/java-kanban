package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(String.valueOf(duration.getSeconds()));
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        long seconds = Long.parseLong(jsonReader.nextString());
        return Duration.ofSeconds(seconds);
    }
}
