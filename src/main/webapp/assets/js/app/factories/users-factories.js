App.factory("UsersREST", function($resource) {
	return $resource(_contextPath+"/app/api/users/:service");
});

App.factory("UserSettingsREST", function($resource) {
	return $resource(_contextPath+"/app/api/settings/user/"+_userId+"/:id",{},{update: { method: 'PUT'}});
});