<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en" ng-app="yaCRAApp">
<head>
<meta charset="utf-8">
<title>Sign in &middot; Yacra Manager</title>
<link href="../assets/css/site.css" rel="stylesheet">
<link href="../assets/css/bootstrap.min.css" rel="stylesheet">
<link href="../assets/css/signin.css" rel="stylesheet">
<link rel="stylesheet" href="../assets/css/angular-motion.min.css">

<link rel="stylesheet"
	href="../assets/css/font-awesome/css/font-awesome.min.css">
</head>
<%-- data-ng-init="error=${error} , errorMessage='${errorMessage}'" --%>
<body >
	<div data-ng-view="" class="am-fade-and-scale"></div>
	<script src="../assets/js/jquery.js"></script>
	<script src="../assets/js/bootstrap.min.js"></script>
	<script src="../assets/js/angular.js"></script>
	<script src="../assets/js/angular-resource.min.js"></script>
	<script src="../assets/js/angular-route.min.js"></script>
	<script src="../assets/js/angular-animate.min.js"></script>
	<script src="../assets/js/app/login.js"></script>
</body>
</html>
