App.factory("CompanyCRUDREST", function($resource) {
	return $resource(_contextPath+"/app/api/company/:id" , {}, {update: { method: 'PUT', params: {id: '@id'} }});
});

App.factory('LogsService', function ($resource) {
    return $resource(_contextPath+"/app/api/logs", {}, {
        'findAll': { method: 'GET', isArray: true},
        'changeLevel':  { method: 'PUT'}
    });
});

App.factory('MetricsService',function ($resource) {
    return $resource(_contextPath+"/app/admin/metrics", {}, {
        'get': { method: 'GET'}
    });
});

//jhipsterApp.factory('ThreadDumpService', function ($http) {
//    return {
//        dump: function() {
//            var promise = $http.get('dump').then(function(response){
//                return response.data;
//            });
//            return promise;
//        }
//    };
//});
//
//jhipsterApp.factory('HealthCheckService', function ($rootScope, $http) {
//    return {
//        check: function() {
//            var promise = $http.get('health').then(function(response){
//                return response.data;
//            });
//            return promise;
//        }
//    };
//});