App.config(function ($stateProvider) {
	$stateProvider.state('search', {
		url : "/search/:searchText",
		templateUrl : _contextPath+'scripts/app/search/search.html',
		controller : 'SearchController',
		resolve : {
			searchText :function($stateParams) {
				return $stateParams.searchText;
			}
		},
		data: {
	        pageTitle: 'Search page'
	      }
	})
});