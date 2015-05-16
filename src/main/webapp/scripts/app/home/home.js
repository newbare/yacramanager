App.config(function ($stateProvider) {
	$stateProvider.state('home', {
		url : "/home",
		templateUrl : _contextPath+'views/app/home.html',
		controller : 'HomeController',
		data: {
	        pageTitle: 'Home',
	        ncyBreadcrumbLabel: 'Home'
	      }
	});
});