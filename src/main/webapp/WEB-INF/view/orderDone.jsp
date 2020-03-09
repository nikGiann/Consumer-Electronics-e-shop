<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%--<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>--%>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Geek</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />

    </head>

    <body>
        <div class="container">

            <a href="${pageContext.request.contextPath}" class="success logoreg">
                <img src="${pageContext.request.contextPath}/static/images/success.png" alt="Thank toy for your order">
            </a>



            <div id="orderDone">
                <ul>
                    Order's summary:
                    <c:forEach items="${cart}" var="p">
                        ${p.getQuantity()} x ${p.getProduct().getName()}
                        <br/>
                    </c:forEach>
                    Final Price: ${finalPrice}€
                    <br/>
                    <li>${user.getFname()} ${user.getLname()}</li>
                    <li>${order.getTelephone()}</li>
                    <li>${order.getEmail()}</li>
                    <li>${order.getAddress()}, ${order.getPostalCode()}</li>
                    <li>${order.getCity()}</li>
                    <li>${order.getSubmitDate()}</li>
                </ul>
            </div>

        </div>

        <jsp:include page="footer.jsp"/>

    </body>
</html>
