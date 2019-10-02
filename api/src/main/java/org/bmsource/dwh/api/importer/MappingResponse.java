package org.bmsource.dwh.api.importer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class MappingResponse {

  @JsonSerialize
  @JsonProperty("sourceColumns")
  Map<String, Object> inputFields = new LinkedHashMap<>();

  @JsonSerialize
  @JsonProperty("destinationColumns")
  Map<String, Map<String, Object>> factFields = new LinkedHashMap<>();

  public void setDestinationFields(Class factModel) {
    for (Field field : factModel.getDeclaredFields()) {
      Map<String, Object> fieldMetadata = new LinkedHashMap<>();
      fieldMetadata.put("type", field.getType().getSimpleName());
      NotNull annotation = field.getAnnotation(NotNull.class);
      if(annotation != null) {
        fieldMetadata.put("required",  true);
      }
      factFields.put(field.getName(), fieldMetadata);
    }
  }

  public void setInputFields(List<Object> fields, List<Object> values) {
    for(int i = 0; i < fields.size(); i++){
      inputFields.put(fields.get(i).toString(), values.get(i));
    };
  }

  public Map<String, Object> getInputFields() {
    return inputFields;
  }

  public Map<String, Map<String, Object>> getFactFields() {
    return factFields;
  }
}
