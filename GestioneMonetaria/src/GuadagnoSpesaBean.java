import javafx.beans.property.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class GuadagnoSpesaBean {
    private SimpleStringProperty data;      // (01)
    private SimpleStringProperty categoria;
    private SimpleStringProperty descrizione;
    private SimpleIntegerProperty importo;
    
    public GuadagnoSpesaBean(String d, String c, String des, Integer i) {
        data = new SimpleStringProperty(d);
        categoria = new SimpleStringProperty(c);
        descrizione = new SimpleStringProperty(des);
        importo = new SimpleIntegerProperty(i);
    }
    
    public String getData() {
        return data.getValue();
    }
    public String getCategoria() {
        return categoria.getValue();
    }
    public String getDescrizione() {
        return descrizione.getValue();
    }
    public int getImporto() {
        return importo.getValue();
    }
}

// (01) La data è rappresentata come stringa per comodità, in quanto questo bean
//      è solamente utilizzato per costruire la tabella dell'interfaccia.