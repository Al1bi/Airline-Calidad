package servlet;

import dao.HavaalaniDAO;
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
import model.Havaalani;
import model.Havaalani_sehir;
import model.Havaalani_ulke;

@WebServlet(urlPatterns = {"/admin/havaalaniliste", "/admin/havaalaniekle", "/admin/gosterhavaalaniekle", "/admin/havaalanisil", "/admin/havaalaniguncelle", "/admin/gosterhavaalaniguncelle"})

public class HavaalaniServlet extends HttpServlet{

    private static String KULLANICI_YETKI = "kullanici_yetki";
    private static String GIRIS = "giris";
    private static String ROUTE_UCAKBILETI = "../ucakbileti";
    private static String HAVAALANILISTE = "havaalaniliste";
    private static String UTF_8 = "UTF-8";
    private static String ISO_8859_1 = "ISO-8859-1";
    private HavaalaniDAO havaalaniDAO;

    public void init() {
        havaalaniDAO = new HavaalaniDAO();
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
                case "/admin/havaalaniliste":
                    havaalaniliste(request, response);
                    break; 
                case "/admin/havaalaniekle":
                    havaalaniekle(request, response);
                    break;  
                case "/admin/gosterhavaalaniekle":
                    gosterhavaalaniekle(request, response);
                    break;
                case "/admin/havaalanisil":
                    havaalanisil(request, response);
                    break;
                case "/admin/havaalaniguncelle":
                    havaalaniguncelle(request, response);
                    break;                    
                case "/admin/gosterhavaalaniguncelle":
                    gosterhavaalaniguncelle(request, response);
                    break;
                case default:
                    throw new ServletException(action);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void havaalaniliste(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(ROUTE_UCAKBILETI);
        }else{
            List<Havaalani> havaalaniliste = havaalaniDAO.havaalaniliste();
            request.setAttribute(HAVAALANILISTE, havaalaniliste);
            RequestDispatcher dispatcher = request.getRequestDispatcher("havaalanilistele.jsp");
            dispatcher.forward(request, response);
        }    
    }
    
    private void havaalaniekle(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(ROUTE_UCAKBILETI);
        }else{
            List<Havaalani_sehir> havaalanisehir = havaalaniDAO.havaalanisehir();
            request.setAttribute("havaalanisehir", havaalanisehir);
            List<Havaalani_ulke> havaalaniulke = havaalaniDAO.havaalaniulke();
            request.setAttribute("havaalaniulke", havaalaniulke);
            RequestDispatcher dispatcher = request.getRequestDispatcher("havaalaniekle.jsp");      
            dispatcher.forward(request, response);
        }
    }
    
    private void gosterhavaalaniekle(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(ROUTE_UCAKBILETI);
        }else{
            int havaalani_ulke_id = Integer.parseInt(request.getParameter("havaalani_ulke_id"));
            int havaalani_sehir_id = Integer.parseInt(request.getParameter("havaalani_sehir_id"));
            String havaalani_ad = new String((request.getParameter("havaalani_ad")).getBytes(ISO_8859_1), UTF_8);
            String havaalani_kod = new String((request.getParameter("havaalani_kod")).getBytes(ISO_8859_1), UTF_8);
            Havaalani yenihavaalani = new Havaalani(havaalani_ulke_id, havaalani_sehir_id, havaalani_ad, havaalani_kod);
            havaalaniDAO.havaalaniekle(yenihavaalani);
            response.sendRedirect(HAVAALANILISTE);
        }
    }
    
    private void havaalanisil(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(ROUTE_UCAKBILETI);
        }else{
            int havaalani_id = Integer.parseInt(request.getParameter("id"));
            havaalaniDAO.havaalanisil(havaalani_id);
            response.sendRedirect(HAVAALANILISTE);
        }
    }
    
    private void havaalaniguncelle(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(ROUTE_UCAKBILETI);
        }else{
            int id = Integer.parseInt(request.getParameter("id"));
            Havaalani havaalani = havaalaniDAO.havaalanisec(id);
            List<Havaalani_sehir> havaalanisehir = havaalaniDAO.havaalanisehir();
            request.setAttribute("havaalanisehir", havaalanisehir);
            List<Havaalani_ulke> havaalaniulke = havaalaniDAO.havaalaniulke();
            request.setAttribute("havaalaniulke", havaalaniulke);
            RequestDispatcher dispatcher = request.getRequestDispatcher("havaalaniguncelle.jsp");
            request.setAttribute("havaalani", havaalani);
            dispatcher.forward(request, response);
        }     
    }
    
    private void gosterhavaalaniguncelle(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(ROUTE_UCAKBILETI);
        }else{
            int havaalani_id = Integer.parseInt(request.getParameter("havaalani_id"));
            int havaalani_sehir_id = Integer.parseInt(request.getParameter("havaalani_sehir_id"));
            int havaalani_ulke_id = Integer.parseInt(request.getParameter("havaalani_ulke_id"));
            String havaalani_ad = new String((request.getParameter("havaalani_ad")).getBytes(ISO_8859_1), UTF_8);
            String havaalani_kod = new String((request.getParameter("havaalani_kod")).getBytes(ISO_8859_1), UTF_8);
            Havaalani havaalani = new Havaalani(havaalani_id, havaalani_ulke_id, havaalani_sehir_id, havaalani_ad, havaalani_kod);
            havaalaniDAO.havaalaniguncelle(havaalani);
            response.sendRedirect(HAVAALANILISTE);
        }
        
    }
    
}
