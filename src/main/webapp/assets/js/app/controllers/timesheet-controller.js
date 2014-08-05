function TimeSheetController($scope,$rootScope,$http,$sce,WorkLogCRUDREST,alertService) {
	$rootScope.page={"title":"Timesheet","description":"View and manage timesheet"}
	$scope.timeType="duration";
	$scope.timesheetCalendarTitle=undefined;
	$scope.durationTime=0;
	var fetchProjects = function(queryParams) {
		return $http.get(
				_contextPath + "/app/api/" + _userCompanyId + "/project/employe/"
						+ _userId, {
					params : {}
				}).then(function(response) {
					$scope.projects=response.data.result;
				});
	}; 

	fetchProjects();
	
	$scope.selectProject=function(project){
		$scope.project=project;
		fetchTasks();
	}
	$scope.selectTask=function(task){
		$scope.task=task;
	}
	
	$scope.selectTask=function(task){
		$scope.task=task;
	}
	
	var fetchTasks = function(queryParams) {
		return $http.get(
				_contextPath + "/app/api/" + _userCompanyId + "/task/"+$scope.project.id+"/"+ _userId, {
					params : {}
				}).then(function(response) {
					$scope.tasks=response.data.result;
				});
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
    
    $scope.calendarDateCriteriaConfig={
			name:"date",
			defaultButtonLabel:"Calendar date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
				if($scope.currentView===undefined) return;
				$scope.currentView.calendar.gotoDate(value.value);
			},
			currentFilter:{},
			displayed: true
	};
    
    //criteria bar config
    $scope.criteriaBarConfig={
    		criterions:[$scope.employeCriteriaConfig,$scope.calendarDateCriteriaConfig],
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
    
    $scope.onViewRender=function(view, element){
    	$scope.currentView=view;
    	$scope.timesheetCalendarTitle=$sce.trustAsHtml(view.title);
    };
    
	/* config object */
    $scope.uiConfig = {
      calendar:{
        height: 450,
        editable: true,
        selectable:true,
        header:{
          left: '',
          center: '',
          right: ''
        },
        defaultView:'agendaWeek',
        viewRender: $scope.onViewRender,
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
    
    /* Change View */
    $scope.changeView = function(view,calendar) {
      calendar.fullCalendar('changeView',view);
    };
    
    $scope.next = function(calendar) {
      calendar.fullCalendar('next');
    };
    $scope.previous = function(calendar) {
        calendar.fullCalendar('prev');
    };
    
    $scope.today = function(calendar) {
        calendar.fullCalendar('today');
    };
    
    $scope.isTodaySelected=function(){
    	if($scope.currentView===undefined) return;
    	var today = new Date();
		if (today >= $scope.currentView.start && today < $scope.currentView.end) {
			return true;
		}
		else {
			return false;
		}
    };
    $scope.postWorkLog = function(hideFn) {
    	$scope.worklog={};
    	 $scope.worklog.title="";
    	 $scope.worklog.start=null;
    	 $scope.worklog.end=null;
    	 $scope.worklog.duration=$scope.durationTime;
    	 $scope.worklog.taskId= $scope.task.id;
    	 $scope.worklog.taskName=$scope.task.name;
    	 $scope.worklog.description=$scope.description;
    	 $scope.worklog.employeId=_userId;
    	 WorkLogCRUDREST.save($scope.worklog).$promise.then(function(result) {
    		 hideFn();
    		 alertService.showInfo('Confirmation', 'Donn� sauvegard�');
		});
    	
    }
    
}
