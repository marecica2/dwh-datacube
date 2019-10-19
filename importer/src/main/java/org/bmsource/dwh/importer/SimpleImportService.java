//package org.bmsource.dwh.importer;
//
//import org.bmsource.dwh.common.appstate.AppStateService;
//import org.bmsource.dwh.common.fileManager.FileManager;
//import org.bmsource.dwh.common.fileManager.FileSystemImpl;
//import org.bmsource.dwh.common.reader.DataHandler;
//import org.bmsource.dwh.common.reader.DataReader;
//import org.bmsource.dwh.common.reader.ExcelReader;
//import org.bmsource.dwh.common.reader.FactModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class SimpleImportService {
//
//    private FileManager fileManager = new FileSystemImpl();
//
//    @Autowired
//    AppStateService appStateService;
//
//    public boolean checkRunningImport(String tenant, String project) {
//        Map<String, Object> state = appStateService.getState(tenant, project,
//            ImporterConfiguration.StateType.IMPORT_STATUS_STATE.getValue());
//        return state.get("running") != null && (Boolean) state.get("running");
//    }
//
//    @Async("asyncExecutor")
//    public void startImport(String transactionId, Map<String, String> mapping, String tenant, String project) throws Exception {
//        List<String> files = fileManager.getFiles(transactionId);
//        final FactModelMapper<Fact>[] modelMapper = new FactModelMapper[]{null};
//        for (String file : files) {
//            try (InputStream stream = fileManager.getStream(transactionId, file);) {
//                DataReader reader = new ExcelReader();
//                reader.readContent(stream, new DataHandler() {
//                    @Override
//                    public void onStart() {
//
//                    }
//
//                    @Override
//                    public void onRead(List<List<Object>> rows, List<String> header, int rowsCount,
//                                       int totalRowsCount) {
//                        long before = System.currentTimeMillis();
//                        if (modelMapper[0] == null) {
//                            modelMapper[0] = new FactModelMapper<>(Fact.class, header, mapping);
//                        }
//                        List<Fact> facts = modelMapper[0].mapList(rows);
//                        System.out.println("Parsed from file " + file + " " + rowsCount + " of total " + totalRowsCount);
//                        Map<String, Object> state = new HashMap<>();
//                        state.put("running", true);
//                        state.put("file", files.indexOf(file) + 1);
//                        state.put("files", files.size());
//                        state.put("fileName", file);
//                        state.put("rowsCount", rowsCount);
//                        state.put("totalRowsCount", totalRowsCount);
//
//                        appStateService.updateState(tenant, project,
//                            ImporterConfiguration.StateType.IMPORT_STATUS_STATE.getValue(), state);
//                        System.out.println((System.currentTimeMillis() - before) + " ms");
//                    }
//
//                    @Override
//                    public void onFinish(int totalRowsCount) {
//                        Map<String, Object> state = new HashMap<>();
//                        state.put("running", false);
//                        appStateService.updateState(tenant, project,
//                            ImporterConfiguration.StateType.IMPORT_STATUS_STATE.getValue(), state);
//                    }
//                });
//            }
//        }
//    }
//}
