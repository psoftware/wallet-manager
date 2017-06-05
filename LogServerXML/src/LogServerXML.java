import java.io.*;
import java.net.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class LogServerXML {
    public static void main(String[] args) {
        try(ServerSocket sv = new ServerSocket(8080);)
        {
            while(true){
                Socket so = sv.accept();
                DataInputStream dis = new DataInputStream(so.getInputStream());
                String risultato = dis.readUTF();
                System.out.println(risultato); 

                // salvo contenuto ricevuto in un file
                FileWriter tempfw = new FileWriter("./ricevuto.xml", false);
                tempfw.write(risultato);
                tempfw.close();
                
                // valido il file
                if(!ValidatoreXML.valida("./ricevuto.xml", "./ElementoLog.xsd"))
                {
                    System.out.println("L'elemeno XML ricevuto non è valido");
                    continue;
                }
                
                // aggiungo l'entrata al file globale degli eventi
                FileWriter fw = new FileWriter("./serverLog.txt", true);
                fw.write(risultato + "\n\n");
                fw.close();

                dis.close();
                so.close();
            }
        } catch(Exception e) { e.printStackTrace(); }
    }
}
