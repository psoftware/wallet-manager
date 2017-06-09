import com.thoughtworks.xstream.*;
import java.io.*;
import java.net.*;
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
public class InvioDatiLog {
    public String IPServer;
    public Integer portaServer;
    public String IPClient;

    public InvioDatiLog(String IPServer, Integer portaServer, String IPClient) {
        this.IPServer = IPServer;
        this.portaServer = portaServer;
        this.IPClient = IPClient;
    }
    
    public void invia(String nomeEvento, String oggettoCoinvolto) {
        ElementoLog elem = new ElementoLog(this.IPClient, new Date().toString(),
                nomeEvento, oggettoCoinvolto);  // (01)
        
        try(Socket s = new Socket(IPServer, portaServer);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());)
        {
            dos.writeUTF(new XStream().toXML(elem));
        } catch(Exception e) {
            e.printStackTrace();    // (02)
        }

    }
}

// (01) La data registrata nell'entrata del log è quella che si registra nel
//      momento in cui viene chiamato il metodo invia, per questo non me la
//      faccio indicare da chi utilizza il metodo
// (02) Utile per avere più informazioni sulle eccezioni