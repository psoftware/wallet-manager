/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;

/**
 *
 * @author Antonio Le Caldare
 */
public class GestioneMonetariaFinestra extends Application {
    private Stage mainStage;
    
    public Label lblTitoloInserimentoVoce;
    public DatePicker pickerDataInserimento;
    public TextField tboxDescrizione;
    public TextField tboxImporto;
    public ComboBox comboCategoriaInserimento;
    public RadioButton radiobtnAccredito;
    public RadioButton radiobtnAddebito;
    public Button btnImporta;
    
    public Label lblTitoloImportaVoci;
    public TextField tboxFilePicker;
    public Button btnCarica;
    public Button btnInserisci;
    
    public Label lblTitoloFiltro;
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
    private InvioDatiLog log;
    
    @Override
    public void start(Stage primaryStage) {
        conf = ConfigurazioneXML.caricaDaFile();
        log = new InvioDatiLog("127.0.0.1", 8080, conf.indirizzoIPUtente);
        db = new OperazioniDatabaseGuadagniSpese("jdbc:mysql://localhost:3306/gestionemonetaria", "root","");

        impostaLayoutInserimentoVoce();
        impostaLayoutImportaVoci();
        impostaLayoutRicerca();
        impostaLayoutGraficoStatistiche();
        
        Group grp = new Group(lblTitoloInserimentoVoce, pickerDataInserimento, tboxDescrizione, tboxImporto,
            comboCategoriaInserimento, radiobtnAccredito, radiobtnAddebito, btnInserisci, lblTitoloImportaVoci,
            tboxFilePicker, btnCarica, btnImporta, lblTitoloFiltro, pickerDataInizioFiltro, pickerDataFineFiltro,
            comboCategoriaFiltro, tboxDescrParziale, btnCerca, lblSaldoTotale,
            comboPeriodoGrafico, tabEntrate, grafico);
        
        Scene scene = new Scene(grp, 660, 600);
        scene.setFill(Color.ALICEBLUE);
        mainStage = primaryStage;
        primaryStage.setTitle("Gestione Monetaria");
        primaryStage.setScene(scene);

        impostaHandler();
        CacheGestioneMonetaria.caricaDaFile(this);
        aggiornaStatoFinanziario();

        log.invia("Avvio", "Applicazione");
        primaryStage.show();
    }
    
    private void impostaLayoutInserimentoVoce() {
        lblTitoloInserimentoVoce = new Label();
        lblTitoloInserimentoVoce.setLayoutX(10); lblTitoloInserimentoVoce.setLayoutY(5);
        lblTitoloInserimentoVoce.setFont(new Font(20));
        lblTitoloInserimentoVoce.setText("Inserimento Voce");
        lblTitoloInserimentoVoce.setTextFill(Color.ROYALBLUE);
        
        pickerDataInserimento = new DatePicker();
        pickerDataInserimento.setLayoutX(10); pickerDataInserimento.setLayoutY(40);
        pickerDataInserimento.setPrefWidth(110);
        pickerDataInserimento.promptTextProperty().setValue("Data Voce");
        pickerDataInserimento.setPrefWidth(200);
        
        tboxDescrizione = new TextField();
        tboxDescrizione.setLayoutX(10); tboxDescrizione.setLayoutY(70);
        tboxDescrizione.promptTextProperty().setValue("Descrizione");
        tboxDescrizione.setPrefWidth(200);
        
        tboxImporto = new TextField();
        tboxImporto.setLayoutX(10); tboxImporto.setLayoutY(100);
        tboxImporto.promptTextProperty().setValue("Importo");
        tboxImporto.setPrefWidth(200);
        
        comboCategoriaInserimento =
                new ComboBox(FXCollections.observableArrayList(conf.listaCategorie));
        comboCategoriaInserimento.getSelectionModel().select(0);
        comboCategoriaInserimento.setLayoutX(10); comboCategoriaInserimento.setLayoutY(130);
        comboCategoriaInserimento.setPrefWidth(200);
        
        // il toggle group mi serve per associare i due RadioButton
        ToggleGroup tg = new ToggleGroup();
        radiobtnAccredito = new RadioButton();
        radiobtnAccredito.setLayoutX(40); radiobtnAccredito.setLayoutY(170);
        radiobtnAccredito.setText("Accredito");
        
        radiobtnAddebito = new RadioButton();
        radiobtnAddebito.setLayoutX(125); radiobtnAddebito.setLayoutY(170);
        radiobtnAddebito.setText("Addebito");
        
        radiobtnAccredito.setToggleGroup(tg);
        radiobtnAddebito.setToggleGroup(tg);
        radiobtnAccredito.setSelected(true);
        
        btnInserisci = new Button();
        btnInserisci.setLayoutX(85); btnInserisci.setLayoutY(200);
        btnInserisci.setText("Inserisci");
    }
    
    private void impostaLayoutImportaVoci() {
        lblTitoloImportaVoci = new Label();
        lblTitoloImportaVoci.setLayoutX(10); lblTitoloImportaVoci.setLayoutY(250);
        lblTitoloImportaVoci.setFont(new Font(20));
        lblTitoloImportaVoci.setText("Importa voci (File XML)");
        lblTitoloImportaVoci.setTextFill(Color.ROYALBLUE);
        
        tboxFilePicker = new TextField();
        tboxFilePicker.setLayoutX(15); tboxFilePicker.setLayoutY(290);
        tboxFilePicker.promptTextProperty().setValue("Percorso file XML");

        btnCarica = new Button();
        btnCarica.setLayoutX(170); btnCarica.setLayoutY(290);
        btnCarica.setText("Carica");

        btnImporta = new Button();
        btnImporta.setLayoutX(85); btnImporta.setLayoutY(320);
        btnImporta.setText("Importa");
    }
    
    private void impostaLayoutRicerca() {
        lblTitoloFiltro = new Label();
        lblTitoloFiltro.setLayoutX(300); lblTitoloFiltro.setLayoutY(5);
        lblTitoloFiltro.setFont(new Font(20));
        lblTitoloFiltro.setText("Visualizza Entrate");
        lblTitoloFiltro.setTextFill(Color.ROYALBLUE);
        
        pickerDataInizioFiltro = new DatePicker();
        pickerDataInizioFiltro.setLayoutX(300); pickerDataInizioFiltro.setLayoutY(40);
        pickerDataInizioFiltro.setPrefWidth(110);
        pickerDataInizioFiltro.promptTextProperty().setValue("Data Inizio");
        
        pickerDataFineFiltro = new DatePicker();
        pickerDataFineFiltro.setLayoutX(420); pickerDataFineFiltro.setLayoutY(40);
        pickerDataFineFiltro.setPrefWidth(110);
        pickerDataFineFiltro.promptTextProperty().setValue("Data Fine");
        
        comboCategoriaFiltro =
                new ComboBox(FXCollections.observableArrayList(conf.listaCategorie));
        comboCategoriaFiltro.getSelectionModel().select(0);
        comboCategoriaFiltro.setLayoutX(540); comboCategoriaFiltro.setLayoutY(40);
        comboCategoriaFiltro.setPrefWidth(110);
        
        tboxDescrParziale = new TextField();
        tboxDescrParziale.setLayoutX(300); tboxDescrParziale.setLayoutY(70);
        tboxDescrParziale.setPrefWidth(290);
        tboxDescrParziale.promptTextProperty().setValue("Descrizione parziale");
        
        btnCerca = new Button();
        btnCerca.setLayoutX(600); btnCerca.setLayoutY(70);
        btnCerca.setPrefWidth(50);
        btnCerca.setText("Cerca");
        
        tabEntrate = new TabellaVisualeGuadagniSpese();
        tabEntrate.setLayoutX(300); tabEntrate.setLayoutY(105);
        tabEntrate.setPrefSize(350, 200);
        
        lblSaldoTotale = new Label("Saldo Totale: ?");
        lblSaldoTotale.setLayoutX(380); lblSaldoTotale.setLayoutY(315);
        lblSaldoTotale.setFont(new Font(20));
    }
    
    private void impostaLayoutGraficoStatistiche() {
        ArrayList listavisuali = new ArrayList();
        listavisuali.add("Mensile");
        listavisuali.add("Semestrale");
        listavisuali.add("Annuale");
        
        int selectedIndex = 0;
        if(conf.vistaDefaultGrafico.equals("Mensile"))          //(01)
            selectedIndex = 0;
        else if(conf.vistaDefaultGrafico.equals("Semestrale"))  //(01)
            selectedIndex = 1;
        else if(conf.vistaDefaultGrafico.equals("Annuale"))     //(01)
            selectedIndex = 2;

        comboPeriodoGrafico = new ComboBox(
                FXCollections.observableArrayList(listavisuali));
        comboPeriodoGrafico.setLayoutX(270); comboPeriodoGrafico.setLayoutY(370);
        comboPeriodoGrafico.getSelectionModel().select(selectedIndex);
        
        grafico = new GraficoStatisticheMonetarie("Statistiche Monetarie");
        grafico.setLayoutX(10); grafico.setLayoutY(400);
        grafico.setPrefWidth(610); grafico.setPrefHeight(200);
    }
    
    private void impostaHandler() {
        GestioneMonetariaFinestra istanzaglobale = this;
        mainStage.setOnCloseRequest((WindowEvent we) ->
            { CacheGestioneMonetaria.salvaSuFile(istanzaglobale); });

        btnInserisci.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                log.invia("Click", "btnInserisci");
                int importo = Integer.parseInt(tboxImporto.getText());
                if(radiobtnAddebito.isSelected())
                    importo = importo * -1;
                GuadagnoSpesa gs = new GuadagnoSpesa(
                        pickerDataInserimento.getValue(),
                        comboCategoriaInserimento.getSelectionModel().getSelectedItem().toString(),
                        tboxDescrizione.getText(),
                        importo);
                db.aggiungiGuadagnoSpesa(gs);
                aggiornaStatoFinanziario();
            }
        });
        
        btnCarica.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                log.invia("Click", "btnCarica");
                String openedfile = apriDialogSelezioneFile(mainStage);
                if(openedfile == null)
                    return;
                tboxFilePicker.setText(openedfile);
            }
        });

        btnImporta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                log.invia("Click", "btnImporta");
                String fileToImport = tboxFilePicker.getText();
                if(fileToImport == null || fileToImport.equals(""))
                    return;

                FileGuadagniSpeseXML filelista = new FileGuadagniSpeseXML(fileToImport);
                db.aggiungiListaGuadagnoSpesa(filelista.caricaEntrate());
                aggiornaStatoFinanziario();
            }
        });
        
        btnCerca.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                log.invia("Click", "btnCerca");
                tabEntrate.aggiornaListaGuadagniSpese(
                        db.ottieniGuadagniSpeseConFiltro(
                                pickerDataInizioFiltro.getValue().toString(),
                                pickerDataFineFiltro.getValue().toString(),
                                comboCategoriaFiltro.getSelectionModel().getSelectedItem().toString(),
                                tboxDescrParziale.getText(),
                                conf.maxNumGuadagniSpeseTabella
                        )
                );
            }
        });
        
        comboPeriodoGrafico.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                log.invia("Click", "comboCategoriaFiltro");
                grafico.popolaGrafico(db.ottieniGuadagniSpeseSettimanali(),
                    comboPeriodoGrafico.getSelectionModel().getSelectedIndex());
            }
        });
    }
    
    private String apriDialogSelezioneFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il file XML");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("File di GuadagniSpese XML", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile == null)
            return null;
        else
            return selectedFile.getAbsolutePath();
    }
    
    private void aggiornaStatoFinanziario()
    {
        tabEntrate.aggiornaListaGuadagniSpese(
                db.ottieniGuadagniSpese(conf.maxNumGuadagniSpeseTabella)
        );

        grafico.popolaGrafico(db.ottieniGuadagniSpeseSettimanali(),
        comboPeriodoGrafico.getSelectionModel().getSelectedIndex());

        int saldo = db.ottieniSaldo();
        lblSaldoTotale.setText("Saldo Totale: " + saldo + " " + conf.tipoMoneta);
        if(saldo <= conf.limiteSpendibili)
            lblSaldoTotale.setTextFill(Color.RED);
        else
            lblSaldoTotale.setTextFill(Color.GREEN);
            
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

// (01) la configurazione mi fornisce la voce di default della combobox in formato
//      testuale, quindi la devo convertire in index per il metodo che aggiorna il
//      grafico