package servlet;

import dao.CevapDAO;
import dao.MesajDAO;
import java.io.IOException;
import java.io.InputStream;
import java.net.PasswordAuthentication;
import java.sql.SQLException;
import java.util.*;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Cevap;
import model.Mesaj;

@WebServlet(urlPatterns = {"/admin/mesajcevapla", "/admin/gostermesajcevapla", "/admin/cevapliste", "/admin/cevapsil", "/admin/cevapincele"})
public class CevapServlet extends HttpServlet {

    private static String KULLANICI_YETKI = "kullanici_yetki";
    private static String GIRIS = "giris";
    private static String ROUTE_UCAKBILETI = "../ucakbileti";
    private static String CEVAPLISTE = "cevapliste";
    private static String UTF_8 = "UTF-8";
    private static final long serialVersionUID = 1L;
    private CevapDAO cevapDAO;
    private MesajDAO mesajDAO;

    public void init() {
        cevapDAO = new CevapDAO();
        mesajDAO = new MesajDAO();
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
                case "/admin/mesajcevapla":
                    mesajcevapla(request, response);
                    break;
                case "/admin/gostermesajcevapla":
                    gostermesajcevapla(request, response);
                    break;
                case "/admin/cevapliste":
                    cevapliste(request, response);
                    break;
                case "/admin/cevapsil":
                    cevapsil(request, response);
                    break;
                case "/admin/cevapincele":
                    cevapincele(request, response);
                    break;
                default:
                    throw new ServletException(action);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void cevapliste(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(ROUTE_UCAKBILETI);
        } else {
            List<Cevap> cevapliste = cevapDAO.cevaplistele();
            request.setAttribute(CEVAPLISTE, cevapliste);
            RequestDispatcher dispatcher = request.getRequestDispatcher("cevaplistele.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void cevapincele(HttpServletRequest request, HttpServletResponse response) 
    throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(ROUTE_UCAKBILETI);
        } else {
            int cevap_id = Integer.parseInt(request.getParameter("id"));
            Cevap cevap = cevapDAO.cevapincele(cevap_id);
            request.setAttribute("cevap", cevap);
            RequestDispatcher dispatcher = request.getRequestDispatcher("cevapincele.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void cevapsil(HttpServletRequest request, HttpServletResponse response) 
    throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(ROUTE_UCAKBILETI);
        } else {
            int cevap_id = Integer.parseInt(request.getParameter("id"));
            cevapDAO.cevapsil(cevap_id);
            response.sendRedirect(CEVAPLISTE);
        }
    }

    private void mesajcevapla(HttpServletRequest request, HttpServletResponse response) 
    throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(ROUTE_UCAKBILETI);
        } else {
            int id = Integer.parseInt(request.getParameter("id"));
            mesajDAO.mesajokunma(id);
            Mesaj mesaj = cevapDAO.mesajsec(id);
            request.setAttribute("mesaj", mesaj);
            RequestDispatcher dispatcher = request.getRequestDispatcher("mesajcevap.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void gostermesajcevapla(HttpServletRequest request, HttpServletResponse response) 
    throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        } else if ((Integer) session.getAttribute(KULLANICI_YETKI) != 2) {
            response.sendRedirect(ROUTE_UCAKBILETI);
        } else {
            int mesaj_id = Integer.parseInt(request.getParameter("mesaj_id"));
            String mesaj_email = request.getParameter("mesaj_email");
            String cevap_baslik = new String((request.getParameter("cevap_baslik")).getBytes("ISO-8859-1"), UTF_8);
            String cevap_icerik = new String((request.getParameter("cevap_icerik")).getBytes("ISO-8859-1"), UTF_8);
            Cevap yenicevap = new Cevap(mesaj_id, cevap_icerik, cevap_baslik);

            Properties configProps = loadEmailProperties();

            final String to = mesaj_email;
            final String subject = cevap_baslik;
            final String messg = cevap_icerik;
            final String from = configProps.getProperty("cevap_servlet.mail.from");
            final String pass = configProps.getProperty("cevap_servlet.mail.pass");

            Properties props = setupEmailProperties();

            Session sessionMail = createEmailSession(props, from, pass);
            sendEmail(sessionMail, to, subject, messg);

            mesajDAO.mesajcevap(mesaj_id);
            cevapDAO.cevapekle(yenicevap);
            response.sendRedirect(CEVAPLISTE);
        }
    }

    private Properties loadEmailProperties() throws CevapServletException {
        Properties configProps = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new CevapServletException("No se pudo encontrar config.properties");
            }
            configProps.load(input);
        } catch (IOException ex) {
            throw new CevapServletException("Error al cargar config.properties", ex);
        }
        return configProps;
    }

    private Properties setupEmailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.ssl.checkserveridentity", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        return props;
    }

    private Session createEmailSession(Properties props, final String from, final String pass) {
        return Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });
    }

    private void sendEmail(Session session, String to, String subject, String messg) throws CevapServletException {
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject, UTF_8);
            message.setText(messg, UTF_8);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new CevapServletException("Error al enviar el correo", e);
        }
    }
}
