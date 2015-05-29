/**
 * 
 */

App.controller('UserProfileController',function ($scope, $rootScope, UsersREST, alertService,EmployeesREST,USERINFO,ActivitiesREST,$upload) {
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
	
	$scope.selectedFile=undefined;
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
	$scope.timelineData=ActivitiesREST.forUser({id:USERINFO.id});
	
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
	
	$scope.onFileSelect=function(selectedFile){
		$scope.selectedFile=selectedFile;
	}
	
	$scope.uploadAvatar = function(hideFn) {
		if($scope.selectedFile){
			$scope.upload = $upload.upload({
				url : _contextPath+'app/api/users/avatar/'+USERINFO.id, // upload.php script, node.js route, or
											// servlet url
				file : $scope.selectedFile, // or list of files: $files for html5
											// only
			}).progress(
					function(evt) {
						console.log('percent: '	+ parseInt(100.0 * evt.loaded / evt.total));
					}).success(function(data, status, headers, config) {
				// file is uploaded successfully
				console.log(data);
				if(status==200){
					hideFn();
				}else {
					hideFn();
				}
			});
		}else {
			
		}
	};
});
