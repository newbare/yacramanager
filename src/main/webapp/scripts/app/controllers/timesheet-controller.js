function TimeSheetController($scope,$rootScope,$http,$sce,WorkLogREST,alertService,$popover,$compile,$modal,ngTableParams) {
	$rootScope.page={"title":"Timesheet","description":"View and manage timesheet"};
	$scope.timeType="duration";
	$scope.timesheetCalendarTitle=undefined;
	var editWorklogModal = $modal({scope: $scope, template: _contextPath+'/views/app/templates/edit-worklog.tpl.html', show: false});
	$scope.showEditWorkLogModal = function() {
		editWorklogModal.$promise.then(editWorklogModal.show);
	};
	
	$scope.resetWorklogForm=function(){
		$scope.worklog={};
		$scope.worklog.durationTime=0;
		$scope.worklog.timeType="DURATION";
		$scope.worklog.description=undefined;
		$scope.project=undefined;
		$scope.task=undefined;
	};
	
	$scope.resetWorklogForm();
	
	var fetchProjects = function(queryParams) {
		return $http.get(_contextPath + "/app/api/" + _userCompanyId + "/project/employe/"+ _userId, {
					params : {}
				}).then(function(response) {
					$scope.projects=response.data.result;
				});
	}; 

	fetchProjects();
	
	$scope.selectProject=function(project){
		$scope.project=project;
		fetchTasks();
	};
	$scope.selectTask=function(task){
		$scope.task=task;
	};
	
	$scope.selectTask=function(task){
		$scope.task=task;
	};
	
	var fetchTasks = function(queryParams) {
		return $http.get(_contextPath + "/app/api/" + _userCompanyId + "/task/"+$scope.project.id+"/"+ _userId, {
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
					});
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
			displayed: false
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
       console.log('Event Droped to make dayDelta ' + dayDelta);
    };
    /* alert on Resize */
    $scope.onEventResize = function(event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
       $scope.alertMessage = ('Event Resized to make dayDelta ' + minuteDelta);
    };
    
    $scope.onSelection=function( start, end, jsEvent, view ){
    	$scope.resetWorklogForm();
    	$scope.worklog.timeStartDate=start;
    	$scope.worklog.timeEndDate=end;
    	$scope.worklog.timeType="TIME";
    	$scope.showEditWorkLogModal();
    	$scope.alertMessage="Start "+start+" End "+end;
    };
    
    $scope.onViewRender=function(view, element){
    	$scope.currentView=view;
    	$scope.timesheetCalendarTitle=$sce.trustAsHtml('<strong>'+view.title+'</strong>');
    };
    
    $scope.eventRender=function(event, element,view) {
    	popover=$popover(element, {title: event.title,placement:'top',trigger:'click',html:true,template: _contextPath+'/views/app/templates/worklog.popover.tpl.html',container:'body' });
    	popover.$scope.event = event;
    	popover.$scope.isValidated=$scope.isValidated;
    	popover.$scope.isWaiting=$scope.isWaiting;
    	popover.$scope.isRejected=$scope.isRejected;
    };
    
    $scope.eventSource = {
            url: _contextPath+"/app/api/worklog/calendar",
            type : 'GET'
    };
    $scope.eventSources = [$scope.eventSource];
    
	/* config object */
    $scope.uiConfig = {
      calendar:{
        height: 450,
        editable: true,
        selectable:true,
        firstDay:1,
        minTime:"06:00:00",
        maxTime:"22:00:00",
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
        select:$scope.onSelection,
        eventRender: $scope.eventRender
      }
    };
    
    
    /* Change View */
    $scope.changeView = function(view,calendar) {
    	$scope.showTable(false);
    	calendar.fullCalendar('changeView',view);
    };
    $scope.showTable=function(visible){
    	$scope.tableVisible=visible;
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
    
//    $scope.today( $scope.uiConfig.calendar);
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
    	var worklog={};
    	 worklog.title=null;
    	 worklog.type=$scope.worklog.timeType;
    	 if("TIME"===$scope.worklog.timeType){
    		 worklog.start=$scope.worklog.timeStartDate;
    		 worklog.end=$scope.worklog.timeEndDate;
    		 worklog.duration=null;
    	 }else {
    		 worklog.start=$scope.worklog.durationStartDate;
    		 worklog.end=null;
    		 worklog.duration=$scope.worklog.durationTime;
		}
    	
    	 worklog.taskId= $scope.task.id;
    	 worklog.taskName=$scope.task.name;
    	 worklog.description=$scope.worklog.description;
    	 worklog.employeId=_userId;
    	 
    	 WorkLogREST.save(worklog).$promise.then(function(result) {
    		 $scope.currentView.calendar.refetchEvents();
    		 hideFn();
    		 alertService.show('success','Confirmation', 'Data saved');
    		 $scope.resetWorklogForm();
    		 $scope.tableParams.reload();
		});
    	
    };
    
    
    $scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			id : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			
			WorkLogREST.get(
					{
						page:params.$params.page-1,
						size:params.$params.count,
						sort:params.$params.sorting,
						filter:$scope.tableFilter
					},function(data) {
				params.total(data.totalCount);
				$scope.startIndex=data.startIndex;
				$scope.endIndex=data.endIndex;
				if(data.totalCount>=1){
					$scope.hasDatas=true;
				}else {
					$scope.hasDatas=false;
				}
				// set new data
				$defer.resolve(data.result);
			});
		}});
}