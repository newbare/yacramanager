<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en" ng-app="yaCRAApp">
<head>
<title>Stock Trading Portfolio</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="assets/css/app.css">
<!-- <link rel="stylesheet" href="assets/css/datepicker/datepicker3.css"> -->
<link rel="stylesheet" href="assets/css/bootstrap-additions.min.css">
<link rel="stylesheet" href="assets/css/angular-motion.min.css">
<link rel="stylesheet" href="assets/css/ng-table.min.css">
<link rel="stylesheet" href="assets/css/jquery-ui.min.css">
<link rel="stylesheet" href="assets/css/jquery.gritter.css">
<link rel="stylesheet" href="assets/css/gritter.css">




<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet"
	href="assets/css/font-awesome/css/font-awesome.min.css">
	


<!-- Optional theme -->
<!-- <link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css"> -->



<!-- Latest compiled and minified JavaScript -->
<!-- <script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
</head>
<body data-ng-controller="AppCtrl" data-web-socket>
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
				<a class="navbar-brand" href="#"><i class="fa fa-bolt fa-1x"></i><span> YACRA manager</span></a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li ng-class="navClass('home')"><a href='' ng-click="loadHome()"><i class="fa fa-home"></i>Home</a></li>
					<li ng-class="navClass('cra')"><a href='' ng-click="loadCRA()"><i class="fa fa-calendar"></i>CRA</a></li>
					<li ng-class="navClass('absences')"><a href="" ng-click="loadAbsences()" >Absences</a></li>
					<li ng-class="navClass('notifications')"><a href='' ng-click="loadHome()"><i class="fa fa-bell"></i>Notifications<span class="badge pull-right">42</span></a></li>
					<li data-has-role="ROLE_SSII_ADMIN" ng-class="navClass('user-settings') + navClass('user-profile')" class="dropdown"><a href="" class="dropdown-toggle"
						data-toggle="dropdown"><i class="fa fa-university"></i>Entreprise <span class="caret"></span></a>
						<ul class="user-menu dropdown-menu" role="menu">
							<li><a href="#user-settings"><i class="fa fa-users"></i>Salariés</a></li>
							<li class="divider"></li>
							<li><a href="#user-settings"><i class="fa fa-cog"></i>Settings</a></li>
						</ul>
					</li>
					<li data-has-role="ROLE_ADMIN" ng-class="navClass('notifications')"><a href='' ng-click="loadAdmin()"><i class="fa fa-gear fa-spin"></i>Admin</a></li>
					<li ng-class="navClass('user-settings') + navClass('user-profile')" class="dropdown"><a href="" class="dropdown-toggle"
						data-toggle="dropdown"><i class="fa fa-user"></i>{{userInfo.prenom}} <span class="caret"></span></a>
						<ul class="user-menu dropdown-menu" role="menu">
							<li><a href="#user-settings"><i class="fa fa-cog"></i>Settings</a></li>
							<li><a href="#user-profile"><i class="fa fa-user"></i> Profile</a></li>
							<li class="divider"></li>
							<li><a href="logout"><i
									class="fa fa-power-off"></i>Logout</a></li>
						</ul>
					</li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
	<!-- End of header navbar -->

	<div class="main container">
		<div class="page-header">
			<h2>
				{{page.title}} <small><i class="fa fa-chevron-right" style="font-size: 40%"></i><i class="fa fa-chevron-right" style="font-size: 40%"></i> {{page.description}}</small>
			</h2>
		</div>
		<div ng-view="" class="am-fade-and-scale"></div>
	</div>
	<!-- Footer -->
	<div class="footer">
      <div class="container">
        <p class="text-muted">Place sticky footer content here.</p>
      </div>
    </div>
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src="assets/js/bootstrap.min.js"></script>
	<script src="assets/js/angular.js"></script>
	<script src="assets/js/angular-resource.min.js"></script>
	<script src="assets/js/angular-route.min.js"></script>
	<!-- <script src="assets/js/i18n/angular-locale_fr.js"></script> -->
	<script src="assets/js/plugins/angular-strap/angular-strap.min.js"></script>
	<script src="assets/js/angular-animate.min.js"></script>
	<script src="assets/js/ng-table.min.js"></script>
	<script src="assets/js/plugins/angular-strap/alert.js"></script>
	<script src="assets/js/plugins/angular-strap/modal.js"></script>
	<script src="assets/js/plugins/angular-strap/angular-strap.tpl.min.js"></script>
	<script src="assets/js/plugins/angular-strap/datepicker.js"></script>
	<script src="assets/js/plugins/angular-strap/tooltip.js"></script>
	<script src="assets/js/plugins/gritter/jquery.gritter.min.js"></script>
	<script src="assets/js/sockjs-0.3.4.js"></script>
	<script src="assets/js/stomp.js"></script>
	<script src="assets/js/app/application.js"></script>
		<script src="assets/js/app/controllers/app-controller.js"></script>
	<script src="assets/js/app/factories/users-factories.js"></script>
	<script src="assets/js/app/controllers/home-controller.js"></script>
	<script src="assets/js/app/factories/cra-factories.js"></script>
	
	<script src="assets/js/app/factories/absences-factories.js"></script>
	<script src="assets/js/app/services/common-services.js"></script>
	<script src="assets/js/app/directives/commons-directives.js"></script>
	<script src="assets/js/app/controllers/users-controller.js"></script>
	<script src="assets/js/app/controllers/absences-controller.js"></script>
	<script src="assets/js/app/controllers/cra-controller.js"></script>
	<script src="assets/js/app/controllers/user-settings-controller.js"></script>
	<script src="assets/js/app/controllers/user-profile-controller.js"></script>
</body>
</html>
