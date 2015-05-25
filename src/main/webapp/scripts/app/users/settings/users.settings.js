App.config(function ($stateProvider) {
	$stateProvider.state('user-settings', {
		url : "/user-settings",
		templateUrl : _contextPath+'scripts/app/users/settings/user-settings.html',
		controller : 'UserSettingsController',
		data: {
	        ncyBreadcrumbLabel: 'User settings'
	      }
	});
});