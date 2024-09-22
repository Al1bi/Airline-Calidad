package servlet;

import dao.FirmaDAO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Firma;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@WebServlet(urlPatterns = {"/admin/firmaliste", "/admin/firmaekle", "/admin/gosterfirmaekle", "/admin/firmasil", "/admin/firmaguncelle", "/admin/gosterfirmaguncelle"})

public class FirmaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FirmaDAO firmaDAO;
    private static final String KULLANICI_YETKI = "kullanici_yetki";
    private static final String GIRIS_PAGE = "giris";
    private static final String UCAK_BILETI_PAGE = "../ucakbileti";
    private static final String ASSETS_DATA_PATH = "C:\\Users\\Asus\\Documents\\NetBeansProjects\\hawkeye\\web\\assets\\data\\";

    public void init() {
        firmaDAO = new FirmaDAO();
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
                case "/admin/firmaliste":
                    firmaliste(request, response);
                    break;
                case "/admin/firmaekle":
                    firmaekle(request, response);
                    break;
                case "/admin/gosterfirmaekle":
                   gosterfirmaekle(request, response);
                    break;
                case "/admin/firmaguncelle":
                    firmaguncelle(request, response);
                    break;
                case "/admin/gosterfirmaguncelle":
                   gosterfirmaguncelle(request, response);
                    break; 
                case "/admin/firmasil":
                   firmasil(request, response);
                    break;   
                default:
                response.sendRedirect("error404.jsp"); // Redirige a una página de error o una acción predeterminada
                break;   
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void firmaliste(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(UCAK_BILETI_PAGE);
        }else{
            List<Firma> firmaliste = firmaDAO.firmalistele();
            request.setAttribute("firmaliste", firmaliste);
            RequestDispatcher dispatcher = request.getRequestDispatcher("firmalistele.jsp");
            dispatcher.forward(request, response);            
        }
    }
    
    private void firmaekle(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(UCAK_BILETI_PAGE);
        }else{
            RequestDispatcher dispatcher = request.getRequestDispatcher("firmaekle.jsp");      
        dispatcher.forward(request, response);
        }
    }  

    private void firmaguncelle(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(UCAK_BILETI_PAGE);
        }else{
            int id = Integer.parseInt(request.getParameter("id"));
            Firma firma = firmaDAO.firmasec(id);
            RequestDispatcher dispatcher = request.getRequestDispatcher("firmaguncelle.jsp");
            request.setAttribute("firma", firma);
            dispatcher.forward(request, response);
        }
        
    }

    private void firmasil(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException {
        HttpSession session = request.getSession();
        if ((Integer) session.getAttribute(KULLANICI_YETKI) == null) {
            response.sendRedirect(GIRIS_PAGE);
        }else if((Integer) session.getAttribute(KULLANICI_YETKI) != 2){
            response.sendRedirect(UCAK_BILETI_PAGE);
        }else{
            int firma_id = Integer.parseInt(request.getParameter("id"));
            String firma_logo = request.getParameter("logo");
            Path path = Paths.get(ASSETS_DATA_PATH + firma_logo)
            try {
                Files.delete(path);
                System.out.println("Archivo eliminado: " + path);
            } catch (IOException e) {
                System.err.println("Error al eliminar el archivo: " + path);
                e.printStackTrace();
            }
            firmaDAO.firmasil(firma_id);
            response.sendRedirect("firmaliste");
        }        
    }
}
