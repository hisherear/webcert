package se.inera.webcert.notifications;

import org.joda.time.LocalDateTime;

import se.inera.webcert.persistence.utkast.model.Utkast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;

public class TestUtkastProducer {

    private ObjectMapper objMapper;

    public Utkast buildUtkast(String pathToJsonFile) {
        try {
            String jsonModel = TestDataUtil.readRequestFromFile(pathToJsonFile);
            if (jsonModel == null) {
                return null;
            }
            ObjectMapper objectMapper = getObjectMapper();
            return objectMapper.readValue(jsonModel, Utkast.class);
        } catch (Exception e) {
            throw new RuntimeException("Error occured when reading intyg json: " + e.getMessage());
        }
    }

    private ObjectMapper getObjectMapper() {
        if (this.objMapper == null) {
            this.objMapper = initObjectMapper();
        }
        return this.objMapper;
    }

    private ObjectMapper initObjectMapper() {

        ObjectMapper objMapper = new ObjectMapper();
        objMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        objMapper.registerModule(module);

        return objMapper;
    }
}