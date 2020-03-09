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
        <title>Header</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />
    </head>

    <body>
        <header>
            <div class="container-fluid">

                <ul id="middle-head">
                    <!-- LOGO -->
                    <li class="col-md-3">
                        <a href="${pageContext.request.contextPath}" class="logo">
                            <img src="${pageContext.request.contextPath}/static/images/GeekLogo.png" alt="logo_jambashop.gr">
                        </a>
                    </li>
                    <!-- /LOGO -->



                    <!-- SEARCH BAR -->
                    <li class="col-md-6">
                        <div class="header-search">
                            <form action="${pageContext.request.contextPath}/search">
                                <input type="text" class="input" value="${userInput}" name="userInput"
                                       placeholder="Search here">
                                <input type="submit" class="search-btn" value="Search">
                            </form>
                        </div>
                    </li>

                    <!-- ACCOUNT -->
                    <li id="cart" class="col-md-3">
                        <div class="loginout">
                            <c:if test="${pageContext.request.userPrincipal.name == null}">
                                <a href="${pageContext.request.contextPath}/loginPage"><i class="fa fa-user-o"></i> Log
                                    in</a>
                                </c:if>

                            <c:if test="${pageContext.request.userPrincipal.name != null}">
                                <p>Welcome,
                                    <security:authentication property="principal.username" /><br>
                                    <c:if test="${pageContext.request.userPrincipal.name != null}">
                                        <security:authorize access="hasRole('ADMIN')"><span style="color:green"> (admin)</span>
                                            </security:authorize>
                                        </c:if>
                                    <security:authorize access="hasRole('ADMIN')">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/showForm">
                                            <span style="color:green">Add new Admin</span></a>
                                        </security:authorize>
                                        <form:form action="${pageContext.request.contextPath}/logout" method="POST">
                                        <i class="fa fa-user-o"></i> <input type="submit" class="logoutbtn" value="Logout">
                                    </form:form>
                                </c:if>
                        </div>
                        <div>
                            <!-- Cart -->
                            <a class="" href="${pageContext.request.contextPath}/cart/showCart">
                                <i class="fa fa-shopping-cart fa-2x"></i>
                                ${countCart} items: ${finalPrice}€ </a>
                            <!-- /Cart -->
                        </div>


                    </li>
                    <p>

                    </p>
                </ul>
            </div>




            <nav class="navbar navbar-expand-lg navbar-dark bg-dark ml-5 mr-5">



                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown"
                        aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>



                <div class="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/tower">Tower</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/motherboard">Motherboard</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/cpu">CPU</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/gpu">GPU</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/ram">RAM</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/storage">Storage</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/psu">PSU</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/monitor">Monitor</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/mouse">Mouse</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/keyboard">Keyboard</a>
                        </li>
                    </ul>
                </div>
            </nav>
                        
            <div id="fadeOut">
                <span style="color:green;"><c:out value="${msgSuccess}"/></span>
            </div>

        </header>

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
        <script>
            window.onload = myFunction()

            function myFunction() {
                setTimeout(function del() {
                    document.getElementById("fadeOut").innerHTML = "";
                }, 3000);
            }


        </script>
    </body>

</html>