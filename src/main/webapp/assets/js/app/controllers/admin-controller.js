function AdminController($scope,$rootScope) {
	$rootScope.page={"title":"Admin","description":"Configure application"}
}

function AdminHomeController($scope,$rootScope) {
	$scope.page={"title":"Admin board","description":"Home page"}
}

function AdminCompaniesController($scope,$rootScope,CompanyCRUDREST,ngTableParams,alertService,notifService) {
	$scope.page={"title":"Companies management","description":"Home page"};
	var allCompany=[];
	$scope.hasDatas=false;
	$scope.startIndex=0;
	$scope.endIndex=0;
	$scope.company={};
	$scope.company.contacts=[];
	$scope.addContact=function(){
		$scope.company.contacts.push({"email":"","numeroTelephone":""});
	};
	$scope.reset=function(){
		$scope.company={};
		$scope.company.contacts=[];
	};
	
	$scope.postCompany = function(hideFn) {
		CompanyCRUDREST.save($scope.company).$promise.then(function(result) {
			alertService.show('info','Confirmation', 'Donn� sauvegard�');
			$scope.reset();
			$scope.tableParams.reload();
			hideFn();
		});
	};
	$scope.deleteCompany = function(id) {
		CompanyCRUDREST.remove({
			id : id
		}).$promise.then(function(result) {
			$scope.tableParams.reload();
			alertService.show('info','Confirmation', 'Company supprimé');
		}, function(error) {
			console.log(error);
			alertService.show('danger','' + error.status, error.data);
		});
	};
	$scope.putCompany = function() {
		CompanyCRUDREST.update($scope.company).$promise.then(function(result) {
			alertService.show('info','Created','Mise � jour effectu�');
			$scope.reset();
			$scope.tableParams.reload();
			hideFn();
		});
	};
	
	
}


function AdminCompanyViewController($scope, $rootScope,$http,CompanyCRUDREST,ngTableParams,$state){
	
	$scope.$state=$state;
	$scope.tableFilter="";
	$scope.nameCriteriaConfig={
			name:"name",
			defaultButtonLabel:"Name",
			filterType:"TEXT",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.licenseEndDateCriteriaConfig={
			name:"licenseEndDate",
			defaultButtonLabel:"License end date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	$scope.registeredDateCriteriaConfig={
			name:"registeredDate",
			defaultButtonLabel:"Registered date",
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
		criterions:[$scope.nameCriteriaConfig,$scope.registeredDateCriteriaConfig,$scope.licenseEndDateCriteriaConfig],
		autoFilter:true,
		filters:[]
	};
	
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			registeredDate : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			
			CompanyCRUDREST.get(
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
				allCompany=data.result;
				// set new data
				$defer.resolve(data.result);
			});
		}});
	
	$scope.doFilter=function(data){
		var serverFilter={filter:data};
		$scope.tableFilter=JSON.stringify(serverFilter);
		$scope.refreshDatas();
	};
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	}
}


function AdminCompanyQuickViewController($scope,$http){
//	$scope.doFilterList($scope.criteriaBarFilter);
//	$scope.$on('criteriaDofilter', function(event, args) {
//		$scope.doFilterList(args);
//	});
	$scope.tableParams.settings().counts=[];
}


function AdminCompanyListController($scope, $rootScope,$http,$state){
	 $scope.changeSelection = function(company) {
	      $state.go('admin.company.details',{ id:company.id });
	 };
	 $scope.tableParams.settings().counts=[10, 25, 50, 100];
};

function AdminCompanyOverviewController($scope,CompanyCRUDREST, $stateParams){
	$scope.companyId=$stateParams.id;
	$scope.company=undefined;
	CompanyCRUDREST.get(
			{companyId : _userCompanyId,id:$scope.companyId},function(data) {
				$scope.company=data;
			});
}

function AdminSettingsController($scope,$rootScope) {
	$scope.page={"title":"Settings","description":"Home page"}
}