App.controller('CompanyHomeController',function ($scope,$rootScope,$state,company,CompanyREST,USERINFO){
	$scope.company=company;
	$scope.licenseDaysLeft=moment().from($scope.company.licenseEndDate,true);
	$scope.updateCompany = function() {
		return CompanyREST.update({},$scope.company).$promise.then(
		        //success
		        function( value ){
		        	$rootScope.$broadcast('event:userInfo-Refresh');
		        },
		        //error
		        function( error ){/*Do something with error*/}
		      );
	};
});