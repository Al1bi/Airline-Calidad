package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.KullaniciDAO;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.Kullanici;

@WebServlet(urlPatterns = {"/uyeol", "/gosteruyeol", "/sifremiunuttum", "/gostersifremiunuttum", "/giris", "/gostergiris", "/cikis", "/admin/giris", "/admin/gostergiris", "/admin/kullaniciliste", "/admin/adminekle", "/admin/gosteradminekle", "/admin/kullanicisil", "/admin/adminguncelle", "/admin/gosteradminguncelle", "/profil", "/profilguncelle", "/sifreguncelle", "/hesapsil", "/admin/cikis", "/admin/bilgilerim"})

public class KullaniciServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private KullaniciDAO kullaniciDAO;
    private static final String KULLANICI_YETKI = "kullanici_yetki";
    private static final String GIRIS_PAGE = "giris";
    private static final String UCAK_BILETI_URL = "../ucakbileti";
    private static final String UCAK_BILETI_PAGE = "ucakbileti";
    private static final String ENCODING_UTF8 = "UTF-8";
    private static final String ENCODING_ISO_8859_1 = "ISO-8859-1";
    private static final String KULLANICI_SIFRE = "kullanici_sifre";
    private static final String KULLANICI_EMAIL = "kullanici_email";
    private static final String KULLANICI_AD = "kullanici_ad";
    private static final String KULLANICI_SOYAD = "kullanici_soyad";
    private static final String KULLANICI_LISTE = "kullaniciliste";
    private static final String PANEL = "panel";

    public void init() {
        kullaniciDAO = new KullaniciDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/uyeol":
                    uyeol(request, response);
                    break;
                default:
                case "/gosteruyeol":
                    gosteruyeol(request, response);
                    break;
                case "/gostergiris":
                    gostergiris(request, response);
                    break;
                case "/giris":
                    giris(request, response);
                    break;
                case "/sifremiunuttum":
                    sifremiunuttum(request, response);
                    break;
                case "/gostersifremiunuttum":
                    gostersifremiunuttum(request, response);
                    break;
                case "/cikis":
                    uyecikis(request, response);
                    break;
                case "/admin/cikis":
                    adminuyecikis(request, response);
                    break;
                case "/admin/kullaniciliste":
                    kullaniciliste(request, response);
                    break;
                case "/admin/adminekle":
                    adminekle(request, response);
                    break;
                case "/admin/gosteradminekle":
                    gosteradminekle(request, response);
                    break;
                case "/admin/kullanicisil":
                    kullanicisil(request, response);
                    break;
                case "/admin/adminguncelle":
                    adminguncelle(request, response);
                    break;
                case "/admin/gosteradminguncelle":
                    gosteradminguncelle(request, response);
                    break;
                case "/admin/gostergiris":
                    admingostergiris(request, response);
                    break;
                case "/admin/giris":
                    admingiris(request, response);
                    break;
                case "/admin/bilgilerim":
                    adminbilgilerim(request, response);
                    break;
                case "/profil":
                    profil(request, response);
                    break;
                case "/profilguncelle":
                    profilguncelle(request, response);
                    break;
                case "/sifreguncelle":
                    sifreguncelle(request, response);
                    break;
                case "/hesapsil":
                    hesapsil(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void adminbilgilerim(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(UCAK_BILETI_URL);
        } else {
            int kullaniciId = (int) session.getAttribute(kullaniciId);
            RequestDispatcher dispatcher = request.getRequestDispatcher("adminguncelle?id=" + kullaniciId);
            dispatcher.forward(request, response);
        }
    }

    private void sifreguncelle(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 1) {
            response.sendRedirect(UCAK_BILETI_PAGE);
        } else {
            int kullaniciId = Integer.parseInt(request.getParameter("kullaniciId_sifre"));
            String kullanici_sifre = new String((request.getParameter("kullanici_suan_sifre")).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            String kullanici_sifre1 = new String((request.getParameter("kullanici_sifre1")).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            Boolean kontrol = kullaniciDAO.sifrekontrol(kullaniciId, kullanici_sifre);
            if (kontrol == true) {
                Kullanici kullanici = new Kullanici(kullaniciId, kullanici_sifre1);
                kullaniciDAO.sifreguncelle(kullanici);
                session.setAttribute(KULLANICI_SIFRE, kullanici_sifre1);
                response.sendRedirect("profil?durum=basarili");
            } else {
                response.sendRedirect("profil?durum=hatali");
            }
        }
    }

    private void profilguncelle(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 1) {
            response.sendRedirect(UCAK_BILETI_PAGE);
        } else {
            String kontrol_email = (String) session.getAttribute(KULLANICI_EMAIL);
            int kullaniciId = Integer.parseInt(request.getParameter(kullaniciId));
            String kullanici_ad = new String((request.getParameter(KULLANICI_AD)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            String kullanici_soyad = new String((request.getParameter(KULLANICI_SOYAD)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            String kullanici_email = request.getParameter(KULLANICI_EMAIL);
            Boolean kontrol = kullaniciDAO.uyekontrol(kullanici_email);
            if (kontrol == true || kullanici_email.equals(kontrol_email)) {
                Kullanici kullanici = new Kullanici(kullaniciId, kullanici_ad, kullanici_soyad, kullanici_email);
                kullaniciDAO.profilguncelle(kullanici);
                session.setAttribute(KULLANICI_AD, kullanici_ad);
                session.setAttribute(KULLANICI_SOYAD, kullanici_soyad);
                session.setAttribute(KULLANICI_EMAIL, kullanici_email);
                response.sendRedirect("profil?durum=basarili");
            } else {
                response.sendRedirect("profil?durum=basarisiz");
            }
        }
    }

    private void profil(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 1) {
            response.sendRedirect(UCAK_BILETI_PAGE);
        } else {
            int kullaniciId = (int) session.getAttribute(kullaniciId);
            String kullanici_ad = (String) session.getAttribute(KULLANICI_AD);
            String kullanici_email = (String) session.getAttribute(KULLANICI_EMAIL);
            String kullanici_soyad = (String) session.getAttribute(KULLANICI_SOYAD);
            Kullanici kullanici = new Kullanici(kullaniciId, kullanici_ad, kullanici_soyad, kullanici_email);
            request.setAttribute("kullanici", kullanici);
            RequestDispatcher dispatcher = request.getRequestDispatcher("profil.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void kullanicisil(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(UCAK_BILETI_URL);
        } else {
            int id = Integer.parseInt(request.getParameter("id"));
            kullaniciDAO.kullanicisil(id);
            response.sendRedirect(KULLANICI_LISTE);
        }
    }

    private void adminekle(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(UCAK_BILETI_URL);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("adminekle.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void gosteradminekle(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(UCAK_BILETI_URL);
        } else {
            String kullanici_ad = new String((request.getParameter(KULLANICI_AD)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            String kullanici_soyad = new String((request.getParameter(KULLANICI_SOYAD)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            String kullanici_email = request.getParameter(KULLANICI_EMAIL);
            String kullanici_sifre = new String((request.getParameter(KULLANICI_SIFRE)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            Boolean kontrol = kullaniciDAO.uyekontrol(kullanici_email);
            if (kontrol == true) {
                Kullanici yenikullanici = new Kullanici(kullanici_ad, kullanici_soyad, kullanici_email, kullanici_sifre);
                kullaniciDAO.adminekle(yenikullanici);
                response.sendRedirect(KULLANICI_LISTE);
            } else {
                response.sendRedirect("kullaniciliste?durum=basarisiz");
            }
        }
    }

    private void kullaniciliste(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(UCAK_BILETI_URL);
        } else {
            List<Kullanici> kullaniciliste = kullaniciDAO.uyelistele();
            request.setAttribute(KULLANICI_LISTE, kullaniciliste);
            RequestDispatcher dispatcher = request.getRequestDispatcher("kullanicilistele.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void adminguncelle(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(UCAK_BILETI_URL);
        } else {
            int id = Integer.parseInt(request.getParameter("id"));
            Kullanici kullanici = kullaniciDAO.kullanicisec(id);
            RequestDispatcher dispatcher = request.getRequestDispatcher("adminguncelle.jsp");
            request.setAttribute("kullanici", kullanici);
            dispatcher.forward(request, response);
        }
    }

    private void gosteradminguncelle(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(UCAK_BILETI_URL);
        } else {
            int kullaniciId = Integer.parseInt(request.getParameter(kullaniciId));
            String kullanici_ad = new String((request.getParameter(KULLANICI_AD)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            String kullanici_soyad = new String((request.getParameter(KULLANICI_SOYAD)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            String kullanici_email = request.getParameter(KULLANICI_EMAIL);
            String kullanici_sifre = new String((request.getParameter(KULLANICI_SIFRE)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            Kullanici kullanici = new Kullanici(kullaniciId, kullanici_ad, kullanici_soyad, kullanici_email, kullanici_sifre);
            kullaniciDAO.adminguncelle(kullanici);
            response.sendRedirect(KULLANICI_LISTE);
        }
    }

    private void gostersifremiunuttum(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession sessionn = request.getSession();
        if ((Integer) sessionn.getAttribute(KULLANICI_YETKI) == null) {
            String kullanici_email = request.getParameter(KULLANICI_EMAIL);
            Boolean kontrol = kullaniciDAO.uyekontrol(kullanici_email);
            if (kontrol == false) {
                Kullanici kullanici = kullaniciDAO.sifreal(kullanici_email);
                String kullanici_sifre = kullanici.getKullanici_sifre();
                final String to = kullanici_email;
                final String subject = "HAWKEYE Giriş Şifresi";
                final String messg = "Sisteme giriş için şifreniz : " + kullanici_sifre;
                final String from = "mail@gmail.com";
                final String pass = "sifre";

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.ssl.checkserveridentity", "true");
                props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");
                Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(from, pass);
                            }
                        });
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    message.setSubject(subject, ENCODING_UTF8);
                    message.setText(messg, ENCODING_UTF8);
                    Transport.send(message);

                } catch (MessagingException e) {
                    throw new RuntimeException(e);

                }
                response.sendRedirect("sifremiunuttum?durum=basarili");
            } else {
                response.sendRedirect("sifremiunuttum?durum=basarisiz");
            }
        } else {
            response.sendRedirect(UCAK_BILETI_PAGE);
        }
    }

    private void gosteruyeol(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            String kullanici_ad = new String((request.getParameter(KULLANICI_AD)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            String kullanici_soyad = new String((request.getParameter(KULLANICI_SOYAD)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            String kullanici_email = request.getParameter(KULLANICI_EMAIL);
            String kullanici_sifre = new String((request.getParameter("kullanici_sifre1")).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);
            Boolean kontrol = kullaniciDAO.uyekontrol(kullanici_email);
            if (kontrol == true) {
                Kullanici yeniKullanici = new Kullanici(kullanici_ad, kullanici_soyad, kullanici_email, kullanici_sifre);
                kullaniciDAO.uyeol(yeniKullanici);
                response.sendRedirect("uyeol?durum=basarili");
            } else {
                response.sendRedirect("uyeol?durum=basarisiz");
            }
        } else {
            response.sendRedirect(UCAK_BILETI_PAGE);
        }
    }

    private void uyeol(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("uyeol.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect(UCAK_BILETI_PAGE);
        }
    }

    private void giris(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("giris.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect(UCAK_BILETI_PAGE);
        }
    }

    private void sifremiunuttum(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("sifremiunuttum.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect(UCAK_BILETI_PAGE);
        }
    }

    private void gostergiris(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            String kullanici_email = request.getParameter(KULLANICI_EMAIL);
            String kullanici_sifre = new String((request.getParameter(KULLANICI_SIFRE)).getBytes(ENCODING_ISO_8859_1), ENCODING_UTF8);

            Boolean kontrol = kullaniciDAO.uyegiriskontrol(kullanici_email, kullanici_sifre);
            if (kontrol == true) {
                Kullanici uye = kullaniciDAO.uyegiris(kullanici_email, kullanici_sifre);
                int kullanici_yetki = uye.getKullanici_yetki();
                String kullanici_ad = uye.getKullanici_ad();
                String kullanici_soyad = uye.getKullanici_soyad();
                int kullaniciId = uye.getkullaniciId();

                session.setAttribute(kullaniciId, kullaniciId);
                session.setAttribute(KULLANICI_AD, kullanici_ad);
                session.setAttribute(KULLANICI_SOYAD, kullanici_soyad);
                session.setAttribute(KULLANICI_EMAIL, kullanici_email);
                session.setAttribute(KULLANICI_YETKI, kullanici_yetki);
                session.setAttribute(KULLANICI_SIFRE, kullanici_sifre);

                response.sendRedirect(UCAK_BILETI_PAGE);
            } else {
                response.sendRedirect("giris?durum=basarisiz");
            }
        } else {
            response.sendRedirect(UCAK_BILETI_PAGE);
        }
    }

    private void admingiris(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("giris.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect(PANEL);
        }
    }

    private void admingostergiris(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            String admin_email = request.getParameter("admin_email");
            String admin_sifre = request.getParameter("admin_sifre");

            Boolean kontrol = kullaniciDAO.admingiriskontrol(admin_email, admin_sifre);
            if (kontrol == true) {
                Kullanici uye = kullaniciDAO.admingiris(admin_email, admin_sifre);

                int kullanici_yetki = uye.getKullanici_yetki();
                String kullanici_ad = uye.getKullanici_ad();
                String kullanici_soyad = uye.getKullanici_soyad();
                int kullaniciId = uye.getkullaniciId();

                session.setAttribute(kullaniciId, kullaniciId);
                session.setAttribute(KULLANICI_AD, kullanici_ad);
                session.setAttribute(KULLANICI_SOYAD, kullanici_soyad);
                session.setAttribute(KULLANICI_EMAIL, admin_email);
                session.setAttribute(KULLANICI_YETKI, kullanici_yetki);

                response.sendRedirect(PANEL);
            } else {
                response.sendRedirect("giris?durum=basarisiz");
            }
        } else {
            response.sendRedirect(PANEL);
        }

    }

    private void hesapsil(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == 1) {
            int kullaniciId = Integer.parseInt(request.getParameter("sifre_id"));
            String kullanici_sifre = request.getParameter("sil_sifre");
            Boolean kontrol = kullaniciDAO.sifrekontrol(kullaniciId, kullanici_sifre);
            if (kontrol == true) {
                kullaniciDAO.hesapsil(kullaniciId);
                response.sendRedirect("cikis");
            } else {
                response.sendRedirect("profil?durum=uyari");
            }
        } else {
            response.sendRedirect(GIRIS_PAGE);
        }
    }

    private void uyecikis(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect(UCAK_BILETI_PAGE);
    }

    private void adminuyecikis(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect(UCAK_BILETI_URL);
    }
}
