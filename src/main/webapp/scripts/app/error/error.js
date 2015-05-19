App.config(function ($stateProvider) {
	$stateProvider.state('error404', {
		url : "/error404",
		templateUrl : _contextPath+'views/app/error/error-404.tpl.html',
		controller : 'ErrorController',
		data: {
	        pageTitle: 'Page not found'
	      }
	})
	.state('error403', {
		url : "/error403",
		templateUrl : _contextPath+'views/app/error/error-403.tpl.html',
		controller : 'ErrorController',
		data: {
	        pageTitle: 'Access denied'
	      }
	});
});