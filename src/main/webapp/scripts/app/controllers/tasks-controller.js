'use strict';

function TasksController($scope, $rootScope,ngTableParams, alertService,ProjectsREST,TasksREST, $http,USERINFO) {
	$rootScope.page = {
		"title" : "Tasks",
		"description" : "My tasks"
	};

	$scope.tableFilter="";
	var allTask=[];
	$scope.resetTaskToAdd=function(){
		$scope.taskToAdd={};
	};
	$scope.projectToAdd=undefined;
	$scope.projects=[];
	
	$scope.resetTaskToAdd();
	
	$scope.employeCriteriaConfig={
			name:"employe",
			defaultButtonLabel:'<i class="fa fa-user"></i>',
			filterType:"ARRAY",
			closeable:false,
			filterValue:[],
			buttonSelectedItemsFormater:function(data){
				if(data.name==""+USERINFO.id+""){
					return ' Me';
				}else {
					return ' '+getUserInitials(data.label);
				}
			},
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+USERINFO.id+""){
						items.push(item);
					}
				});
				return items;
			},
			getData:function($defer){
				$http.get(_contextPath+"app/api/users/managed/"+USERINFO.id,{params:{"me":true} })
					.success(function(data, status) {
						$defer.resolve(data);
					});
			},
			currentFilter:{},
			displayed: true
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
					if(item.name==""+USERINFO.id+""){
						items.push(item);
					}
				});
				return items;
			},
			getData:function($defer){
				$http.get(_contextPath+"app/api/"+USERINFO.company.id+"/project/employe/"+USERINFO.id)
					.success(function(data, status) {
						var value=[];
						if(data.totalCount>0){
							angular.forEach(data.result,function(entry){
								value.push({name: ""+entry.id, label: entry.name});
							});
						}
						$defer.resolve(value);
					});
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
	$scope.taskStatusCriteriaConfig={
			name:"taskStatus",
			defaultButtonLabel:"Status",
			filterType:"ARRAY",
			closeable:false,
			filterValue:
				[{name:"OPEN",label:"OPEN",ticked:true},{name:"COMPLETED",label:"COMPLETED",ticked:false}]	,
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	$scope.criteriaBarConfig = {
		criterions : [$scope.employeCriteriaConfig,$scope.projectCriteriaConfig,$scope.taskNameCriteriaConfig,$scope.taskStatusCriteriaConfig],
		autoFilter : true,
		filters : []
	};
	
	$scope.doFilter=function(data){
		var serverFilter={filter:data};
		$scope.tableFilter=JSON.stringify(serverFilter);
		$scope.tableParams.reload();
	};
	
	$scope.$on('criteriaDofilter', function(event, filterData) {
		$scope.tableFilter=filterData;
		$scope.refreshProjects();
	});
	
	$scope.canAddTask=function(){
		return $scope.taskToAdd !==undefined && $scope.taskToAdd.taskName!==undefined && $scope.taskToAdd.taskName.length>0;
	};
	
	$scope.addTask=function(project){
		var newtask={name:$scope.taskToAdd.taskName,description:$scope.taskToAdd.taskDescription,projectId:project.id,employeId:USERINFO.id};
		TasksREST.save({companyId :USERINFO.company.id},newtask).$promise.then(function(result){
			alertService.show('success','Confirmation', 'New task created');
			$scope.resetTaskToAdd();
			$scope.refreshProjects();
		});
		
	};
	


	$scope.refreshProjects = function() {
		$http.get(_contextPath+"app/api/"+USERINFO.company.id+"/project/employe/"+USERINFO.id)
		.success(function(data, status) {
			$scope.projects = data.result;
		});
	};
	
	$scope.tableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10 ,
        sorting : {
			createdDate : 'desc' // initial sorting
		}// count per page
    }, {
        groupBy: function (task) {
          return task.project.name;
        },
        getData: function($defer, params) {
        	if($scope.tableFilter!==undefined && $scope.tableFilter!==''){
        		TasksREST.getAll(
						{
        					companyId:USERINFO.company.id,
        					employeId:USERINFO.id,
							page:params.$params.page-1,
							size:params.$params.count,
							sort:params.$params.sorting,
							filter:$scope.tableFilter
						},function(data) {
					params.total(data.totalCount);
					$scope.startIndex=data.startIndex;
					$scope.endIndex=data.endIndex;
					if(data.totalCount>=1){
						$scope.hasDatas=true;
					}else {
						$scope.hasDatas=false;
					}
					allTask=data.result;
					// set new data
					$defer.resolve(data.result);
				});
			}
        }
    });
	
	$scope.refreshProjects();
}