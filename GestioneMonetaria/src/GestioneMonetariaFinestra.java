/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

import javafx.scene.Group;

import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author Antonio Le Caldare
 */
public class GestioneMonetariaFinestra extends Application {
    private Stage mainStage;
    
    public DatePicker pickerDataInserimento;
    public TextField tboxDescrizione;
    public TextField tboxImporto;
    public ComboBox comboCategoriaInserimento;
    public RadioButton radiobtnAccredito;
    public RadioButton radiobtnAddebito;
    public Button btnImporta;
    
    public TextField tboxFilePicker;
    public Button btnCarica;
    public Button btnInserisci;
    
    public DatePicker pickerDataInizioFiltro;
    public DatePicker pickerDataFineFiltro;
    public ComboBox comboCategoriaFiltro;
    public TextField tboxDescrParziale;
    public Button btnCerca;
    
    public Label lblSaldoTotale;
    
    public ComboBox comboPeriodoGrafico;
    
    private TabellaVisualeGuadagniSpese tabEntrate;
    private GraficoStatisticheMonetarie grafico;
    
    private ConfigurazioneXML conf;
    private OperazioniDatabaseGuadagniSpese db;
    
    @Override
    public void start(Stage primaryStage) {
        //------------------------------------------
        conf = ConfigurazioneXML.caricaDaFile();

        //------------------------------------------
        
        pickerDataInserimento = new DatePicker();
        pickerDataInserimento.setLayoutX(10);
        pickerDataInserimento.setLayoutY(10);
        
        tboxDescrizione = new TextField();
        tboxDescrizione.setLayoutX(10);
        tboxDescrizione.setLayoutY(40);
        
        tboxImporto = new TextField();
        tboxImporto.setLayoutX(10);
        tboxImporto.setLayoutY(70);
        
        comboCategoriaInserimento =
                new ComboBox(FXCollections.observableArrayList(conf.listaCategorie));
        comboCategoriaInserimento.getSelectionModel().select(0);
        comboCategoriaInserimento.setLayoutX(10);
        comboCategoriaInserimento.setLayoutY(100);
        
        // il toggle group mi serve per associare i due RadioButton
        ToggleGroup tg = new ToggleGroup();
        radiobtnAccredito = new RadioButton();
        radiobtnAccredito.setLayoutX(10);
        radiobtnAccredito.setLayoutY(130);
        radiobtnAccredito.setText("Accredito");
        radiobtnAddebito = new RadioButton();
        radiobtnAddebito.setLayoutX(90);
        radiobtnAddebito.setLayoutY(130);
        radiobtnAddebito.setText("Addebito");
        radiobtnAccredito.setToggleGroup(tg);
        radiobtnAddebito.setToggleGroup(tg);
        radiobtnAccredito.setSelected(true);
        
        btnInserisci = new Button();
        btnInserisci.setLayoutX(10);
        btnInserisci.setLayoutY(150);
        btnInserisci.setText("Inserisci");
        
        tboxFilePicker = new TextField();
        tboxFilePicker.setLayoutX(10);
        tboxFilePicker.setLayoutY(190);
        
        btnCarica = new Button();
        btnCarica.setLayoutX(100);
        btnCarica.setLayoutY(200);
        btnCarica.setText("Carica");

        btnImporta = new Button();
        btnImporta.setLayoutX(40);
        btnImporta.setLayoutY(230);
        btnImporta.setText("Importa");

        pickerDataInizioFiltro = new DatePicker();
        pickerDataInizioFiltro.setLayoutX(200);
        pickerDataInizioFiltro.setLayoutY(10);
        
        pickerDataFineFiltro = new DatePicker();
        pickerDataFineFiltro.setLayoutX(200);
        pickerDataFineFiltro.setLayoutY(40);
        
        comboCategoriaFiltro =
                new ComboBox(FXCollections.observableArrayList(conf.listaCategorie));
        comboCategoriaFiltro.getSelectionModel().select(0);
        comboCategoriaFiltro.setLayoutX(200);
        comboCategoriaFiltro.setLayoutY(70);
        
        tboxDescrParziale = new TextField();
        tboxDescrParziale.setLayoutX(200);
        tboxDescrParziale.setLayoutY(100);
        
        btnCerca = new Button();
        btnCerca.setLayoutX(200);
        btnCerca.setLayoutY(130);
        btnCerca.setText("Cerca");

        lblSaldoTotale = new Label("Saldo Totale");
        lblSaldoTotale.setLayoutX(200);
        lblSaldoTotale.setLayoutY(160);

        comboPeriodoGrafico = new ComboBox();
        comboPeriodoGrafico.setLayoutX(200-10);
        comboPeriodoGrafico.setLayoutY(400);
        
        tabEntrate = new TabellaVisualeGuadagniSpese();
        tabEntrate.setLayoutX(200);
        tabEntrate.setLayoutY(200);
        
        grafico = new GraficoStatisticheMonetarie("Statistiche Monetarie");
        grafico.setLayoutX(10);
        grafico.setLayoutY(250);
        
        Group grp = new Group(pickerDataInserimento, tboxDescrizione, tboxImporto,
            comboCategoriaInserimento, radiobtnAccredito, radiobtnAddebito, btnInserisci, tboxFilePicker,
            btnCarica, btnImporta, pickerDataInizioFiltro, pickerDataFineFiltro,
            comboCategoriaFiltro, tboxDescrParziale, btnCerca, lblSaldoTotale,
            comboPeriodoGrafico, tabEntrate, grafico);
        
        Scene scene = new Scene(grp, 600, 600);
        mainStage = primaryStage;
        primaryStage.setTitle("Gestione Monetaria");
        primaryStage.setScene(scene);
        
        impostaHandler();
        CacheGestioneMonetaria.caricaDaFile(this);
        
        db = new OperazioniDatabaseGuadagniSpese("jdbc:mysql://localhost:3306/gestionemonetaria", "root","");
        aggiornaStatoFinanziario();

        primaryStage.show();
    }
    
    private void impostaHandler() {
        GestioneMonetariaFinestra istanzaglobale = this;
        mainStage.setOnCloseRequest((WindowEvent we) ->
            { CacheGestioneMonetaria.salvaSuFile(istanzaglobale); });

        btnInserisci.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int importo = Integer.parseInt(tboxImporto.getText());
                if(radiobtnAddebito.isSelected())
                    importo = importo * -1;
                GuadagnoSpesa gs = new GuadagnoSpesa(
                        pickerDataInserimento.getValue(),
                        tboxDescrizione.getText(),
                        comboCategoriaInserimento.getSelectionModel().getSelectedItem().toString(),
                        importo);
                db.aggiungiGuadagnoSpesa(gs);
                aggiornaStatoFinanziario();
            }
        });
        
        btnCarica.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String openedfile = apriDialogSelezioneFile(mainStage);
                if(openedfile == null)
                    return;
                tboxFilePicker.setText(openedfile);
            }
        });

        btnImporta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String fileToImport = tboxFilePicker.getText();
                if(fileToImport == null || fileToImport  == "")
                    return;

                FileGuadagniSpeseXML filelista = new FileGuadagniSpeseXML(fileToImport);
                db.aggiungiListaGuadagnoSpesa(filelista.caricaEntrate());
                aggiornaStatoFinanziario();
            }
        });
        
        btnCerca.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tabEntrate.aggiornaListaGuadagniSpese(
                        db.ottieniGuadagniSpeseConFiltro(
                                pickerDataInizioFiltro.getValue().toString(),
                                pickerDataFineFiltro.getValue().toString(),
                                comboCategoriaFiltro.getSelectionModel().getSelectedItem().toString(),
                                tboxDescrParziale.getText()
                        )
                );
            }
        });
        
        comboCategoriaFiltro.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
    }
    
    private String apriDialogSelezioneFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il file XML");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("File di GuadagniSpese XML", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile.getAbsolutePath();
    }
    
    private void aggiornaStatoFinanziario()
    {
        tabEntrate.aggiornaListaGuadagniSpese(db.ottieniGuadagniSpese());
        grafico.popolaGrafico(db.ottieniGuadagniSpese());
        
        int saldo = db.ottieniSaldo();
        lblSaldoTotale.setText("Saldo Totale: " + saldo);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
