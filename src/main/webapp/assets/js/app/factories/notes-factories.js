App.factory("NoteCRUDREST", function($resource) {
	return $resource("rest/frais/:id" , {}, {update: { method: 'PUT', params: {id: '@id'} }});
});