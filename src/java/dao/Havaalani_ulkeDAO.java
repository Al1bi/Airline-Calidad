package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;

import model.Havaalani_ulke;

public class Havaalani_ulkeDAO {
    
    private final String jdbcURL = "jdbc:mysql://localhost:3306/hawkeye";
    private final String jdbcKullaniciname = "root";
    private String jdbcPassword;    
    private static final Logger logger = Logger.getLogger(Havaalani_ulkeDAO.class.getName());
    
    
    private static final String ULKE_SELECT_ID = "select * from havaalani_ulke where havaalani_ulke_id=?;";
    private static final String ULKE_SELECT_ALL = "select * from havaalani_ulke;";
    private static final String ULKE_INSERT = "INSERT INTO Havaalani_ulke" + "  (havaalani_ulke_ad) VALUES " +
        " (?);"; 
    private static final String ULKE_DELETE = "delete from Havaalani_ulke where havaalani_ulke_id = ?;";
    private static final String ULKE_UPDATE = "update Havaalani_ulke set havaalani_ulke_ad = ? where havaalani_ulke_id = ?;";
    
    public Havaalani_ulkeDAO() {
        Properties configProps = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            configProps.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        jdbcPassword = configProps.getProperty("havaalani_ulke_dao.mail.pass");
    }
    
    protected Connection getConnection() {
        Connection connection = null;
         
        try {
            connection = DriverManager.getConnection(jdbcURL,jdbcKullaniciname,jdbcPassword);
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }   
    
    public List<Havaalani_ulke> ulkelistele() {

        List<Havaalani_ulke> ulkeler = new ArrayList<> ();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ULKE_SELECT_ALL);) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int havaalani_ulke_id = rs.getInt("havaalani_ulke_id");
                String havaalani_ulke_ad = rs.getString("havaalani_ulke_ad");
                ulkeler.add(new Havaalani_ulke(havaalani_ulke_id, havaalani_ulke_ad));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return ulkeler;
    }
    
    public void ulkeekle(Havaalani_ulke ulke) throws SQLException {  
        try (           
            Connection connection = getConnection();                                
            PreparedStatement preparedStatement = connection.prepareStatement(ULKE_INSERT)) {
            preparedStatement.setString(1, ulke.getHavaalani_ulke_ad());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    } 
    
    public boolean ulkesil(int id) throws SQLException {
        boolean silinenSatir;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(ULKE_DELETE);) {
            statement.setInt(1, id);
            silinenSatir = statement.executeUpdate() > 0;
        }
        return silinenSatir;
    } 
    
    public boolean ulkeguncelle(Havaalani_ulke ulke) throws SQLException {
        boolean guncellenenSatir;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(ULKE_UPDATE);) {
            statement.setString(1, ulke.getHavaalani_ulke_ad());           
            statement.setInt(2, ulke.getHavaalani_ulke_id());
            guncellenenSatir = statement.executeUpdate() > 0;
        }
        return guncellenenSatir;
    } 
    
    public Havaalani_ulke ulkesec(int id) {
        Havaalani_ulke ulke = null;
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ULKE_SELECT_ID);) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String havaalani_ulke_ad = rs.getString("havaalani_ulke_ad");
                ulke = new Havaalani_ulke(id, havaalani_ulke_ad);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return ulke;
    } 
    
    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                logger.log(Level.SEVERE, "SQLState: {0}", ((SQLException) e).getSQLState());
                logger.log(Level.SEVERE, "Error Code: {0}", ((SQLException) e).getErrorCode());
                logger.log(Level.SEVERE, "Message: {0}", e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    logger.log(Level.SEVERE, "Cause: {0}", t.toString());
                    t = t.getCause();
                }
            }
        }
    }
}
