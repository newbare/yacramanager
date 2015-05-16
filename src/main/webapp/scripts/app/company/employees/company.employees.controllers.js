/*COMPANY-EMPLOYEE section*/
App.controller('CompanyEmployeesBasicInfosController',function ($scope,employe){
	$scope.employe=employe;
});

App.controller('CompanyEmployeesAdministrationController',function ($scope,employe,EmployeesREST,USERINFO){
	$scope.employe=employe;
	$scope.companyEmployees=[];
	EmployeesREST.get(
			{
				page:0,
				size:100,
				sort:"lastName",
				filter:{"filter":[{"type":"ARRAY","field":"company","value":[{"name":""+USERINFO.company.id+"","label":"","ticked":true}]}]}
			},function(data) {
				angular.forEach(data.result,function(tempEmploye){
					if(tempEmploye.id!==$scope.employe.id){
						$scope.companyEmployees.push(employe);
					}
				});
	});
	$scope.updateManager=function(){
		EmployeesREST.updateManager({"employeeId": employe.id},$scope.employeManager.id,function(response){
			
		})
	};
	$scope.userRight=[];
	angular.forEach(USERINFO.roles,function(currentRole){
		$scope.userRight.push(currentRole.role);
	});
	
	$scope.existingRights=['ROLE_INDEP','ROLE_SALARIE','ROLE_SSII_ADMIN','ROLE_ADMIN'];
	$scope.updateUserRights=function(){
		EmployeesREST.updateUserRights({"employeeId": employe.id},$scope.userRight,function(response){
					
				})
	}
});

App.controller('CompanyEmployeesActivitiesController',function ($scope,employe,ActivitiesREST){
	$scope.employe=employe;
	$scope.timelineSource=undefined;
	$scope.timelineSource=ActivitiesREST.forUser({id:$scope.employe.id});
});

App.controller('CompanyEmployeesSalaryController',function ($scope,employe){
	$scope.employe=employe;
});
App.controller('CompanyEmployeesController',function ($scope,$state,USERINFO) {
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
});

App.controller('CompanyEmployeesViewController',function ($scope, $rootScope,$http,EmployeesREST,CompanyREST,ngTableParams,$state,alertService,USERINFO){
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
		$scope.employe.companyId=USERINFO.company.id;
		EmployeesREST.save($scope.employe).$promise.then(function(result) {
   		 hideFn();
   		 alertService.show('info','Confirmation', 'Employe created');
		});
	};
	$scope.inviteEmploye=function(hideFn){
		$scope.employe.companyId=USERINFO.company.id;
		CompanyREST.inviteEmployee({"id": USERINFO.company.id},$scope.employeToInvite).$promise.then(function(result) {
   		 hideFn();
   		 alertService.show('info','Confirmation', 'Employe has been invited ');
		});
	};
});

App.controller('CompanyEmployeesQuickViewController',function ($scope,$http,EmployeesREST,ngTableParams,$state,USERINFO){
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
});

App.controller('CompanyEmployeesListController',function ($scope, $rootScope,$http,EmployeesREST,ngTableParams,$state){
	
	 $scope.changeSelection = function(user) {
	        //console.info(user);
	        $state.go('company.employees.details',{ id:user.id });
	 };
	 $scope.tableParams.settings().counts=[10, 25, 50, 100];
	 
});

App.controller('CompanyEmployeesOverviewController',function ($scope,employe,EmployeesREST,USERINFO,ActivitiesREST){
	$scope.employe=employe;
	$scope.activateTab('basicInfos');
	$scope.updateEmploye = function() {
		return EmployeesREST.update($scope.employe);
	};
	$scope.employeManager=undefined;
	$scope.employeManager=$scope.employe.manager;
	$scope.currentEmployee=$scope.employe;
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
});
/*COMPANY-EMPLOYEE End of section*/