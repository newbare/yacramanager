<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en" ng-app="yaCRAApp">
<head>
<title>Stock Trading Portfolio</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet"
	href="assets/css/font-awesome/css/font-awesome.min.css">


<!-- Optional theme -->
<!-- <link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css"> -->
<link rel="stylesheet" href="assets/css/app.css">

<!-- Latest compiled and minified JavaScript -->
<!-- <script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
</head>
<body>
	<!-- Header nav bar -->
	<!-- Fixed navbar -->
	<div class="navbar navbar-default navbar-fixed-top" role="navigation" ng-controller="NavCtrl">
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
					<li ng-class="navClass('home')"><a href='' ng-click="loadHome()">Home</a></li>
					<li class="active"><a href="#cra">CRA</a></li>
					<li><a href="../navbar-static-top/">Static top</a></li>

					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">${userName} <span class="caret"></span></a>
						<ul class="user-menu dropdown-menu" role="menu">
							<li><a href="#user-settings"><i class="fa fa-cog"></i>Settings</a></li>
							<li><a href="#user-profile"><i class="fa fa-user"></i> Profile</a></li>
							<li class="divider"></li>
							<li><a href="logout"><i
									class="fa fa-power-off"></i>Logout</a></li>
						</ul></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
	<!-- End of header navbar -->

	<div class="container">
		<div class="page-header">
			<h2>
				{{page.title}} <small><i class="fa fa-chevron-right" style="font-size: 40%"></i><i class="fa fa-chevron-right" style="font-size: 40%"></i>{{page.description}}</small>
			</h2>
		</div>
		<div ng-view></div>
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
	<script src="assets/js/app/home-controller.js"></script>
	<script src="assets/js/app/cra-controller.js"></script>
	<script src="assets/js/app/user-settings-controller.js"></script>
	<script src="assets/js/app/user-profile-controller.js"></script>
</body>
</html>
