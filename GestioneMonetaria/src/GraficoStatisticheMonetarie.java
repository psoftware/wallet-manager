import java.util.ArrayList;
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
    private XYChart.Series seriePuntiPos;
    private XYChart.Series seriePuntiNeg;
    
    public GraficoStatisticheMonetarie(String titolo) {
        super(new NumberAxis(), new NumberAxis());
        
        seriePuntiPos = new XYChart.Series();
        seriePuntiNeg = new XYChart.Series();
        seriePuntiPos.setName("Entrate");
        seriePuntiNeg.setName("Uscite");

        getData().add(seriePuntiPos);
        getData().add(seriePuntiNeg);
    }
    public void popolaGrafico(ArrayList<GuadagnoSpesa> listaEntrate) {
        for(int i=0; i < listaEntrate.size(); i++)
            if(listaEntrate.get(i).importo > 0)
                seriePuntiPos.getData().add(new XYChart.Data(i, listaEntrate.get(i).importo));
            else
                seriePuntiNeg.getData().add(new XYChart.Data(i, listaEntrate.get(i).importo));
    } 
}
