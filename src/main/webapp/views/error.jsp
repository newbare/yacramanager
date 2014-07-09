<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="fr" ng-app="yaCRAApp">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
<%@ page contentType="application/json" pageEncoding="UTF-8"%>
{
 status:<%=request.getAttribute("javax.servlet.error.status_code") %>,
 reason:<%=request.getAttribute("javax.servlet.error.message") %>
}
</body>
</html>