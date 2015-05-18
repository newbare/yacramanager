App.config(function ($stateProvider) {
	$stateProvider.state('user-messages', {
		url : "/user-messages",
		templateUrl : _contextPath+'views/app/users/users-messages.html',
		controller : 'UsersMessagesController',
		data: {
		    ncyBreadcrumbLabel: "User messages"
		  }
	});
});