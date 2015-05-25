App.config(function ($stateProvider) {
	$stateProvider.state('company.activities', {
		url : "/activities",
		templateUrl : _contextPath+'scripts/app/company/activities/company-activities.html',
		controller : 'CompanyActivitiesController',
		resolve : {
			company :function(CompanyREST,USERINFO) {
				return CompanyREST.get(
						{id : USERINFO.company.id}).$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : 'Activities'
		}
	});
});