function CompanyController($scope, $rootScope) {
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
};

/*COMPANY-EMPLOYEE section*/
function CompanyEmployeesViewController($scope, $rootScope,$http,EmployeesCRUDREST,ngTableParams,$state){
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
			name:"prenom",
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
			name:"nom",
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
			name:"civilite",
			defaultButtonLabel:"H/F",
			filterType:"ARRAY",
			closeable:true,
			filterValue:
				[{name:"HOMME",label:"Homme",ticked:false},{name:"FEMME",label:"Femme",ticked:false}]
				,
			onFilter: function(value) {
				console.log('Filter checkbox ['+value.field+'] selected items '+value.value.length);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.dateNaissanceCriteriaConfig={
			name:"dateNaissance",
			defaultButtonLabel:"Date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.criteriaBarConfig={
		criterions:[$scope.companyCriteriaConfig,$scope.civilityCriteriaConfig,$scope.firstNameCriteriaConfig,$scope.lastNameCriteriaConfig,$scope.dateNaissanceCriteriaConfig],
		autoFilter:true,
		filters:[]
	};
	
	$scope.doFilter=function(data){
		console.log("Server filer launch with: "+JSON.stringify(data));
		var serverFilter={filter:data};
		$scope.criteriaBarFilter=JSON.stringify(serverFilter);
		$scope.$broadcast('criteriaDofilter', JSON.stringify(serverFilter));
	};
}

function CompanyEmployeesQuickViewController($scope,$http,EmployeesCRUDREST,ngTableParams,$state){
	$scope.employees=[];
	$scope.employeesListFilter="";
	$scope.refreshEmployeesList=function(){
		if($scope.employeesListFilter !=="" && $scope.employeesListFilter !== undefined ){
			EmployeesCRUDREST.get(
					{
						//sort:params.$params.sorting,
						filter:$scope.employeesListFilter
					},function(data) {
						$scope.employees=data.result;
					});
		}
	};
	$scope.doFilterList=function(data){
		console.log("Server filer launch with: "+JSON.stringify(data));
		$scope.employeesListFilter=data;
		$scope.refreshEmployeesList();
	};
	$scope.doFilterList($scope.criteriaBarFilter);
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
}

function CompanyEmployeesListController($scope, $rootScope,$http,EmployeesCRUDREST,ngTableParams,$state){
	
	 $scope.changeSelection = function(user) {
	        //console.info(user);
	        $state.go('company.employees.details',{ id:user.id });
	 };
	
	$scope.hasDatas=false;
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	};
	$scope.tableFilter="";
	
	
	$scope.doFilterList=function(data){
		console.log("Server filer launch with: "+JSON.stringify(data));
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
				EmployeesCRUDREST.get(
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
	$scope.doFilterList($scope.criteriaBarFilter);
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
	
};

function CompanyEmployeesOverviewController($scope,EmployeesCRUDREST, $stateParams){
	$scope.employeId=$stateParams.id;
	$scope.employe=undefined;
	EmployeesCRUDREST.get(
			{id:$scope.employeId},function(data) {
				$scope.employe=data;
			});
}
/*COMPANY-EMPLOYEE End of section*/


/*COMPANY-CLIENT section*/
function CompanyClientsViewController($scope, $rootScope,$http,ClientsCRUDREST,ngTableParams,$state){
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
		console.log("Server filer launch with: "+JSON.stringify(data));
		var serverFilter={filter:data};
		$scope.criteriaBarFilter=JSON.stringify(serverFilter);
		$scope.$broadcast('criteriaDofilter', JSON.stringify(serverFilter));
	};
}

function CompanyClientsQuickViewController($scope,$http,ClientsCRUDREST,ngTableParams,$state){
	$scope.clients=[];
	$scope.clientsListFilter="";
	$scope.refreshClientsList=function(){
		if($scope.clientsListFilter !=="" && $scope.clientsListFilter !== undefined ){
			ClientsCRUDREST.get(
					{
						companyId : _userCompanyId,
						//sort:params.$params.sorting,
						filter:$scope.clientsListFilter
					},function(data) {
						$scope.clients=data.result;
					});
		}
	};
	$scope.doFilterList=function(data){
		console.log("Server filer launch with: "+JSON.stringify(data));
		$scope.clientsListFilter=data;
		$scope.refreshClientsList();
	};
	$scope.doFilterList($scope.criteriaBarFilter);
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
}

function CompanyClientsListController($scope, $rootScope,$http,ClientsCRUDREST,ngTableParams,$state){
	
	 $scope.changeSelection = function(client) {
	        //console.info(user);
	        $state.go('company.clients.details',{ id:client.id });
	 };
	
	$scope.hasDatas=false;
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	};
	$scope.tableFilter="";
	
	
	$scope.doFilterList=function(data){
		console.log("Server filer launch with: "+JSON.stringify(data));
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
				ClientsCRUDREST.get(
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
	
};

function CompanyClientsOverviewController($scope,ClientsCRUDREST, $stateParams){
	$scope.clientId=$stateParams.id;
	$scope.client=undefined;
	ClientsCRUDREST.get(
			{companyId : _userCompanyId,id:$scope.clientId},function(data) {
				$scope.client=data;
			});
}
/*COMPANY-CLIENT End of section*/


/*COMPANY-PROJECT section*/
function CompanyProjectsViewController($scope, $rootScope,$http,ProjectsCRUDREST,ngTableParams,$state){
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
		console.log("Server filer launch with: "+JSON.stringify(data));
		var serverFilter={filter:data};
		$scope.criteriaBarFilter=JSON.stringify(serverFilter);
		$scope.$broadcast('criteriaDofilter', JSON.stringify(serverFilter));
	};
}

function CompanyProjectsQuickViewController($scope,$http,ProjectsCRUDREST,ngTableParams,$state){
	$scope.projects=[];
	$scope.projectsListFilter="";
	$scope.refreshProjectsList=function(){
		if($scope.projectsListFilter !=="" && $scope.projectsListFilter !== undefined ){
			ProjectsCRUDREST.get(
					{
						companyId : _userCompanyId,
						//sort:params.$params.sorting,
						filter:$scope.projectsListFilter
					},function(data) {
						$scope.projects=data.result;
					});
		}
	};
	$scope.doFilterList=function(data){
		console.log("Server filer launch with: "+JSON.stringify(data));
		$scope.projectsListFilter=data;
		$scope.refreshProjectsList();
	};
	$scope.doFilterList($scope.criteriaBarFilter);
	$scope.$on('criteriaDofilter', function(event, args) {
		$scope.doFilterList(args);
	});
}

function CompanyProjectsListController($scope, $rootScope,$http,ProjectsCRUDREST,ngTableParams,$state){
	
	 $scope.changeSelection = function(project) {
	        //console.info(user);
	        $state.go('company.projects.details',{ id:project.id });
	 };
	
	$scope.hasDatas=false;
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	};
	$scope.tableFilter="";
	
	
	$scope.doFilterList=function(data){
		console.log("Server filer launch with: "+JSON.stringify(data));
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
				ProjectsCRUDREST.get(
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
	
};

function CompanyProjectsOverviewController($scope,ProjectsCRUDREST, $stateParams){
	$scope.projectId=$stateParams.id;
	$scope.project=undefined;
	ProjectsCRUDREST.get(
			{companyId : _userCompanyId,id:$scope.projectId},function(data) {
				$scope.project=data;
			});
}
/*COMPANY-PROJECT End of section*/

function CompanyProjectsController($scope, $rootScope,$http,ngTableParams,ProjectsCRUDREST){
	
	$scope.$on('userInfo', function(event, userInfo) {
		//do afteruserInfo is retrieved 
	
		
		//End userInfo listener
	});
	
	
	$scope.hasDatas=false;
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	}
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
					})
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
		console.log("Server filer launch with: "+JSON.stringify(data));
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
				ProjectsCRUDREST.get(
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
	
};
