'use strict';

function CraController($scope,$rootScope,CraREST,$filter) {
	$rootScope.page={"title":"CRA","description":"View and manage you CRA"};
	$scope.dateFormat="dd MMMM yyyy";
	$scope.craDateFormat="EEE dd/MM";
	var formatDate=function(date){
		return $filter('date')(date, 'yyyy-MM-dd');
	};
	$scope.periodCRA=["Current week","Current month"];
	$scope.selectedPeriod="Current month";
	var date = new Date();
	var currentMonthfirstDay = new Date(date.getFullYear(), date.getMonth(), 1);
	var currentMonthlastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
	
	var currentWeekfirstday = new Date(date.setDate(date.getDate() - date.getDay()));
	var currentWeeklastday = new Date(date.setDate(date.getDate() - date.getDay()+6));
	
	$scope.startDate=formatDate(currentMonthfirstDay);
	$scope.endDate=formatDate(currentMonthlastDay);
	$scope.retrieveCra=function(){
		CraREST.get({start:formatDate($scope.startDate),end:formatDate($scope.endDate)},function(data) {
		    $scope.cra = data;
		    $scope.respStartDate=data.startDate;
		    $scope.respEndDate=data.endDate;
		});
	};	
	$scope.periodChanged=function(){
		if($scope.selectedPeriod){
			if($scope.selectedPeriod==="Current week"){
				$scope.startDate=formatDate(currentWeekfirstday);
				$scope.endDate=formatDate(currentWeeklastday);
			}else if ($scope.selectedPeriod==="Current month") {
				$scope.startDate=formatDate(currentMonthfirstDay);
				$scope.endDate=formatDate(currentMonthlastDay);
			}
			$scope.retrieveCra();
		}
	};
	$scope.periodChanged();
}
