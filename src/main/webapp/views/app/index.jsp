<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="fr" ng-app="yaCRAApp">
<head>
<title data-update-title></title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${contextPath}/assets/css/app.css">
<link rel="stylesheet" href="${contextPath}/assets/css/flags/flags.css">
<link rel="stylesheet"
	href="${contextPath}/assets/css/angular-criterias.css">
<link rel="stylesheet"
	href="${contextPath}/assets/css/jquery.jOrgChart.css">
<link rel="stylesheet" href="${contextPath}/assets/css/yacra-fonts.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/bootstrap-additions/dist/bootstrap-additions.min.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/angular-motion/dist/angular-motion.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/angular-loading-bar/build/loading-bar.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/bootstrap-daterangepicker/daterangepicker-bs3.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/ng-table/ng-table.min.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/angular-xeditable/dist/css/xeditable.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/angular-bootstrap-colorpicker/css/colorpicker.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/ngQuickDate/dist/ng-quick-date.css">
	<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/ngQuickDate/dist/ng-quick-date-default-theme.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/fullcalendar/fullcalendar.css">
	<link rel="stylesheet" href="${contextPath}/assets/bower_components/chosen/chosen.min.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/jquery.gritter/css/jquery.gritter.css">
<link rel="stylesheet" href="${contextPath}/assets/css/gritter.css">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bower_components/fontawesome/css/font-awesome.min.css">

<script type="text/javascript">
	var _contextPath = "${contextPath}";
	var _userId = "${userId}";
	var _userCompanyName = "${userCompanyName}";
	var _userCompanyId = "${userCompanyId}";
</script>
</head>
<body data-ng-controller="AppCtrl" data-web-socket data-auth-application-support class="waiting-for-angular" data-events-to-wait="eventsToWait" data-application-loading-support data-connection-lost-support>
	<div id="initializing-panel"></div>
	<div id="app-login-content" data-ng-controller="LoginCtrl">
		<div class="row">
			<div class=" auth-form col-md-4 col-md-offset-4 col-xs-10 col-xs-offset-1">
		
				<form data-ng-submit="submit()">
					<h3 class="form-signin-heading" style="text-align: center;">
						Session expired
					</h3>
					<hr>
					<div class="alert alert-danger alert-dismissable" role="alert"
						data-ng-show="error">
						<button type="button" class="close" data-dismiss="alert">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						{{errorMessage}}
					</div>
					<div class="margin-bottom">
						<div class="input-group margin-bottom-sm">
							<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
							<input class="form-control" type="text" name="username" data-ng-model="username"
								placeholder="Username" required autofocus>
						</div>
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-key fa-fw"></i></span>
							<input class="form-control" type="password" name="password" data-ng-model="password"
								placeholder="Password" required>
						</div>
					</div>
					<br>
					<button class="btn btn-lg btn-primary btn-block" type="submit">
						<span class="bigger-110"> Login</span>
					</button>
				</form>
			</div>
		</div>
	</div>
	<div id="app-content">
	<!-- Fixed navbar -->
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation"
		data-ng-cloak>
		<div data-ng-class="containerNavClass()">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#"><i class="fa fa-bolt fa-1x"></i><span>
						YACRA</span></a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navtop navbar-nav navbar-left">
					<li data-has-role="ROLE_ADMIN"
						data-ng-class="navClass('admin/')"><a
						data-ui-sref="admin">{{'app.navbar.menu.admin' | translate}}</a></li>
					<li data-has-role="ROLE_SSII_ADMIN" data-ng-cloak
						data-ng-class="navClass('company/')"><a
						data-ui-sref="company"><i class="fa fa-university"></i>{{userInfo.company.name}}</a></li>
					<li class="dropdown" data-ng-class="navClass('frais') + navClass('cra') + navClass('absences') + navClass('timesheet')">
						<a href="" class="dropdown-toggle" data-toggle="dropdown">{{'app.navbar.menu.workspace' | translate}}<span class="caret"></span></a>
						<ul class="workspace-menu inverse-dropdown dropdown-menu " role="menu">
							<li data-ng-class="navClass('frais')"><a data-ui-sref="frais">{{'app.navbar.menu.frais' | translate}}</a></li>
							<li data-ng-class="navClass('cra')"><a data-ui-sref="cra">{{'app.navbar.menu.cra' | translate}}</a></li>
							<li data-ng-class="navClass('absences')"><a
								data-ui-sref="absences">{{'app.navbar.menu.absences' | translate}}</a></li>
							<li data-ng-class="navClass('timesheet')"><a
								data-ui-sref="timesheet">{{'app.navbar.menu.timesheet' | translate}}</a></li>
						</ul>
					</li>
					<li data-ng-class="navClass('tasks')">
						<a data-ui-sref="tasks"><i class="fa fa-tasks"></i>{{'app.navbar.menu.tasks' | translate}}</a>
					</li>
				</ul>
				<ul class="nav navtop navbar-nav navbar-right">
					<li data-ng-class="navClass('home')"><a data-ui-sref="home"><i class="fa fa-home"></i></a></li>
					<li>
						<div class="timer-widget dropdown" data-ng-controller="WorkLogCtrl" data-ng-class="{running: timerRunning}">
							<a data-toggle="dropdown" href=""><i class="fa fa-clock-o" ></i><span data-ng-show="timerRunning && task!==undefined">[{{task.name}}]</span></a>
							<timer autostart="false" interval="1000" data-ng-show="timerRunning">{{hhours}}:{{mminutes}}:{{sseconds}}</timer>
							<button type="button" class="btn btn-danger btn-xs"  data-ng-click="stopTimer()" data-ng-show="timerRunning">Stop</button>
							<div class="timer-widget-content dropdown-menu">
									<form role="form">
										<div class="form-group">
    										<!-- <label for="project">{{'app.navbar.timer.project' | translate}}</label> -->
											<select id="project" class="form-control"  data-chosen data-ng-change="selectProject(project)"
									          data-placeholder="Select a project"
									          data-ng-model="project"
									          data-ng-options="p.name for p in projects">
							           		<option value=""></option>
								  			</select>
								  		</div>
								  		<div class="form-group">
    										<!-- <label for="task">Task</label> -->
								  			<select id="task" class="form-control"  data-chosen data-ng-change="selectTask(task)"
										          data-placeholder="Select a task"
										          data-ng-model="task"
										          data-ng-options="t.name for t in tasks">
								           		<option value=""></option>
								  			</select>
								  		</div>
							  			 <div class="form-group">
										    <label for="description">Note</label>
										    <textarea id="description" data-ng-model="description" class="form-control" data-placeholder="Enter a note"></textarea>
										  </div>
										<button type="button" class="btn btn-success btn-xs btn-block" data-ng-click="startTimer()" data-ng-disabled="!startable">Start</button>
									</form>
							</div>
						</div>
					</li>
					<li data-ng-class="navClass('messages')" data-ng-show="userInfo.hasNewMessages"><a
						data-ui-sref="messages"><i class="fa fa-envelope"></i></a></li>
					<!-- <li data-ng-class="navClass('notifications')"><a
						data-ui-sref="notifications"><i class="fa fa-bell"></i></a></li> -->
					<li class="dropdown">
						<a class="dropdown-toggle"	data-toggle="dropdown" href=""><i class="fa fa-globe"></i>{{currentLanguage | uppercase}}</a>
						<ul class="dropdown-menu inverse-dropdown" role="menu">
							<li><a href="" data-ng-click="toggleLanguage('fr')"><img class="flag flag-fr"><span style="padding-left: 10px">Fr</span></a></li>
							<li><a href="" data-ng-click="toggleLanguage('en')"><img class="flag flag-gb"><span style="padding-left: 10px">Eng</span></a></li>
						</ul>
					</li>
					<li
						data-ng-class="navClass('user-settings') + navClass('user-profile')"
						class="dropdown"><a href="" class="dropdown-toggle"
						data-toggle="dropdown"><i class="fa fa-user"></i>{{userInfo.firstName}} {{userInfo.lastName}}<span
							class="caret"></span></a>
						<ul class="user-menu dropdown-menu inverse-dropdown" role="menu">
							<li data-ng-class="navClass('messages')" >
								<a data-ui-sref="messages"><i class="fa fa-envelope"></i>Messages</a>
							</li>
							<li class="divider"></li>
							<li><a data-ui-sref="user-settings"><i class="fa fa-cog"></i>{{'app.navbar.menu.settings' | translate}}</a></li>
							<li><a data-ui-sref="user-profile"><i class="fa fa-user"></i>
									{{'app.navbar.menu.profile' | translate}}</a></li>
							<li class="divider"></li>
							<li><a href="${contextPath}/auth/logout"><i
									class="fa fa-power-off"></i>{{'app.navbar.menu.logout' | translate}}</a></li>
						</ul></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
	<!-- End Fixed navbar -->
	<div data-ng-class="containerClass()" data-ng-cloak data-http-request-error><!-- class="global-container" --> 
		<!-- <div class="sidebar sidebar-fixed">hey i'am a side bar</div> -->
		<div class="main"><!-- <div class="main-content"> -->
			<div class="page-header">
				<h4>
					{{page.title | translate}} <i class="fa fa-angle-double-right"
						style="font-size: 80%"></i><small> {{page.description | translate}}</small>
				</h4>
			</div>
			<div id="alerts-container"></div>
			<div data-ui-view="" class="" data-ng-cloak></div>
			<div class="footer">
				<p class="text-muted">@ wati 2014</p>
			</div>
		</div>
	</div>

	</div>
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->

	<script
		src="${contextPath}/assets/bower_components/jquery/dist/jquery.min.js"></script>
		<script
		src="${contextPath}/assets/bower_components/jquery-ui/jquery-ui.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular/angular.js"></script>
	<script src="${contextPath}/assets/js/app/application.js"></script>
	<script src="${contextPath}/assets/js/jquery.jOrgChart.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/app-controller.js"></script>
	<script
		src="${contextPath}/assets/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/ng-file-upload/angular-file-upload-shim.min.js"></script>
	<%-- <script src="${contextPath}/assets/bower_components/angular/angular.js"></script> --%>
	<script
		src="${contextPath}/assets/bower_components/ng-file-upload/angular-file-upload.js"></script>
	<script src="${contextPath}/assets/js/bootstrap.file-input.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-resource/angular-resource.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-xeditable/dist/js/xeditable.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/fullcalendar/fullcalendar.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-ui-calendar/src/calendar.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-route/angular-route.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/angular-strap.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules/popover.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules/popover.tpl.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-animate/angular-animate.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-loading-bar/build/loading-bar.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/ng-table/ng-table.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules/alert.min.js"></script>
		<script
		src="${contextPath}/assets/bower_components/angular-filter/dist/angular-filter.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-daterangepicker/js/angular-daterangepicker.js"></script>
	<script
		src="${contextPath}/assets/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules/select.js"></script>
	<script
		src="${contextPath}/assets/bower_components/moment/moment.js"></script>
		<script
		src="${contextPath}/assets/bower_components/moment-range/lib/moment-range.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-translate/angular-translate.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules/modal.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-bootstrap-colorpicker/js/bootstrap-colorpicker-module.js"></script>
	<script
		src="${contextPath}/assets/bower_components/ngQuickDate/dist/ng-quick-date.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/angular-strap.tpl.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules/datepicker.min.js"></script>
		<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules/timepicker.min.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-strap/dist/modules//tooltip.js"></script>
	<script
		src="${contextPath}/assets/bower_components/angular-timer/dist/angular-timer.js"></script>
	<script
		src="${contextPath}/assets/bower_components/jquery.gritter/js/jquery.gritter.min.js"></script>
	<script src="${contextPath}/assets/js/sockjs-0.3.4.js"></script>
	<script src="${contextPath}/assets/js/stomp.js"></script>
	<script src="${contextPath}/assets/js/http-auth-interceptor.js"></script>
	<script src="${contextPath}/assets/js/colResizable-1.3.min.js"></script>
	<script src="${contextPath}/assets/bower_components/angular-chosen-localytics/chosen.js"></script>
	<script src="${contextPath}/assets/bower_components/chosen/chosen.jquery.js"></script>
	<script src="${contextPath}/assets/js/app/filters/commons-filters.js"></script>
	<script src="${contextPath}/assets/js/app/factories/users-factories.js"></script>
	<script
		src="${contextPath}/assets/js/app/factories/companies-factories.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/home-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/sidebar-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/admin-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/frais-controllers.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/messages-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/company-controller.js"></script>

	<script src="${contextPath}/assets/js/app/factories/cra-factories.js"></script>
	<script src="${contextPath}/assets/js/app/factories/notes-factories.js"></script>
	<script src="${contextPath}/assets/js/app/factories/admin-factories.js"></script>
	<script src="${contextPath}/assets/js/app/factories/worklog-factories.js"></script>
	<script
		src="${contextPath}/assets/js/app/factories/absences-factories.js"></script>
	<script src="${contextPath}/assets/js/app/services/common-services.js"></script>
	<script
		src="${contextPath}/assets/js/app/directives/commons-directives.js"></script>
	<script
		src="${contextPath}/assets/js/app/directives/angular-criterias.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/timesheet-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/users-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/absences-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/tasks-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/cra-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/user-settings-controller.js"></script>
	<script
		src="${contextPath}/assets/js/app/controllers/user-profile-controller.js"></script>
</body>
</html>
