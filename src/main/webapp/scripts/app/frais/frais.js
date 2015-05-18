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
		controller : 'FraisListController',
		data: {
	        pageTitle: 'Fees',
	        ncyBreadcrumbLabel: 'List view'
	      }
	}).state('frais.list.mine', {
		url : "/mine",
		templateUrl : _contextPath+'views/app/frais/frais-list-mine.html',
		controller : 'FraisListMineController',
		data: {
	        pageTitle: 'My fees',
	        ncyBreadcrumbLabel: 'Mine'
	      }
	}).state('frais.list.tobeapproved', {
		url : "/tobeapproved",
		templateUrl : _contextPath+'views/app/frais/frais-list-tobeapproved.html',
		controller : 'FraisListToBeApprovedController',
		data: {
	        pageTitle: 'Fees to be approved',
	        ncyBreadcrumbLabel: 'To be approved'
	      }
	})
	.state('frais.detail', {
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