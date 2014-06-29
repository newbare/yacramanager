<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en" ng-app="yaCRAApp">
<head>
<meta charset="utf-8">
<title>Stock Trading Portfolio</title>
<meta http-equiv="Cache-Control"
	content="no-store, no-cache, must-revalidate, max-age=0">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<!-- <script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
</head>
<body>
	<!-- Header nav bar -->
	<!-- Fixed navbar -->
	<div class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">YACRA manager</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="">Home</a></li>
					<li class="active"><a href="#cra">CRA</a></li>
					<li><a href="../navbar-static-top/">Static top</a></li>
					
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">${userName} <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#settings">Settings</a></li>
							<li><a href="#profile">Profile</a></li>
							<li class="divider"></li>
							<li><a href="logout"><span class="glyphicon glyphicon-off"></span>Logout</a></li>
						</ul></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
	<!-- End of header navbar -->
	
	<div ng-view>
	</div>
	
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src="assets/js/bootstrap.min.js"></script>
	    <script src="assets/js/angular-1.0.1.js"></script>
    <script src="assets/js/angular-resource-1.0.1.js"></script>
	<script src="assets/js/app/application.js"></script>
    <script src="assets/js/app/services.js"></script>
    <script src="assets/js/app/controllers.js"></script>

</body>
</html>
