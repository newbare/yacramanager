App.config(function ($stateProvider) {
	$stateProvider.state('user-profile', {
		url : "/user-profile",
		templateUrl : _contextPath+'scripts/app/users/profile/user-profile.html',
		controller : 'UserProfileController',
		data: {
			pageTitle: 'User Profile',
	        ncyBreadcrumbLabel: 'User profile'
	      }
	});
});