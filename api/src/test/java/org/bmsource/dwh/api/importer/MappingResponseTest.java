package org.bmsource.dwh.api.importer;

import java.util.Map;
import org.bmsource.dwh.api.model.Fact;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class MappingResponseTest {
  @Test
  public void testMappingFact() {
    MappingResponse mappingResponse = new MappingResponse();
    mappingResponse.setDestinationFields(Fact.class);
    Map<String, Object> cost = mappingResponse.getFactFields().get("cost");

    assertThat(cost).isNotNull();
    assertThat(cost.get("required")).isEqualTo(true);
    assertThat(cost.get("type")).isEqualTo("BigDecimal");
  }
}
