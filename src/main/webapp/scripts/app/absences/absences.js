App.config(function ($stateProvider) {
	$stateProvider.state('absence', {
		url : "/absence",
		templateUrl : _contextPath+'views/app/absence/absence.html',
		data: {
	        pageTitle: 'Absences',
	        ncyBreadcrumbLabel: 'Absences'
	      }
	}).state('absence.list', {
		url : "/list",
		templateUrl : _contextPath+'views/app/absence/absence-list.html',
		controller : 'AbsencesController',
		data: {
	        pageTitle: 'Absences',
	        ncyBreadcrumbLabel: 'List view'
	      }
	}).state('absence.detail', {
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