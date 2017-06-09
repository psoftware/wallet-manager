import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.scene.chart.*;
import javafx.util.*;

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
    private XYChart.Series<Number, Number> seriePuntiPos;
    private XYChart.Series<Number, Number> seriePuntiNeg;
    
    public GraficoStatisticheMonetarie(String titolo) {
        super(new NumberAxis(), new NumberAxis());
        
        seriePuntiPos = new XYChart.Series<>();
        seriePuntiNeg = new XYChart.Series<>();
        seriePuntiPos.setName("Entrate");
        seriePuntiNeg.setName("Uscite");
        
        ((NumberAxis)getXAxis()).setAutoRanging(false); //(02)
        ((NumberAxis)getXAxis()).setTickUnit(1.0); //(02)

        getData().add(seriePuntiPos);
        getData().add(seriePuntiNeg);
    }
    
    public void popolaGrafico(ArrayList<GuadagnoSpesaSettimanale> listaEntrateSett, int sceltaVisuale)
    {
        seriePuntiPos.getData().clear();
        seriePuntiNeg.getData().clear();
        
        //int indexCategoriaFiltro = comboPeriodoGrafico.getSelectionModel().getSelectedIndex();
        LocalDate inizioPeriodo;
        if(sceltaVisuale == 0)         // Mensile
            inizioPeriodo = LocalDate.now().plusMonths(-1);
        else if(sceltaVisuale == 1)    // Semestrale
            inizioPeriodo = LocalDate.now().plusMonths(-6);
        else if(sceltaVisuale == 2)    // Annuale
            inizioPeriodo = LocalDate.now().plusYears(-1);
        else
            return;
        
        LocalDate finePeriodo = LocalDate.now();
        if(inizioPeriodo.getYear() != finePeriodo.getYear())
            inizioPeriodo = LocalDate.of(finePeriodo.getYear(), 1, 2);// (05)
        
        WeekFields weekFields = WeekFields.of(Locale.getDefault()); // (03)
        int inizioWeekday = inizioPeriodo.get(weekFields.weekOfWeekBasedYear()); // (03)
        int fineWeekday = finePeriodo.get(weekFields.weekOfWeekBasedYear()); // (03)
        ((NumberAxis)getXAxis()).setLowerBound(inizioWeekday);  // (02)
        ((NumberAxis)getXAxis()).setUpperBound(fineWeekday);  // (02)

        for(GuadagnoSpesaSettimanale gsset : listaEntrateSett)
            if(gsset.settimana >= inizioWeekday && gsset.settimana <= fineWeekday)  // (04)
            {
                if(gsset.sommaimporti > 0)
                    seriePuntiPos.getData().add(new XYChart.Data<>(gsset.settimana,
                            gsset.sommaimporti));
                else
                    seriePuntiNeg.getData().add(new XYChart.Data<>(gsset.settimana,
                            gsset.sommaimporti * -1)); //(01)
            }
    }
}

// (01) Gli importi negativi vengono comunque riportati nel quadrante positivo,
//      come da requisiti
// (02) Mi serve che le label dell'asse x siano intere. Per questo devo disabilitarmi
//      l'autoranging e definirmi tick, upperbound e lower bound.
// (03) La classe WeekFields mi permette di calcolare il numero della settimana,
//      su riferimento annuale, in modo che possa settare il range corretto
//      sull'asse temporale delle ascisse
// (04) Devo scartare tutte le date fuori dal range. Non lo faccio nelle query
//      del database perchè mi costringerebbe ad aggiungere competenze alla classe
//      GestioneMonetariaFinestra, cosa che non vorrei fare
// (05) Devo stare attento al fatto che la data iniziale potrebbe sforare l'anno
//      corrente. Questo è un problema perchè gli identificativi delle settimane
//      sono identificativi solo se relativi ad un anno. Inoltre il database mi
//      restituisce solo GuadagniSpeseSettimanali relativi all'anno corrente