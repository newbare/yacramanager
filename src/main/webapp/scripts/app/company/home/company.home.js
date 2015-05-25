App.config(function ($stateProvider) {
	$stateProvider.state('company.home', {
		url : "/home",
		templateUrl : _contextPath+'scripts/app/company/home/company-home.html',
		controller : 'CompanyHomeController',
		resolve : {
			company :function(CompanyREST,USERINFO) {
				return CompanyREST.get(
						{id : USERINFO.company.id}).$promise;
			}
		},
		data: {
		    ncyBreadcrumbSkip: true 
		  }
		//controller : EntrepriseController,
	});
});