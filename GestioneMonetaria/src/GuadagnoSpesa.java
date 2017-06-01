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
public class GuadagnoSpesa {
    private SimpleStringProperty data;
    private SimpleStringProperty categoria;
    private SimpleStringProperty descrizione;
    private SimpleIntegerProperty importo;
    
    public GuadagnoSpesa(String d, String c, String des, Integer i) {
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
