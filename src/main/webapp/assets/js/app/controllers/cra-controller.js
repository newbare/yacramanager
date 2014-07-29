'use strict';

function CraController($scope,$rootScope,CraREST,$filter,$http) {
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
	
	$scope.tableFilter="";
	$scope.employeCriteriaConfig={
			name:"employe",
			defaultButtonLabel:"Who",
			filterType:"ARRAY",
			closeable:false,
			filterValue:[],
			buttonSelectedItemsFormater:function(data){
				if(data.name==""+_userId+""){
					return '<i class="fa fa-user"></i> Me';
				}else {
					return '<i class="fa fa-user"></i> '+getUserInitials(data.label);
				}
			},
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+_userId+""){
						items.push(item);
					}
				});
				return items;
			},
			getData:function($defer){
				$http.get(_contextPath+"/app/api/users/managed/"+_userId,{params:{"me":true} })
					.success(function(data, status) {
						$defer.resolve(data);
					});
			},
			currentFilter:{},
			displayed: true
	};
	$scope.criteriaBarConfig={
			criterions:[$scope.employeCriteriaConfig],
			autoFilter:true,
			filters:[]
		};
		
		$scope.doFilter=function(data){
			console.log("Server filer launch with: "+JSON.stringify(data));
			var serverFilter={filter:data};
			$scope.tableFilter=JSON.stringify(serverFilter);
			//$scope.refreshDatas();
		};
	
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
