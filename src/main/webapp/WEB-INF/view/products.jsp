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
        <title>Geek</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />



    </head>


    <body>
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous">
        </script>

        <jsp:include page="header.jsp"/>




        <!-- -----------------------Carousel------------- -->

        <div id="carouselExampleControls" class="carousel slide mt-5" data-ride="carousel">
            <div class="carousel-inner">
                <div class="carousel-item active">
                    <img class="d-block w-100" src="${pageContext.request.contextPath}/static/images/carousel5.jpg" alt="First slide">
                </div>
                <div class="carousel-item">
                    <img class="d-block w-100" src="${pageContext.request.contextPath}/static/images/carousel3.jpg" alt="Second slide">
                </div>
                <div class="carousel-item">
                    <img class="d-block w-100" src="${pageContext.request.contextPath}/static/images/carousel6.jpg" alt="Third slide">
                </div>
                <div class="carousel-item">
                    <img class="d-block w-100" src="${pageContext.request.contextPath}/static/images/carousel1.jpg" alt="Third slide">
                </div>
                <div class="carousel-item">
                    <img class="d-block w-100" src="${pageContext.request.contextPath}/static/images/carousel2.jpg" alt="Fourth slide">
                </div>
            </div>
            <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>
        <!-- ------------------------------------ -->

        <div class="container mt-5">
            <!-- <h3>New Products</h3>-->

            <div class="title">
                <img src="${pageContext.request.contextPath}/static/geekfonts/NewProducts.png" alt="cpu">
            </div>

            <c:forEach items="${latestProducts}" var="p">
                <c:url var="addToCart" value="/cart/addToCart">
                    <c:param name="productId" value="${p.id}" />
                    <c:param name="jspName" value="products" />
                </c:url>
                <c:url value="/${p.category.pcategory.toLowerCase()}/update" var="updateLink" >
                    <c:param name="productId" value="${p.id}" />
                </c:url>

                <c:url value="/${p.category.pcategory.toLowerCase()}/delete" var="deleteLink">
                    <c:param name="productId" value="${p.id}" />
                </c:url>
                <div class="divproduct row col-12 col-md-12">

                    <a href="${pageContext.request.contextPath}/${p.category.pcategory.toLowerCase()}/information/${p.id}">
                        <img class="col-12 col-md-2 my-2" src="http://localhost:8080/images/${p.id}.jpg" alt="TODO" height="100%" width="100%">
                    </a>

                    <div class="indivproduct col-12 col-md-6">
                        <div>
                            <p><a href="${pageContext.request.contextPath}/${p.category.pcategory.toLowerCase()}/information/${p.id}">${p.name}</a><p>
                        </div>
                        <div>
                            Code: ${p.pcode}
                        </div>
                        <div>
                            Category: <span class="category"><a
                                    href="${pageContext.request.contextPath}/${p.category.pcategory.toLowerCase()}">
                                    ${p.category}</a></span>
                        </div>
                    </div>
                    <div class="indivproduct col-12 col-md-2">
                        <div>

                        </div>
                        <div>

                        </div>
                        <div>

                        </div>
                    </div>
                    <div class="indivproduct col-12 col-md-2">
                        <div class="price">
                            Price:
                            <c:if test="${p.sales>0}">
                                <del>${p.price}€</del>
                            </c:if>
                            ${p.price-p.sales}€
                        </div>
                        <div>

                        </div>
                        <security:authorize access="hasRole('ADMIN')">
                            <div class="btncart">
                                <a class="btn btn-outline-primary" role="button" href="${updateLink}" >Update</a>
                            </div>
                            <div class="btncart">
                                <a class="btn btn-outline-primary" role="button" href="${deleteLink}" 
                                   onclick="if (!(confirm('Are you sure you want to delete product with name: ${p.name} and code: ${p.pcode}?')))
                                               return false"
                                   >Delete</a>

                            </div>
                        </security:authorize>
                        <c:choose>
                            <c:when test="${p.quantity>0}">
                                <div class="btncart">
                                    <a class="btn btn-danger" href="${addToCart}" role="button">Add to Cart</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="outOfStock">
                                    <img src="${pageContext.request.contextPath}/static/images/soldOut.png" alt="soldOut" >
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>

            </c:forEach>

        </div>

        <div class="container-fluid p-0">
            <img class="d-block w-100" src="${pageContext.request.contextPath}/static/images/advert.jpg" alt="advert" >
        </div>

        <div class="container mt-5">

            <div class="title">
                <img src="${pageContext.request.contextPath}/static/geekfonts/ProductsOnSale.png" alt="productssales">
            </div>

            <!-- <h3 class="text-md-left">Products with sale</h3>-->
            <c:forEach items="${salesProducts}" var="q">
                <c:url var="addToCart" value="/cart/addToCart">
                    <c:param name="productId" value="${q.id}" />
                    <c:param name="jspName" value="products" />
                </c:url>
                <c:url value="/${q.category.pcategory.toLowerCase()}/update" var="updateLink" >
                    <c:param name="productId" value="${q.id}" />
                </c:url>

                <c:url value="/${q.category.pcategory.toLowerCase()}/delete" var="deleteLink">
                    <c:param name="productId" value="${q.id}" />
                </c:url>
                <div class="divproduct row col-12 col-md-12">

                    <a href="${pageContext.request.contextPath}/${q.category.pcategory.toLowerCase()}/information/${q.id}">
                        <img class="col-12 col-md-2 my-2" src="http://localhost:8080/images/${q.id}.jpg" alt="TODO" height="100%" width="100%">
                    </a>

                    <div class="indivproduct col-12 col-md-6">
                        <div>
                            <span class="category"><a href="${pageContext.request.contextPath}/${q.category.pcategory.toLowerCase()}/information/${q.id}">${q.name}</a></span>
                        </div>
                        <div>
                            Code: ${q.pcode}
                        </div>
                        <div>
                            Category: <span class="category"><a
                                    href="${pageContext.request.contextPath}/${q.category.pcategory.toLowerCase()}">
                                    ${q.category}</a></span>
                        </div>
                    </div>
                    <div class="indivproduct col-12 col-md-2">
                        <div>

                        </div>
                        <div>

                        </div>
                        <div>

                        </div>
                    </div>
                    <div class="indivproduct col-12 col-md-2">
                        <div class="price">
                            Price:
                            <c:if test="${q.sales>0}">
                                <del>${q.price}€</del>
                            </c:if>
                            ${q.price-q.sales}€
                        </div>
                        <security:authorize access="hasRole('ADMIN')">
                            <div class="btncart">
                                <a class="btn btn-outline-primary" role="button" href="${updateLink}" >Update</a>
                            </div>
                            <div class="btncart">
                                <a class="btn btn-outline-primary" role="button" href="${deleteLink}" 
                                   onclick="if (!(confirm('Are you sure you want to delete product with name: ${q.name} and code: ${q.pcode}?')))
                                               return false"
                                   >Delete</a>

                            </div>
                        </security:authorize>
                        <c:choose>
                            <c:when test="${q.quantity>0}">
                                <div class="btncart">
                                    <a class="btn btn-danger" href="${addToCart}" role="button">Add to Cart</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="outOfStock">
                                    <img class="col-12 col-md-2 my-2" src="${pageContext.request.contextPath}/static/images/soldOut.png" alt="TODO" >
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>



            </c:forEach>
        </div>


        <jsp:include page="footer.jsp"/>



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