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

import model.Havaalani_sehir;

public class Havaalani_sehirDAO {

    private final String jdbcURL = "jdbc:mysql://localhost:3306/hawkeye";
    private final String jdbcKullaniciname = "root";
    private String jdbcPassword;    
    
    private static final String SEHIR_SELECT_ID = "select * from havaalani_sehir where havaalani_sehir_id=?;";
    private static final String SEHIR_SELECT_ALL = "select * from havaalani_sehir;";
    private static final String SEHIR_INSERT = "INSERT INTO havaalani_sehir (havaalani_sehir_ad) VALUES (?);";
    private static final String SEHIR_DELETE = "delete from havaalani_sehir where havaalani_sehir_id = ?;";
    private static final String SEHIR_UPDATE = "update havaalani_sehir set havaalani_sehir_ad = ? where havaalani_sehir_id = ?;";

    // Definir un logger para la clase
    private static final Logger logger = Logger.getLogger(Havaalani_sehirDAO.class.getName());

    public Havaalani_sehirDAO() {

        Properties configProps = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.log(Level.SEVERE, "No se pudo encontrar config.properties");
                return;
            }
            configProps.load(input);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error al cargar el archivo de configuración", ex);
        }
        jdbcPassword = configProps.getProperty("havaalani_sehir_dao.mail.pass");
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

    public List<Havaalani_sehir> sehirlistele() {
        List<Havaalani_sehir> sehirler = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEHIR_SELECT_ALL);) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int havaalani_sehir_id = rs.getInt("havaalani_sehir_id");
                String havaalani_sehir_ad = rs.getString("havaalani_sehir_ad");
                sehirler.add(new Havaalani_sehir(havaalani_sehir_id, havaalani_sehir_ad));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return sehirler;
    }

    public void sehirekle(Havaalani_sehir sehir) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEHIR_INSERT)) {
            preparedStatement.setString(1, sehir.getHavaalani_sehir_ad());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public boolean sehirsil(int id) throws SQLException {
        boolean silinenSatir;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(SEHIR_DELETE);) {
            statement.setInt(1, id);
            silinenSatir = statement.executeUpdate() > 0;
        }
        return silinenSatir;
    }

    public boolean sehirguncelle(Havaalani_sehir sehir) throws SQLException {
        boolean guncellenenSatir;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SEHIR_UPDATE);) {
            statement.setString(1, sehir.getHavaalani_sehir_ad());
            statement.setInt(2, sehir.getHavaalani_sehir_id());
            guncellenenSatir = statement.executeUpdate() > 0;
        }
        return guncellenenSatir;
    }

    public Havaalani_sehir sehirsec(int id) {
        Havaalani_sehir sehir = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEHIR_SELECT_ID);) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String havaalani_sehir_ad = rs.getString("havaalani_sehir_ad");
                sehir = new Havaalani_sehir(id, havaalani_sehir_ad);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return sehir;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                logger.log(Level.SEVERE, "SQLState: {0}", ((SQLException) e).getSQLState());
                logger.log(Level.SEVERE, "Error Code: {0}", ((SQLException) e).getErrorCode());
                logger.log(Level.SEVERE, "Message: {0}", e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    logger.log(Level.SEVERE, "Cause: {0}", t.toString()); // Cambiado de System.out a logger
                    t = t.getCause();
                }
            }
        }
    }
}
