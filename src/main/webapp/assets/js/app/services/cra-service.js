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

		// function handleError( response ) {
		//
		// // The API response from the server should be returned in a
		// // nomralized format. However, if the request was not handled by the
		// // server (or what not handles properly - ex. server error), then we
		// // may have to normalize it on our end, as best we can.
		// if (
		// ! angular.isObject( response.data ) ||
		// ! response.data.message
		// ) {
		//
		// return( $q.reject( "An unknown error occurred." ) );
		//
		// }
		//
		// // Otherwise, use expected error message.
		// return( $q.reject( response.data.message ) );
		//
		// }
		//
		//
		// // I transform the successful response, unwrapping the application
		// data
		// // from the API response payload.
		// function handleSuccess( response ) {
		// return( response.data );
		//
		// }

	}
});