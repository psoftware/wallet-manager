import java.util.*;
import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class OperazioniDatabaseGuadagniSpese {
    Connection co;
    public OperazioniDatabaseGuadagniSpese(String hostname, String user, String password) {
        try {
            co = DriverManager.getConnection(hostname, user, password);
        } catch (SQLException ex) {
            System.out.println("errore: impossibile stabilire connessione con Database");
        }
    }
    
    public void aggiungiGuadagnoSpesa(String data, String descrizione, String categoria, int importo) {
        try (PreparedStatement ps = co.prepareStatement("INSERT INTO guadagnispese VALUES (?, ?, ?, ?)");) { 
              ps.setDate(1, java.sql.Date.valueOf(data));
              ps.setString(2, categoria);
              ps.setString(3, descrizione);
              ps.setInt(4, importo);
              ps.executeUpdate();
        } catch (SQLException e) {System.err.println(e.getMessage());}     
    }
    
    public ArrayList<GuadagnoSpesa> ottieniGuadagniSpese() {
        ArrayList risultato = new ArrayList<>();
        try (Statement st = co.createStatement()) { 
              ResultSet rs = st.executeQuery("SELECT * FROM guadagnispese");  
              while (rs.next())
                risultato.add(new GuadagnoSpesa(rs.getDate("datariferimento"),
                                                rs.getString("categoria"),
                                                rs.getString("descrizione"),
                                                rs.getInt("importo")));
              return risultato;
        } catch (SQLException e) {System.err.println(e.getMessage()); return null;}
    }
    
    public ArrayList ottieniGuadagniSpeseConFiltro(String datalnizio, String dataFine,
            String categoria, String descrizioneParziale) {
        ArrayList risultato = new ArrayList<>();
        try ( PreparedStatement ps = co.prepareStatement(
                "SELECT * FROM guadagnispese "
                + "WHERE datariferimento >= ? AND datariferimento <= ? "
                + "AND categoria LIKE ? and descrizione LIKE ?;");
        ) {
              ps.setDate(1, java.sql.Date.valueOf(datalnizio));
              ps.setDate(2, java.sql.Date.valueOf(dataFine));
              ps.setString(3, "%"+categoria+"%");
              ps.setString(4, "%"+descrizioneParziale+"%");
              ResultSet rs = ps.executeQuery();
              
              while (rs.next()) //12
                risultato.add(new GuadagnoSpesa(rs.getDate("datariferimento"),
                                                rs.getString("categoria"),
                                                rs.getString("descrizione"),
                                                rs.getInt("importo")));
              return risultato;
        } catch (SQLException e) {System.err.println(e.getMessage()); return null;}
    }
}