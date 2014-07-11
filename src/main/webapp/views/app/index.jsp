<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="fr" ng-app="yaCRAApp">
<head>
<title >YACRA Manager</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${contextPath}/assets/css/app.css">
<link rel="stylesheet" href="${contextPath}/assets/css/yacra-fonts.css">
<link rel="stylesheet" href="${contextPath}/assets/bower_components/bootstrap-additions/dist/bootstrap-additions.min.css">
<link rel="stylesheet" href="${contextPath}/assets/css/angular-motion.min.css">
<link rel="stylesheet" href="${contextPath}/assets/bower_components/ng-table/ng-table.min.css">
<link rel="stylesheet" href="${contextPath}/assets/bower_components/jquery.gritter/css/jquery.gritter.css">
<link rel="stylesheet" href="${contextPath}/assets/css/gritter.css">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/fontawesome/css/font-awesome.min.css">
<script	src="${contextPath}/assets/bower_components/jquery/dist/jquery.min.js"></script>
<script src="${contextPath}/assets/bower_components/angular/angular.js"></script>
	<script src="${contextPath}/assets/js/app/application.js"></script>
		<script src="${contextPath}/assets/js/app/controllers/app-controller.js"></script>

 <script type="text/javascript">
    var _contextPath = "${contextPath}";
</script>
</head>
<body data-ng-controller="AppCtrl" data-web-socket>
	
	    <div id="wrapper">

        <!-- Sidebar -->
        <div id="sidebar-wrapper">
            <ul class="sidebar-nav">
                <li class="sidebar-brand"><a href="#">Start Bootstrap</a>
                </li>
                <li><a href="#">Dashboard</a>
                </li>
                <li><a href="#" class="active">Shortcuts</a>
                </li>
                <li><a href="#">Overview</a>
                </li>
                <li><a href="#">Events</a>
                </li>
                <li><a href="#">About</a>
                </li>
                <li><a href="#">Services</a>
                </li>
                <li><a href="#">Contact</a>
                </li>
            </ul>
        </div>

        <!-- Page content -->
        <div id="page-content-wrapper">
            
            <!-- Keep all page content within the page-content inset div! -->
            <div class="page-content inset">
            	<!-- Fixed navbar -->
				<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
					<div class="container-fluid">
						<div class="navbar-header">
							<button type="button" class="navbar-toggle" data-toggle="collapse"
								data-target=".navbar-collapse">
								<span class="sr-only">Toggle navigation</span> <span
									class="icon-bar"></span> <span class="icon-bar"></span> <span
									class="icon-bar"></span>
							</button>
							<a class="navbar-brand" href="#"><i class="fa fa-bolt fa-1x"></i><span> YACRA</span></a>
						</div>
						<div class="navbar-collapse collapse">
							<ul class="nav navbar-nav navbar-right">
								<li data-ng-class="navClass('home')"><a data-ui-sref="home">Home</a></li>
								<li data-ng-class="navClass('frais')"><a data-ui-sref="frais">Frais</a></li>
								<li data-ng-class="navClass('cra')"><a data-ui-sref="cra">CRA</a></li>
								<li data-ng-class="navClass('absences')"><a data-ui-sref="absences" >Absences</a></li>
								<li data-has-role="ROLE_SSII_ADMIN" data-ng-class="navClass('entreprise')"><a data-ui-sref="entreprise" >Entreprise</a></li>
								<li data-has-role="ROLE_ADMIN" data-ng-class="navClass('admin')"><a data-ui-sref="admin">Admin</a></li>
								<li data-ng-class="navClass('notifications')"><a data-ui-sref="notifications">Notifications</a></li>
								<li data-ng-class="navClass('user-settings') + navClass('user-profile')" class="dropdown"><a href="" class="dropdown-toggle"
									data-toggle="dropdown"><i class="fa fa-user"></i>{{userInfo.prenom}} <span class="caret"></span></a>
									<ul class="user-menu dropdown-menu" role="menu">
										<li><a data-ui-sref="user-settings"><i class="fa fa-cog"></i>Settings</a></li>
										<li><a data-ui-sref="user-profile"><i class="fa fa-user"></i> Profile</a></li>
										<li class="divider"></li>
										<li><a href="${contextPath}/auth/logout"><i
												class="fa fa-power-off"></i>Logout</a></li>
									</ul>
								</li>
							</ul>
						</div>
						<!--/.nav-collapse -->
					</div>
				</div>
				<!-- Header nav bar -->
				<div class="page-header">
					<h3>
						{{page.title}} <small><i class="fa fa-chevron-right" style="font-size: 40%"></i><i class="fa fa-chevron-right" style="font-size: 40%"></i> {{page.description}}</small>
					</h3>
				</div>
				<div  data-ui-view="" class="am-fade-and-scale"></div>
            </div>
        </div>

    </div>
	
	
	
	
	
	
	<!-- 
	

	<div class="main container">
		<div id="wrapper">
		    <div id="sidebar-wrapper">
		        <ul class="sidebar-nav">
		            <li class="sidebar-brand"><a href="#">Home</a></li>
		            <li><a href="#">Another link</a></li>
		            <li><a href="#">Next link</a></li>
		            <li><a href="#">Last link</a></li>
		        </ul>
		    </div>
		    <div id="page-content-wrapper">
		        <div class="page-content">
		            <div class="container">
		                <div class="row">
		                    <div class="col-md-12">
		                        content of page
		                        <div class="page-header">
							<h3>
								{{page.title}} <small><i class="fa fa-chevron-right" style="font-size: 40%"></i><i class="fa fa-chevron-right" style="font-size: 40%"></i> {{page.description}}</small>
							</h3>
						</div>
						<div  data-ui-view="" class="am-fade-and-scale"></div>
		                    </div>
		                </div>
		            </div>
		        </div>
		    </div>
		</div>
		
		
		<div class="row">
			<div class="col-md-2">
				<ul class="nav nav-pills nav-stacked side-nav" >
				  <li class="active"><a href="#">Home</a></li>
				  <li><a href="#">Profile</a></li>
				  <li><a href="#">Messages</a></li>
				</ul>
			</div>
			<div class="col-md-10">
				<div class="page-header">
					<h3>
						{{page.title}} <small><i class="fa fa-chevron-right" style="font-size: 40%"></i><i class="fa fa-chevron-right" style="font-size: 40%"></i> {{page.description}}</small>
					</h3>
				</div>
				<div  data-ui-view="" class="am-fade-and-scale"></div>
			</div>
		</div>
	</div>
	Footer
	<div class="footer">
      <div class="container">
        <p class="text-muted">@ wati 2014</p>
      </div>
    </div> -->
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	
	<script src="${contextPath}/assets/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	<script src="${contextPath}/assets/bower_components/ng-file-upload/angular-file-upload-shim.min.js"></script> 
	<%-- <script src="${contextPath}/assets/bower_components/angular/angular.js"></script> --%>
	<script src="${contextPath}/assets/bower_components/ng-file-upload/angular-file-upload.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-resource/angular-resource.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-route/angular-route.min.js"></script>
	<script src="${contextPath}/assets/js/bootstrap.file-input.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-strap/dist/angular-strap.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-animate/angular-animate.min.js"></script>
	<script src="${contextPath}/assets/bower_components/ng-table/ng-table.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-strap/dist/modules/alert.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-strap/dist/modules/select.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-strap/dist/modules/modal.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-strap/dist/angular-strap.tpl.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-strap/dist/modules/datepicker.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-strap/dist/modules//tooltip.js"></script>
	<script src="${contextPath}/assets/bower_components/jquery.gritter/js/jquery.gritter.min.js"></script>
	<script src="${contextPath}/assets/js/sockjs-0.3.4.js"></script>
	<script src="${contextPath}/assets/js/stomp.js"></script>
	
	
	

<!-- 	<script src="${contextPath}/assets/js/app/filters/commons-filters.js"></script> -->
	<script src="${contextPath}/assets/js/app/factories/users-factories.js"></script>
	<script src="${contextPath}/assets/js/app/controllers/home-controller.js"></script>
	<script src="${contextPath}/assets/js/app/controllers/admin-controller.js"></script>
	<script src="${contextPath}/assets/js/app/controllers/frais-controllers.js"></script>
	<script src="${contextPath}/assets/js/app/controllers/entreprise-controller.js"></script>
	
	<script src="${contextPath}/assets/js/app/factories/cra-factories.js"></script>
	<script src="${contextPath}/assets/js/app/factories/notes-factories.js"></script>
	<script src="${contextPath}/assets/js/app/factories/absences-factories.js"></script>
	<script src="${contextPath}/assets/js/app/services/common-services.js"></script>
	<script src="${contextPath}/assets/js/app/directives/commons-directives.js"></script>
	<script src="${contextPath}/assets/js/app/controllers/users-controller.js"></script>
	<script src="${contextPath}/assets/js/app/controllers/absences-controller.js"></script>
	<script src="${contextPath}/assets/js/app/controllers/cra-controller.js"></script>
	<script src="${contextPath}/assets/js/app/controllers/user-settings-controller.js"></script>
	<script src="${contextPath}/assets/js/app/controllers/user-profile-controller.js"></script>
</body>
</html>
