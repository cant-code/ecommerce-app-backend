package com.mini.ecommerceapp.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.mini.ecommerceapp.models.Order;

import java.io.IOException;

public class OrderSerializer extends StdSerializer<Order> {

    public OrderSerializer() {
        this(null);
    }

    public OrderSerializer(Class<Order> t) {
        super(t);
    }

    @Override
    public void serialize(Order order, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", order.getId());
        jsonGenerator.writeStringField("userId", order.getUserId());
        jsonGenerator.writeStringField("start", order.getStart().toString());
        jsonGenerator.writeStringField("expiry", order.getExpiry().toString());
        jsonGenerator.writeStringField("dateCreated", order.getDateCreated().toString());
        jsonGenerator.writeStringField("End Time", order.getEndTime() != null ? order.getEndTime().toString() : null);
        jsonGenerator.writeStringField("Status", order.getStatus().toString());
        jsonGenerator.writeNumberField("totalCost", order.getTotalCost());
        jsonGenerator.writeNumberField("extraCharge", order.getExtraCharges());
        jsonGenerator.writeNumberField("finalCharge", order.getFinalCharge());
        jsonGenerator.writeObjectField("vehicularSpace", order.getItems());
        jsonGenerator.writeEndObject();
    }
}
