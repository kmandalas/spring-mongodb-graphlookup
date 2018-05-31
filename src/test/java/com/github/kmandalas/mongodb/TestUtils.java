package com.github.kmandalas.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TestUtils {

  public static String prettyPrint(String jsonString) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Object jsonObject = mapper.readValue(jsonString, Object.class);

    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
  }

}
