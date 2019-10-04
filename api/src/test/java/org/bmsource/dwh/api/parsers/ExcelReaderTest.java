package org.bmsource.dwh.api.parsers;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import static org.assertj.core.api.Assertions.*;

public class ExcelReaderTest {

  @Test
  public void testHeaderRowParser() throws Exception {
    File xlsx = ResourceUtils.getFile(this.getClass().getResource("/spends.xlsx"));
    MappingResult mapping =  new ExcelReader().readHeaderRow(FileUtils.openInputStream(xlsx));
    assertThat(mapping.getHeaderRow().size()).isEqualTo(38);
    assertThat(mapping.getPreviewRow().size()).isEqualTo(38);
    assertThat(mapping.getHeaderRow().get(4)).isEqualTo("Origin-State");
    assertThat(mapping.getPreviewRow().get(4)).isEqualTo("Illinois");
  }

  @Test
  public void testPreviewParser() throws Exception {
    File xlsx = ResourceUtils.getFile(this.getClass().getResource("/spends.xlsx"));
    List<List<Object>> rows = new ExcelReader().readPreview(FileUtils.openInputStream(xlsx), 20);
    assertThat(rows.size()).isEqualTo(20);
  }
}
