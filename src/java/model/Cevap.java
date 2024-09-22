package model;

public class Cevap {
    int cevap_id;
    int mesaj_id;
    String cevap_icerik;
    String cevap_baslik;
    String cevap_tarih;
    String mesaj_adsoyad;
    String mesaj_email;
    String mesaj_konu;
    String mesaj_icerik;
    String mesaj_tarih;               
    int mesaj_okunma;
    int mesaj_cevap;

    public Cevap() {
        //I don't know why the method is empty, probably in the future could be implemented
    }

    public static class CevapParams {
        private int cevap_id;
        private int mesaj_id;
        private String cevap_icerik;
        private String cevap_baslik;
        private String cevap_tarih;
        private String mesaj_adsoyad;
        private String mesaj_email;
        private String mesaj_konu;
        private String mesaj_icerik;
        private String mesaj_tarih;
    
        private CevapParams(Builder builder) {
            this.cevap_id = builder.cevap_id;
            this.mesaj_id = builder.mesaj_id;
            this.cevap_icerik = builder.cevap_icerik;
            this.cevap_baslik = builder.cevap_baslik;
            this.cevap_tarih = builder.cevap_tarih;
            this.mesaj_adsoyad = builder.mesaj_adsoyad;
            this.mesaj_email = builder.mesaj_email;
            this.mesaj_konu = builder.mesaj_konu;
            this.mesaj_icerik = builder.mesaj_icerik;
            this.mesaj_tarih = builder.mesaj_tarih;
        }
    
        public static class Builder {
            private int cevap_id;
            private int mesaj_id;
            private String cevap_icerik;
            private String cevap_baslik;
            private String cevap_tarih;
            private String mesaj_adsoyad;
            private String mesaj_email;
            private String mesaj_konu;
            private String mesaj_icerik;
            private String mesaj_tarih;
    
            public Builder(int mesaj_id, String cevap_icerik) {
                this.mesaj_id = mesaj_id;
                this.cevap_icerik = cevap_icerik;
            }
    
            public Builder cevap_id(int cevap_id) {
                this.cevap_id = cevap_id;
                return this;
            }
    
            public Builder cevap_baslik(String cevap_baslik) {
                this.cevap_baslik = cevap_baslik;
                return this;
            }
    
            public Builder cevap_tarih(String cevap_tarih) {
                this.cevap_tarih = cevap_tarih;
                return this;
            }
    
            public Builder mesaj_adsoyad(String mesaj_adsoyad) {
                this.mesaj_adsoyad = mesaj_adsoyad;
                return this;
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
    
            public CevapParams build() {
                return new CevapParams(this);
            }
        }

        public int getCevap_id() { return cevap_id; }
        public int getMesaj_id() { return mesaj_id; }
        public String getCevap_icerik() { return cevap_icerik; }
        public String getCevap_baslik() { return cevap_baslik; }
        public String getCevap_tarih() { return cevap_tarih; }
        public String getMesaj_adsoyad() { return mesaj_adsoyad; }
        public String getMesaj_email() { return mesaj_email; }
        public String getMesaj_konu() { return mesaj_konu; }
        public String getMesaj_icerik() { return mesaj_icerik; }
        public String getMesaj_tarih() { return mesaj_tarih; }
    }
}    