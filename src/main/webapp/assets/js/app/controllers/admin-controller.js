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
	
	$scope.company={};
	$scope.company.contacts=[];
	$scope.addContact=function(){
		$scope.company.contacts.push({"email":"","numeroTelephone":""});
	};
	$scope.reset=function(){
		$scope.company={};
		$scope.company.contacts=[];
	};
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			name : 'asc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			
			CompanyCRUDREST.get(
					{
						page:params.$params.page-1,
						size:params.$params.count
					},function(data) {
				params.total(data.totalCount);
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
			alertService.showInfo('Confirmation', 'Donnï¿½ sauvegardï¿½');
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
			alertService.showInfo('info','Confirmation', 'Company supprimÃ©');
		}, function(error) {
			console.log(error);
			alertService.showError('error','' + error.status, error.data);
		});
	};
	$scope.putCompany = function() {
		CompanyCRUDREST.update($scope.company).$promise.then(function(result) {
			alertService.showInfo('info','Created','Mise à jour effectué');
			$scope.reset();
			$scope.tableParams.reload();
			hideFn();
		});
	};
}

function AdminSettingsController($scope,$rootScope) {
	$scope.page={"title":"Settings","description":"Home page"}
}