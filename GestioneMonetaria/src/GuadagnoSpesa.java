import java.time.*;
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
public class GuadagnoSpesa {
    public Date data;
    public String categoria;
    public String descrizione;
    public Integer importo;

    public GuadagnoSpesa(Date data, String categoria, String descrizione, Integer importo) {
        this.data = data;
        this.categoria = categoria;
        this.descrizione = descrizione;
        this.importo = importo;
    }   
}
