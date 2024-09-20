package servlet;

import dao.FirmaDAO;
import java.io.File;
import java.io.IOException;
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
    
    private void gosterFirmaEkle(HttpServletRequest solicitud, HttpServletResponse respuesta) 
        throws SQLException, IOException, ServletException {
    
        HttpSession sesion = solicitud.getSession();
        
        if (!esUsuarioAutorizado(sesion, respuesta)) {
            return;
        }
        
        String firmaAd = null;
        String firmaLogo = null;

        respuesta.setContentType("text/html; charset=UTF-8");

        if (!ServletFileUpload.isMultipartContent(solicitud)) {
            return;
        }

        List<FileItem> campos = parsearSolicitud(solicitud);
        if (campos == null || campos.isEmpty()) {
            return;
        }

        for (FileItem campo : campos) {
            if (campo.isFormField()) {
                firmaAd = obtenerValorFormulario(campo, "firma_ad", firmaAd);
            } else {
                firmaLogo = manejarArchivo(campo, firmaLogo);
            }
        }

        if (firmaAd != null && firmaLogo != null) {
            Firma nuevaFirma = new Firma(firmaAd, firmaLogo);
            FirmaDAO.firmaEkle(nuevaFirma);
        }
    }

    private boolean esUsuarioAutorizado(HttpSession sesion, HttpServletResponse respuesta) throws IOException {
        Integer usuarioAutorizacion = (Integer) sesion.getAttribute(KULLANICI_YETKI);
        if (usuarioAutorizacion == null) {
            respuesta.sendRedirect(GIRIS_PAGE);
            return false;
        } else if (usuarioAutorizacion != 2) {
            respuesta.sendRedirect(UCAK_BILETI_PAGE);
            return false;
        }
        return true;
    }

    private List<FileItem> parsearSolicitud(HttpServletRequest solicitud) {
        FileItemFactory fabrica = new DiskFileItemFactory();
        ServletFileUpload subir = new ServletFileUpload(fabrica);
        subir.setHeaderEncoding(ENCODING_UTF8);
        try {
            return subir.parseRequest(solicitud);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String obtenerValorFormulario(FileItem campo, String nombreCampo, String valorActual) throws UnsupportedEncodingException {
        if (valorActual == null && campo.getFieldName().equals(nombreCampo)) {
            return campo.getString(ENCODING_UTF8);
        }
        return valorActual;
    }

    private String manejarArchivo(FileItem archivo, String nombreArchivoActual) throws Exception {
        if (archivo.getSize() > 0) {
            String nombreArchivo = archivo.getName();
            archivo.write(new File(FILE_PATH + nombreArchivo));
            return nombreArchivo;
        }
        return nombreArchivoActual;
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
    
    private void gosterFirmaGuncelle(HttpServletRequest solicitud, HttpServletResponse respuesta) 
        throws SQLException, ServletException, IOException {
    
        HttpSession sesion = solicitud.getSession();
        
        if (!esUsuarioAutorizado(sesion, respuesta)) {
            return;
        }
        
        String firmaAd = null;
        String firmaLogo = null;
        int firmaId = 0;
        
        respuesta.setContentType("text/html; charset=UTF-8");

        if (!ServletFileUpload.isMultipartContent(solicitud)) {
            return;
        }

        List<FileItem> campos = parsearSolicitud(solicitud);
        if (campos == null || campos.isEmpty()) {
            return;
        }

        for (FileItem campo : campos) {
            if (campo.isFormField()) {
                firmaAd = obtenerValorFormulario(campo, "firma_ad", firmaAd);
                firmaId = obtenerValorEnteroFormulario(campo, "firma_id", firmaId);
            } else {
                firmaLogo = manejarArchivo(campo, firmaLogo);
            }
        }

        if (firmaAd != null && firmaLogo != null) {
            eliminarArchivoAntiguo(firmaLogo);
            actualizarFirma(firmaId, firmaAd, firmaLogo);
        }
    }

    private boolean esUsuarioAutorizado(HttpSession sesion, HttpServletResponse respuesta) throws IOException {
        Integer usuarioAutorizacion = (Integer) sesion.getAttribute(KULLANICI_YETKI);
        if (usuarioAutorizacion == null) {
            respuesta.sendRedirect(GIRIS_PAGE);
            return false;
        } else if (usuarioAutorizacion != 2) {
            respuesta.sendRedirect(UCAK_BILETI_PAGE);
            return false;
        }
        return true;
    }

    private List<FileItem> parsearSolicitud(HttpServletRequest solicitud) {
        FileItemFactory fabrica = new DiskFileItemFactory();
        ServletFileUpload subir = new ServletFileUpload(fabrica);
        subir.setHeaderEncoding("UTF-8");
        try {
            return subir.parseRequest(solicitud);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String obtenerValorFormulario(FileItem campo, String nombreCampo, String valorActual) throws UnsupportedEncodingException {
        if (valorActual == null && campo.getFieldName().equals(nombreCampo)) {
            return campo.getString("UTF-8");
        }
        return valorActual;
    }

    private int obtenerValorEnteroFormulario(FileItem campo, String nombreCampo, int valorActual) throws UnsupportedEncodingException {
        if (valorActual == 0 && campo.getFieldName().equals(nombreCampo)) {
            return Integer.parseInt(campo.getString());
        }
        return valorActual;
    }

    private String manejarArchivo(FileItem archivo, String nombreArchivoActual) throws Exception {
        if (archivo.getSize() > 0) {
            String nombreArchivo = archivo.getName();
            archivo.write(new File(ASSETS_DATA_PATH + nombreArchivo));
            return nombreArchivo;
        }
        return nombreArchivoActual;
    }

    private void eliminarArchivoAntiguo(String logotipo) {
        File archivo = new File(ASSETS_DATA_PATH + logotipo);
        archivo.delete();
    }

    private void actualizarFirma(int firmaId, String firmaAd, String firmaLogo) throws SQLException {
        Firma firma = new Firma(firmaId, firmaAd, firmaLogo);
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
            File f = new File(ASSETS_DATA_PATH + firma_logo);
            f.delete();
            firmaDAO.firmasil(firma_id);
            response.sendRedirect("firmaliste");
        }        
    }
}
