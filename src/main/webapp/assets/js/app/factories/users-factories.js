App.factory("UsersREST", function($resource) {
	return $resource("api/users/:service");
});