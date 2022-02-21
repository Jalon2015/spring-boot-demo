package com.jalon.jackson10;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * <p>
 *
 * </p>
 *
 * @author: syj
 * @date: 2022/2/21
 */
public class CustomDateSerializer extends StdSerializer<Date> {

    private static SimpleDateFormat formatter
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CustomDateSerializer() {
        this(null);
    }

    public CustomDateSerializer(Class<Date> t) {
        super(t);
    }

    @Override
    public void serialize(
            Date value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        String format = formatter.format(value);
        gen.writeString(format);
    }
}
