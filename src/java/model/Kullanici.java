package model;

public class Kullanici {

    protected int kullaniciid;
    protected String kullaniciad;
    protected String kullanicisoyad;
    protected String kullaniciemail;
    protected String kullanicisifre;
    protected int kullaniciyetki;

    public Kullanici() {
    }

    public Kullanici(int kullaniciid, String kullanicisifre) {
        this.kullaniciid = kullaniciid;
        this.kullanicisifre = kullanicisifre;
    }

    public Kullanici(String kullaniciemail, String kullanicisifre) {
        this.kullaniciemail = kullaniciemail;
        this.kullanicisifre = kullanicisifre;
    }

    public Kullanici(int kullaniciid, String kullaniciad, String kullanicisoyad, String kullaniciemail, String kullanicisifre) {
        this.kullaniciid = kullaniciid;
        this.kullaniciad = kullaniciad;
        this.kullanicisoyad = kullanicisoyad;
        this.kullaniciemail = kullaniciemail;
        this.kullanicisifre = kullanicisifre;
    }

    public Kullanici(String kullaniciad, String kullanicisoyad, String kullaniciemail, int kullaniciyetki) {
        this.kullaniciad = kullaniciad;
        this.kullanicisoyad = kullanicisoyad;
        this.kullaniciemail = kullaniciemail;
        this.kullaniciyetki = kullaniciyetki;
    }

    public Kullanici(String kullaniciad, String kullanicisoyad, String kullaniciemail, String kullanicisifre, int kullaniciyetki) {
        this.kullaniciad = kullaniciad;
        this.kullanicisoyad = kullanicisoyad;
        this.kullaniciemail = kullaniciemail;
        this.kullanicisifre = kullanicisifre;
        this.kullaniciyetki = kullaniciyetki;
    }

    public Kullanici(int kullaniciid, String kullaniciad, String kullanicisoyad, String kullaniciemail, int kullaniciyetki) {
        this.kullaniciid = kullaniciid;
        this.kullaniciad = kullaniciad;
        this.kullanicisoyad = kullanicisoyad;
        this.kullaniciemail = kullaniciemail;
        this.kullaniciyetki = kullaniciyetki;
    }

    public Kullanici(int kullaniciid, String kullaniciad, String kullanicisoyad, String kullaniciemail, String kullanicisifre, int kullaniciyetki) {
        this.kullaniciid = kullaniciid;
        this.kullaniciad = kullaniciad;
        this.kullanicisoyad = kullanicisoyad;
        this.kullaniciemail = kullaniciemail;
        this.kullanicisifre = kullanicisifre;
        this.kullaniciyetki = kullaniciyetki;
    }

    public Kullanici(String kullaniciad, String kullanicisoyad, String kullaniciemail, String kullanicisifre) {
        this.kullaniciad = kullaniciad;
        this.kullanicisoyad = kullanicisoyad;
        this.kullaniciemail = kullaniciemail;
        this.kullanicisifre = kullanicisifre;
    }
    
    public Kullanici(int kullaniciid, String kullaniciad, String kullanicisoyad, String kullaniciemail) {
        this.kullaniciid = kullaniciid;
        this.kullaniciad = kullaniciad;
        this.kullanicisoyad = kullanicisoyad;
        this.kullaniciemail = kullaniciemail;
    }
    
    public int getKullanici_id() {
        return kullaniciid;
    }

    public void setKullanici_id(int kullaniciid) {
        this.kullaniciid = kullaniciid;
    }

    public String getKullanici_ad() {
        return kullaniciad;
    }

    public void setKullanici_ad(String kullaniciad) {
        this.kullaniciad = kullaniciad;
    }

    public String getKullanici_soyad() {
        return kullanicisoyad;
    }

    public void setKullanici_soyad(String kullanicisoyad) {
        this.kullanicisoyad = kullanicisoyad;
    }

    public String getKullanici_email() {
        return kullaniciemail;
    }

    public void setKullanici_email(String kullaniciemail) {
        this.kullaniciemail = kullaniciemail;
    }

    public String getKullanici_sifre() {
        return kullanicisifre;
    }

    public void setKullanici_sifre(String kullanicisifre) {
        this.kullanicisifre = kullanicisifre;
    }

    public int getKullanici_yetki() {
        return kullaniciyetki;
    }

    public void setKullanici_yetki(int kullaniciyetki) {
        this.kullaniciyetki = kullaniciyetki;
    }
    
    
}