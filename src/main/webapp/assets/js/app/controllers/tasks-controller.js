'use strict';

function TasksController($scope, $rootScope,ngTableParams, alertService,AbsenceCRUDREST, $http) {
	$rootScope.page = {
		"title" : "Tasks",
		"description" : "My tasks"
	};

	$scope.projectCriteriaConfig={
			name:"project",
			defaultButtonLabel:'Project',
			filterType:"ARRAY",
			closeable:false,
			filterValue:[],
			buttonSelectedItemsFormater:function(data){
				return "["+data.label+"]";
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
				$http.get(_contextPath+"/app/api/"+_userCompanyId+"/project/employe/"+_userId)
					.success(function(data, status) {
						var value=[];
						if(data.totalCount>0){
							angular.forEach(data.result,function(entry){
								value.push({name: ""+entry.id, label: entry.name});
							});
						}
						$defer.resolve(value);
					})
			},
			currentFilter:{},
			displayed: true
	};
	$scope.taskNameCriteriaConfig={
			name:"name",
			defaultButtonLabel:"Task",
			filterType:"TEXT",
			closeable:false,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	$scope.criteriaBarConfig = {
		criterions : [$scope.projectCriteriaConfig,$scope.taskNameCriteriaConfig],
		autoFilter : true,
		filters : []
	};
	
	$scope.doFilter=function(data){
		var serverFilter={filter:data};
		$scope.criteriaBarFilter=JSON.stringify(serverFilter);
		$scope.$broadcast('criteriaDofilter', JSON.stringify(serverFilter));
	};
	
}