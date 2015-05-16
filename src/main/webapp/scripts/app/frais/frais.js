App.config(function ($stateProvider) {
	$stateProvider.state('frais', {
		url : "/frais",
		templateUrl : _contextPath+'views/app/frais/frais.html',
		data: {
	        pageTitle: 'Frais',
	        ncyBreadcrumbLabel: 'Frais'
	      }
	}).state('frais.list', {
		url : "/list",
		templateUrl : _contextPath+'views/app/frais/frais-list.html',
		controller : 'FraisController',
		data: {
	        pageTitle: 'Frais',
	        ncyBreadcrumbLabel: 'List view'
	      }
	}).state('frais.detail', {
		url : "/detail/:id",
		templateUrl : _contextPath+'views/app/frais/frais-detail.html',
		controller : 'FraisDetailController',
		resolve : {
			frais :function(NoteREST,$stateParams) {
				return NoteREST.get({id:$stateParams.id}).$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : ' N° {{currentNote.id}}',
			 ncyBreadcrumbParent: 'frais'
		}
	})
});