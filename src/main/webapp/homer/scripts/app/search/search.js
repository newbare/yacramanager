App.config(function ($stateProvider) {
	$stateProvider.state('search', {
		url : "/search/:searchText",
		templateUrl : _contextPath+'scripts/app/search/search.html',
		controller : 'SearchController',
		resolve : {
			searchResult :function($http,$stateParams) {
				return $http.get("/app/api/globalsearch/"+$stateParams.searchText,{});
			}
		},
		data: {
	        pageTitle: 'Search page'
	      }
	})
});