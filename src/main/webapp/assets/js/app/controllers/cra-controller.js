'use strict';

function CraController($scope,$rootScope,craService) {
	$rootScope.page={"title":"CRA","description":"View and manage you CRA"};
	craService.getCra('2014-06-30','2014-06-30',function callbackFunc(data){
		console.log(data);
	});
}
