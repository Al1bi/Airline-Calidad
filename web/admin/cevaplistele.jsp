<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file = "header.jsp" %>
<%@ include file = "sidebar.jsp" %>
<%@ include file = "topbar.jsp" %>
<div class="container-fluid">
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-white">Cevap Listesi</h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered" id="dataTable">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Mesaj Konu</th>
                            <th scope="col">Mesaj Tarihi</th>
                            <th scope="col">Cevap Tarihi</th>
                            <th scope="col">İşlemler</th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Mesaj Konu</th>
                            <th scope="col">Mesaj Tarihi</th>
                            <th scope="col">Cevap Tarihi</th>
                            <th scope="col">İşlemler</th>
                        </tr>
                    </tfoot>
                    <tbody>
                        <c:forEach var="cevap" items="${cevapliste}">
                            <tr>
                                <td><c:out value="${cevap.cevap_id}" /></td>
                                <td><c:out value="${cevap.mesaj_konu}" /></td>
                                <td><c:out value="${cevap.mesaj_tarih}" /></td>
                                <td><c:out value="${cevap.cevap_tarih}" /></td>
                                <td>
                                    <a href="cevapincele?id=<c:out value='${cevap.cevap_id}' />">
                                        <button class="btn btn-success btn-sm"><i class="far fa-file-alt"></i> İncele</button>
                                    </a>
                                    <a href="cevapsil?id=<c:out value='${cevap.cevap_id}' />">
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
