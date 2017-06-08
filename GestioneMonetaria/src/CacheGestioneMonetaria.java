import java.io.*;
import java.time.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class CacheGestioneMonetaria implements Serializable {
    private static final String percorsoFile = "./FileCache.bin";
    public LocalDate dataInserimentoVoce;
    public String descrizioneInserimentoVoce;
    public String importoInserimentoVoce;
    public int indexCategoriaInserimentoVoce;
    public Boolean accreditoInserimentoVoce;
    public LocalDate dataInizioFiltro;
    public LocalDate dataFineFiltro;
    public int indexCategoriaFiltro;
    public String descrizioneParzialeFiltro;
    
    private static CacheGestioneMonetaria caricaBin()
    {
        CacheGestioneMonetaria risultato = null;
        try ( FileInputStream fin = new FileInputStream(percorsoFile);
              ObjectInputStream oin = new ObjectInputStream(fin); ) { //01
            risultato = (CacheGestioneMonetaria)oin.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("errore: impossibile caricare la cache da file");
        }
        
        return risultato;
    }
    
    private static void salvaBin(CacheGestioneMonetaria cache)
    {
        try ( FileOutputStream fout = new FileOutputStream(percorsoFile);
              ObjectOutputStream oout = new ObjectOutputStream(fout); ) { //01
            oout.writeObject(cache);
        } catch (IOException ex) {
            System.out.println("errore: impossibile caricare la cache da file");
        }
    }
    
    public static void caricaDaFile(GestioneMonetariaFinestra finestra) {
        CacheGestioneMonetaria cache = caricaBin();
        
        if(cache == null)   //(02)
            return;
        
        // sezione inserimento voce
        finestra.pickerDataInserimento.setValue(cache.dataInserimentoVoce);
        finestra.tboxDescrizione.setText(cache.descrizioneInserimentoVoce);
        finestra.tboxImporto.setText(cache.importoInserimentoVoce);
        finestra.comboCategoriaInserimento.getSelectionModel()
                .select(cache.indexCategoriaInserimentoVoce);
        finestra.radiobtnAccredito.setSelected(cache.accreditoInserimentoVoce); //(01)
        finestra.radiobtnAddebito.setSelected(!cache.accreditoInserimentoVoce);//(01)

        // sezione filtro di ricerca
        finestra.pickerDataInizioFiltro.setValue(cache.dataInizioFiltro);
        finestra.pickerDataFineFiltro.setValue(cache.dataFineFiltro);
        finestra.comboCategoriaFiltro.getSelectionModel()
                .select(cache.indexCategoriaFiltro);
        finestra.tboxDescrParziale.setText(cache.descrizioneParzialeFiltro);
    }
    
    public static void salvaSuFile(GestioneMonetariaFinestra finestra) {
        CacheGestioneMonetaria nuovacache = new CacheGestioneMonetaria();
        
        // sezione inserimento voce
        nuovacache.dataInserimentoVoce = finestra.pickerDataInserimento.getValue();
        nuovacache.descrizioneInserimentoVoce = finestra.tboxDescrizione.getText();
        nuovacache.importoInserimentoVoce = finestra.tboxImporto.getText();
        nuovacache.indexCategoriaInserimentoVoce = 
                finestra.comboCategoriaInserimento.getSelectionModel().getSelectedIndex();
        nuovacache.accreditoInserimentoVoce = finestra.radiobtnAccredito.isSelected(); //(01)

        // sezione filtro di ricerca
        nuovacache.dataInizioFiltro = finestra.pickerDataInizioFiltro.getValue();
        nuovacache.dataFineFiltro = finestra.pickerDataFineFiltro.getValue();
        nuovacache.indexCategoriaFiltro =
                finestra.comboCategoriaFiltro.getSelectionModel().getSelectedIndex();
        nuovacache.descrizioneParzialeFiltro = finestra.tboxDescrParziale.getText();
        
        // salviamo la cache appena creata
        salvaBin(nuovacache);
    }
}

// (01) i due RadioButton fanno parte dello stesso toggle group, per cui è inutile
//      mantenere in cache lo stato di entrambi. Basta sapere se uno dei due è
//      selezionato e lo stato dell'altro button è deducibile dal primo.
//      Quindi, in questo caso, salvo solo lo stato di radiobtnAccredito
// (02) Non è detto che il file di cache sia già stato generato, per questo
//      motivo devo evitare di accedere ai membri dell'istanza CacheGestioneMonetaria
//      perchè sono tutti non inizializzati. La funzione salvaSuFile(), tramite la
//      salvaBin(), provvederà successivamente a creare il nuovo file