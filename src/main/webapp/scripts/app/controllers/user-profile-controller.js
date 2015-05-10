/**
 * 
 */

function UserProfileController($scope, $rootScope, UsersREST, alertService,EmployeesREST,USERINFO) {
	$rootScope.page = {
		"title" : "User profile",
		"description" : "Edit your profile"
	};
	$scope.civilities = [ {
		value : 'HOMME',
		text : 'Homme'
	}, {
		value : 'FEMME',
		text : 'Femme'
	} ];
	$scope.newPassword = "";
	$scope.confirmPassword = "";
	$scope.canChangePassword = function() {
		return $scope.newPassword !== "" && $scope.confirmPassword !== ""	&& $scope.newPassword === $scope.confirmPassword;
	};
	$scope.changePassword = function() {
		UsersREST.changePassword(JSON.stringify($scope.newPassword), function(status) {
			alertService.show('success','Confirmation','Password has been changed');
		});
	};
	
	$scope.refresh=function(){
		$scope.employe=EmployeesREST.get({id:USERINFO.id});
	};
	
	$scope.refresh();
	
	$scope.currentTab = 'basicInfos';
	$scope.activateTab = function(tab) {
		$scope.currentTab = tab;
	};
	$scope.isActiveTab = function(tab) {
		return tab == $scope.currentTab;
	};
	
	$scope.updateEmploye = function() {
		return $scope.employe.$update();
	};
}
