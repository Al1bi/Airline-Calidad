<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="header.jsp" %>
<%@ include file="navigasyon.jsp"%>
<div class="container" style="margin-top:90px;">
    <div class="row">
        <div class="col-md-4" id="flight-paymentblock-left">
            <div class="row">
                <div class="col-12 mb-2">
                    <div class="card bg-payment-card shadow-sm">
                        <div class="card-header bg-payment-card-header card-header-payment pb-0">
                            <div class="d-flex justify-content-start align-items-center">
                                <p class="font-weight-normal card-title-payment f-16 card-text-payment-bold">Uçuş Bilgileri</p>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <p class="f-14 card-text-payment card-text-payment-bold">Gidiş</p>
                                <p class="f-14 card-text-payment card-text-payment-bold">
                                    <c:out value="${ucusbilgileri.ucus_tarih}" />
                                </p>
                            </div>
                            <p class="card-border-bottom-payment"></p>

                            <div>
                                <div class="d-flex justify-content-between">
                                    <p class="small card-text-payment f-08em">
                                        <img class="img-fluid" src="<%=request.getContextPath()%>/assets/data/<c:out value='${ucusbilgileri.firma_logo}' />" style="width: 1.5em">
                                        <c:out value="${ucusbilgileri.firma_ad}" /> - <c:out value="${ucusbilgileri.ucak_ad}" />
                                    </p>                        
                                </div>
                                <div class="d-flex justify-content-between align-item-center">
                                    <span class="card-text-payment card-text-payment-bold f-16">
                                        <c:out value="${ucusbilgileri.kalkis_kod}" /> <c:out value="${ucusbilgileri.ucus_saat}" /> 
                                    </span>
                                    <hr class="divider-small">
                                    <span class="f-08em text-soft">
                                        <i class="far fa-clock text-soft" aria-hidden="true"></i> 
                                        <c:out value="${ucusbilgileri.ucus_s}" /> Sa <c:out value="${ucusbilgileri.ucus_d}" /> dk 
                                    </span>
                                    <hr class="divider-small">
                                    <span class="card-text-payment card-text-payment-bold f-16">
                                        <c:out value="${ucusbilgileri.varis_kod}" /> <c:out value="${ucusbilgileri.varis_saat}" />
                                    </span>
                                </div>
                                <div class="d-flex justify-content-between align-item-center mb-2 card-text-payment pt-1">
                                    <small class="f-07em"><c:out value="${ucusbilgileri.kalkis_ad}" /></small> 
                                    <small class="f-07em"><c:out value="${ucusbilgileri.varis_ad}" /></small>
                                </div>
                            </div>               
                        </div>
                    </div>
                </div>
            </div>
            <!-- Flight Price Details Block -->
            <div class="row" id="flight-price-detail">
                <div class="col-12">
                    <div class="card bg-payment-card shadow-sm mt-2">
                        <div class="card-header bg-payment-card-header card-header-payment pb-0">
                            <div class="d-flex justify-content-start align-items-center">
                                <p class="font-weight-normal card-title-payment f-16 card-text-payment-bold">Fiyat Bilgileri</p>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="row mb-0">
                                <div class="col-md-12">
                                    <div class="table-responsive">
                                        <table class="table table-borderless" style="margin-bottom:0px !important;">
                                            <tbody>
                                                <tr>
                                                    <th scope="col"><span id="yetiskin_ucret" name="yetiskin_ucret">
                                                        <c:out value="${yolcusayi.yetiskin_sayi}" /></span> Yetişkin</th>
                                                    <td class="text-right py-0">
                                                        <c:out value="${yolcusayi.yetiskin_sayi * ucusbilgileri.ucus_ucret}" /> ₺
                                                    </td>
                                                </tr>
                                                <c:if test="${yolcusayi.cocuk_sayi != 0}">
                                                    <tr>
                                                        <th scope="col"><span id="cocuk_ucret" name="cocuk_ucret">
                                                            <c:out value="${yolcusayi.cocuk_sayi}" /></span> Çocuk</th>
                                                        <td class="text-right py-0">
                                                            <c:out value="${yolcusayi.cocuk_sayi * ucusbilgileri.ucus_ucret}" /> ₺
                                                        </td>
                                                    </tr>
                                                </c:if>    
                                                <tr>                                  
                                                    <td id="fiyat_goster">
                                                        <b class="card-text-payment">Toplam Tutar</b>                                      
                                                    </td>
                                                    <td class="text-right card-text-payment-bold card-text-payment">
                                                        <span id="toplam_tutar" name="toplam_tutar">
                                                            <c:out value="${yolcusayi.cocuk_sayi * ucusbilgileri.ucus_ucret + yolcusayi.yetiskin_sayi * ucusbilgileri.ucus_ucret}" />
                                                        </span> ₺
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Passenger Information and Seat Selection -->
        <div class="col-md-8" id="flight-paymentblock-mid">
            <form action="gosterrezervasyonislemlerim" method="post">
                <!-- Hidden fields for flight details -->
                <input type="hidden" id="ucus_id" name="ucus_id" value="<c:out value='${ucusbilgileri.ucus_id}' />">
                <input type="hidden" id="y_sayi" name="y_sayi" value="<c:out value='${yolcusayi.yetiskin_sayi}' />">
                <input type="hidden" id="c_sayi" name="c_sayi" value="<c:out value='${yolcusayi.cocuk_sayi}' />">
                <input type="hidden" id="u_ucret" name="u_ucret" value="<c:out value='${ucusbilgileri.ucus_ucret}' />">

                <!-- Contact Information Section -->
                <div class="row mb-2">
                    <div class="col-12 mb-2">
                        <div class="card shadow-sm">
                            <div class="card-header white card-header-payment pb-1">
                                <div class="d-flex justify-content-start align-items-center">
                                    <p class="font-weight-normal card-title-payment f-16 card-text-payment-bold">İletişim Bilgileri</p>
                                </div>
                            </div>
                            <div class="card-body">
                                <div class="form-group">
                                    <label for="yolcu_email" class="active">E-mail</label>
                                    <input type="email" class="form-control" id="yolcu_email" name="yolcu_email" placeholder="Email" required>
                                </div>
                                <div class="form-group">
                                    <label for="yolcu_tel">Cep Telefonu</label>
                                    <input type="text" class="form-control" id="yolcu_tel" name="yolcu_tel" pattern="[0-9]{11}" title="05554443322 biçiminde yazınız." placeholder="Telefon" required>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Passenger Details Section -->
                <div class="row mb-4">
                    <div class="col-12 mb-2">
                        <div class="card shadow-sm">
                            <div class="card-header white card-header-payment pb-1">
                                <div class="d-flex justify-content-start align-items-center">
                                    <p class="font-weight-normal card-title-payment f-16 card-text-payment-bold">Yolcu Bilgileri</p>
                                </div>
                            </div>
                            <c:forEach var="i" begin="1" end="${yolcusayi.yetiskin_sayi}">
                                <div class="card-body">
                                    <div class="row mb-4">
                                        <div class="col-md-12 col-12"> 
                                            <input type="hidden" id="yolcu_tip<c:out value='${i}' />" name="yolcu_tip<c:out value='${i}' />" value="0">
                                            <p class="font-weight-bold text-black-50"><c:out value="${i}" />. Yolcu (Yetişkin)</p>
                                        </div>
                                        <div class="col-md-6 col-12">
                                            <div class="form-group">
                                                <label for="yolcu_ad<c:out value='${i}' />">Adı</label>
                                                <input type="text" class="form-control" id="yolcu_ad<c:out value='${i}' />" name="yolcu_ad<c:out value='${i}' />" required>
                                            </div>
                                        </div>
                                        <div class="col-md-6 col-12">
                                            <div class="form-group">
                                                <label for="yolcu_soyad<c:out value='${i}' />">Soyadı</label>
                                                <input type="text" class="form-control" id="yolcu_soyad<c:out value='${i}' />" name="yolcu_soyad<c:out value='${i}' />" required>
                                            </div>
                                        </div>
                                        <div class="col-md-6 mb-2 col-12">
                                            <div class="form-group">
                                                <label for="yolcu_tc<c:out value='${i}' />">TC Kimlik Numarası</label>
                                                <input type="text" class="form-control" id="yolcu_tc<c:out value='${i}' />" pattern="[0-9]{11}" name="yolcu_tc<c:out value='${i}' />" required>
                                            </div>
                                        </div>
                                        <div class="col-md-6 mb-4 mb-lg-4 col-12">
                                            <div class="form-group">
                                                <label for="yolcu_tarih<c:out value='${i}' />">Doğum Tarihi</label>
                                                <input type="date" class="form-control" name="yolcu_tarih<c:out value='${i}' />" id="yolcu_tarih<c:out value='${i}' />" required>
                                            </div>
                                        </div>     
                                    </div>   
                                </div>
                            </c:forEach>

                            <!-- Similar block for child passengers (if any) -->
                            <c:if test="${yolcusayi.cocuk_sayi != 0}">
                                <c:forEach var="k" begin="${yolcusayi.yetiskin_sayi + 1}" end="${yolcusayi.cocuk_sayi + yolcusayi.yetiskin_sayi}">
                                    <!-- Similar structure as above for child passengers -->
                                </c:forEach>
                            </c:if> 
                        </div>
                    </div>
                </div>

                <!-- Seat Selection Section -->
                <!-- Code for seat selection similar to the existing code -->
                <!-- ... -->

                <!-- Submit Button -->
                <div class="mx-auto">
                    <button type="submit" class="btn btn-warning btn-rounded text-white selectbtn-go mb-5 mt-5">Rezervasyon İşlemini Tamamla</button>
                </div>
            </form>
            <script type="text/javascript">
                function koltuk_sec(id) {
                    var value = document.querySelector('input[name="seat"]:checked').value;
                    document.getElementById("yolcu_koltuk" + id).value = value;
                    document.getElementById("koltuk" + value).disabled = true;
                    document.getElementById(id).disabled = true;
                }
            </script>
        </div>
    </div>
</div>
<%@ include file="footer.jsp" %>
