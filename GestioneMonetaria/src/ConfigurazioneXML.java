import com.thoughtworks.xstream.*;
import java.io.IOException;
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
public class ConfigurazioneXML {
    private static final String percorsoFileXML = "./FileConfigurazione.xml";
    private static final String percorsoFileXSD = "./FileConfigurazione.xsd";
    public String tipoMoneta;
    public Integer limiteSpendibili;
    public String vistaDefaultGrafico;
    public String indirizzoIPUtente;
    public ArrayList<String> listaCategorie;
    public Integer maxNumGuadagniSpeseTabella;
    
    public static ConfigurazioneXML caricaDaFile() {
        try {
            //validiamo il file
            if(!ValidatoreXML.valida(percorsoFileXML, percorsoFileXSD))
                return null;
            
            //carichiamo il file
            String filestring =
                    new String(Files.readAllBytes(Paths.get(percorsoFileXML)));

            //convertiamolo in un'istanza di ConfigurazioneXML e poi restituiamolo
            XStream xs = new XStream();
            xs.useAttributeFor(ConfigurazioneXML.class, "tipoMoneta");
            ConfigurazioneXML risultato = (ConfigurazioneXML)xs.fromXML(filestring);
            System.out.println(risultato.tipoMoneta);
            return risultato;
        } catch (IOException ex) {
            System.out.println("errore: impossibile caricare la configurazione XML");
            return null;
        }
    }
}
