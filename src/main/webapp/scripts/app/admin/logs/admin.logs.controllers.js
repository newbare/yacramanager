App.controller('LogsController',function ($scope, LogsService,ngTableParams) {
    $scope.loggers = [];
    $scope.totalLoggersCount = 0;
    $scope.filter='';
    
    $scope.changeLevel = function (name, level) {
        LogsService.changeLevel({name: name, level: level}, function () {
        	$scope.doFilter();
        });
    };
    $scope.doFilter=function(){
    	$scope.tableParams.page(1); 
    	$scope.tableParams.reload();
    };
    
    $scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 100, // count per page
		sorting : {
			date : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			if(true){
				LogsService.findAll(
						{
							page:params.$params.page-1,
							size:params.$params.count,
							key:$scope.filter
						},function(data) {
					params.total(data.totalCount);
					$scope.totalLoggersCount=data.totalCount;
					$scope.startIndex=data.startIndex;
					$scope.endIndex=data.endIndex;
					if(data.totalCount>=1){
						$scope.hasDatas=true;
					}else {
						$scope.hasDatas=false;
					}
					 $scope.loggers=data.result;
					// set new data
					$defer.resolve(data.result);
				});
			}
		}});
});