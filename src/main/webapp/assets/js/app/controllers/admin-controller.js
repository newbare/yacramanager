function AdminController($scope,$rootScope) {
	$rootScope.page={"title":"Admin","description":"Configure application"}
}

function AdminHomeController($scope,$rootScope) {
	$scope.page={"title":"Admin board","description":"Home page"}
}

function AdminCompaniesController($scope,$rootScope,CompanyCRUDREST,ngTableParams) {
	$scope.page={"title":"Companies management","description":"Home page"};
	var allAbsence=[];
	$scope.hasDatas=false;
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
				allAbsence=data.result;
				// set new data
				$defer.resolve(data.result);
			});
		}});
}

function AdminSettingsController($scope,$rootScope) {
	$scope.page={"title":"Settings","description":"Home page"}
}