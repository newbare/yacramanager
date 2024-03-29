App.controller('TasksWidgetController',function ($scope,TasksREST,USERINFO) {
//	Tasks section
	$scope.tasks=[];
	$scope.refreshTask=function(){
		TasksREST.getAll(
				{
					companyId:USERINFO.company.id,
					employeId:USERINFO.id,
					page:0,
					size:20,
					sort:{
						lastModifiedDate : 'desc' // initial sorting
					}
				},function(data) {
					$scope.tasks=data;
				});
	};
	$scope.refreshTask();
});

App.controller('ExpensesWidgetController',function ($scope,NoteREST,USERINFO) {
//	Fees section
	$scope.expenses=[];
	$scope.refreshExpenses=function(){
		NoteREST.getAll(
				{
					employeId:USERINFO.id,
					page:0,
					size:10,
					sort:{
						date : 'desc' // initial sorting
					}
				},function(data) {
					$scope.expenses=data;
				});
	};
	$scope.refreshExpenses();
});
	
App.controller('ActivitiesWidgetController',function ($scope,USERINFO,ActivitiesREST) {
//	Activities feed
	$scope.timelineData=undefined
	$scope.refreshLastActivities=function(){
		$scope.timelineData=ActivitiesREST.forUser({id:USERINFO.id});
	};
	$scope.refreshLastActivities();
});
	
