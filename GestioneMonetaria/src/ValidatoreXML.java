import java.io.*;
import java.nio.file.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.validation.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class ValidatoreXML {
    public static boolean validaStringa(String stringaXML, String fileXSD) {
        try {
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(stringaXML));
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document d = db.parse(is);
            
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema s = sf.newSchema(new StreamSource(new File(fileXSD)));
            s.newValidator().validate(new DOMSource(d));
            return true;
        } catch (Exception e) {
          if (e instanceof SAXException) 
            System.out.println("Errore di validazione: " + e.getMessage());
          else
            System.out.println("Errore generico: " + e.getMessage());
          
          // questo metodo è utile per avere più dettagli sulle eccezioni
          e.printStackTrace();
        }

        return false;
    }
    
    public static boolean validaFile(String fileXML, String fileXSD) {
        try {
            // carichiamo il file
            String stringaXML =
                    new String(Files.readAllBytes(Paths.get(fileXML)));
            
            // usiamo il metodo già definito per la validazione delle stringhe
            return validaStringa(stringaXML, fileXSD);
        } catch (IOException e) {
            System.out.println("Errore I/O validaFile: " + e.getMessage());
        }

        return false;
    }
}
