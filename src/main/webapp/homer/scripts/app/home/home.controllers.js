App.controller('HomeController',function ($scope,$rootScope,$translate,TasksREST,NoteREST,USERINFO,ActivitiesREST) {
	
	$scope.sortableOptions = {
	        connectWith: ".connectPanels",
	        handler: ".panel-body"
	    };
});