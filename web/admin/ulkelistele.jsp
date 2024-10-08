<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file = "header.jsp" %>
<%@ include file = "sidebar.jsp" %>
<%@ include file = "topbar.jsp" %>
<div class="container-fluid">
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <span class="m-0 font-weight-bold text-white">Ülke Listesi</span>
            <a href="ulkeekle" class="btn btn-dark btn-sm float-right"><i class="fas fa-plus"></i> Ülke Ekle</a>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered" id="dataTable" >
                    <thead>
                        <tr>
                            <th scope="cols">#</th>
                            <th scope="cols">Ülke Adı</th>
                            <th scope="cols">İşlemler</th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th scope="cols">#</th>
                            <th scope="cols">Ülke Adı</th>
                            <th scope="cols">İşlemler</th>
                        </tr>
                    </tfoot>
                    <tbody>
                        <c:forEach var="ulke" items="${ulkeliste}">
                            <tr>
                                <td><c:out value="${ulke.havaalani_ulke_id}" /></td>
                                <td><c:out value="${ulke.havaalani_ulke_ad}" /></td>
                                <td>
                                    <a href="ulkeguncelle?id=<c:out value='${ulke.havaalani_ulke_id}' />">
                                        <button class="btn btn-primary btn-sm"><i class="fa fa-edit"></i> Düzenle</button>
                                    </a>
                                    <a href="ulkesil?id=<c:out value='${ulke.havaalani_ulke_id}' />">
                                        <button class="btn btn-danger btn-sm"><i class="fa fa-trash"></i> Sil</button>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<%@ include file = "footer.jsp" %>