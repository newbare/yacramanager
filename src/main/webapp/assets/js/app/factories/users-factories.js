App.factory("UsersREST", function($resource) {
	return $resource("rest/users/:service");
});