package com.byls.boat.constant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

class BoatTypeSerializer extends JsonSerializer<BoatType> {
    @Override
    public void serialize(BoatType boatType, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", boatType.getType());
        gen.writeNumberField("dbIndex", boatType.getDbIndex());
        gen.writeStringField("boatTypeName", boatType.getBoatTypeName());
        gen.writeEndObject();
    }
}