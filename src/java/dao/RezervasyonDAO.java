package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Rezervasyon;

public class RezervasyonDAO {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/hawkeye";
    private final String jdbcKullaniciname = "root";
    private final String jdbcPassword = System.getenv("DB_PASSWORD"); 
     // Definir un logger para la clase
    private static final Logger logger = Logger.getLogger(RezervasyonDAO.class.getName());
    
    public static final String DATE_PATTERN = "aaaa-MM-dd";
    public static final String TIME_PATTERN = "HH:mm"; 
    public static final String YOLCU_AD_COLUMN = "yolcu_ad";  
    public static final String YOLCU_EMAIL_COLUMN = "yolcu_email"; 
    public static final String YOLCU_TEL_COLUMN = "yolcu_tel";
    public static final String YOLCU_TC_COLUMN = "yolcu_tc";
    public static final String YOLCU_TIP_COLUMN = "yolcu_tip";
    public static final String KOLTUK_NO_COLUMN = "koltuk_no";
    public static final String UCUS_SAAT_COLUMN = "ucus_saat";
    public static final String UCUS_TARIH_COLUMN = "ucus_tarih";
    public static final String UCUS_SURE_COLUMN = "ucus_seguro";
    public static final String REZERVASYON_ID_COLUMN = "Rezervasyon_id";
    public static final String KALKIS_SEHIR_COLUMN = "kalkis_sehir";
    public static final String KALKIS_AD_COLUMN = "kalkis_ad";
    public static final String KALKIS_KOD_COLUMN = "kalkis_kod";
    public static final String VARIS_SEHIR_COLUMN = "varis_sehir";
    public static final String VARIS_AD_COLUMN = "varis_ad";
    public static final String VARIS_KOD_COLUMN = "varis_kod";
    public static final String FIRMA_AD_COLUMN = "firma_ad";
    public static final String FIRMA_LOGO_COLUMN = "firma_logo";
    public static final String UCAK_AD_COLUMN = "ucak_ad";
    public static final String REZERVASYON_TARIH_COLUMN = "rezervasyon_tarih";  
    public static final String UCUS_ID_COLUMN = "ucus_id";  
    public static final String UCUS_UCRET_COLUMN = "ucus_ucret";  
    public static final String SONUC_COLUMN = "sonuc";  
    
    private static final String TEKYON_SORGULAMA_SELECT1="select distinct ucus_id,(ucak.ucak_koltuk-(SELECT COUNT(ucus_id) FROM rezervasyon WHERE ucus_id=ucus.ucus_id )) as bos_koltuk, a.havaalani_sehir_ad as kalkis_sehir, b.havaalani_sehir_ad as varis_sehir ,s.havaalani_ad as kalkis_ad,s.havaalani_kod as kalkis_kod, p.havaalani_ad as varis_ad, p.havaalani_kod as varis_kod, ucus_tarih, ucus_saat, ucus_sure, firma.firma_ad,firma.firma_logo , ucak.ucak_ad, ucus_ucret from ucus JOIN havaalani JOIN havaalani_sehir\n" +
                                    "INNER JOIN  ucak ON (ucak.ucak_id = ucus.ucak_id)\n" +
                                    "INNER JOIN  firma ON (firma.firma_id = ucus.firma_id)\n" +
                                    "INNER JOIN  havaalani s ON (s.havaalani_id = ucus.ucus_kalkis_id)\n" +
                                    "INNER JOIN  havaalani p ON (p.havaalani_id = ucus.ucus_varis_id)\n" +
                                    "INNER JOIN  havaalani_sehir a ON (a.havaalani_sehir_id = s.havaalani_sehir_id)\n" +
                                    "INNER JOIN  havaalani_sehir b ON (b.havaalani_sehir_id = p.havaalani_sehir_id)\n" +
                                    "WHERE s.havaalani_id = ? AND p.havaalani_id =? AND ucus_tarih=? AND (ucak.ucak_koltuk-(SELECT COUNT(ucus_id) FROM rezervasyon WHERE ucus_id=ucus.ucus_id )) >= ?;";
    private static final String TEKYON_SORGULAMA_SELECT2="select distinct ucus_id,(ucak.ucak_koltuk-(SELECT COUNT(ucus_id) FROM rezervasyon WHERE ucus_id=ucus.ucus_id )) as bos_koltuk, a.havaalani_sehir_ad as kalkis_sehir, b.havaalani_sehir_ad as varis_sehir ,s.havaalani_ad as kalkis_ad,s.havaalani_kod as kalkis_kod, p.havaalani_ad as varis_ad, p.havaalani_kod as varis_kod, ucus_tarih, ucus_saat, ucus_sure, firma.firma_ad,firma.firma_logo , ucak.ucak_ad, ucus_ucret from ucus JOIN havaalani JOIN havaalani_sehir\n" +
                                    "INNER JOIN  ucak ON (ucak.ucak_id = ucus.ucak_id)\n" +
                                    "INNER JOIN  firma ON (firma.firma_id = ucus.firma_id)\n" +
                                    "INNER JOIN  havaalani s ON (s.havaalani_id = ucus.ucus_kalkis_id)\n" +
                                    "INNER JOIN  havaalani p ON (p.havaalani_id = ucus.ucus_varis_id)\n" +
                                    "INNER JOIN  havaalani_sehir a ON (a.havaalani_sehir_id = s.havaalani_sehir_id)\n" +
                                    "INNER JOIN  havaalani_sehir b ON (b.havaalani_sehir_id = p.havaalani_sehir_id)\n" +
                                    "WHERE s.havaalani_id = ? AND p.havaalani_id =? AND ucus_tarih=? AND ucus_saat > ? AND (ucak.ucak_koltuk-(SELECT COUNT(ucus_id) FROM rezervasyon WHERE ucus_id=ucus.ucus_id )) >= ?;";
    private static final String REZERVASYON_SELECT_COUNT="SELECT COUNT(*) as sonuc FROM rezervasyon WHERE rezervasyon_tarih BETWEEN ? AND ?;";
    private static final String UCUS_SELECT_COUNT="SELECT count(*) as sonuc FROM ucus WHERE ucus_tarih >= ? ;";
    private static final String MESAJ_SELECT_COUNT="SELECT count(*) as sonuc FROM mesaj WHERE mesaj_okunma = 0;";
    private static final String REZERVASYON_DELETE = "delete from rezervasyon where rezervasyon_id = ?;";
    private static final String REZERVASYON_SELECT_PNRNO="SELECT * FROM rezervasyon where pnrNo=? and yolcu_soyad=?;";
    private static final String REZERVASYON_SELECT_UCUS_ID="select DISTINCT k.ucak_ad, u.ucus_saat, u.ucus_tarih, u.ucus_sure, yolcu_ad, yolcu_soyad, yolcu_email, yolcu_tc, yolcu_tip, a.havaalani_sehir_ad AS kalkis_sehir, s.havaalani_ad as kalkis_ad, s.havaalani_kod as kalkis_kod, b.havaalani_sehir_ad as varis_sehir, p.havaalani_ad as varis_ad, p.havaalani_kod as varis_kod, f.firma_ad, f.firma_logo from rezervasyon JOIN havaalani JOIN havaalani_sehir JOIN ucus JOIN firma JOIN ucak\n" +
                                    "INNER JOIN  ucus u ON (rezervasyon.ucus_id = ucus.ucus_id)\n" +
                                    "INNER JOIN  firma f ON (f.firma_id = u.firma_id)\n" +
                                    "INNER JOIN  ucak k ON (k.ucak_id = u.ucak_id)\n" +
                                    "INNER JOIN  havaalani s ON (u.ucus_kalkis_id = s.havaalani_id)\n" +
                                    "INNER JOIN  havaalani p ON (u.ucus_varis_id = p.havaalani_id)\n" +
                                    "INNER JOIN  havaalani_sehir a ON (s.havaalani_sehir_id = a.havaalani_sehir_id )\n" +
                                    "INNER JOIN  havaalani_sehir b ON (p.havaalani_sehir_id = b.havaalani_sehir_id)\n" +
                                    "WHERE u.ucus_id=? and rezervasyon.rezervasyon_id=?;";
    private static final String SELECT_UCUS_BILGILERI = "select distinct ucus_id,(ucak.ucak_koltuk-(SELECT COUNT(ucus_id) FROM rezervasyon WHERE ucus_id=ucus.ucus_id )) as bos_koltuk, a.havaalani_sehir_ad as kalkis_sehir, b.havaalani_sehir_ad as varis_sehir ,s.havaalani_ad as kalkis_ad,s.havaalani_kod as kalkis_kod, p.havaalani_ad as varis_ad, p.havaalani_kod as varis_kod, ucus_tarih, ucus_saat, ucus_sure, firma.firma_ad,firma.firma_logo , ucak.ucak_ad, ucak.ucak_koltuk, ucus_ucret from ucus JOIN havaalani JOIN havaalani_sehir\n" +
                                    "INNER JOIN  ucak ON (ucak.ucak_id = ucus.ucak_id)\n" +
                                    "INNER JOIN  firma ON (firma.firma_id = ucus.firma_id)\n" +
                                    "INNER JOIN  havaalani s ON (s.havaalani_id = ucus.ucus_kalkis_id)\n" +
                                    "INNER JOIN  havaalani p ON (p.havaalani_id = ucus.ucus_varis_id)\n" +
                                    "INNER JOIN  havaalani_sehir a ON (a.havaalani_sehir_id = s.havaalani_sehir_id)\n" +
                                    "INNER JOIN  havaalani_sehir b ON (b.havaalani_sehir_id = p.havaalani_sehir_id)\n" +
                                    "WHERE ucus_id=?;";
    private static final String KOLTUK_BILGI_SELECT="SELECT koltuk_no FROM rezervasyon \n" +
                                                "WHERE ucus_id=?\n" +
                                                "ORDER BY koltuk_no ASC;";  
    private static final String KOLTUK_DOLU_SELECT="SELECT COUNT(koltuk_no) as koltuk_dolu FROM rezervasyon \n" +
                                                "WHERE ucus_id=?;";
    private static final String REZERVASYON_INSERT ="INSERT INTO rezervasyon (ucus_id, kullanici_id, pnrNo, yolcu_ad, yolcu_soyad, yolcu_email, yolcu_tel, yolcu_tc, yolcu_tip, yolcu_tarih, yolcu_ucret, koltuk_no) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";   
    private static final String KOLTUK_NO_SELECT="SELECT * FROM rezervasyon WHERE ucus_id=? and koltuk_no=?;";
    private static final String REZERVASYON_ISLEMLERIM_SELECT="SELECT r.rezervasyon_id,r.durum ,r.rezervasyon_tarih, r.pnrNo, r.yolcu_ad, r.yolcu_soyad, r.yolcu_email, r.yolcu_tel, r.yolcu_tc, r.yolcu_tip, r.yolcu_tarih, r.yolcu_ucret, r.koltuk_no, u.ucus_tarih, u.ucus_saat, u.ucus_sure, k.havaalani_ad as kalkis_ad, k.havaalani_kod as kalkis_kod, v.havaalani_ad as varis_ad, v.havaalani_kod as varis_kod, s1.havaalani_sehir_ad as kalkis_sehir, s2.havaalani_sehir_ad as varis_sehir, f.firma_ad, f.firma_logo, p.ucak_ad from rezervasyon AS r\n" +
                                                "JOIN ucus AS u ON u.ucus_id = r.ucus_id\n" +
                                                "JOIN havaalani AS k ON k.havaalani_id=u.ucus_kalkis_id \n" +
                                                "JOIN havaalani AS v ON v.havaalani_id=u.ucus_varis_id\n" +
                                                "JOIN havaalani_sehir AS s1 ON s1.havaalani_sehir_id=k.havaalani_sehir_id\n" +
                                                "JOIN havaalani_sehir AS s2 ON s2.havaalani_sehir_id=v.havaalani_sehir_id\n" +
                                                "JOIN firma AS f ON f.firma_id=u.firma_id\n" +
                                                "JOIN ucak AS p ON p.ucak_id=u.ucak_id\n" +
                                                "WHERE r.kullanici_id=?\n"+
                                                "ORDER BY r.rezervasyon_tarih DESC;";
    private static final String IPTAL_DURUM1="update rezervasyon r\n" +
                                                "join ucus u on r.ucus_id = u.ucus_id\n" +
                                                "set r.durum = '1'\n" +
                                                "WHERE (r.kullanici_id=? and u.ucus_tarih > ?) OR (u.ucus_tarih = ? and u.ucus_saat > ?);";
    private static final String IPTAL_DURUM0="update rezervasyon r\n" +
                                                "join ucus u on r.ucus_id = u.ucus_id\n" +
                                                "set r.durum = '0'\n" +
                                                "WHERE (r.kullanici_id=? and u.ucus_tarih < ?) OR (u.ucus_tarih = ? and u.ucus_saat < ?);";
    private static final String REZERVASYON_UPDATE = "update rezervasyon set yolcu_ad = ?, yolcu_soyad=?, yolcu_tc=?, yolcu_tarih=?, yolcu_email=?, yolcu_tel=? where rezervasyon_id = ?;";
    
    private static final String RZERVASYON_INCELE="SELECT r.rezervasyon_id,r.durum ,r.rezervasyon_tarih, r.pnrNo, r.yolcu_ad, r.yolcu_soyad, r.yolcu_email, r.yolcu_tel, r.yolcu_tc, r.yolcu_tip, r.yolcu_tarih, r.yolcu_ucret, r.koltuk_no, u.ucus_tarih, u.ucus_saat, u.ucus_sure, k.havaalani_ad as kalkis_ad, k.havaalani_kod as kalkis_kod, v.havaalani_ad as varis_ad, v.havaalani_kod as varis_kod, s1.havaalani_sehir_ad as kalkis_sehir, s2.havaalani_sehir_ad as varis_sehir, f.firma_ad, f.firma_logo, p.ucak_ad from rezervasyon AS r\n" +
                                                "JOIN ucus AS u ON u.ucus_id = r.ucus_id\n" +
                                                "JOIN havaalani AS k ON k.havaalani_id=u.ucus_kalkis_id\n" +
                                                "JOIN havaalani AS v ON v.havaalani_id=u.ucus_varis_id\n" +
                                                "JOIN havaalani_sehir AS s1 ON s1.havaalani_sehir_id=k.havaalani_sehir_id\n" +
                                                "JOIN havaalani_sehir AS s2 ON s2.havaalani_sehir_id=v.havaalani_sehir_id\n" +
                                                "JOIN firma AS f ON f.firma_id=u.firma_id\n" +
                                                "JOIN ucak AS p ON p.ucak_id=u.ucak_id\n" +
                                                "ORDER BY r.rezervasyon_tarih DESC;";
    
    public RezervasyonDAO() {
        // Este constructor está vacío porque aún no se ha definido la lógica de inicialización necesaria.
        // En futuras implementaciones, se podría incluir la configuración inicial de recursos.
    }
    
    protected Connection getConnection() {
        Connection connection = null;
         
        try {
            connection = DriverManager.getConnection(jdbcURL,jdbcKullaniciname,jdbcPassword);
           
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cargar el archivo de configuración", ex);
        } 
        return connection;
    }
    
    public boolean iptaldurum1(int id) throws SQLException {
        boolean guncellenenSatir;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);  
        DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        LocalDateTime now = LocalDateTime.now(); 
        String tarih = now.format(formatter);
        String saat = now.format(timeformatter);
        
        String[] arraySaat = saat.split(":");
        String h = arraySaat[0];
        String m = arraySaat[1];
        int hh = Integer.parseInt(h);
        int mm = Integer.parseInt(m);
        String sdakika;
        if (mm < 10) {
            sdakika = "0" + String.valueOf(mm);
        } else {
            sdakika = String.valueOf(mm);
        }
        String ssaat;
        if (hh < 10) {
            ssaat = "0" + String.valueOf(hh + 2);
        } else {
            ssaat = String.valueOf(hh + 2);
        }
        String ucusSaat = ssaat + ":" + sdakika;            
        try (Connection connection = getConnection(); 
            PreparedStatement statement = connection.prepareStatement(IPTAL_DURUM1);) {
            statement.setInt(1, id);
            statement.setString(2, tarih);
            statement.setString(3, tarih);
            statement.setString(4, ucusSaat);
            guncellenenSatir = statement.executeUpdate() > 0;
        }
        return guncellenenSatir;
    }
    
    public boolean iptaldurum0(int id) throws SQLException {
        boolean guncellenenSatir;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);  
        DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        LocalDateTime now = LocalDateTime.now(); 
        String tarih = now.format(formatter);
        String saat = now.format(timeformatter);
        
        String[] arraySaat = saat.split(":");
        String h = arraySaat[0];
        String m = arraySaat[1];
        int hh = Integer.parseInt(h);
        int mm = Integer.parseInt(m);
        String sdakika;
        if (mm < 10) {
            sdakika = "0" + String.valueOf(mm);
        } else {
            sdakika = String.valueOf(mm);
        }
        String ssaat;
        if (hh < 10) {
            ssaat = "0" + String.valueOf(hh + 2);
        } else {
            ssaat = String.valueOf(hh + 2);
        }
        String ucusSaat = ssaat + ":" + sdakika;            
        try (Connection connection = getConnection(); 
            PreparedStatement statement = connection.prepareStatement(IPTAL_DURUM0);) {
            statement.setInt(1, id);
            statement.setString(2, tarih);
            statement.setString(3, tarih);
            statement.setString(4, ucusSaat);
            guncellenenSatir = statement.executeUpdate() > 0;
        }
        return guncellenenSatir;
    }
    
    public boolean koltukkontrol(int id, String koltukNo) {

        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(KOLTUK_NO_SELECT);) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, koltukNo);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                return true;
            }
            
        } catch (SQLException e) {
            printSQLException(e);
        }
        
        return false;
    }
    
    public void rezervasyonekle(Rezervasyon rez) throws SQLException {  
        try (           
            Connection connection = getConnection();                                
            PreparedStatement preparedStatement = connection.prepareStatement(REZERVASYON_INSERT)) {
            preparedStatement.setInt(1, rez.getUcus_id());
            preparedStatement.setInt(2, rez.getKullanici_id());
            preparedStatement.setString(3, rez.getpnrNo());
            preparedStatement.setString(4, rez.getYolcu_ad());
            preparedStatement.setString(5, rez.getYolcu_soyad());
            preparedStatement.setString(6, rez.getYolcu_email());
            preparedStatement.setString(7, rez.getYolcu_tel());
            preparedStatement.setString(8, rez.getYolcu_tc());
            preparedStatement.setInt(9, rez.getYolcu_tip());
            preparedStatement.setString(10, rez.getYolcu_tarih());
            preparedStatement.setDouble(11, rez.getYolcu_ucret());
            preparedStatement.setString(12, rez.getKoltuk_no());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    } 
    
    public List<Rezervasyon> rezervasyonislem(int id) {
        List<Rezervasyon> rez = new ArrayList<> ();
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(REZERVASYON_ISLEMLERIM_SELECT);) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                int durum = rs.getInt("durum");
                String pnrNo = rs.getString("pnrNo");
                String yolcuAd = rs.getString(YOLCU_AD_COLUMN);
                String yolcuSoyad = rs.getString("yolcu_soyad");
                String yolcuEmail = rs.getString(YOLCU_EMAIL_COLUMN );
                String yolcuTel = rs.getString(YOLCU_TEL_COLUMN);
                String yolcuTc = rs.getString(YOLCU_TC_COLUMN);
                int yolcuTip = rs.getInt(YOLCU_TIP_COLUMN);
                String yolcu_tarih = rs.getString("yolcu_tarih");
                Double yolcu_ucret=rs.getDouble("yolcu_ucret"); 
                String koltuk_no = rs.getString(KOLTUK_NO_COLUMN);
                String ucus_saat=rs.getString(UCUS_SAAT_COLUMN);
                ucus_saat=ucus_saat.substring(0, 5);
                String ucus_tarih=rs.getString(UCUS_TARIH_COLUMN);
                String ucus_sure=rs.getString(UCUS_SURE_COLUMN);
                String[] ARRAYucus_sure = ucus_sure.split(":"); 
                String ucus_s = ARRAYucus_sure[0];
                String ucus_d = ARRAYucus_sure[1];
                String[] ARRAYucus_saat = ucus_saat.split(":");
                String s = ARRAYucus_saat[0];
                String d = ARRAYucus_saat[1];
                int saat=(Integer.parseInt(s)+Integer.parseInt(ucus_s))%24 ;
                int dakika=(Integer.parseInt(d)+Integer.parseInt(ucus_d))%60 ;
                String sdakika;
                if(dakika < 10){
                    sdakika="0"+String.valueOf(dakika);
                }else{                                    
                    sdakika=String.valueOf(dakika);
                }
                String ssaat;
                if(saat < 10){
                    ssaat="0"+String.valueOf(saat);
                }else{                                    
                    ssaat=String.valueOf(saat);
                }
                String varis_saat = ssaat+":"+sdakika;              
                int rezervasyon_id = rs.getInt(REZERVASYON_ID_COLUMN);
                String kalkis_sehir=rs.getString(KALKIS_SEHIR_COLUMN);
                String kalkis_ad=rs.getString(KALKIS_AD_COLUMN);
                String kalkis_kod=rs.getString(KALKIS_KOD_COLUMN);
                String varis_sehir=rs.getString(VARIS_SEHIR_COLUMN);
                String varis_ad=rs.getString(VARIS_AD_COLUMN);
                String varis_kod=rs.getString(VARIS_KOD_COLUMN);
                String firma_ad=rs.getString(FIRMA_AD_COLUMN);
                String firma_logo=rs.getString(FIRMA_LOGO_COLUMN);
                String ucak_ad=rs.getString(UCAK_AD_COLUMN);
                String rezervasyon_tarih=rs.getString(REZERVASYON_TARIH_COLUMN);          
                rez.add(new Rezervasyon(durum, rezervasyon_id, rezervasyon_tarih, pnrNo,yolcuAd, yolcuSoyad,yolcuEmail, yolcuTel, yolcuTc, yolcuTip, koltuk_no, ucus_tarih, kalkis_sehir, kalkis_ad, kalkis_kod, varis_sehir, varis_ad, varis_kod, ucus_saat, ucus_sure, firma_ad, firma_logo, ucus_s, ucus_d, varis_saat,ucak_ad, yolcu_tarih, yolcu_ucret));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rez;
    }
    
    public boolean rezervasyonguncelle(Rezervasyon rez) throws SQLException {
        boolean guncellenenSatir;
        try (Connection connection = getConnection(); 
            PreparedStatement preparedStatement = connection.prepareStatement(REZERVASYON_UPDATE);) {
            preparedStatement.setString(1, rez.getYolcu_ad());
            preparedStatement.setString(2, rez.getYolcu_soyad());
            preparedStatement.setString(3, rez.getYolcu_tc());
            preparedStatement.setString(4, rez.getYolcu_tarih());
            preparedStatement.setString(5, rez.getYolcu_email());
            preparedStatement.setString(6, rez.getYolcu_tel());
            preparedStatement.setInt(7, rez.getRezervasyon_id());
            guncellenenSatir = preparedStatement.executeUpdate() > 0;
        }
        return guncellenenSatir;
    } 
    
    public List<Rezervasyon> tekyonsorgulama(Rezervasyon rezervasyon) {
        List<Rezervasyon> rez = new ArrayList<>();
    
        String currentFormattedTime = getCurrentFormattedTime();
        String u_saat = calculateNextHour(currentFormattedTime);
        String currentDate = getCurrentDate();
    
        String query = rezervasyon.getUcus_tarih().equals(currentDate)
                                   ? TEKYON_SORGULAMA_SELECT2 : TEKYON_SORGULAMA_SELECT1;
        executeQueryAndPopulateResults(rez, rezervasyon, u_saat, query);
    
        return rez;
    }
    
    private String getCurrentFormattedTime() {
        DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        return LocalDateTime.now().format(timeformatter);
    }
    
    private String calculateNextHour(String formattedTime) {
        String[] timeParts = formattedTime.split(":");
        int hh = Integer.parseInt(timeParts[0]);
        int mm = Integer.parseInt(timeParts[1]);
    
        String formattedMinute = String.format("%02d", mm);
        String formattedHour = String.format("%02d", (hh + 1) % 24);
    
        return formattedHour + ":" + formattedMinute;
    }
    
    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return LocalDate.now().format(formatter);
    }
    
    private void executeQueryAndPopulateResults(List<Rezervasyon> rez, Rezervasyon rezervasyon, String u_saat, String query) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setInt(1, rezervasyon.getHavaalani_kalkis_id());
            statement.setInt(2, rezervasyon.getHavaalani_varis_id());
            statement.setString(3, rezervasyon.getUcus_tarih());
            if (query.equals(TEKYON_SORGULAMA_SELECT2)) {
                statement.setString(4, u_saat);
                statement.setInt(5, rezervasyon.getCocuk_sayi() + rezervasyon.getYetiskin_sayi());
            } else {
                statement.setInt(4, rezervasyon.getCocuk_sayi() + rezervasyon.getYetiskin_sayi());
            }
    
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Rezervasyon r = extractRezervasyonFromResultSet(rs);
                rez.add(r);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    
    private Rezervasyon extractRezervasyonFromResultSet(ResultSet rs) throws SQLException {
        int ucus_id = rs.getInt(UCUS_ID_COLUMN);
        String kalkis_sehir = rs.getString(KALKIS_SEHIR_COLUMN);
        String kalkis_ad = rs.getString(KALKIS_AD_COLUMN);
        String kalkis_kod = rs.getString(KALKIS_KOD_COLUMN);
        String varis_sehir = rs.getString(VARIS_SEHIR_COLUMN);
        String varis_ad = rs.getString(VARIS_AD_COLUMN);
        String varis_kod = rs.getString(VARIS_KOD_COLUMN);
        String ucusSaat = rs.getString(UCUS_SAAT_COLUMN).substring(0, 5);
        String ucus_tarih = rs.getString(UCUS_TARIH_COLUMN);
        String ucus_sure = rs.getString(UCUS_SURE_COLUMN);
    
        
    
        String firma_ad = rs.getString(FIRMA_AD_COLUMN);
        String firma_logo = rs.getString(FIRMA_LOGO_COLUMN);
        Double ucus_ucret = rs.getDouble(UCUS_UCRET_COLUMN);
    
        
    
        return new Rezervasyon(ucus_id, ucus_tarih, kalkis_sehir, kalkis_ad, kalkis_kod, varis_sehir, varis_ad, varis_kod, ucusSaat, ucus_sure, firma_ad, firma_logo, ucus_ucret);
    }
    
    public Rezervasyon ucusbilgileri(int id) {
        Rezervasyon rez=null;
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_UCUS_BILGILERI);) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                int ucus_id = rs.getInt(UCUS_ID_COLUMN);
                String kalkis_sehir=rs.getString(KALKIS_SEHIR_COLUMN);
                String kalkis_ad=rs.getString(KALKIS_AD_COLUMN);
                String kalkis_kod=rs.getString(KALKIS_KOD_COLUMN);
                String varis_sehir=rs.getString(VARIS_SEHIR_COLUMN);
                String varis_ad=rs.getString(VARIS_AD_COLUMN);
                String varis_kod=rs.getString(VARIS_KOD_COLUMN);
                String ucusSaat=rs.getString(UCUS_SAAT_COLUMN);
                ucusSaat=ucusSaat.substring(0, 5);
                String ucus_tarih=rs.getString(UCUS_TARIH_COLUMN);
                String ucus_sure=rs.getString(UCUS_SURE_COLUMN);
                
                String[] ARRAYucus_sure = ucus_sure.split(":"); 
                String ucus_s = ARRAYucus_sure[0];
                String ucus_d = ARRAYucus_sure[1];
                String[] ARRAYucus_saat = ucusSaat.split(":");
                String s = ARRAYucus_saat[0];
                String d = ARRAYucus_saat[1];
                int saat=(Integer.parseInt(s)+Integer.parseInt(ucus_s))%24 ;
                int dakika=(Integer.parseInt(d)+Integer.parseInt(ucus_d))%60 ;
                String sdakika;
                if(dakika < 10){
                    sdakika="0"+String.valueOf(dakika);
                }else{                                    
                    sdakika=String.valueOf(dakika);
                }
                String ssaat;
                if(saat < 10){
                    ssaat="0"+String.valueOf(saat);
                }else{                                    
                    ssaat=String.valueOf(saat);
                }
                String varis_saat = ssaat+":"+sdakika;
                String firma_ad=rs.getString(FIRMA_AD_COLUMN);
                String firma_logo=rs.getString(FIRMA_LOGO_COLUMN);
                Double ucus_ucret=rs.getDouble(UCUS_UCRET_COLUMN);
                String ucak_ad=rs.getString(UCAK_AD_COLUMN);
                int ucak_koltuk = rs.getInt("ucak_koltuk");
                rez = new Rezervasyon(ucus_tarih,ucus_id, kalkis_sehir,kalkis_ad,kalkis_kod,varis_sehir,varis_ad,varis_kod,ucus_saat,ucus_sure,firma_ad,firma_logo,ucus_ucret, ucus_s, ucus_d, varis_saat,ucak_ad,ucak_koltuk);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rez;
    } 
    
    public Rezervasyon rezervasyonsec(String pnrNo, String yolcuSoyad) {
        Rezervasyon rezervasyon = null;
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(REZERVASYON_SELECT_PNRNO);) {
            preparedStatement.setString(1, pnrNo);
            preparedStatement.setString(2, yolcuSoyad);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int rezervasyon_id = rs.getInt(REZERVASYON_ID_COLUMN);
                String rezervasyon_tarih = rs.getString(REZERVASYON_TARIH_COLUMN);
                int ucus_id = rs.getInt(UCUS_ID_COLUMN);
                int kullanici_id = rs.getInt("kullanici_id"); 
                String yolcuAd = rs.getString(YOLCU_AD_COLUMN);
                String yolcuEmail = rs.getString(YOLCU_EMAIL_COLUMN );
                String yolcuTel = rs.getString(YOLCU_TEL_COLUMN);
                String yolcuTc = rs.getString(YOLCU_TC_COLUMN);
                int yolcuTip = rs.getInt(YOLCU_TIP_COLUMN); 
                String koltuk_no = rs.getString(KOLTUK_NO_COLUMN);
                rezervasyon = new Rezervasyon(rezervasyon_id, rezervasyon_tarih,pnrNo,yolcuAd, yolcuSoyad,yolcuEmail,yolcuTel,yolcuTc,yolcuTip,koltuk_no,kullanici_id, ucus_id  );
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rezervasyon;
    }
    
    public Rezervasyon rezervasyonbilgi(int ucus_id, int rezervasyon_id) {
        Rezervasyon rezervasyon = null;
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(REZERVASYON_SELECT_UCUS_ID);) {
            preparedStatement.setInt(1, ucus_id);
            preparedStatement.setInt(2, rezervasyon_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String ucusSaat = rs.getString(UCUS_SAAT_COLUMN);
                ucusSaat=ucusSaat.substring(0, 5);
                String ucus_sure = rs.getString(UCUS_SURE_COLUMN); 
                String[] ARRAYucus_sure = ucus_sure.split(":"); 
                String ucus_s = ARRAYucus_sure[0];
                String ucus_d = ARRAYucus_sure[1];
                String ucus_tarih = rs.getString(UCUS_TARIH_COLUMN);
                String ucak_ad = rs.getString(UCAK_AD_COLUMN);
                String firma_ad = rs.getString(FIRMA_AD_COLUMN);
                String firma_logo = rs.getString(FIRMA_LOGO_COLUMN); 
                String kalkis_sehir = rs.getString(KALKIS_SEHIR_COLUMN);
                String kalkis_ad = rs.getString(KALKIS_AD_COLUMN);
                String kalkis_kod = rs.getString(KALKIS_KOD_COLUMN);
                String varis_sehir = rs.getString(VARIS_SEHIR_COLUMN);
                String varis_ad = rs.getString(VARIS_AD_COLUMN);
                String varis_kod = rs.getString(VARIS_KOD_COLUMN);
                
                rezervasyon = new Rezervasyon(ucus_tarih, kalkis_sehir, kalkis_ad, kalkis_kod, varis_sehir, varis_ad, varis_kod, ucusSaat, firma_ad, firma_logo, ucus_s, ucus_d, ucak_ad);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rezervasyon;
    }
    
    public boolean rezervasyoniptal(int id) throws SQLException {
        boolean silinenSatir;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(REZERVASYON_DELETE);) {
            statement.setInt(1, id);
            silinenSatir = statement.executeUpdate() > 0;
        }
        return silinenSatir;
    }
    
    public List<Rezervasyon> rezervasyonsayisi() {
        List<Rezervasyon> rezervasyon = new ArrayList<> ();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);  
        LocalDate months = LocalDate.now().minusMonths(1);
        String date1 = months.format(formatter);
        LocalDateTime now = LocalDateTime.now().plusDays(1); 
        String date2 = now.format(formatter);
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(REZERVASYON_SELECT_COUNT);) {
            statement.setString(1, date1);
            statement.setString(2, date2);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int sonuc = rs.getInt(SONUC_COLUMN);               
                rezervasyon.add(new Rezervasyon(sonuc));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rezervasyon;
    }
    
    public List<Rezervasyon> ucussayisi() {
        List<Rezervasyon> rezervasyon = new ArrayList<> ();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);  
        LocalDateTime now = LocalDateTime.now(); 
        String date1 = now.format(formatter);
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(UCUS_SELECT_COUNT);) {
            statement.setString(1, date1);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int sonuc = rs.getInt(SONUC_COLUMN);               
                rezervasyon.add(new Rezervasyon(sonuc));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rezervasyon;
    }
    
    public List<Rezervasyon>mesajsayisi() {
        List<Rezervasyon> rezervasyon = new ArrayList<> ();
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(MESAJ_SELECT_COUNT);) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int sonuc = rs.getInt(SONUC_COLUMN);               
                rezervasyon.add(new Rezervasyon(sonuc));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rezervasyon;
    }
    
    public List<Rezervasyon> rezervasyonlistele() {
        List<Rezervasyon> rez = new ArrayList<> ();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(RZERVASYON_INCELE);) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int durum = rs.getInt("durum");
                String pnrNo = rs.getString("pnrNo");
                String yolcuAd = rs.getString(YOLCU_AD_COLUMN);
                String yolcuSoyad = rs.getString("yolcu_soyad");
                String yolcuEmail = rs.getString(YOLCU_EMAIL_COLUMN );
                String yolcuTel = rs.getString(YOLCU_TEL_COLUMN);
                String yolcuTc = rs.getString(YOLCU_TC_COLUMN);
                int yolcuTip = rs.getInt(YOLCU_TIP_COLUMN);
                String yolcu_tarih = rs.getString("yolcu_tarih");
                Double yolcu_ucret=rs.getDouble("yolcu_ucret"); 
                String koltuk_no = rs.getString(KOLTUK_NO_COLUMN);
                String ucusSaat=rs.getString(UCUS_SAAT_COLUMN);
                ucusSaat=ucusSaat.substring(0, 5);
                String ucus_tarih=rs.getString(UCUS_TARIH_COLUMN);
                String ucus_sure=rs.getString(UCUS_SURE_COLUMN);
                String[] ARRAYucus_sure = ucus_sure.split(":"); 
                String ucus_s = ARRAYucus_sure[0];
                String ucus_d = ARRAYucus_sure[1];
                String[] ARRAYucus_saat = ucus_saat.split(":");
                String s = ARRAYucus_saat[0];
                String d = ARRAYucus_saat[1];
                int saat=(Integer.parseInt(s)+Integer.parseInt(ucus_s))%24 ;
                int dakika=(Integer.parseInt(d)+Integer.parseInt(ucus_d))%60 ;
                String sdakika;
                if(dakika < 10){
                    sdakika="0"+String.valueOf(dakika);
                }else{                                    
                    sdakika=String.valueOf(dakika);
                }
                String ssaat;
                if(saat < 10){
                    ssaat="0"+String.valueOf(saat);
                }else{                                    
                    ssaat=String.valueOf(saat);
                }
                String varis_saat = ssaat+":"+sdakika;              
                int rezervasyon_id = rs.getInt(REZERVASYON_ID_COLUMN);
                String kalkis_sehir=rs.getString(KALKIS_SEHIR_COLUMN);
                String kalkis_ad=rs.getString(KALKIS_AD_COLUMN);
                String kalkis_kod=rs.getString(KALKIS_KOD_COLUMN);
                String varis_sehir=rs.getString(VARIS_SEHIR_COLUMN);
                String varis_ad=rs.getString(VARIS_AD_COLUMN);
                String varis_kod=rs.getString(VARIS_KOD_COLUMN);
                String firma_ad=rs.getString(FIRMA_AD_COLUMN);
                String firma_logo=rs.getString(FIRMA_LOGO_COLUMN);
                String ucak_ad=rs.getString(UCAK_AD_COLUMN);
                String rezervasyon_tarih=rs.getString(REZERVASYON_TARIH_COLUMN);          
                rez.add(new Rezervasyon(durum, rezervasyon_id, rezervasyon_tarih, pnrNo,yolcuAd, yolcuSoyad,yolcuEmail, yolcuTel, yolcuTc, yolcuTip, koltuk_no, ucus_tarih, kalkis_sehir, kalkis_ad, kalkis_kod, varis_sehir, varis_ad, varis_kod, ucusSaat, ucus_sure, firma_ad, firma_logo, ucus_s, ucus_d, varis_saat,ucak_ad, yolcu_tarih, yolcu_ucret));
            }
        } catch (SQLException e) {
            printSQLException(e);
        } return rez;
    }  
    
    public List<Rezervasyon> koltukbilgi(int id) {
        List<Rezervasyon> rezervasyonlar = new ArrayList<> ();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(KOLTUK_BILGI_SELECT);) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int koltukNo = rs.getInt(KOLTUK_NO_COLUMN);
                rezervasyonlar.add(new Rezervasyon(koltukNo));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rezervasyonlar;
    }
    
    public Rezervasyon dolukoltuk(int id) {
        Rezervasyon rezervasyonlar = new Rezervasyon();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(KOLTUK_DOLU_SELECT);) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int koltuk_dolu = rs.getInt("koltuk_dolu");
                rezervasyonlar = new Rezervasyon(koltuk_dolu);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rezervasyonlar;
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
