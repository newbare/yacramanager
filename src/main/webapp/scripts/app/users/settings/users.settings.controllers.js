/**
 * 
 */

App.controller('UserSettingsController',function ($scope,$rootScope,UserSettingsREST) {
	$rootScope.page={"title":"User settings","description":"Edit your settings"};
	
	$scope.settingsFilter='';
	
	$scope.settings=[];
	$scope.inserted={};
	
	UserSettingsREST.get(function(data) {
		    $scope.settings = data.result;
	});
	
	
	$scope.saveSettings = function(setting, id) {
	    //$scope.user not updated yet
//	    angular.extend(data, {id: id});
		if(id!==undefined && id !==null){
			setting.id=id;
			return UserSettingsREST.update(setting);
		}
	    return UserSettingsREST.save(setting);
	  };

	  // remove user
	  $scope.removeSettings = function(index) {
	    UserSettingsREST.remove({id:$scope.settings[index].id}).$promise.then(function(result){
	    	 $scope.settings.splice(index, 1);
	    });
	  };

	  // add user
	  $scope.addSettings = function() {
	   $scope.inserted = {
	      id: null,
	      key: '',
	      value: null,
	      description: null 
	    };
	  $scope.settings.push($scope.inserted);
	  };
});
