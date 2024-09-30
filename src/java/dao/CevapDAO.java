package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Cevap;
import model.Mesaj;

public class CevapDAO {
    
    private final String jdbcURL = "jdbc:mysql://localhost:3306/hawkeye";
    private final String jdbcKullaniciname = "root";
    private final String jdbcPassword = System.getenv("DB_PASSWORD");   
    
    // Definir constantes para los literales de cadena duplicados
    private static final String MESAJ_ADSOYAD = "mesaj_adsoyad";
    private static final String MESAJ_EMAIL = "mesaj_email";
    private static final String MESAJ_KONU = "mesaj_konu";
    private static final String MESAJ_ICERIK = "mesaj_icerik";
    private static final String MESAJ_TARIH = "mesaj_tarih";

    private static final String CEVAP_SELECT_ALL = "SELECT * FROM cevap " +
            "INNER JOIN mesaj ON (mesaj.mesajId = cevap.mesajId);";
    private static final String CEVAP_DELETE = "DELETE FROM cevap WHERE cevapId = ?;";
    private static final String MESAJ_SELECT_ID = "SELECT * FROM mesaj WHERE mesajId=?;";
    private static final String CEVAP_INSERT = "INSERT INTO cevap (mesajId, cevapIcerik, cevapBalsik) VALUES (?,?,?);"; 
    private static final String CEVAP_SELECT_ID = "SELECT * FROM cevap " +
            "INNER JOIN mesaj ON (mesaj.mesajId = cevap.mesajId) WHERE cevapId=?;";

    private static final Logger logger = Logger.getLogger(CevapDAO.class.getName());

    public CevapDAO() {
        // Constructor vacío
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcKullaniciname, jdbcPassword);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la conexión: {0}", e.getMessage());
        }
        return connection;
    }

    public List<Cevap> cevaplistele() {
        List<Cevap> cevaplar = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CEVAP_SELECT_ALL);) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int cevapId = rs.getInt("cevapId");
                int mesajId = rs.getInt("mesajId");
                String cevapIcerik = rs.getString("cevapIcerik");
                String cevapBalsik = rs.getString("cevapBalsik");
                String cevapTarih = rs.getString("cevapTarih");
                String mesajAdsoyad = rs.getString(MESAJ_ADSOYAD);
                String mesaj_email = rs.getString(MESAJ_EMAIL);
                String mesaj_konu = rs.getString(MESAJ_KONU);
                String mesaj_icerik = rs.getString(MESAJ_ICERIK);
                String mesaj_tarih = rs.getString(MESAJ_TARIH);
                
                cevaplar.add(new Cevap(cevapId, mesajId, cevapIcerik, cevapBalsik, cevapTarih, mesajAdsoyad, mesaj_email, mesaj_konu, mesaj_icerik, mesaj_tarih));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return cevaplar;
    } 

    public Mesaj mesajsec(int id) {
        Mesaj mesaj = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(MESAJ_SELECT_ID);) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String mesajAdsoyad = rs.getString(MESAJ_ADSOYAD);
                String mesaj_email = rs.getString(MESAJ_EMAIL);
                String mesaj_konu = rs.getString(MESAJ_KONU);
                String mesaj_icerik = rs.getString(MESAJ_ICERIK);
                String mesaj_tarih = rs.getString(MESAJ_TARIH);
                int mesaj_okunma = rs.getInt("mesaj_okunma");
                int mesaj_cevap = rs.getInt("mesaj_cevap");

                mesaj = new Mesaj(id, mesajAdsoyad, mesaj_email, mesaj_konu, mesaj_icerik, mesaj_tarih, mesaj_okunma, mesaj_cevap);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return mesaj;
    }

    public Cevap cevapincele(int id) {
        Cevap cevap = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CEVAP_SELECT_ID);) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int cevapId = rs.getInt("cevapId");
                int mesajId = rs.getInt("mesajId");
                String cevapIcerik = rs.getString("cevapIcerik");
                String cevapBalsik = rs.getString("cevapBalsik");
                String cevapTarih = rs.getString("cevapTarih");
                String mesajAdsoyad = rs.getString(MESAJ_ADSOYAD);
                String mesaj_email = rs.getString(MESAJ_EMAIL);
                String mesaj_konu = rs.getString(MESAJ_KONU);
                String mesaj_icerik = rs.getString(MESAJ_ICERIK);
                String mesaj_tarih = rs.getString(MESAJ_TARIH);
                
                cevap = new Cevap(cevapId, mesajId, cevapIcerik, cevapBalsik, cevapTarih, mesajAdsoyad, mesaj_email, mesaj_konu, mesaj_icerik, mesaj_tarih);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return cevap;
    }

    public void cevapekle(Cevap cevap) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CEVAP_INSERT)) {
            preparedStatement.setInt(1, cevap.getmesajId());
            preparedStatement.setString(2, cevap.getcevapIcerik());
            preparedStatement.setString(3, cevap.getcevapBalsik());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public boolean cevapsil(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CEVAP_DELETE);) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                logger.log(Level.SEVERE, "SQLState: {0}", ((SQLException) e).getSQLState());
                logger.log(Level.SEVERE, "Error Code: {0}", ((SQLException) e).getErrorCode());
                logger.log(Level.SEVERE, "Message: {0}", e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    logger.log(Level.SEVERE, "SQLState: {0}", ((SQLException) e).getSQLState());
                    t = t.getCause();
                }
            }
        }
    }
}
