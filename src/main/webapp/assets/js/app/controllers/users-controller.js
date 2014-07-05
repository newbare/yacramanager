App.controller('UsersController',function ($rootScope,$scope,UsersREST) {
	console.log(UsersREST);
	var loadUserInfo = function() {
		UsersREST.get({
			service : 'user-info'
		}, function(data) {
			$rootScope.userInfo = data;
		});
	};
	loadUserInfo();
});