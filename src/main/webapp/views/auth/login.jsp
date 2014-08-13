<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en" ng-app="yaCRAApp">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<title>Sign in &middot; Yacra Manager</title>
<link href="${contextPath}/assets/css/site.css" rel="stylesheet">
<link
	href="${contextPath}/assets/bower_components/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${contextPath}/assets/css/signin.css" rel="stylesheet">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/angular-motion/dist/angular-motion.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/fontawesome/css/font-awesome.min.css">
</head>
<%-- data-ng-init="error=${error} , errorMessage='${errorMessage}'" --%>
<script type="text/javascript">
	var _contextPath = "${contextPath}";
</script>
<c:if test="${error==true}">
	<c:if test="${not empty errorMessage}">
		<script type="text/javascript">
			var error = "${error}";
			var errorMessage = "${errorMessage}";
		</script>
	</c:if>
	</div>
</c:if>
<body>
	<div class="container">
		<div id="alerts-container"></div>
		<div data-ng-view="" class="am-fade"></div>
	</div>
	<script
		src="${contextPath}/assets/bower_components/jquery/dist/jquery.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular/angular.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-resource/angular-resource.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-route/angular-route.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-animate/angular-animate.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/angular-strap.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules/modal.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules/alert.min.js"></script>
	<script src="${contextPath}/assets/js/app/login.js"></script>
</body>
</html>
