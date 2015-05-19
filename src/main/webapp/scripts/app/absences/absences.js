App.config(function ($stateProvider) {
	$stateProvider.state('absence', {
		url : "/absence",
		templateUrl : _contextPath+'views/app/absence/absence.html',
		abstract: true
	}).state('absence.list', {
		url : "/list",
		templateUrl : _contextPath+'views/app/absence/absence-list.html',
		abstract: true,
		controller : 'AbsencesListController'
	}).state('absence.list.mine', {
		url : "/mine",
		templateUrl : _contextPath+'views/app/absence/absence-list-mine.html',
		controller : 'AbsencesListMineController',
		data: {
	        pageTitle: 'My time off',
	        ncyBreadcrumbLabel: 'My time off'
	      }
	})
	.state('absence.list.tobeapproved', {
		url : "/tobeapproved",
		templateUrl : _contextPath+'views/app/absence/absence-list-tobeapproved.html',
		controller : 'AbsencesListToBeApprovedController',
		data: {
	        pageTitle: 'Time off to be approved',
	        ncyBreadcrumbLabel: 'Time off To be approved'
	      }
	})
	.state('absence.detail', {
		url : "/detail/:id",
		templateUrl : _contextPath+'views/app/absence/absence-detail.html',
		controller : 'AbsencesDetailController',
		resolve : {
			absence :function(AbsenceREST,$stateParams) {
				return AbsenceREST.get({id:$stateParams.id}).$promise;
			},
			absencesType: function(AbsenceREST){
				return AbsenceREST.getTypes().$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : '{{currentAbsence.typeAbsence.name}} from {{currentAbsence.startDate | date : "mediumDate"}} to {{currentAbsence.endDate | date : "mediumDate"}}'
		}
	})
});