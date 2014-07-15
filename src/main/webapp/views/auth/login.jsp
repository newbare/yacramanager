<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en" ng-app="yaCRAApp">
<head>
<meta charset="utf-8">
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<title>Sign in &middot; Yacra Manager</title>
<link href="${contextPath}/assets/css/site.css" rel="stylesheet">
<link href="${contextPath}/assets/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/assets/css/signin.css" rel="stylesheet">
<link rel="stylesheet" href="${contextPath}/assets/bower_components/fontawesome/css/font-awesome.min.css">

<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/fontawesome/css/font-awesome.min.css">
</head>
<%-- data-ng-init="error=${error} , errorMessage='${errorMessage}'" --%>
<body >
	<div data-ng-view="" class="am-fade"></div>
	<script src="${contextPath}/assets/bower_components/jquery/dist/jquery.min.js"></script>
	<script src="${contextPath}/assets/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular/angular.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-resource/angular-resource.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-route/angular-route.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-animate/angular-animate.min.js"></script>
	<script src="${contextPath}/assets/js/app/login.js"></script>
</body>
</html>
