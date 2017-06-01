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
public class TabellaVisualeGuadagniSpese extends TableView<GuadagnoSpesa> {
    private final ObservableList<GuadagnoSpesa> listaOsservabileGuadagniSpese
            = FXCollections.observableArrayList(); //(01)
    
    public TabellaVisualeGuadagniSpese() {
        TableColumn dataCol = new TableColumn("DATA");
        dataCol.setCellValueFactory(new PropertyValueFactory<>("Data"));
 
        TableColumn categoriaCol = new TableColumn("CATEGORIA"); //3
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("Categoria"));
        
        TableColumn descrCol = new TableColumn("DESCRIZIONE"); //3
        descrCol.setCellValueFactory(new PropertyValueFactory<>("Descrizione"));
        
        TableColumn importoCol = new TableColumn("IMPORTO"); //3
        importoCol.setCellValueFactory(new PropertyValueFactory<>("Importo"));
        
        this.setItems(listaOsservabileGuadagniSpese);
        this.getColumns().addAll(dataCol, categoriaCol, descrCol, importoCol);
    }
    
    public void aggiornaListaGuadagniSpese(List<GuadagnoSpesa> entrate) {
        listaOsservabileGuadagniSpese.clear();
        listaOsservabileGuadagniSpese.addAll(entrate);
    }
}

// (01) La lista va inizializzata