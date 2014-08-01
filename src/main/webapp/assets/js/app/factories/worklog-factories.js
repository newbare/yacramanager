App.factory("WorkLogCRUDREST", function($resource) {
	return $resource(_contextPath+"/app/api/worklog/:id" , {}, {update: { method: 'PUT', params: {id: '@id'} }});
});