App.config(function ($stateProvider) {
	$stateProvider.state('user-settings', {
		url : "/user-settings",
		templateUrl : _contextPath+'views/app/users/user-settings.html',
		controller : 'UserSettingsController',
		data: {
	        ncyBreadcrumbLabel: 'User settings'
	      }
	});
});