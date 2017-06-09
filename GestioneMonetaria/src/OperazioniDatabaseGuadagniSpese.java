import java.util.*;
import java.sql.*;
import java.time.*;

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
    
    public void aggiungiGuadagnoSpesa(GuadagnoSpesa gs) {
        try (PreparedStatement ps = co.prepareStatement("INSERT INTO guadagnispese VALUES (?, ?, ?, ?)");) { 
              ps.setDate(1, java.sql.Date.valueOf(gs.data));
              ps.setString(2, gs.categoria);
              ps.setString(3, gs.descrizione);
              ps.setInt(4, gs.importo);
              ps.executeUpdate();
        } catch (SQLException e) {System.err.println(e.getMessage());}     
    }
    
    public void aggiungiListaGuadagnoSpesa(List<GuadagnoSpesa> lista)
    {
        for(GuadagnoSpesa gs: lista)
            aggiungiGuadagnoSpesa(gs);
    }
    
    public int ottieniSaldo()
    {
        try (Statement st = co.createStatement()) { 
              ResultSet rs = st.executeQuery("SELECT SUM(importo) AS s FROM guadagnispese");  
              rs.next();
              return rs.getInt("s");
        } catch (SQLException e) {System.err.println(e.getMessage()); return 0;}
    }
    
    public ArrayList<GuadagnoSpesa> ottieniGuadagniSpese(int limiteEntrate) {
        ArrayList risultato = new ArrayList<>();
        try (PreparedStatement ps = co.prepareStatement(
                        "SELECT * FROM guadagnispese "
                      + "ORDER BY datariferimento "
                      + "LIMIT ?")) {
              ps.setInt(1, limiteEntrate);
              ResultSet rs = ps.executeQuery();  
              while (rs.next())
                risultato.add(new GuadagnoSpesa(rs.getDate("datariferimento").toLocalDate(),
                                                rs.getString("categoria"),
                                                rs.getString("descrizione"),
                                                rs.getInt("importo")));
              return risultato;
        } catch (SQLException e) {System.err.println(e.getMessage()); return null;}
    }
    
    public ArrayList ottieniGuadagniSpeseConFiltro(String datalnizio, String dataFine,
            String categoria, String descrizioneParziale, int limiteEntrate) {
        ArrayList risultato = new ArrayList<>();
        try ( PreparedStatement ps = co.prepareStatement(
                "SELECT * FROM guadagnispese "
                + "WHERE datariferimento >= ? AND datariferimento <= ? "
                + "AND categoria LIKE ? and descrizione LIKE ? "
                + " ORDER BY datariferimento "
                + "LIMIT ?");
        ) {
              ps.setDate(1, java.sql.Date.valueOf(datalnizio));
              ps.setDate(2, java.sql.Date.valueOf(dataFine));
              ps.setString(3, "%"+categoria+"%");
              ps.setString(4, "%"+descrizioneParziale+"%");
              ps.setInt(5, limiteEntrate);
              ResultSet rs = ps.executeQuery();
              
              while (rs.next()) //12
                risultato.add(new GuadagnoSpesa(rs.getDate("datariferimento").toLocalDate(),
                                                rs.getString("categoria"),
                                                rs.getString("descrizione"),
                                                rs.getInt("importo")));
              return risultato;
        } catch (SQLException e) {System.err.println(e.getMessage()); return null;}
    }
    
    public ArrayList<GuadagnoSpesaSettimanale> ottieniGuadagniSpeseSettimanali() {
        ArrayList risultato = new ArrayList<>();
        try (PreparedStatement ps = co.prepareStatement(
                "SELECT WEEK(datariferimento) AS settimana, SUM(importo) AS sommaimporti " +
                "FROM guadagnispese " +
                "WHERE importo > 0 " +
                "AND YEAR(datariferimento)=YEAR(NOW()) " +
                "GROUP BY WEEK(datariferimento) " +
                "UNION ALL " +
                "SELECT WEEK(datariferimento) AS settimana, SUM(importo) AS sommaimporti " +
                "FROM guadagnispese " +
                "WHERE importo < 0 " +
                "AND YEAR(datariferimento)=YEAR(NOW()) " +
                "GROUP BY WEEK(datariferimento)")) //(02)
        {
              ResultSet rs = ps.executeQuery();  
              while (rs.next())
                risultato.add(new GuadagnoSpesaSettimanale(rs.getInt("settimana"),
                                                rs.getInt("sommaimporti")));    //(01)
              return risultato;
        } catch (SQLException e) {System.err.println(e.getMessage()); return null;}
    }
}

// (01) Mi serve un'altra (semplice) classe per restituire il particolare risultato
//      restituito dalla ottieniGuadagniSpeseSettimanali.
// (02) La query mi restituisce i risultati solo relativi all'anno corrente in modo
//      tale che il numero della settimana possa essere una chiave