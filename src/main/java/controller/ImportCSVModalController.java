package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import service.ImportCSVService;
import java.io.File;

public class ImportCSVModalController {

    private String tableName;
    private ImportCSVService importCSVService;

    @FXML
    private Button uploadButton;


    @FXML
    void initialize(){
        importCSVService = new ImportCSVService();
    }

    @FXML
    private void handleUploadButtonAction(){
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        importCSVService.loadCSV(file, tableName);
        //((Node)event.getSource()).getScene().
    }

    private void configureFileChooser(FileChooser fileChooser){
        fileChooser.setTitle("Import CSV file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV", "*.csv"));
    }

    void setTableName(String tableName){
        this.tableName = tableName;
    }
}
