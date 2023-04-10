package ru.yandex.practicum.filmorate.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;

public class FilmDeserializer extends StdDeserializer<Film> {
    //Неудачная попытка создать десериализатор
    //R.I.P.
    //Если ты читаешь это сообщение, что он создавался не зря,
    //Этот класс был славным парнем, любил парсить, бегал и прыгал
    //К несчастью, его не стало...Прошу 10 секунд отсутствия кликанья по клавишам и мышке
    public FilmDeserializer() {
        this(null);
    }

    public FilmDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Film deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Integer id = (Integer) ((IntNode) node.get("id")).numberValue();
        String name = node.get("name").asText();
        String description = node.get("description").asText();
        Integer duration = (Integer) ((IntNode) node.get("duration")).numberValue();
        //LocalDate releaseDate = LocalDate.of(((Date) node.get("releaseDate").as)

        return null;

    }
}
