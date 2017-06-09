import java.io.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class ElementoLog implements Serializable {
    public String indirizzoIP;
    public String dataOra;      // (01)
    public String nomeEvento;
    public String oggettoCoinvolto;

    public ElementoLog(String indirizzoIP, String dataOra, String nomeEvento, String oggettoCoinvolto) {
        this.indirizzoIP = indirizzoIP;
        this.dataOra = dataOra;            // (01)
        this.nomeEvento = nomeEvento;
        this.oggettoCoinvolto = oggettoCoinvolto;
    }
}

// (01) La dataOra è restituita in formato universale attraverso una stringa
//      (esempio: Thu Jun 08 19:26:18 CEST 2017)