function CompanyController($scope, $rootScope) {
	if($scope.userInfo){
		$rootScope.page={
				"title" : $scope.userInfo.company.name,
				"description" : "Dashboard"
			} 
	}
	$scope.$on('userInfo', function(event, args) {
		$rootScope.page = {
			"title" : args.company.name,
			"description" : "Dashboard"
		};
	});

}
