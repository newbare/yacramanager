App.controller('CompanyActivitiesController',function ($scope,company,CompanyREST,USERINFO,ActivitiesREST){
	$scope.company=company;
	$scope.timelineData=ActivitiesREST.forCompany({id:$scope.company.id});
});