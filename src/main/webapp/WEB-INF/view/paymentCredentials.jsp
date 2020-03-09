<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">


    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>CPU</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />
    </head>
    <body>

        <jsp:include page="header.jsp"/>
        <!-- SECTION -->
        <div class="section">
            <!-- container -->
            <div class="container mt-3">
                <!-- row -->
                <div class="row">

                    <div class="col-md-7">
                        <br/>
                        <!-- Billing Details -->
                        Please fill your credentials  <security:authorize access="isAuthenticated()">or: <a href="${pageContext.request.contextPath}/cart/user">use existing delivery data</a> </security:authorize>

                        <form:form action="${pageContext.request.contextPath}/cart/payment/${finalPrice}" 
                                   method="POST"
                                   modelAttribute="user">

                            <form:hidden path="uid"/>
                            <div class="form-group">
                                <form:input path="fname" type="text" class="form-control" placeholder="First Name" required="required"/>
                            </div>
                            <div class="form-group">
                                <form:input path="lname" type="text" class="form-control" placeholder="Last Name" required="required"/>
                            </div>
                            <div class="form-group">
                                <form:input path="telephone" type="number" class="form-control" placeholder="Telephone" required="required"/>
                            </div>
                            <div class="form-group">
                                <form:input path="email" type="email" class="form-control" placeholder="Email" required="required"/>
                            </div>
                            <div class="form-group">
                                <form:input path="address" type="text" class="form-control" placeholder="Address" required="required"/>
                            </div>
                            <div class="form-group">
                                <form:input path="city" type="text" class="form-control" placeholder="City" required="required"/>
                            </div>
                            <div class="form-group">
                                <form:input path="country" type="text" class="form-control" placeholder="Country" required="required"/>
                            </div>
                            <div class="form-group">
                                <form:input path="postal_code" type="number" class="form-control" placeholder="Postal Code" required="required"/>
                            </div>

                            <!-- Order notes -->
                            <div class="order-notes">
                                <textarea class="form-control" placeholder="Order Notes"></textarea>
                            </div>
                            <!-- /Order notes -->

                            <!--Payment method-->
                            <div>
                                <br>
                                <h3>Choose a payment method</h3>
                                <label>
                                    <input type="radio" class="radio" value="cash" name="paymentMethod"/>
                                    <i class="fa fa-coins"></i>Cash</label>

                                <label>
                                    <input type="radio" class="radio" value="creditCart" name="paymentMethod"/>
                                    <i class="fa fa-credit-card"></i>Credit Cart</label>
                            </div>
                            <!--/Payment method-->
                            <br/>

                            <div class="input-checkbox">
                                <input type="checkbox" id="terms" required>
                                <label for="terms">
                                    <span></span>
                                    I've read and accept the <a href="#">terms & conditions</a>
                                </label>
                            </div>
                            <br/>

                            <div class="form-group">
                                <button type="submit">Submit</button>
                            </div>

                        </form:form>
                        <!-- /Billing Details -->
                    </div>

                    <br/>
                    <!-- Order Details -->
                    <br/>
                    <div class="col-md-5 mt-3 order-details">
                        <div class="section-title text-center">
                            <h3 class="title">Your Order</h3>
                        </div>
                        <div class="order-summary">
                            <div class="order-col">
                                <div><strong>Products</strong></div>
                            </div>
                            <br/>

                            <ul>
                                <c:forEach items="${cart}" var="c">
                                    <li><div class="order-products">
                                            <div class="order-col">
                                                <div>${c.getQuantity()}x ${c.getProduct().getName()}</div>
                                                <div>${c.getTotalPrice()}€</div>
                                            </div>
                                        </div>
                                        <br/>
                                    </li>
                                </c:forEach>
                            </ul>

                            <div class="order-col">
                                <c:choose>
                                    <c:when test="${finalPrice<50}">
                                        <div class="order-col">
                                            <div><strong>Total Price:</strong>${finalPrice}€</div>
                                        </div>
                                        <br/>
                                        <strong>Shiping:</strong> 5€
                                        <br/>
                                        <br/>
                                        <div class="order-col">
                                            <div><strong>Final Price: ${finalPrice+5}€</strong></div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <br/>
                                        <div><strong>FREE Shiping</strong></div>
                                        <br/>
                                        <div class="order-col">
                                            <div><strong>Final Price: ${finalPrice}€</strong></div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <br/>


                        </div>
                    </div>
                    <!-- /Order Details -->
                </div>
            </div>
            <!-- /row -->
        </div>
        <!-- /container -->
    </div>
    <!-- /SECTION -->

    <jsp:include page="footer.jsp"/>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
    crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
    crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
    crossorigin="anonymous"></script>

</body>
</html>