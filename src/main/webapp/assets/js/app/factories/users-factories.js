App.factory("UsersREST", function($resource,$rootScope) {
	return $resource(_contextPath+"/app/api/users/:service");
});