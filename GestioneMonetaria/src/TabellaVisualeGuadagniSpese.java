import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class TabellaVisualeGuadagniSpese extends TableView<GuadagnoSpesaBean> {
    private final ObservableList<GuadagnoSpesaBean> listaOsservabileGuadagniSpese
            = FXCollections.observableArrayList(); //(01)
    
    public TabellaVisualeGuadagniSpese() {
        TableColumn dataCol = new TableColumn("DATA");
        dataCol.setCellValueFactory(new PropertyValueFactory<>("Data"));
 
        TableColumn categoriaCol = new TableColumn("CATEGORIA");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("Categoria"));
        
        TableColumn descrCol = new TableColumn("DESCRIZIONE");
        descrCol.setCellValueFactory(new PropertyValueFactory<>("Descrizione"));
        
        TableColumn importoCol = new TableColumn("IMPORTO");
        importoCol.setCellValueFactory(new PropertyValueFactory<>("Importo"));
        
        this.setItems(listaOsservabileGuadagniSpese);
        this.getColumns().addAll(dataCol, categoriaCol, descrCol, importoCol);
    }
    
    public void aggiornaListaGuadagniSpese(List<GuadagnoSpesa> entrate) {
        listaOsservabileGuadagniSpese.clear();

        for(GuadagnoSpesa gs : entrate) // (02)
            listaOsservabileGuadagniSpese.add(
                    new GuadagnoSpesaBean(gs.data.toString(), gs.categoria, gs.descrizione, gs.importo)
            );
    }
}

// (01) La lista va inizializzata
// (02) Gli oggetti di tipo GuadagnoSpesa vanno convertiti in GuadagnoSpesaBeen
//      perchè relativi alla tabella che dobbiamo popolare