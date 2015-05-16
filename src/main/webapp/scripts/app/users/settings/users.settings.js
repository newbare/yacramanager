App.config(function ($stateProvider) {
	$stateProvider.state('user-settings', {
		url : "/user-settings",
		templateUrl : _contextPath+'views/app/user-settings.html',
		controller : 'UserSettingsController'
	});
});