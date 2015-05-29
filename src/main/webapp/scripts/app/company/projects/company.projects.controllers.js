/*COMPANY-PROJECT section*/
App.controller('CompanyProjectsController',function ($scope, $rootScope,$http,ngTableParams,ProjectsREST,USERINFO){
	
	$scope.$on('userInfo', function(event, userInfo) {
		//do afteruserInfo is retrieved 
		//End userInfo listener
	});
	
	var fetchClients = function(queryParams) {
		return $http.get(
				_contextPath + "app/api/" + USERINFO.company.id + "/client", {
					params : {}
				}).then(function(response) {
					$scope.clients=response.data;
				});
	}; 

	fetchClients();
	
	$scope.selectClient=function(client){
		$scope.client=client;
	};
	
	$scope.hasDatas=false;
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	};
	$scope.tableFilter="";
	$scope.companyCriteriaConfig={
			name:"company",
			defaultButtonLabel:"Company",
			filterType:"ARRAY",
			closeable:false,
			editable:false,
			buttonSelectedItemsFormater:function(data){
				return data.label;
			},
			filterValue:[{name:USERINFO.company.id,label:USERINFO.company.name,ticked:false}],
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+USERINFO.company.id+""){
						items.push(item);
					}
				});
				return items;
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.clientCriteriaConfig={
			name:"client",
			defaultButtonLabel:"Client",
			filterType:"ARRAY",
			closeable:false,
			filterValue:[],
			buttonSelectedItemsFormater:function(data){
				return "["+data.label+"]";
			},
			getData:function($defer){
				$http.get(_contextPath+"app/api/"+USERINFO.company.id+"/client",
						{
							params:
								{	
									sort:{}, 
									filter:{filter:[{"type":"ARRAY","field":"company","value":$scope.companyCriteriaConfig.filterValue}]}
								} 
						})
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
	
	$scope.nameCriteriaConfig={
			name:"name",
			defaultButtonLabel:"Project name",
			filterType:"TEXT",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				//console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.descriptionCriteriaConfig={
			name:"description",
			defaultButtonLabel:"Description",
			filterType:"TEXT",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				//console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.createdDateCriteriaConfig={
			name:"createdDate",
			defaultButtonLabel:"Date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				//console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.criteriaBarConfig={
		criterions:[$scope.companyCriteriaConfig,$scope.clientCriteriaConfig,$scope.nameCriteriaConfig,$scope.descriptionCriteriaConfig,$scope.createdDateCriteriaConfig],
		autoFilter:true,
		filters:[]
	};
	
	$scope.doFilter=function(data){
		var serverFilter={filter:data};
		$scope.tableFilter=JSON.stringify(serverFilter);
		$scope.refreshDatas();
	};
	
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			id : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			if($scope.tableFilter!==""){
				ProjectsREST.get(
						{
							companyId : USERINFO.company.id,
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
					allAbsence=data.result;
					// set new data
					$defer.resolve(data.result);
				});
			}
		}});
	
});

App.controller('CompanyProjectsViewController',function ($scope, $rootScope,$http,ProjectsREST,ngTableParams,$state,alertService,$state,USERINFO){
	$scope.tableFilter="";
	$scope.$state=$state;
	$scope.project={};
	$scope.projectFilterText="";
	$scope.projectFilter="";
	
	$scope.filterProjects=function(projectFilterText){
		$scope.projectFilter="{\"filter\":[{\"type\":\"TEXT\",\"field\":\"global\",\"value\":\""+projectFilterText+"\"}]}";
		$scope.tableParams.reload();
	};
	
	
	$scope.hasDatas=false;
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	};
	$scope.tableFilter="";
	
	
	$scope.doFilterList=function(data){
		$scope.tableFilter=data;
		$scope.refreshDatas();
	};
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			id : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
				ProjectsREST.get(
						{
							companyId : USERINFO.company.id,
							page:params.$params.page-1,
							size:params.$params.count,
							sort:params.$params.sorting,
							filter:$scope.projectFilter
						},function(data) {
					params.total(data.totalCount);
					$scope.startIndex=data.startIndex;
					$scope.endIndex=data.endIndex;
					if(data.totalCount>=1){
						$scope.hasDatas=true;
					}else {
						$scope.hasDatas=false;
					}
					allAbsence=data.result;
					// set new data
					$defer.resolve(data.result);
				});
		}});
	$scope.doFilterList($scope.criteriaBarFilter);
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
	$scope.postProject=function(hideFn){
		ProjectsREST.save({companyId :USERINFO.company.id,clientId :$scope.client.id},$scope.project).$promise.then(function(result) {
   		 hideFn();
   		 alertService.show('info','Confirmation', 'Project created');
		});
	};
});

App.controller('CompanyProjectsQuickViewController',function ($scope,$http,ProjectsREST,ngTableParams,$state,USERINFO){
	$scope.projects=[];
	$scope.projectsListFilter="";
	$scope.tableParams.settings().counts=[];
	$scope.doFilterList=function(data){
		$scope.projectsListFilter=data;
	};
	$scope.doFilterList($scope.criteriaBarFilter);
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
});

App.controller('CompanyProjectsListController',function ($scope, $rootScope,$http,ProjectsREST,ngTableParams,$state,USERINFO){
	 $scope.tableParams.settings().counts=[10, 25, 50, 100];
	 $scope.changeSelection = function(project) {
	        //console.info(user);
	        $state.go('company.projects.details',{ id:project.id });
	 };
	
});

App.controller('CompanyProjectsOverviewController',function ($scope, ProjectsREST, TasksREST,EmployeesREST,
		project,USERINFO,ActivitiesREST) {
	$scope.project = project;
	$scope.employeeByTask={};
	$scope.groupByTask=true;
	
	$scope.timelineSource=undefined;
	$scope.timelineSource=ActivitiesREST.forProject({id:$scope.project.id});
	$scope.reset=function(){
		$scope.project.$promise.then(function(project) {
			angular.forEach(project.tasks,function(task){
				TasksREST.getAssignedEmployee({companyId:USERINFO.company.id,taskId:task.id,employeId:USERINFO.id}, 
						function(value) {
							$scope.employeeByTask[task.id]=value.result;
						}
				);
			});
			$scope.currentProject=project;
			EmployeesREST.get(
					{
						page:0,
						size:100,
						sort:"lastName",
						filter:{"filter":[{"type":"ARRAY","field":"projects","value":[{"name":""+$scope.currentProject.id+"","label":"","ticked":true}]}]}
					},function(data) {
						$scope.employeesByProject=data.result;
			});
		});
		
		EmployeesREST.get(
				{
					page:0,
					size:100,
					sort:"lastName",
					filter:{"filter":[{"type":"ARRAY","field":"company","value":[{"name":""+USERINFO.company.id+"","label":"","ticked":true}]}]}
				},function(data) {
					$scope.companyEmployees=data.result;
		});
		
	};
	$scope.reset();
	$scope.format=function(employee){
		return employee.firstName +' '+employee.lastName;
	};
	$scope.addSelectedEmployees=function(currentTask,foundEmployees,hideFn){
		var employeesList=[];
		angular.forEach(foundEmployees,function(employee){
			employeesList.push(employee.id);
		});
		
		TasksREST.assignEmployeeToTask({companyId:USERINFO.company.id,taskId:currentTask.id,employeesIds:employeesList},null,function(result){
			$scope.reset();
			hideFn();
		});
	};
	$scope.unAssignEmploye=function(task,employee){
		TasksREST.unAssignEmployeeToTask({companyId:USERINFO.company.id,taskId:task.id,employeesIds:[employee.id]},null,
			function(result){
			$scope.reset();
			});
	};
	$scope.selectActiveTask =function(task){
		$scope.activeTask=task;
	};
	$scope.updateProject=function(project){
		var projectToUpdate={id:$scope.project.id,name:$scope.project.name,description:$scope.project.description,color:$scope.project.color};
		ProjectsREST.update({companyId :USERINFO.company.id},projectToUpdate).$promise.then(
		        //success
		        function( value ){
		        	 $scope.tableParams.reload();
		        },
		        //error
		        function( error ){/*Do something with error*/}
		      );
		};
});
/*COMPANY-PROJECT End of section*/