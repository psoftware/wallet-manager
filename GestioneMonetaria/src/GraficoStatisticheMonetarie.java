import java.time.*;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.scene.chart.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class GraficoStatisticheMonetarie extends LineChart {
    private XYChart.Series<String, Number> seriePuntiPos;
    private XYChart.Series<String, Number> seriePuntiNeg;
    
    public GraficoStatisticheMonetarie(String titolo) {
        super(new CategoryAxis(), new NumberAxis());
        
        seriePuntiPos = new XYChart.Series<>();
        seriePuntiNeg = new XYChart.Series<>();
        seriePuntiPos.setName("Entrate");
        seriePuntiNeg.setName("Uscite");

        getData().add(seriePuntiPos);
        getData().add(seriePuntiNeg);
    }
    
    public void popolaGrafico(ArrayList<GuadagnoSpesa> listaEntrate, int indexTemporale) {
        seriePuntiPos.getData().clear();
        seriePuntiNeg.getData().clear();
        
        LocalDate inizioPeriodo;
        if(indexTemporale == 0)
            inizioPeriodo = LocalDate.now().plusDays(-7);
        else if(indexTemporale == 1)
            inizioPeriodo = LocalDate.now().plusMonths(-1);
        else if(indexTemporale == 2)
            inizioPeriodo = LocalDate.now().plusYears(-1);
        else
            return;

        LocalDate finePeriodo = LocalDate.now();
        for(int i=0; i < listaEntrate.size(); i++)
        {
            if(listaEntrate.get(i).data.isAfter(finePeriodo) || listaEntrate.get(i).data.isBefore(inizioPeriodo))
                continue; //(01)
            if(listaEntrate.get(i).importo > 0)
                seriePuntiPos.getData().add(new XYChart.Data<>(listaEntrate.get(i).data.toString(),
                        listaEntrate.get(i).importo));
            else
                seriePuntiNeg.getData().add(new XYChart.Data<>(listaEntrate.get(i).data.toString(),
                        listaEntrate.get(i).importo));
        }
    } 
}

// (01) Devo controllare che ogni entrata non stia fuori dai margini del tipo di
//      visuale (giornaliera, mensile o settimanale)