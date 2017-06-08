import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class FileGuadagniSpeseXML {
    private static final String percorsoFileXSD = "./FileGuadagniSpese.xsd";
    private String filename;

    public FileGuadagniSpeseXML(String fn) {
        filename = fn;
    }
    
    public ArrayList<GuadagnoSpesa> caricaEntrate(){
        try {
            //validiamo il file
            if(!ValidatoreXML.validaFile(filename, percorsoFileXSD))
                return null;
            
            //carichiamo il file
            String filestring =
                    new String(Files.readAllBytes(Paths.get(filename)));

            //convertiamolo in un'istanza di ArrayList<GuadagnoSpesa> e poi restituiamolo
            LocalDateConverter conv = new LocalDateConverter(); // (01)
            XStream xs = new XStream();
            xs.registerConverter(conv);                         // (01)
            return (ArrayList<GuadagnoSpesa>)xs.fromXML(filestring);

        } catch (IOException ex) {
            System.out.println("errore: impossibile caricare la lista di GuadagniSpese XML");
            return null;
        }
    }
}

// (01) XStream non supporta il tipo LocalDate, quindi definiamo una classe che si
//      occupa di gestire questo nuovo tipo. Non possiamo usare direttamente il
//      tipo Date per questioni di integrazione con la classe OperazioniDatabaseGuadagniSpese:
//      la libreria mysql lavora meglio con i tipi LocalDate. Usare Date porterebbe
//      ad effettuare cast esplici abbastanza complessi.