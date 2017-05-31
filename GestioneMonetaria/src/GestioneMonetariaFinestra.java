/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.chart.*;

import javafx.scene.control.*;
import javafx.collections.*;
import java.util.List;

/**
 *
 * @author Antonio Le Caldare
 */
public class GestioneMonetariaFinestra extends Application {
    public DatePicker datePicker = new DatePicker();
    
    
    RadioButton button1 = new RadioButton("select first");
    RadioButton button2 = new RadioButton("select second");
    ToggleGroup tg = new ToggleGroup();
    
    LineChart lc;
    
    ComboBox comboBox;
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                LocalDate asd = datePicker.getValue();
                System.out.println(asd.toString());
                System.out.println("Hello World!");
            }
        });
        
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Selezionato button1");
            }
        });
        
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Selezionato button2");
            }
        });
        
        //------------------------------------------
        datePicker.setLayoutX(11);
        datePicker.setLayoutY(40);
        //------------------------------------------
        button1.setLayoutX(0);
        button1.setLayoutY(100);
        button2.setLayoutX(70);
        button2.setLayoutY(130);
        button1.setToggleGroup(tg);
        button2.setToggleGroup(tg);
        button1.setSelected(true);
        //------------------------------------------
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il file XML");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("File di GuadagniSpese XML", "*.xml"));
        //File selectedFile = fileChooser.showOpenDialog(primaryStage);
        //System.out.println(selectedFile.getAbsolutePath());
        //------------------------------------------
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        lc = new LineChart(xAxis, yAxis);
        
        XYChart.Series series = new XYChart.Series();
        series.setName("Riepilogo Guadagni/Spese");
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        lc.getData().add(series);
        
        //------------------------------------------
        ArrayList xmlcateg = new ArrayList();
        xmlcateg.add("tasse");
        xmlcateg.add("stipendio");
        xmlcateg.add("svago");
        // questo metodo serve per convertire una ArrayList in una ObservableList
        ObservableList<String> categList = FXCollections.observableArrayList(xmlcateg);
        comboBox = new ComboBox(categList);
        // selezione del primo elemento della Observable List
        comboBox.getSelectionModel().select(0);
        // per ottenere la voce selezionata
        System.out.println(comboBox.getSelectionModel().getSelectedItem());
        
        // handler per click su una voce
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Selezionato " + comboBox.getSelectionModel().getSelectedItem());
            }
        });

        //------------------------------------------
        Group grp = new Group(btn, datePicker, button1, button2, comboBox);
        
        /*StackPane root = new StackPane();
        root.getChildren().add(btn);
        root.getChildren().add(datePicker);*/
        
        
        
        Scene scene = new Scene(grp, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
