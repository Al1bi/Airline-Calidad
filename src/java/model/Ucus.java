package model;

import model.Ucus.UcusDetay;

public class Ucus {
    protected int ucus_id;
    protected int ucus_kalkis_id;
    protected int ucus_varis_id;
    protected String ucus_tarih;
    protected String ucus_saat;
    protected String ucus_sure;
    protected int firma_id;
    protected int ucak_id;
    protected double ucus_ucret;
    protected String firma_ad;
    protected String ucak_ad;
    protected String ucus_kalkis;
    protected String ucus_varis;

    public Ucus() {
        //I don't know why the method is empty, probably in the future could be implemented
    }

    public class UcusDetay {
        private String ucus_tarih;
        private String ucus_saat;
        private String ucus_sure;

        public UcusDetay(String ucus_tarih, String ucus_saat, String ucus_sure) {
            this.ucus_tarih = ucus_tarih;
            this.ucus_saat = ucus_saat;
            this.ucus_sure = ucus_sure;
        }

        public String getUcus_tarih() {
            return ucus_tarih;
        }
    
        public String getUcus_saat() {
            return ucus_saat;
        }
    
        public String getUcus_sure() {
            return ucus_sure;
        }
    }
    
    public class UcusS {
        private int ucus_id;
        private int ucus_kalkis_id;
        private int ucus_varis_id;
        private double ucus_ucret;
        private int firma_id;
        private int ucak_id;
        private String firma_ad;
        private String ucak_ad;
        private String ucus_kalkis;
        private String ucus_varis;
        private UcusDetay detay;

        public UcusS(int ucus_id, int ucus_kalkis_id, int ucus_varis_id, 
        UcusDetay detay, int firma_id, int ucak_id, double ucus_ucret) {
            this.ucus_id = ucus_id;
            this.ucus_kalkis_id = ucus_kalkis_id;
            this.ucus_varis_id = ucus_varis_id;
            this.detay = detay;
            this.firma_id = firma_id;
            this.ucak_id = ucak_id;
            this.ucus_ucret = ucus_ucret;
        }

        public UcusS(int ucus_id, UcusDetay detay, double ucus_ucret, 
                String firma_ad, String ucak_ad, String ucus_kalkis, String ucus_varis) {
            this.ucus_id = ucus_id;
            this.detay = detay;
            this.ucus_ucret = ucus_ucret;
            this.firma_ad = firma_ad;
            this.ucak_ad = ucak_ad;
            this.ucus_kalkis = ucus_kalkis;
            this.ucus_varis = ucus_varis;
        }
            
        public int getUcus_id() {
            return ucus_id;
        }
    
        public void setUcus_id(int ucus_id) {
            this.ucus_id = ucus_id;
        }
    
        public int getUcus_kalkis_id() {
            return ucus_kalkis_id;
        }
    
        public void setUcus_kalkis_id(int ucus_kalkis_id) {
            this.ucus_kalkis_id = ucus_kalkis_id;
        }
    
        public int getUcus_varis_id() {
            return ucus_varis_id;
        }
    
        public void setUcus_varis_id(int ucus_varis_id) {
            this.ucus_varis_id = ucus_varis_id;
        }
    
        public UcusDetay getDetay() {
            return detay;
        }
    
        public void setDetay(UcusDetay detay) {
            this.detay = detay;
        }
    
        public double getUcus_ucret() {
            return ucus_ucret;
        }
    
        public void setUcus_ucret(double ucus_ucret) {
            this.ucus_ucret = ucus_ucret;
        }
    
        public int getFirma_id() {
            return firma_id;
        }
    
        public void setFirma_id(int firma_id) {
            this.firma_id = firma_id;
        }
    
        public int getUcak_id() {
            return ucak_id;
        }
    
        public void setUcak_id(int ucak_id) {
            this.ucak_id = ucak_id;
        }
    
        public String getFirma_ad() {
            return firma_ad;
        }
    
        public void setFirma_ad(String firma_ad) {
            this.firma_ad = firma_ad;
        }
    
        public String getUcak_ad() {
            return ucak_ad;
        }
    
        public void setUcak_ad(String ucak_ad) {
            this.ucak_ad = ucak_ad;
        }
    
        public String getUcus_kalkis() {
            return ucus_kalkis;
        }
    
        public void setUcus_kalkis(String ucus_kalkis) {
            this.ucus_kalkis = ucus_kalkis;
        }
    
        public String getUcus_varis() {
            return ucus_varis;
        }
    
        public void setUcus_varis(String ucus_varis) {
            this.ucus_varis = ucus_varis;
        }
    }
    
}
