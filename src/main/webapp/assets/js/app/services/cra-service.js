/**
 * 
 */
App.service('craService', function($http, $q) {
	this.getCra = function(start, end, callbackFunc) {
		var request = $http({
			method : "get",
			url : "rest/cra",
			params : {
				start : start,
				end : end
			}
		});
		request.success(function(data, status, headers, config) {
			callbackFunc(data);
		}).error(function(data, status, headers, config) {
			alert("error" + status);
		});
	}
});