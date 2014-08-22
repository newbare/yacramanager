'use strict';

function CraController($scope,$rootScope,CraREST,$filter,$http,WorkLogCRUDREST,alertService) {
	$rootScope.page={"title":"CRA","description":"View and manage you CRA"};
	$scope.dateFormat="dd MMMM yyyy";
	$scope.craDateFormat="EEE dd/MM";
	var formatDate=function(date){
		return $filter('date')(date, 'yyyy-MM-dd');
	};
	
	$scope.currentFilter=undefined;
	
	$scope.currentView=undefined;
	$scope.currentDate=moment();
	
	$scope.dateRange={startDate:null,endDate: null}
	
	$scope.updateCurrentview=function(view){
		$scope.currentView=view;
		if('week'===$scope.currentView){
			$scope.dateRange={
					startDate:$scope.currentDate.clone().startOf('week'),
					endDate: $scope.currentDate.clone().endOf('week')
			}
		}else if ('month'===$scope.currentView) {
			$scope.dateRange={
					startDate:$scope.currentDate.clone().startOf('month'),
					endDate: $scope.currentDate.clone().endOf('month')
			}
		}
		$scope.$broadcast('craViewChanged', $scope.currentView);
	}
	
	$scope.previous=function(){
		if('week'===$scope.currentView){
			$scope.currentDate=$scope.currentDate.clone().subtract('days',6);
		}else if ('month'===$scope.currentView) {
			$scope.currentDate=$scope.currentDate.clone().subtract('days',30);
		}
		$scope.refreshView();
	};
	
	$scope.next=function(){
		if('week'===$scope.currentView){
			$scope.currentDate=$scope.currentDate.clone().add('days',6);
		}else if ('month'===$scope.currentView) {
			$scope.currentDate=$scope.currentDate.clone().add('days',30);
		}
		$scope.refreshView();
	};
	
	$scope.refreshView=function(){
		$scope.updateCurrentview($scope.currentView);
	};
	
	$scope.today=function(){
		$scope.currentDate=moment();
		$scope.refreshView();
	};
	$scope.isTodaySelected=function(){
		var range = moment().range($scope.dateRange.startDate, $scope.dateRange.endDate);
		return range.contains(moment());
	}
	
	$scope.employeCriteriaConfig={
			name:"employe",
			defaultButtonLabel:"<i class='fa fa-user'></i>",
			filterType:"ARRAY",
			closeable:false,
			filterValue:[],
			buttonSelectedItemsFormater:function(data){
				if(data.name==""+_userId+""){
					return ' Me';
				}else {
					return ' '+getUserInitials(data.label);
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
			$scope.currentFilter=data;
			var serverFilter={filter:data};
			$scope.tableFilter=JSON.stringify(serverFilter);
			//$scope.retrieveCra();
			$scope.retrieveCraDetails(data);
		};
	
//	$scope.retrieveCra=function(){
//		$http.get(
//				_contextPath + "/app/api/cra?start=" +$scope.dateRange.startDate.toISOString()+"&end="+$scope.dateRange.endDate.toISOString(), {
//					params : {}
//				}).then(function(response) {
//					$scope.cra = response.data;
//				    $scope.respStartDate=response.data.startDate;
//				    $scope.respEndDate=response.data.endDate;
//				});
//	};
	$scope.retrieveCraDetails=function(filter){
		var userIds=[];
		angular.forEach(filter,function(filterElement){
			if(filterElement.field==='employe'){
				angular.forEach(filterElement.value,function(value){
					userIds.push(value.name);
				});
				
			}
		});
		$http.get(
				_contextPath + "/app/api/cra/details?employeIds="+userIds.join()+"&start=" +$scope.dateRange.startDate.toISOString()+"&end="+$scope.dateRange.endDate.toISOString(), {
					params : {}
				}).then(function(response) {
					$scope.craDetails = response.data;
				});
	};
	$scope.formatCraDetailDuration=function(duration){
		return $filter('date')(duration*1000, 'shortTime');
	}
	
	$scope.updateCurrentview('week');
	
	$scope.$on('craViewChanged', function(event, args) {
		$scope.retrieveCraDetails($scope.currentFilter);
		//$scope.retrieveCra();
	});
	
	$scope.isCurrentEmploye=function(employeId){
		return employeId==_userId;
	}
	


	$scope.updateCraValue = function(newValue, taskRow, day) {
		var diff = newValue - taskRow.duration[day.date];
		if (diff < 0) {
			return 'Cannot reduce worklog';
		}
		var worklog = {};
		worklog.title = null;
		worklog.type = 'DURATION';
		worklog.start = day.date;
		worklog.end = null;
		worklog.duration = diff;
		worklog.taskId = taskRow.task.id;
		worklog.taskName = taskRow.task.name;
		worklog.description = 'Created from Cra view';
		worklog.employeId = _userId;
		return WorkLogCRUDREST.save(worklog).$promise.then(function(result) {
			alertService.show('info', 'Confirmation', 'Donn� sauvegard�');
		});
	};
	
	
}
