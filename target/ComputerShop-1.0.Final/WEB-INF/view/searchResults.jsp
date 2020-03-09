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
        <title>Products</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />


    </head>
    <body>

        <jsp:include page="header.jsp"/>

        <div class="container mt-5">
            <h3 class="text-md-left">Products</h3>

            <c:choose>
                <c:when test="${list.size()>0}">
                    <c:forEach items="${list}" var="q">
                        <c:url var="addToCart" value="/cart/addToCart">
                            <c:param name="productId" value="${q.id}" />
                            <c:param name="jspName" value="products"/>
                        </c:url>

                        <!--more information*************************************************************************************-->
                        <c:url value="/${q.category.pcategory.toLowerCase()}/information/${q.id}" var="informationLink">
                            <c:param name="productId" value="${q.id}" />
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
                                    <p><a href="${pageContext.request.contextPath}/${q.category.pcategory.toLowerCase()}/information/${q.id}">${q.name}</a><p>
                                </div>
                                <div class="row">
                                    <div class="col-4">Code: ${q.pcode}</div>
                                </div>
                                <div class="row">
                                    <div class="col-4">Category: <span class="category"><a
                                                href="${pageContext.request.contextPath}/${q.category.pcategory.toLowerCase()}">
                                                ${q.category}</a></span></div>
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
                                    Price: <c:if test="${q.sales>0}">
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
                                <div class="btncart">
                                    <a class="btn btn-danger" href="${addToCart}" role="button">Add to Cart</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>    
                <c:otherwise>
                    <br/>
                    No Results Found
                </c:otherwise>
            </c:choose> 
        </div>

        <jsp:include page="footer.jsp"/>
    </body>
</html>
