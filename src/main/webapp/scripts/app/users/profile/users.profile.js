App.config(function ($stateProvider) {
	$stateProvider.state('user-profile', {
		url : "/user-profile",
		templateUrl : _contextPath+'views/app/users/user-profile.html',
		controller : 'UserProfileController',
		data: {
	        ncyBreadcrumbLabel: 'User profile'
	      }
	});
});