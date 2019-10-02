package org.bmsource.dwh.api.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XslxParser {

  private final OPCPackage xlsxPackage;

  private final SheetContentsHandler sheetHandler;

  public XslxParser(OPCPackage pkg, SheetContentsHandler sheetHandler) {
    this.xlsxPackage = pkg;
    this.sheetHandler = sheetHandler;
  }

  /**
   * Parses and shows the content of one sheet
   * using the specified styles and shared-strings tables.
   *
   * @param styles The table of styles that may be referenced by cells in the sheet
   * @param strings The table of strings that may be referenced by cells in the sheet
   * @param sheetInputStream The stream to read the sheet-data from.

   * @exception java.io.IOException An IO exception from the parser,
   *            possibly from a byte stream or character stream
   *            supplied by the application.
   * @throws SAXException if parsing the XML data fails.
   */
  public void processSheet(
      Styles styles,
      SharedStrings strings,
      SheetContentsHandler sheetHandler,
      InputStream sheetInputStream) throws IOException, SAXException {
    DataFormatter formatter = new DataFormatter();
    InputSource sheetSource = new InputSource(sheetInputStream);
    try {
      XMLReader sheetParser = SAXHelper.newXMLReader();
      ContentHandler handler = new XSSFSheetXMLHandler(
          styles, null, strings, sheetHandler, formatter, false);
      sheetParser.setContentHandler(handler);
      sheetParser.parse(sheetSource);
    } catch(ParserConfigurationException e) {
      throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
    }
  }

  /**
   * Initiates the processing of the XLS workbook file to CSV.
   *
   * @throws IOException If reading the data from the package fails.
   * @throws SAXException if parsing the XML data fails.
   */
  public void process() throws IOException, OpenXML4JException, SAXException {
    ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
    XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
    StylesTable styles = xssfReader.getStylesTable();
    XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
    int index = 0;
    while (iter.hasNext()) {
      try (InputStream stream = iter.next()) {
        String sheetName = iter.getSheetName();
        processSheet(styles, strings, this.sheetHandler, stream);
      }
      ++index;
    }
  }

  public static ColumnMapping getColumnHeaders(InputStream inputStream) throws Exception {
    int minColumns = -1;
    try (OPCPackage p = OPCPackage.open(inputStream)) {
      ColumnHeaderHandler sheetHandler = new ColumnHeaderHandler(minColumns);
      XslxParser parser = new XslxParser(p, sheetHandler);
      parser.process();
      return sheetHandler.getColumnMapping();
    }
  }
}