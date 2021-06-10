package io.mrs.web.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Reader;

public class JsonFileReader {
  private static Class<?> thisClass =
      new Object() { }.getClass().getEnclosingClass();

  public static JsonObject load(String filename) throws Exception {
    JsonObject jsonObject = null;
    URL resource = thisClass.getClassLoader().getResource(filename);
    
    if(null == resource) {
      throw new IOException(String.format("couldn't find file [%s] under resources", filename));
    }
    try {     
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      jsonObject =  JsonParser.parseReader(reader).getAsJsonObject();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return jsonObject;
  }

}
