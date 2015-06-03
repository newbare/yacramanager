App.config(function ($stateProvider) {
	$stateProvider.state('user-messages', {
		url : "/user-messages",
		templateUrl : _contextPath+'scripts/app/users/messages/users-messages.html',
		controller : 'UsersMessagesController',
		data: {
		    ncyBreadcrumbLabel: "User messages"
		  }
	});
});