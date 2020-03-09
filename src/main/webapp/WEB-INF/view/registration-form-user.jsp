<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration Form</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />

    </head>

    <body>



        <a href="${pageContext.request.contextPath}" class="logo">
            <img src="${pageContext.request.contextPath}/static/images/GeekLogo.png" alt="logo_jambashop.gr">
        </a>


        <div class="container my-3">
            <h3>Registration Form</h3>
            <form:form action="${pageContext.request.contextPath}/user/processRegistration" method="POST"
                       modelAttribute="user">

                <div class="formUser mb-3">
                    <div class="col-12 col-md-4">
                        <label for="userName">User Name:</label><span style="color:red;">
                            <c:out value="${msgUserName}" /></span>
                            <form:input class="form-control" path="username" id="userName" required="required" maxlength="30"/><span>


                    </div>
                    <div class="col-12 col-md-4">
                        <label for="password">Password:</label><span style="color:red;">
                            <c:out value="${msgPassword}" /></span>
                            <form:input class="form-control" path="password" type="password" id="password" required="required" maxlength="68"/>

                    </div>
                    <div class="col-12 col-md-4">
                        <label for="rePassword">Confirm Password:</label><span style="color:red;">
                            <c:out value="${msgRePassword}" /></span>
                        <input class="form-control" id="rePassword" type="password" value="${rePassword}" name="rePassword" required="required" maxlength="68"/>

                    </div>
                    <div class="col-12 col-md-4">
                        <label for="fname">First Name:</label><span style="color:red;">
                            <c:out value="${msgFname}" /></span>
                        <form:input class="form-control" type="text" path="fname" id="fname" required="required" maxlength="20"/><span>

                    </div>
                    <div class="col-12 col-md-4">
                        <label for="lname">Last Name:</label><span style="color:red;">
                            <c:out value="${msgLname}" /></span>
                        <form:input class="form-control" path="lname" type="text" id="lname" required="required" maxlength="20"/><span>

                    </div>
                    <div class="col-12 col-md-4">
                        <label for="email">E-mail:</label><span style="color:red;">
                            <c:out value="${msgEmail}" /></span>
                        <form:input class="form-control" path="email" type="email" id="email" required="required" maxlength="50"/><span>


                    </div>
                    <div class="col-12 col-md-4">
                        <label for="telephone">Telephone:</label><span style="color:red;">
                            <c:out value="${msgTel}" /></span>
                            <form:input class="form-control" type="number" path="telephone" id="telephone" maxlength="20"/>


                    </div>
                    <div class="col-12 col-md-4">
                        <label for="country">Country:</label><span style="color:red;">
                            <c:out value="${msgCountry}" /></span>
                            <form:input class="form-control" type="text" path="country" id="country" maxlength="50"/>


                    </div>

                    <div class="col-12 col-md-4">
                        <label for="city">City:</label><span style="color:red;">
                            <c:out value="${msgCity}" /></span>
                            <form:input class="form-control" type="text" path="city" id="city" maxlength="50"/>


                    </div>
                    <div class="col-12 col-md-4">
                        <label for="address">Address:</label><span style="color:red;">
                            <c:out value="${msgAddress}" /></span>
                            <form:input class="form-control" type="text" path="address" id="address" maxlength="50"/>


                    </div>
                    <div class="col-12 col-md-4">
                        <label for="postal_code">Postal Code:</label><span style="color:red;">
                            <c:out value="${msgPostal}" /></span>
                            <form:input class="form-control" type="number" path="postal_code" id="postal_code" />


                    </div>
                    <div class="col-4 col-md-2">
                        <input type="submit" class="form-control regbtn" value="Register">
                    </div>
                </div>
            </form:form>
        </div>
        <!-- BootStrap JS -->
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