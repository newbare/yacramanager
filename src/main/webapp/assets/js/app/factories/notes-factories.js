App.factory("NoteCRUDREST", function($resource) {
	return $resource("api/frais/:id" , {}, {update: { method: 'PUT', params: {id: '@id'} }});
});