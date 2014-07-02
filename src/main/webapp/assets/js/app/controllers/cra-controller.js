'use strict';

function CraController($scope,$rootScope,CraREST,$filter) {
	$rootScope.page={"title":"CRA","description":"View and manage you CRA"};
	$scope.dateFormat="dd MMMM yyyy";
	$scope.craDateFormat="EEE dd/MM";
	var formatDate=function(date){
		return $filter('date')(date, 'yyyy-MM-dd');
	}
	var date = new Date();
	var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
	var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
	$scope.startDate=formatDate(firstDay);
	$scope.endDate=formatDate(lastDay);
	$scope.retrieveCra=function(){
		CraREST.get({start:formatDate($scope.startDate),end:formatDate($scope.endDate)},function(data) {
		    $scope.cra = data;
		    $scope.respStartDate=data.startDate;
		    $scope.respEndDate=data.endDate;
		});
	}	
	$scope.retrieveCra();
}
