package com.jalon.jackson11;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author: syj
 * @date: 2022/2/21
 */
public class CustomDateDeserializer
        extends StdDeserializer<Date> {

    private static SimpleDateFormat formatter
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CustomDateDeserializer() {
        this(null);
    }

    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(
            JsonParser jsonparser, DeserializationContext context)
            throws IOException {

        String date = jsonparser.getText();
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
