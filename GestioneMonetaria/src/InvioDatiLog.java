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
        Date datacorrente = new Date();
        ElementoLog elem = new ElementoLog(this.IPClient, datacorrente.toString(), nomeEvento, oggettoCoinvolto);
        
        try(Socket s = new Socket(IPServer, portaServer);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            )
        {
            dos.writeUTF(new XStream().toXML(elem));
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
