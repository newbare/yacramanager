'use strict';

function TasksController($scope, $rootScope,ngTableParams, alertService,ProjectsCRUDREST,TasksCRUDREST, $http) {
	$rootScope.page = {
		"title" : "Tasks",
		"description" : "My tasks"
	};

	$scope.tableFilter="";
	
	$scope.resetTaskToAdd=function(){
		$scope.taskToAdd={};
	};
	$scope.projects=[];
	
	$scope.resetTaskToAdd();
	
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
	
	$scope.$on('criteriaDofilter', function(event, filterData) {
		$scope.tableFilter=filterData;
		$scope.refreshProjects();
	});
	
	$scope.canAddTask=function(){
		return $scope.taskToAdd !=undefined && $scope.taskToAdd.taskName!=undefined && $scope.taskToAdd.taskName.length>0;
	};
	
	$scope.addTask=function(project){
		$scope.taskToAdd !=undefined;
		var newtask={name:$scope.taskToAdd.taskName,description:$scope.taskToAdd.taskDescription,projectId:project.id,employeId:_userId};
		TasksCRUDREST.save({companyId :_userCompanyId},newtask).$promise.then(function(result){
			alertService.show('success','Confirmation', 'New task created');
			$scope.resetTaskToAdd();
			$scope.refreshProjects();
		});
		
	};
	


	$scope.refreshProjects = function() {
		$http.get(_contextPath+"/app/api/"+_userCompanyId+"/project/employe/"+_userId)
		.success(function(data, status) {
			$scope.projects = data.result;
		});
	};
	
//	$scope.tableParams = new ngTableParams({
//		page : 1, // show first page
//		count : 10, // count per page
//		sorting : {
//			id : 'desc' // initial sorting
//		}
//	}, {
//		total : 0, // length of data
//		getData : function($defer, params) {
//			if($scope.tableFilter!==""){
//				TasksCRUDREST.get(
//						{
//							companyId : _userCompanyId,
//							page:params.$params.page-1,
//							size:params.$params.count,
//							sort:params.$params.sorting,
//							filter:$scope.tableFilter
//						},function(data) {
//					params.total(data.totalCount);
//					$scope.startIndex=data.startIndex;
//					$scope.endIndex=data.endIndex;
//					if(data.totalCount>=1){
//						$scope.hasDatas=true;
//					}else {
//						$scope.hasDatas=false;
//					}
//					allAbsence=data.result;
//					// set new data
//					$defer.resolve(data.result);
//				});
//			}
//		}});
	$scope.refreshProjects();
}