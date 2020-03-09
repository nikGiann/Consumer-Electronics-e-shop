<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Bootstrap Simple Login Form</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> 
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/loginForm.css" />
    
    </head>
    <body>
        <div class="login-form">
            <c:if test="${param.error !=null}">
                <div class="alert alert-danger">
                    <i>Invalid username/password</i>
                </div>
            </c:if>
            <c:if test="${param.logout !=null}">
                <div class="alert alert-success">
                    <i>Logged out successfully</i>
                </div>
            </c:if>

            <form:form action="${pageContext.request.contextPath}/authenticate" method="POST">
                <a href="${pageContext.request.contextPath}" class="logo">
                    <img src="${pageContext.request.contextPath}/static/images/GeekLogo.png" alt="logo_jambashop.gr">
                </a>
                <div class="form-group">
                    <input name="username" type="text" class="form-control" placeholder="Username" required="required" maxlength="30">
                </div>
                <div class="form-group">
                    <input name="password" type="password" class="form-control" placeholder="Password" required="required" maxlength="68">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block">Log in</button>
                </div>
               
                     <a id="signup" href ="${pageContext.request.contextPath}/user/showForm" method="GET">Sign up</a>
            
            </form:form>


        </div>
    </body>
</html>                                                                    




