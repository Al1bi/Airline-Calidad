package model;

public class Mesaj {
    private int mesaj_id;
    private String mesaj_adsoyad;
    private String mesaj_email;
    private String mesaj_konu;
    private String mesaj_icerik;
    private String mesaj_tarih;
    private int mesaj_okunma;
    private int mesaj_cevap;

    private Mesaj(Builder builder) {
        this.mesaj_id = builder.mesaj_id;
        this.mesaj_adsoyad = builder.mesaj_adsoyad;
        this.mesaj_email = builder.mesaj_email;
        this.mesaj_konu = builder.mesaj_konu;
        this.mesaj_icerik = builder.mesaj_icerik;
        this.mesaj_tarih = builder.mesaj_tarih;
        this.mesaj_okunma = builder.mesaj_okunma;
        this.mesaj_cevap = builder.mesaj_cevap;
    }

    public static class Builder {
        private int mesaj_id;
        private String mesaj_adsoyad;
        private String mesaj_email;
        private String mesaj_konu;
        private String mesaj_icerik;
        private String mesaj_tarih;
        private int mesaj_okunma;
        private int mesaj_cevap;

        public Builder(int mesaj_id, String mesaj_adsoyad) {
            this.mesaj_id = mesaj_id;
            this.mesaj_adsoyad = mesaj_adsoyad;
        }

        public Builder mesaj_email(String mesaj_email) {
            this.mesaj_email = mesaj_email;
            return this;
        }

        public Builder mesaj_konu(String mesaj_konu) {
            this.mesaj_konu = mesaj_konu;
            return this;
        }

        public Builder mesaj_icerik(String mesaj_icerik) {
            this.mesaj_icerik = mesaj_icerik;
            return this;
        }

        public Builder mesaj_tarih(String mesaj_tarih) {
            this.mesaj_tarih = mesaj_tarih;
            return this;
        }

        public Builder mesaj_okunma(int mesaj_okunma) {
            this.mesaj_okunma = mesaj_okunma;
            return this;
        }

        public Builder mesaj_cevap(int mesaj_cevap) {
            this.mesaj_cevap = mesaj_cevap;
            return this;
        }

        public Mesaj build() {
            return new Mesaj(this);
        }
    }

    // Getters
    public int getMesaj_id() {
        return mesaj_id;
    }

    public String getMesaj_adsoyad() {
        return mesaj_adsoyad;
    }

    public String getMesaj_email() {
        return mesaj_email;
    }

    public String getMesaj_konu() {
        return mesaj_konu;
    }

    public String getMesaj_icerik() {
        return mesaj_icerik;
    }

    public String getMesaj_tarih() {
        return mesaj_tarih;
    }

    public int getMesaj_okunma() {
        return mesaj_okunma;
    }

    public int getMesaj_cevap() {
        return mesaj_cevap;
    }
}
