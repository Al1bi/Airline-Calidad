package model;
class Yolcu {
    private String nombre;
    private String apellido;
    private String documentoIdentidad;

    public Yolcu(String nombre, String apellido, String documentoIdentidad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documentoIdentidad = documentoIdentidad;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + documentoIdentidad + ")";
    }
}

class Ucus {
    private String origen;
    private String destino;
    private String fechaVuelo;

    public Ucus(String origen, String destino, String fechaVuelo) {
        this.origen = origen;
        this.destino = destino;
        this.fechaVuelo = fechaVuelo;
    }

    @Override
    public String toString() {
        return "Vuelo de " + origen + " a " + destino + " el " + fechaVuelo;
    }
}

class Firma {
    private String nombre;
    private String codigoIATA;

    public Firma(String nombre, String codigoIATA) {
        this.nombre = nombre;
        this.codigoIATA = codigoIATA;
    }

    @Override
    public String toString() {
        return nombre + " (" + codigoIATA + ")";
    }
}

public class Rezervasyon {
    private int rezervasyonId;
    private String rezervasyonTarih;
    private String pnrNo;
    private Yolcu yolcu;
    private String koltukNo;
    private int kullaniciId;
    private Ucus ucus;
    private Firma firma;
    private int yetiskinSayi;
    private int cocukSayi;
    private int durum;
    private int koltukDolu;

    public Rezervasyon(RezervasyonBuilder builder) {
        this.rezervasyonId = builder.rezervasyonId;
        this.rezervasyonTarih = builder.rezervasyonTarih;
        this.pnrNo = builder.pnrNo;
        this.yolcu = builder.yolcu;
        this.koltukNo = builder.koltukNo;
        this.kullaniciId = builder.kullaniciId;
        this.ucus = builder.ucus;
        this.firma = builder.firma;
        this.yetiskinSayi = builder.yetiskinSayi;
        this.cocukSayi = builder.cocukSayi;
        this.durum = builder.durum;
        this.koltukDolu = builder.koltukDolu;
    }

    public int getRezervasyonId() {
        return rezervasyonId;
    }

    public String getRezervasyonTarih() {
        return rezervasyonTarih;
    }

    public String getPnrNo() {
        return pnrNo;
    }

    public Yolcu getYolcu() {
        return yolcu;
    }

    public String getKoltukNo() {
        return koltukNo;
    }

    public int getKullaniciId() {
        return kullaniciId;
    }

    public Ucus getUcus() {
        return ucus;
    }

    public Firma getFirma() {
        return firma;
    }

    public int getYetiskinSayi() {
        return yetiskinSayi;
    }

    public int getCocukSayi() {
        return cocukSayi;
    }

    public int getDurum() {
        return durum;
    }

    public int getKoltukDolu() {
        return koltukDolu;
    }

    public static class RezervasyonBuilder {
        private int rezervasyonId;
        private String rezervasyonTarih;
        private String pnrNo;
        private Yolcu yolcu;
        private String koltukNo;
        private int kullaniciId;
        private Ucus ucus;
        private Firma firma;
        private int yetiskinSayi;
        private int cocukSayi;
        private int durum;
        private int koltukDolu;

        public RezervasyonBuilder rezervasyonId(int rezervasyonId) {
            this.rezervasyonId = rezervasyonId;
            return this;
        }

        public RezervasyonBuilder rezervasyonTarih(String rezervasyonTarih) {
            this.rezervasyonTarih = rezervasyonTarih;
            return this;
        }

        public RezervasyonBuilder pnrNo(String pnrNo) {
            this.pnrNo = pnrNo;
            return this;
        }

        public RezervasyonBuilder yolcu(Yolcu yolcu) {
            this.yolcu = yolcu;
            return this;
        }

        public RezervasyonBuilder koltukNo(String koltukNo) {
            this.koltukNo = koltukNo;
            return this;
        }

        public RezervasyonBuilder kullaniciId(int kullaniciId) {
            this.kullaniciId = kullaniciId;
            return this;
        }

        public RezervasyonBuilder ucus(Ucus ucus) {
            this.ucus = ucus;
            return this;
        }

        public RezervasyonBuilder firma(Firma firma) {
            this.firma = firma;
            return this;
        }

        public RezervasyonBuilder yetiskinSayi(int yetiskinSayi) {
            this.yetiskinSayi = yetiskinSayi;
            return this;
        }

        public RezervasyonBuilder cocukSayi(int cocukSayi) {
            this.cocukSayi = cocukSayi;
            return this;
        }

        public RezervasyonBuilder durum(int durum) {
            this.durum = durum;
            return this;
        }

        public RezervasyonBuilder koltukDolu(int koltukDolu) {
            this.koltukDolu = koltukDolu;
            return this;
        }

        public Rezervasyon build() {
            return new Rezervasyon(this);
        }
    }

    @Override
    public String toString() {
        return "Rezervasyon{" +
                "rezervasyonId=" + rezervasyonId +
                ", rezervasyonTarih='" + rezervasyonTarih + '\'' +
                ", pnrNo='" + pnrNo + '\'' +
                ", yolcu=" + yolcu +
                ", koltukNo='" + koltukNo + '\'' +
                ", kullaniciId=" + kullaniciId +
                ", ucus=" + ucus +
                ", firma=" + firma +
                ", yetiskinSayi=" + yetiskinSayi +
                ", cocukSayi=" + cocukSayi +
                ", durum=" + durum +
                ", koltukDolu=" + koltukDolu +
                '}';
    }
}
