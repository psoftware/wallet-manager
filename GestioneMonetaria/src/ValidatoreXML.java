import javax.xml.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.*;
import javax.xml.validation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

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
    public static boolean valida(String fileXML, String fileXSD) {
        try {  
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = db.parse(new File(fileXML));
            Schema s = sf.newSchema(new StreamSource(new File(fileXSD)));
            s.newValidator().validate(new DOMSource(d));
            return true;
        } catch (Exception e) {
          if (e instanceof SAXException) 
            System.out.println("Errore di validazione: " + e.getMessage());
          else
            System.out.println(e.getMessage());
        }

        return false;
    }
}
