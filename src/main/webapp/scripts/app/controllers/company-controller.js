function CompanyController($scope, $rootScope,$state) {
	if($scope.userInfo){
		$rootScope.page={
				"title" : $scope.userInfo.company.name,
				"description" : "Dashboard"
			};
	}
	$scope.$on('userInfo', function(event, args) {
		$rootScope.page = {
			"title" : args.company.name,
			"description" : "Dashboard"
		};
	});
	$scope.$state=$state;
}

/*COMPANY-EMPLOYEE section*/
function CompanyEmployeesController($scope,$state) {
	$scope.currentTab = 'basicInfos';
	$scope.currentEmployee=null;
	$scope.activateTab = function(tab) {
		$scope.currentTab = tab;
	};
	$scope.isActiveTab = function(tab) {
		return tab == $scope.currentTab;
	};
	$scope.civilities = [ {
		value : 'HOMME',
		text : 'Homme'
	}, {
		value : 'FEMME',
		text : 'Femme'
	} ];
	$scope.birthDay=new Date();
}

function CompanyEmployeesViewController($scope, $rootScope,$http,EmployeesREST,CompanyREST,ngTableParams,$state,alertService){
	$scope.$state=$state;
	$scope.hasDatas=false;
	$scope.viewStyle=undefined;
	$scope.employe={};
	$scope.employeToInvite={};
	$scope.companyCriteriaConfig={
			name:"company",
			defaultButtonLabel:"Company",
			filterType:"ARRAY",
			closeable:false,
			editable:false,
			buttonSelectedItemsFormater:function(data){
				return data.label;
			},
			filterValue:[{name:_userCompanyId,label:_userCompanyName,ticked:false}],
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+_userCompanyId+""){
						items.push(item);
					}
				});
				return items;
			},
			currentFilter:{},
			displayed: true
	};
	$scope.firstNameCriteriaConfig={
			name:"firstName",
			defaultButtonLabel:"First name",
			filterType:"TEXT",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.lastNameCriteriaConfig={
			name:"lastName",
			defaultButtonLabel:"Last name",
			filterType:"TEXT",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.civilityCriteriaConfig={
			name:"gender",
			defaultButtonLabel:"H/F",
			filterType:"ARRAY",
			closeable:true,
			filterValue:
				[{name:"HOMME",label:"Homme",ticked:false},{name:"FEMME",label:"Femme",ticked:false}],
			onFilter: function(value) {
				console.log('Filter checkbox ['+value.field+'] selected items '+value.value.length);
			},
			currentFilter:{},
			displayed: false
	};
	
	$scope.dateNaissanceCriteriaConfig={
			name:"birthDay",
			defaultButtonLabel:"Date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: false
	};
	
	$scope.criteriaBarConfig={
		criterions:[$scope.companyCriteriaConfig,$scope.civilityCriteriaConfig,$scope.firstNameCriteriaConfig,$scope.lastNameCriteriaConfig,$scope.dateNaissanceCriteriaConfig],
		autoFilter:true,
		filters:[]
	};
	
	$scope.doFilter=function(data){
		var serverFilter={filter:data};
		$scope.criteriaBarFilter=JSON.stringify(serverFilter);
		$scope.$broadcast('criteriaDofilter', JSON.stringify(serverFilter));
	};
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	};
	$scope.tableFilter="";
	
	
	$scope.doFilterList=function(data){
		$scope.tableFilter=data;
		$scope.refreshDatas();
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
			if($scope.tableFilter!=="" && $scope.tableFilter!==undefined){
				EmployeesREST.get(
						{
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
	
	$scope.postEmploye=function(hideFn){
		$scope.employe.companyId=_userCompanyId;
		EmployeesREST.save($scope.employe).$promise.then(function(result) {
   		 hideFn();
   		 alertService.show('info','Confirmation', 'Employe created');
		});
	};
	$scope.inviteEmploye=function(hideFn){
		$scope.employe.companyId=_userCompanyId;
		CompanyREST.inviteEmployee({"id": _userCompanyId},$scope.employeToInvite.email).$promise.then(function(result) {
   		 hideFn();
   		 alertService.show('info','Confirmation', 'Employe has been invited ');
		});
	};
}

function CompanyEmployeesQuickViewController($scope,$http,EmployeesREST,ngTableParams,$state){
	$scope.employees=[];
	$scope.employeesListFilter="";
	$scope.doFilterList=function(data){
		$scope.employeesListFilter=data;
	};
	$scope.tableParams.settings().counts=[];
	$scope.doFilterList($scope.criteriaBarFilter);
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
}

function CompanyEmployeesListController($scope, $rootScope,$http,EmployeesREST,ngTableParams,$state){
	
	 $scope.changeSelection = function(user) {
	        //console.info(user);
	        $state.go('company.employees.details',{ id:user.id });
	 };
	 $scope.tableParams.settings().counts=[10, 25, 50, 100];
	 
}

function CompanyEmployeesOverviewController($scope,employe,EmployeesREST){
	$scope.employe=employe;
	$scope.activateTab('basicInfos');
	$scope.updateEmploye = function() {
		return EmployeesREST.update($scope.employe);
	};
	$scope.employeManager=undefined;
	$scope.employe.$promise.then(function(result) {
		$scope.employeManager=result.manager;
		$scope.currentEmployee=result;
	});
	$scope.addPhoneNumbers=function(employe){
		employe.phoneNumbers.push('');
	};
	$scope.format=function(employee){
		return employee.firstName +' '+employee.lastName;
	};
	
	$scope.isCurrentSelected=function(employe){
		if($scope.currentEmployee!=undefined){
			return $scope.currentEmployee.id==employe.id;
		}else {
			return false;
		}
	};
	
	EmployeesREST.get(
			{
				page:0,
				size:100,
				sort:"lastName",
				filter:{"filter":[{"type":"ARRAY","field":"company","value":[{"name":_userCompanyId,"label":"","ticked":true}]}]}
			},function(data) {
				$scope.companyEmployees=data.result;
	});
	$scope.updateManager=function(){
		EmployeesREST.updateManager({"employeeId": employe.id},$scope.employeManager.id,function(response){
			
		})
	};
}
/*COMPANY-EMPLOYEE End of section*/


/*COMPANY-CLIENT section*/
function CompanyClientsViewController($scope, $rootScope,$http,ClientsREST,ngTableParams,$state,alertService,$state){
	$scope.tableFilter="";
	$scope.$state=$state;
	$scope.client={};
	$scope.companyCriteriaConfig={
			name:"company",
			defaultButtonLabel:"Company",
			filterType:"ARRAY",
			closeable:false,
			editable:false,
			buttonSelectedItemsFormater:function(data){
				return data.label;
			},
			filterValue:[{name:_userCompanyId,label:_userCompanyName,ticked:false}],
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+_userCompanyId+""){
						items.push(item);
					}
				});
				return items;
			},
			currentFilter:{},
			displayed: true
	};
	$scope.nameCriteriaConfig={
			name:"name",
			defaultButtonLabel:"Client name",
			filterType:"TEXT",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				//console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	
	$scope.criteriaBarConfig={
		criterions:[$scope.companyCriteriaConfig,$scope.nameCriteriaConfig],
		autoFilter:true,
		filters:[]
	};
	
	$scope.doFilter=function(data){
		var serverFilter={filter:data};
		$scope.criteriaBarFilter=JSON.stringify(serverFilter);
		$scope.$broadcast('criteriaDofilter', JSON.stringify(serverFilter));
	};
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	};
	
	$scope.hasDatas=false;
	
	$scope.doFilterList=function(data){
		$scope.tableFilter=data;
		$scope.refreshDatas();
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
			if($scope.tableFilter!=="" && $scope.tableFilter!==undefined){
				ClientsREST.get(
						{
							companyId : _userCompanyId,
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
	$scope.postClient=function(hideFn){
		$scope.client.companyId=parseInt(_userCompanyId);
		ClientsREST.save({companyId :_userCompanyId},$scope.client).$promise.then(function(result) {
   		 hideFn();
   		 alertService.show('info','Confirmation', 'Client created');
   		$scope.tableParams.reload();
		});
	};
}

function CompanyClientsQuickViewController($scope,$http,ClientsREST,ngTableParams,$state){
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
}

function CompanyClientsListController($scope, $rootScope,$http,ClientsREST,ngTableParams,$state){
	 $scope.changeSelection = function(client) {
	      $state.go('company.clients.details',{ id:client.id });
	 };
	 $scope.tableParams.settings().counts=[10, 25, 50, 100];
}

function CompanyClientsOverviewController($scope,ClientsREST,ProjectsREST,client,alertService){
	$scope.client=client;
	$scope.contactFilter='';
	
	$scope.updateClient = function() {
		var clientToUpdate={id:$scope.client.id,name:$scope.client.name,email:$scope.client.email,contacts:$scope.client.contacts};
		angular.forEach(clientToUpdate.contacts,function(contact){
			delete contact.searchField;
		});
		return ClientsREST.update({companyId :_userCompanyId},clientToUpdate).$promise.then(
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
		ProjectsREST.save({companyId :_userCompanyId,clientId :client.id},projectToSave).$promise.then(function(result) {
	  		 alertService.show('info','Confirmation', 'Project created');
	  		 $scope.client=ClientsREST.get({companyId : _userCompanyId,id:$scope.client.id})
			});
	};
	$scope.updateProject=function(project){
		if(!project.isnew){
			var projectToUpdate=clone(project);
			delete projectToUpdate.searchField;
			ProjectsREST.update({companyId :_userCompanyId},projectToUpdate).$promise.then(
			        //success
			        function( value ){
			        	 $scope.tableParams.reload();
			        	 $scope.client=ClientsREST.get({companyId : _userCompanyId,id:$scope.client.id})
			        },
			        //error
			        function( error ){/*Do something with error*/}
			      );
			};
		}
		
	$scope.deleteProject=function(project,index){
		client.projects.splice(index,1);
	}
	
}
/*COMPANY-CLIENT End of section*/


/*COMPANY-PROJECT section*/
function CompanyProjectsViewController($scope, $rootScope,$http,ProjectsREST,ngTableParams,$state,alertService,$state){
	$scope.tableFilter="";
	$scope.$state=$state;
	$scope.project={};
	$scope.companyCriteriaConfig={
			name:"company",
			defaultButtonLabel:"Company",
			filterType:"ARRAY",
			closeable:false,
			editable:false,
			buttonSelectedItemsFormater:function(data){
				return data.label;
			},
			filterValue:[{name:_userCompanyId,label:_userCompanyName,ticked:false}],
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+_userCompanyId+""){
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
				$http.get(_contextPath+"/app/api/"+_userCompanyId+"/client",
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
		$scope.criteriaBarFilter=JSON.stringify(serverFilter);
		$scope.$broadcast('criteriaDofilter', JSON.stringify(serverFilter));
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
			if($scope.tableFilter!=="" && $scope.tableFilter!==undefined){
				ProjectsREST.get(
						{
							companyId : _userCompanyId,
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
	$scope.doFilterList($scope.criteriaBarFilter);
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
	$scope.postProject=function(hideFn){
		ProjectsREST.save({companyId :_userCompanyId,clientId :$scope.client.id},$scope.project).$promise.then(function(result) {
   		 hideFn();
   		 alertService.show('info','Confirmation', 'Project created');
		});
	};
}

function CompanyProjectsQuickViewController($scope,$http,ProjectsREST,ngTableParams,$state){
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
}

function CompanyProjectsListController($scope, $rootScope,$http,ProjectsREST,ngTableParams,$state){
	 $scope.tableParams.settings().counts=[10, 25, 50, 100];
	 $scope.changeSelection = function(project) {
	        //console.info(user);
	        $state.go('company.projects.details',{ id:project.id });
	 };
	
}

function CompanyProjectsOverviewController($scope, ProjectsREST, TasksREST,EmployeesREST,
		project) {
	$scope.project = project;
	$scope.employeeByTask={};
	$scope.groupByTask=true;
	
	$scope.reset=function(){
		$scope.project.$promise.then(function(project) {
			angular.forEach(project.tasks,function(task){
				TasksREST.getAssignedEmployee({companyId:_userCompanyId,taskId:task.id,employeId:_userId}, 
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
					filter:{"filter":[{"type":"ARRAY","field":"company","value":[{"name":_userCompanyId,"label":"","ticked":true}]}]}
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
		
		TasksREST.assignEmployeeToTask({companyId:_userCompanyId,taskId:currentTask.id,employeesIds:employeesList},null,function(result){
			$scope.reset();
			hideFn();
		});
	};
	$scope.unAssignEmploye=function(task,employee){
		TasksREST.unAssignEmployeeToTask({companyId:_userCompanyId,taskId:task.id,employeesIds:[employee.id]},null,
			function(result){
			$scope.reset();
			});
	}
	$scope.selectActiveTask =function(task){
		$scope.activeTask=task;
	}
}
/*COMPANY-PROJECT End of section*/

function CompanyProjectsController($scope, $rootScope,$http,ngTableParams,ProjectsREST){
	
	$scope.$on('userInfo', function(event, userInfo) {
		//do afteruserInfo is retrieved 
		//End userInfo listener
	});
	
	var fetchClients = function(queryParams) {
		return $http.get(
				_contextPath + "/app/api/" + _userCompanyId + "/client", {
					params : {}
				}).then(function(response) {
					$scope.clients=response.data.result;
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
			filterValue:[{name:_userCompanyId,label:_userCompanyName,ticked:false}],
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+_userCompanyId+""){
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
				$http.get(_contextPath+"/app/api/"+_userCompanyId+"/client",
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
							companyId : _userCompanyId,
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
	
}

function CompanySettingsController($scope,CompanySettingsREST,$rootScope){
	$rootScope.page={"title":"Company settings","description":"Edit your settings"};
	$scope.settingsFilter='';
	$scope.settings=[];
	$scope.inserted={};
	CompanySettingsREST.get(function(data) {
		    $scope.settings = data.result;
	});
	
	
	$scope.saveSettings = function(setting, id) {
	    //$scope.user not updated yet
//	    angular.extend(data, {id: id});
		if(id!==undefined && id !==null){
			setting.id=id;
			return CompanySettingsREST.update(setting);
		}
	    return CompanySettingsREST.save(setting);
	  };

	  // remove user
	  $scope.removeSettings = function(index) {
		  CompanySettingsREST.remove({id:$scope.settings[index].id}).$promise.then(function(result){
	    	 $scope.settings.splice(index, 1);
	    });
	  };

	  // add user
	  $scope.addSettings = function() {
	   $scope.inserted = {
	      id: null,
	      key: '',
	      value: null,
	      description: null 
	    };
	  $scope.settings.push($scope.inserted);
	  };
}
