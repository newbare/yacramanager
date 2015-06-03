App.controller('MetricsController',function ($scope, MetricsService,HealthCheckService) {

	$scope.healthCheck={};
	$scope.currentStackTrace=[];
	$scope.setStackTrace=function(key,error){
		$scope.currentStackTrace={"key":key,"error":error};
	};
    $scope.refresh = function() {
    	
    	HealthCheckService.check().then(function(promise) {
            $scope.healthCheck = promise;
        },function(promise) {
            $scope.healthCheck = promise.data;
        });

    	 $scope.metrics = MetricsService.get();

         $scope.metrics.$get({}, function(items) {

	            $scope.servicesStats = {};
	            $scope.cachesStats = {};
	            angular.forEach(items.timers, function(value, key) {
	                if (key.indexOf("web.api") != -1 || key.indexOf("service") != -1) {
	                    $scope.servicesStats[key] = value;
	                }

	                if (key.indexOf("net.sf.ehcache.Cache") != -1) {
	                    // remove gets or puts
	                    var index = key.lastIndexOf(".");
	                    var newKey = key.substr(0, index);

	                    // Keep the name of the domain
	                    index = newKey.lastIndexOf(".");
	                    $scope.cachesStats[newKey] = {
	                        'name': newKey.substr(index + 1),
	                        'value': value
	                    };
	                }
	            });
	        });
    };

    $scope.refresh();
    $scope.getLabelClass = function(threadState) {
        if (threadState == 'RUNNABLE') {
            return "label-success";
        } else if (threadState == 'WAITING') {
            return "label-info";
        } else if (threadState == 'TIMED_WAITING') {
            return "label-warning";
        } else if (threadState == 'BLOCKED') {
            return "label-danger";
        }
    };
});