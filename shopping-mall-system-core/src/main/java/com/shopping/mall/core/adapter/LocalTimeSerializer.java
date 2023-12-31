package com.shopping.mall.core.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * <pre>
 * Description : Gson LocalTime Adapter
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
public class LocalTimeSerializer implements JsonSerializer<LocalTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public JsonElement serialize(LocalTime localTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(localTime));
    }
}
