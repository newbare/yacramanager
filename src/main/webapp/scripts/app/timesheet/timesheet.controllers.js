App.controller('TimeSheetController',function ($scope,$rootScope,$http,$sce,WorkLogREST,alertService,$popover,$compile,$modal,ngTableParams,USERINFO) {
	$rootScope.page={"title":"Timesheet","description":"View and manage timesheet"};
	$scope.timeType="duration";
	var worklogDateFormat="YYYY-MM-DDTHH:mm:ss.SSS";
	$scope.timesheetCalendarTitle=undefined;
	var editWorklogModal = $modal({scope: $scope, template: _contextPath+'scripts/templates/edit-worklog.tpl.html', show: false});
	$scope.showEditWorkLogModal = function() {
		editWorklogModal.$promise.then(editWorklogModal.show);
	};
	
	$scope.resetWorklogForm=function(){
		$scope.worklog={};
		$scope.worklog.durationTime=0;
		$scope.worklog.timeType="DURATION";
		$scope.worklog.description=undefined;
		$scope.projects=null;
		fetchProjects();
		$scope.project=null;
		$scope.tasks=null;
		$scope.task=null;
	};
	
	
	
	var fetchProjects = function(queryParams) {
		return $http.get(_contextPath + "app/api/" + USERINFO.company.id + "/project/employe/"+ USERINFO.id, {
					params : {}
				}).then(function(response) {
					$scope.projects=response.data.result;
				});
	}; 

	fetchProjects();
	
	$scope.resetWorklogForm();
	
	$scope.selectProject=function(project){
		$scope.project=project;
		if(project!=null){
			fetchTasks();
		}else {
			$scope.tasks=null;
		}
		
	};
	$scope.selectTask=function(task){
		$scope.task=task;
	};
	
	
	var fetchTasks = function(queryParams) {
		return $http.get(_contextPath + "app/api/" + USERINFO.company.id + "/task/"+$scope.project.id+"/"+ USERINFO.id, {
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
				if(data.name==""+USERINFO.id+""){
					return '<i class="fa fa-user"></i> Me';
				}else {
					return '<i class="fa fa-user"></i> '+getUserInitials(data.label);
				}
			},
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+USERINFO.id+""){
						items.push(item);
					}
				});
				return items;
			},
			getData:function($defer){
				$http.get(_contextPath+"app/api/users/managed/"+USERINFO.id,{params:{"me":true} })
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
       WorkLogREST.update(eventToWorkLog(event)).$promise.then(
		        //success
		        function( value ){},
		        //error
		        function( error ){/*Do something with error*/}
		      );
    };
    /* alert on Resize */
    $scope.onEventResize = function(event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
    	WorkLogREST.update(eventToWorkLog(event)).$promise.then(
 		        //success
 		        function( value ){},
 		        //error
 		        function( error ){/*Do something with error*/}
 		      );
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
    	popover=$popover(element, {title: event.title,placement:'top',trigger:'click',html:true,template: _contextPath+'scripts/templates/worklog.popover.tpl.html',container:'body' });
    	popover.$scope.event = event;
    	popover.$scope.isValidated=$scope.isValidated;
    	popover.$scope.isWaiting=$scope.isWaiting;
    	popover.$scope.isRejected=$scope.isRejected;
    	popover.$scope.deleteEvent=function(event){
    		WorkLogREST.remove(event);
    	}
    };
    
    $scope.eventSource = {
            url: _contextPath+"app/api/worklog/calendar",
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
    	$scope[calendar].fullCalendar('changeView',view);
    };
    $scope.showTable=function(visible){
    	$scope.tableVisible=visible;
    };
    
    $scope.next = function(calendar) {
    	$scope[calendar].fullCalendar('next');
    };
    $scope.previous = function(calendar) {
    	$scope[calendar].fullCalendar('prev');
    };
    
    $scope.today = function(calendar) {
    	$scope[calendar].fullCalendar('today');
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
	var eventToWorkLog = function(event) {
		var worklog = {};
		worklog.title = event.title;
		worklog.id=event.id;
		worklog.type = event.type;
		if ("TIME" === event.type) {
			worklog.start = moment(event.start).format(worklogDateFormat);
			worklog.end = moment(event.end).format(worklogDateFormat);
			worklog.duration = null;
		} else {
			worklog.start = moment(event.start).format(worklogDateFormat);
			worklog.end = null;
			worklog.duration = event.duration;
		}

		worklog.taskId = event.taskId;
		worklog.taskName = event.taskName;
		worklog.description = event.description;
		worklog.validationStatus=event.validationStatus;
		worklog.employeId = USERINFO.id;
		return worklog;
	}
    $scope.postWorkLog = function(hideFn) {
    	var worklog={};
    	 worklog.title=null;
    	 worklog.type=$scope.worklog.timeType;
    	 if("TIME"===$scope.worklog.timeType){
    		 worklog.start=moment($scope.worklog.timeStartDate).format(worklogDateFormat);
    		 worklog.end=moment($scope.worklog.timeEndDate).format(worklogDateFormat);
    		 worklog.duration=null;
    	 }else {
    		 worklog.start=moment($scope.worklog.durationStartDate).format(worklogDateFormat);
    		 worklog.end=null;
    		 worklog.duration=$scope.worklog.durationTime;
		}
    	
    	 worklog.taskId= $scope.task.id;
    	 worklog.taskName=$scope.task.name;
    	 worklog.description=$scope.worklog.description;
    	 worklog.employeId=USERINFO.id;
    	 
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
});