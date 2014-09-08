/**
 * 
 */

function UserProfileController($scope, $rootScope, UsersREST, alertService,EmployeesREST) {
	$rootScope.page = {
		"title" : "User profile",
		"description" : "Edit your profile"
	}
	$scope.newPassword = "";
	$scope.confirmPassword = "";
	$scope.canChangePassword = function() {
		return $scope.newPassword != "" && $scope.confirmPassword != ""
				&& $scope.newPassword == $scope.confirmPassword;
	};
	$scope.changePassword = function() {
		UsersREST.changePassword(JSON.stringify($scope.newPassword), function(
				status) {
			alertService.show('success','Confirmation','Password has been changed');
		});
	};
	
	$scope.employe=undefined;
	$scope.refresh=function(){
		$scope.employe=EmployeesREST.get({id:_userId});
	};
	
	$scope.refresh();
	
	$scope.currentTab = 'basicInfos';
	$scope.activateTab = function(tab) {
		$scope.currentTab = tab;
	}
	$scope.isActiveTab = function(tab) {
		return tab == $scope.currentTab;
	};
	
	$scope.updateEmploye = function() {
		return $scope.employe.$update();
	};
}
