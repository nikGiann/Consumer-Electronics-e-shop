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
        <title>RAM</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />
    </head>

    <body>

        <jsp:include page="header.jsp"/>

        <div class="container mt-5">
            <security:authorize access="hasRole('ADMIN')">
                <div id="form">
                    <c:choose>
                        <c:when test="${product.id==null}">
                            <h3>Add new RAM</h3>
                        </c:when>
                        <c:otherwise>
                            <h3>Update RAM</h3>
                        </c:otherwise>
                    </c:choose>
                    <form:form 
                        action="${pageContext.request.contextPath}/ram/add" 
                        method="POST"
                        modelAttribute="product"
                        enctype="multipart/form-data">

                        <form:hidden path="id"/>
                        <div class="formes col-12 row">
                            <div class="col-3">
                                <label for="name">Enter Name:</label>

                                <form:input path="name" id="name" class="form-control" maxlength="80"/><span style="color:red;"><c:out value="${msgName}"/></span>
                            </div >
                            <div class="col-3">
                                <label for="code">Enter Code:</label>
                                <form:input path="pcode" id="code" class="form-control" maxlength="30"/><span style="color:red;"><c:out value="${msgCode}"/></span>

                                <!-- form:errors path="password" cssClass="error"/-->
                            </div>
                            <div class="col-3">
                                <label for="price">Enter Price:</label>

                                <input type="text" id="price" class="form-control" maxlength="6" value="${pricee}" name="pricee"/><span style="color:red;"><c:out value="${msgNumbersPrice}"/></span>
                            </div>
                            <div class="col-3">
                                <label for="discount">Enter discount:</label>
                                <input type="text" id="discount" class="form-control" maxlength="6" value="${discountt}" name="discountt"/><span style="color:red;"><c:out value="${msgNumbersDiscount}"/></span>

                            </div>
                        </div>
                        <div class="formes col-12 row">
                            <div class="col-3">
                                <label for="ammount">Enter ammount:</label>

                                <input type="text" id="ammount" class="form-control" maxlength="6" value="${ammountt}" name="ammountt"/><span style="color:red;"><c:out value="${msgNumbersAmmount}"/></span>

                            </div>
                            <div class="col-3">
                                <label for="manufacturer">Select Manufacturer:</label>
                                <select id="manufacturer" class="form-control" name="manufacturer">
                                    <optgroup label = "Select Manufacturer">
                                        <c:forEach items="${manufacturers}" var="m">
                                            <option value="${m.getId()}">${m.name}</option>
                                        </c:forEach>
                                    </optgroup>
                                </select>
                            </div>
                            <div class="col-3">
                                <label for="size">Select Size:</label>
                                <select id="size" class="form-control" name="size">

                                    <optgroup label = "Select Size">
                                        <c:forEach items="${sizes}" var="m"> 
                                            <option  value="${m.getId()}">${m.name}</option>
                                        </c:forEach>
                                    </optgroup>
                                </select>
                            </div>
                            <div class="col-3">
                                <label for="type">Select Type:</label>
                                <select id="type" class="form-control" name = "type">
                                    <optgroup label = "Select Type">
                                        <c:forEach items="${types}" var="t">
                                            <option value="${t.getId()}">${t.name}</option>
                                        </c:forEach>
                                    </optgroup>
                                </select>
                            </div>
                            <div class="formes col-12 row">
                                <div class="col-6">
                                    <label for="description">Enter Description:</label>
                                    <form:textarea path="description" id="description" class="form-control" value="" placeholder="..."/>
                                </div>
                                <div class="col-3">
                                    <label for="voltage">Select Type:</label>
                                    <select id="voltage" class="form-control" name = "voltage">
                                        <optgroup label = "Select Voltage">
                                            <c:forEach items="${voltages}" var="c"> 
                                                <option value="${c}">${c}</option>
                                            </c:forEach>
                                        </optgroup>
                                    </select>
                                </div>
                                <div class="col-3">
                                    <label for="frequency">Select Frequency:</label>
                                    <select id="frequency" class="form-control" name = "frequency">                                    
                                        <optgroup label = "Select Frequency">
                                            <c:forEach items="${frequencies}" var="c"> 
                                                <option value="${c}">${c}</option>
                                            </c:forEach>
                                        </optgroup>
                                    </select>
                                </div>
                            </div>

                            <div class="formes col-12 row">
                                <div class="col-6">
                                    <p>Select a file to upload:</p>
                                    <input type="file" name="file"/> <span style="color:red;"><c:out value="${msgImg}"/></span>
                                </div>
                                <div class="col-3">
                                    <span style="color:red;"><c:out value="${msgGeneral}"/></span>
                                    <input type="submit" value="Register">
                                </div>

                            </div>
                        </form:form>
                    </div>
                </security:authorize>
            </div>
        </div>

        <div class="container mt-5">

            <h3>Filters</h3>

            <div class="btn-group">
                <div class="btn-group">
                    <button type="button" class="btn bg-dark dropdown-toggle" data-toggle="dropdown">
                        Price
                    </button>

                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/ram/price/0/75">up to 75€</a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/ram/price/75/150">75€ up to 150€</a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/ram/price/150/225">150€ up to 225€</a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/ram/price/225/300">225€ up to 300€</a>
                    </div>
                </div>


                <!--MANUFACTURER-->
                <div class="btn-group">
                    <button type="button" class="btn bg-dark dropdown-toggle" data-toggle="dropdown">
                        Manufacturer
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <c:forEach items="${manufacturers}" var="p">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/ram/manufacturer/${p.getId()}">${p.name}</a>
                        </c:forEach>
                    </div>
                </div>
                <br/>

                <!--TYPE-->
                <div class="btn-group">
                    <button type="button" class="btn bg-dark dropdown-toggle" data-toggle="dropdown">
                        Type
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <c:forEach items="${types}" var="p">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/ram/type/${p.getId()}">${p.name}</a>
                        </c:forEach>
                    </div>
                </div>

                <!--SIZE-->
                <div class="btn-group">
                    <button type="button" class="btn bg-dark dropdown-toggle" data-toggle="dropdown">
                        Size
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <c:forEach items="${sizes}" var="p">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/ram/size/${p.getId()}">${p.name}</a>
                        </c:forEach>
                    </div>
                </div>

                <!--FREQUENCY-->
                <div class="btn-group">
                    <button type="button" class="btn bg-dark dropdown-toggle" data-toggle="dropdown">
                        Frequency
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <c:forEach items="${frequencies}" var="p">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/ram/frequency/${p}">${p}MHz</a>
                        </c:forEach>
                    </div>
                </div>

                <!--VOLTAGE-->
                <div class="btn-group">
                    <button type="button" class="btn bg-dark dropdown-toggle" data-toggle="dropdown">
                        Voltage
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <c:forEach items="${voltages}" var="p">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/ram/voltage/${p}">${p}V</a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <div class="container mt-5">
            <!--            <h3 class="text-md-left">Ram</h3>-->
            <div class="title">
                <img src="${pageContext.request.contextPath}/static/geekfonts/ram.png" alt="ram">
            </div>
            <c:choose>
                <c:when test="${productList.size()>0}">
                    <c:forEach items="${productList}" var="q" varStatus="status">
                        <c:url var="addToCart" value="/cart/addToCart">
                            <c:param name="productId" value="${q.id}" />
                            <c:param name="jspName" value="ram"/>
                        </c:url>
                        <c:url value="/ram/update" var="updateLink" >
                            <c:param name="productId" value="${q.id}" />
                        </c:url>

                        <c:url value="/ram/delete" var="deleteLink">
                            <c:param name="productId" value="${q.id}" />
                        </c:url>

                        <!--more information*************************************************************************************-->
                        <c:url value="/ram/information/${q.id}" var="informationLink">
                            <c:param name="productId" value="${q.id}" />
                        </c:url>
                        <div class="divproduct row col-12 col-md-12">

                            <a href="${informationLink}"><img class="col-12 col-md-2 my-2" src="http://localhost:8080/images/${q.id}.jpg" alt="TODO" ></a>

                            <div class="indivproduct col-12 col-md-6">
                                <div>
                                    <p><a href="${informationLink}">${q.name}<a/></p>
                                </div>
                                <div class="row">
                                    <div class="col-4">Code: ${q.pcode}</div>
                                    <div class="col-4"> Voltage ${ramList[status.index].getVoltage()}V</div>
                                    <div class="col-4">Frequency: ${ramList[status.index].getFrequency()}MHz</div>
                                </div>

                                <div class="row">
                                    <div class="col-4">Category: <span class="category"><a
                                                href="${pageContext.request.contextPath}/ram">
                                                ${q.category}</a></span></div>
                                    <div class="col-4">Manufacturer: ${ramList[status.index].manufacturer}</div>
                                    <div class="col-4">Size: ${ramList[status.index].size1}</div>
                                </div>
                            </div>
                            <div class="indivproduct col-12 col-md-2">
                                <div>

                                </div>
                                <div>

                                </div>
                                <div>
                                    Type: ${ramList[status.index].type}<br/>
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
                                <c:choose>
                                    <c:when test="${q.quantity>0}">
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
                </c:when>    
                <c:otherwise>
                    <br/>
                    No Results Found
                </c:otherwise>
            </c:choose>    
        </div>


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