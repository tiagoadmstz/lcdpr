package br.com.dsc.lcdpr.deserializers;

import br.com.dsc.lcdpr.util.BigDecimalUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return BigDecimalUtil.stringToBigDecimal(p.getValueAsString(), 2);
    }

}
