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
	$scope.dateRange={
			startDate:currentMonthfirstDay,
			endDate:currentMonthlastDay
	}
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
	
	$scope.dateCriteriaConfig={
			name:"date",
			defaultButtonLabel:"Date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			timePicker:true,
			onFilter: function(filter) {
				$scope.dateRange.startDate=filter.value.start;
				$scope.dateRange.endDate=filter.value.end;
				console.log('Filter text ['+filter.field+'] searching: '+filter.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.criteriaBarConfig={
			criterions:[$scope.employeCriteriaConfig,$scope.dateCriteriaConfig],
			autoFilter:true,
			filters:[]
		};
		
		$scope.doFilter=function(data){
			console.log("Server filer launch with: "+JSON.stringify(data));
			var serverFilter={filter:data};
			$scope.tableFilter=JSON.stringify(serverFilter);
			$scope.retrieveCra();
		};
	
	$scope.retrieveCra=function(){
		CraREST.get({start:$scope.dateRange.startDate,end:$scope.dateRange.endDate},function(data) {
		    $scope.cra = data;
		    $scope.respStartDate=data.startDate;
		    $scope.respEndDate=data.endDate;
		});
	};	
}
