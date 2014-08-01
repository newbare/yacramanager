function TimeSheetController($scope,$rootScope,$http) {
	$rootScope.page={"title":"Timesheet","description":"View and manage timesheet"}
	
	$scope.timeType="duration";
	$scope.project=undefined;


	var fetchProjects = function(queryParams) {
		return $http.get(
				_contextPath + "/app/api/" + _companyid + "/project/employe/"
						+ _userId, {
					params : {
						"me" : true
					}
				}).then(queryParams.success);
	}; 

	$scope.projectSelectOptions = {
		minimumInputLength : 3,
		ajax : {
			data : function(term, page) {
				return {
					query : term
				};
			},
			quietMillis : 500,
			transport : fetchProjects,
			results : function(data, page) { // parse the results into the
												// format expected by Select2
				return {
					results : data
				};
			}
		}
	};
	
	var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
	
    
    $scope.employeCriteriaConfig={
			name:"employe",
			defaultButtonLabel:"Who",
			filterType:"ARRAY",
			closeable:false,
			filterValue:[],
			buttonSelectedItemsFormater:function(data){
				if(data.name==""+_userId+""){
					return '<i class="fa fa-user"></i> Me';
				}else {
					return '<i class="fa fa-user"></i> '+getUserInitials(data.label);
				}
			},
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+_userId+""){
						items.push(item);
					}
				});
				return items;
			},
			getData:function($defer){
				$http.get(_contextPath+"/app/api/users/managed/"+_userId,{params:{"me":true} })
					.success(function(data, status) {
						$defer.resolve(data);
					})
			},
			currentFilter:{},
			displayed: true
	};
    
    //criteria bar config
    $scope.criteriaBarConfig={
    		criterions:[$scope.employeCriteriaConfig],
    		autoFilter:true,
    		filters:[]
    	};
    
	
    $scope.onDayClick=function(date, jsEvent, view){
    	$scope.alertMessage = ("Day"+date + ' was clicked ');
    };
    /* alert on eventClick */
    $scope.onEventClick = function( event, allDay, jsEvent, view ){
        $scope.alertMessage = (event.title + ' was clicked ');
    };
    /* alert on Drop */
     $scope.onEventDrop = function(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view){
       $scope.alertMessage = ('Event Droped to make dayDelta ' + dayDelta);
    };
    /* alert on Resize */
    $scope.onEventResize = function(event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
       $scope.alertMessage = ('Event Resized to make dayDelta ' + minuteDelta);
    };
    
    $scope.onSelection=function( start, end, jsEvent, view ){
    	$scope.alertMessage="Start "+start+" End "+end;
    };
    
	/* config object */
    $scope.uiConfig = {
      calendar:{
        height: 450,
        editable: true,
        selectable:true,
        header:{
          left: 'today prev,next',
          center: 'title',
          right: 'agendaDay agendaWeek month  '
        },
        defaultView:'agendaWeek',
        dayClick:$scope.onDayClick,
        eventClick: $scope.onEventClick,
        eventDrop: $scope.onEventDrop,
        eventResize: $scope.onEventResize,
        select:$scope.onSelection
      }
    };
    
    $scope.eventSource = {
            url: _contextPath+"/app/api/worklog",
            type : 'GET'
    };
    $scope.eventSources = [$scope.eventSource];
    
    
}
