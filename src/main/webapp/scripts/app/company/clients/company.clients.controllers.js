/*COMPANY-CLIENT section*/
App.controller('CompanyClientsViewController',function ($scope, $rootScope,$http,ClientsREST,ngTableParams,$state,alertService,$state,USERINFO){
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
			filterValue:[{name:""+USERINFO.company.id+"",label:USERINFO.company.name,ticked:false}],
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

App.controller('CompanyClientsOverviewController',function ($scope,ClientsREST,ProjectsREST,client,alertService,USERINFO,ActivitiesREST,EmployeesProjectsREST,ngTableParams){
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
        	if($scope.employeesProjectsTableFilter!==undefined && $scope.employeesProjectsTableFilter!==''){
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
					employeesProjects=data;
					// set new data
					$defer.resolve(data);
				});
			}
        }
    });
});
/*COMPANY-CLIENT End of section*/