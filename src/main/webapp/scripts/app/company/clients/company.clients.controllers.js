/*COMPANY-CLIENT section*/
App.controller('CompanyClientsViewController',function ($scope, $rootScope,$http,ClientsREST,ngTableParams,$state,alertService,$state,USERINFO){
	$scope.tableFilter="";
	$scope.clientFilterText="";
	$scope.clientFilter="";
	$scope.$state=$state;
	$scope.client={};
	
	$scope.hasDatas=false;
	

	$scope.filterClients=function(clientFilterText){
		$scope.clientFilter="{\"filter\":[{\"type\":\"TEXT\",\"field\":\"global\",\"value\":\""+clientFilterText+"\"}]}";
		$scope.tableParams.reload();
	};
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
	
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			id : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
				ClientsREST.query(
						{
							companyId : USERINFO.company.id,
							page:params.$params.page-1,
							size:params.$params.count,
							sort:params.$params.sorting,
							filter:$scope.clientFilter
						},function(data) {
					params.total(data.totalCount);
					$scope.startIndex=data.startIndex;
					$scope.endIndex=data.endIndex;
					if(data.totalCount>=1){
						$scope.hasDatas=true;
					}else {
						$scope.hasDatas=false;
					}
					allAbsence=data;
					// set new data
					$defer.resolve(data);
				});
		}});
	$scope.postClient=function(hideFn){
		$scope.client.companyId=parseInt(USERINFO.company.id);
		ClientsREST.save({companyId :USERINFO.company.id},$scope.client).$promise.then(function(result) {
   		 hideFn();
   		 alertService.show('info','Confirmation', 'Client created');
   		$scope.tableParams.reload();
		});
	};
});

App.controller('CompanyClientsQuickViewController',function CompanyClientsQuickViewController($scope,$http,ClientsREST,ngTableParams,$state,USERINFO){
	$scope.clients=[];
	$scope.clientsListFilter="";
	$scope.tableParams.settings().counts=[];
	$scope.doFilterList=function(data){
		$scope.clientsListFilter=data;
	};
	$scope.doFilterList($scope.criteriaBarFilter);
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
});

App.controller('CompanyClientsListController',function ($scope, $rootScope,$http,ClientsREST,ngTableParams,$state,USERINFO){
	 $scope.changeSelection = function(client) {
	      $state.go('company.clients.details',{ id:client.id });
	 };
	 $scope.tableParams.settings().counts=[10, 25, 50, 100];
});

App.controller('CompanyClientsOverviewController',function ($scope,ClientsREST,$timeout,$http,ProjectsREST,client,alertService,USERINFO,ActivitiesREST,EmployeesProjectsREST,ngTableParams,EmployeesREST){
	$scope.client=client;
	$scope.contactFilter='';
	$scope.timelineSource=undefined;
	$scope.timelineSource=ActivitiesREST.forClient({id:$scope.client.id});
	$scope.updateClient = function() {
		var clientToUpdate={id:$scope.client.id,name:$scope.client.name,email:$scope.client.email,contacts:$scope.client.contacts};
		angular.forEach(clientToUpdate.contacts,function(contact){
			delete contact.searchField;
		});
		return ClientsREST.update({companyId :USERINFO.company.id},clientToUpdate).$promise.then(
		        //success
		        function( value ){
		        	 $scope.tableParams.reload();
		        },
		        //error
		        function( error ){/*Do something with error*/}
		      );
	};
	
	$scope.contactsManagerConfig={
			dataObject:$scope.client,
			update:$scope.updateClient
	};
	
	$scope.addNewProject=function(client){
		client.projects.push({name: '', description: '',isnew:true});
	};
	$scope.saveProject=function(project,client){
		var projectToSave={
				name:project.name,
				description:project.description
		}
		ProjectsREST.save({companyId :USERINFO.company.id,clientId :client.id},projectToSave).$promise.then(function(result) {
	  		 alertService.show('info','Confirmation', 'Project created');
	  		 $scope.client=ClientsREST.get({companyId : USERINFO.company.id,id:$scope.client.id})
			});
	};
	$scope.updateProject=function(project){
		if(!project.isnew){
			var projectToUpdate=clone(project);
			delete projectToUpdate.searchField;
			ProjectsREST.update({companyId :USERINFO.company.id},projectToUpdate).$promise.then(
			        //success
			        function( value ){
			        	 $scope.tableParams.reload();
			        	 $scope.client=ClientsREST.get({companyId : USERINFO.company.id,id:$scope.client.id})
			        },
			        //error
			        function( error ){/*Do something with error*/}
			      );
			};
		};
		
	$scope.deleteProject=function(project,index){
		client.projects.splice(index,1);
	};
	
	//company.clients.projects
	$scope.employeesProjectsTableFilter="";
	
	$scope.employeesProjectsDoFilter=function(data){
		var serverFilter={filter:data};
		$scope.employeesProjectsCriteriaBarFilter=JSON.stringify(serverFilter);
		$scope.$broadcast('employeesProjectsCriteriaDofilter', JSON.stringify(serverFilter));
	};
	$scope.$on('employeesProjectsCriteriaDofilter', function(event, args) {
		$scope.employeesProjectsDoFilterList(args);
	});
	
	$scope.employeesProjectsDoFilterList=function(data){
		$scope.employeesProjectsTableFilter=data;
		$scope.refreshEmployeesProjectsDatas();
	};
	$scope.refreshEmployeesProjectsDatas=function(){
		$scope.clientsEmployeesTableParams.reload();
	};
	
	$scope.updateEmployeProject = function(employeesProject,dailyRate) {
		var epToSave = {};
		epToSave.dailyRate = dailyRate;
		epToSave.projectLead = employeesProject.projectLead;
		return EmployeesProjectsREST.update(
				{
					companyId:USERINFO.company.id,
					clientId:client.id,
					projectId: employeesProject.id.projectId,
					employeeId: employeesProject.id.employeeId
				},epToSave).$promise.then(function(result) {
			alertService.show('success', 'Confirmation', 'Data has been updated');
		});
	};
	
	//Assign employe to project
	$scope.format=function(employee){
		return employee.firstName +' '+employee.lastName;
	};
	
	$scope.selectActiveProject =function(project){
		$scope.activeProject=project;
	};
	$scope.addSelectedEmployees=function(currentProject,foundEmployees,hideFn){
		var employeesList=[];
		angular.forEach(foundEmployees,function(employee){
			employeesList.push(employee.id);
		});
		
		ProjectsREST.assignEmployeeToProject({companyId:USERINFO.company.id,projectId:$scope.activeProject.id,employeesIds:employeesList},null,function(result){
			$scope.reset();
			hideFn();
		});
	};
	
	EmployeesREST.get(
			{
				page:0,
				size:100,
				sort:"lastName",
				filter:{"filter":[{"type":"ARRAY","field":"company","value":[{"name":""+USERINFO.company.id+"","label":"","ticked":true}]}]}
			},function(data) {
				$scope.companyEmployees=data.result;
	});
	//End of assign employe to project
	
	$scope.employeesProjectsClientCriteriaConfig={
			name:"client",
			defaultButtonLabel:"Client",
			filterType:"ARRAY",
			closeable:false,
			editable:false,
			buttonSelectedItemsFormater:function(data){
				return data.label;
			},
			filterValue:[{name:""+$scope.client.id+"",label:$scope.client.name,ticked:false}],
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+$scope.client.id+""){
						items.push(item);
					}
				});
				return items;
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.employeesProjectsCriteriaBarConfig={
			criterions:[$scope.employeesProjectsClientCriteriaConfig],
			autoFilter:true,
			filters:[]
		};
	
	$scope.percents=[];
	$scope.userDurations=[];
	$scope.totalDurations=[];
	 $scope.options = {
	         animate:{ duration: 1000, enabled: true },
	         barColor:'#3983c2',
	         scaleColor:false,
	         lineWidth:3,
	         size:50,
	         lineCap: 'butt'
	     };
	
	$scope.clientsEmployeesTableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10 ,
        sorting : {
        	joinDate : 'desc' // initial sorting
		}// count per page
    }, {
        groupBy: function (employeesProjects) {
          return employeesProjects.project.name;
        },
        getData: function($defer, params) {
        		EmployeesProjectsREST.query(
						{
        					companyId:USERINFO.company.id,
        					clientId:client.id,
							page:params.$params.page-1,
							size:params.$params.count,
							sort:params.$params.sorting,
							filter:$scope.employeesProjectsTableFilter
						},function(data) {
					params.total(data.length);
					$scope.employeesProjectsStartIndex=(params.$params.page-1)*params.$params.count+1;
					$scope.employeesProjectsEndIndex=$scope.employeesProjectsStartIndex+data.length-1;
					if(data.length>=1){
						$scope.hasDatas=true;
					}else {
						$scope.hasDatas=false;
					}
					
					angular.forEach(data,function(project){
			    		 $scope.percents[project.project.id]=0;
			    		 $scope.totalDurations[project.project.id]=0;
			    		 $http.get(_contextPath + "app/api/worklog/project/"+project.project.id,{})
					 		.success(function(data, status) {
					 			$scope.totalDurations[project.project.id]=data;
					 			
					 			if(project.id.employeeId!==null){
						 			$http.get(_contextPath + "app/api/worklog/project/"+project.project.id,{params:{'employeId':''+project.id.employeeId+''}})
						 				.success(function(data, status) {
						 					$scope.userDurations[project.id.employeeId]=[];
						 					$scope.userDurations[project.id.employeeId][project.project.id]=data;
						 					$scope.percents[project.id.employeeId]=[];
						 					var percent=0;
						 					if($scope.totalDurations[project.project.id]==0){
						 						percent=0;
						 					}else {
						 						percent= Math.round( ((data/$scope.totalDurations[project.project.id])*100) * 100 ) / 100;
											}
						 					$timeout(function(){
						 						$scope.percents[project.id.employeeId][project.project.id]=percent;
										     }, 1000);
						 				});
					 		}
					 		});
			    	 });
					
					 
					employeesProjects=data;
					// set new data
					$defer.resolve(data);
				});
        }
    });
});
/*COMPANY-CLIENT End of section*/