'use strict';

function CraController($scope,$rootScope,CraREST) {
	$rootScope.page={"title":"CRA","description":"View and manage you CRA"};
	$scope.startDate='2014-07-01';
	$scope.endDate='2014-07-01';
	CraREST.get({start:'2014-07-01',end:'2014-07-01'},function(data) {
	    $scope.cra = data;
	    console.log(data);
	  });
}
