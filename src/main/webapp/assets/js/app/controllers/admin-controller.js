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
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	}
	$scope.postCompany = function(hideFn) {
		CompanyCRUDREST.save($scope.company).$promise.then(function(result) {
			alertService.showInfo('Confirmation', 'Donn� sauvegard�');
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
			alertService.showInfo('Confirmation', 'Company supprimé');
		}, function(error) {
			console.log(error);
			alertService.showError('' + error.status, error.data);
		});
	};
	$scope.putCompany = function() {
		CompanyCRUDREST.update($scope.company).$promise.then(function(result) {
			alertService.showInfo('info','Created','Mise � jour effectu�');
			$scope.reset();
			$scope.tableParams.reload();
			hideFn();
		});
	};
}

function AdminSettingsController($scope,$rootScope) {
	$scope.page={"title":"Settings","description":"Home page"}
}