package org.bmsource.dwh.api.parsers;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

public class XslxParserTest {

  @Test
  public void testHeadrParser() throws Exception {
    File xlsx = ResourceUtils.getFile(this.getClass().getResource("/spends.xlsx"));
    ColumnMapping mapping =  XslxParser.getColumnHeaders(FileUtils.openInputStream(xlsx));
    System.out.println("Header " + mapping.getHeaderColumn());
    System.out.println("Preview " + mapping.getPreviewColumn());
  }
}
