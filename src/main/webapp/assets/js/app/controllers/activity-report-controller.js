'use strict';

function ActivityReportController($scope,$rootScope,ActivityReportREST,$filter,$http,WorkLogREST,alertService,AbsenceREST) {
	$rootScope.page={"title":"CRA","description":"View and manage you CRA"};
	$scope.dateFormat="dd MMMM yyyy";
	$scope.craDateFormat="EEE dd/MM";
	$scope.numberOfWeek=0;
	$scope.resetNewAbsence=function(){
		$scope.newAbsence={};
	};
	$scope.resetNewAbsence();
	var formatDate=function(date){
		return $filter('date')(date, 'yyyy-MM-dd');
	};
	
	$scope.absencePeriods = [ {
		name : 'ALL',
		label : 'All day'
	}, {
		name : 'MORNING',
		label : 'Only morning'
	}, {
		name : 'AFTERNOON',
		label : 'Only afternoon'
	} ];
	
	$scope.currentFilter=undefined;
	
	$scope.currentView=undefined;
	$scope.currentDate=moment();
	
	$scope.dateRange={startDate:null,endDate: null}
	
	$scope.updateCurrentview=function(view){
		$scope.currentView=view;
		if('week'===$scope.currentView){
			$scope.dateRange={
					startDate:$scope.currentDate.clone().startOf('isoWeek'),
					endDate: $scope.currentDate.clone().endOf('isoWeek')
			};
		}else if ('month'===$scope.currentView) {
			$scope.dateRange={
					startDate:$scope.currentDate.clone().startOf('month'),
					endDate: $scope.currentDate.clone().endOf('month')
			};
		}
		$scope.numberOfWeek=($scope.dateRange.endDate.diff($scope.dateRange.startDate, 'days')+1)/7;
		$scope.$broadcast('craViewChanged', $scope.currentView);
	}
	
	$scope.previous=function(){
		if('week'===$scope.currentView){
			$scope.currentDate=$scope.currentDate.clone().subtract('days',6);
		}else if ('month'===$scope.currentView) {
			$scope.currentDate=$scope.currentDate.clone().subtract('days',30);
		}
		$scope.refreshView();
	};
	
	$scope.next=function(){
		if('week'===$scope.currentView){
			$scope.currentDate=$scope.currentDate.clone().add('days',6);
		}else if ('month'===$scope.currentView) {
			$scope.currentDate=$scope.currentDate.clone().add('days',30);
		}
		$scope.refreshView();
	};
	
	$scope.exportToPDF=function(){
		var doc = new jsPDF('landscape');

		var specialElementHandlers = {
				'.tool-img': function(element, renderer){
					return true;
				}
		};
		// All units are in the set measurement for the document
		// This can be changed to "pt" (points), "mm" (Default), "cm", "in"
		doc.fromHTML($('#activity-report-fragment').get(0), 15, 15, {
			'width': 170,
			'elementHandlers': specialElementHandlers
		});
		doc.save('test-body.pdf');
	};
	
	$scope.refreshView=function(){
		$scope.updateCurrentview($scope.currentView);
	};
	
	$scope.today=function(){
		$scope.currentDate=moment();
		$scope.refreshView();
	};
	$scope.isTodaySelected=function(){
		var range = moment().range($scope.dateRange.startDate, $scope.dateRange.endDate);
		return range.contains(moment());
	}
	
	$scope.isToday=function(date){
		return moment().isSame(date, 'day');;
	}
	$scope.loadAbsenceTypes=function(){
		AbsenceREST.getTypes(function(data) {
			$scope.absencesTypes = data;
		});
	};
	
	$scope.saveTimeOff=function(selectedDate,hideFn){
		console.log('Saving ...'+$scope.newAbsence);
		var toCreate={
				employeId:_userId,
				date: moment(),
				startDate: selectedDate,
				endDate: selectedDate,
				description:$scope.newAbsence.description,
				startAfternoon:$scope.newAbsence.period=='AFTERNOON' ? true:false,
				endMorning:$scope.newAbsence.period=='MORNING' ? true:false,
				typeAbsence:$scope.newAbsence.typeAbsence
		};
		AbsenceREST.save(toCreate).$promise.then(function(result) {
			alertService.show('success','Confirmation', 'Donn� sauvegard�');
			$scope.resetNewAbsence();
			hideFn();
			$scope.retrieveCraDetails($scope.currentFilter);
			$scope.$broadcast('absence-portfolio-changed');
		});
	}
	
	
	$scope.employeCriteriaConfig={
			name:"employe",
			defaultButtonLabel:"<i class='fa fa-user'></i>",
			filterType:"ARRAY",
			closeable:false,
			filterValue:[],
			buttonSelectedItemsFormater:function(data){
				if(data.name==""+_userId+""){
					return ' Me';
				}else {
					return ' '+getUserInitials(data.label);
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
	
	$scope.criteriaBarConfig={
			criterions:[$scope.employeCriteriaConfig],
			autoFilter:true,
			filters:[]
		};
		
		$scope.doFilter=function(data){
			$scope.currentFilter=data;
			var serverFilter={filter:data};
			$scope.tableFilter=JSON.stringify(serverFilter);
			//$scope.retrieveCra();
			$scope.retrieveCraDetails(data);
		};
	
	$scope.retrieveCraDetails=function(filter){
		var userIds=[];
		angular.forEach(filter,function(filterElement){
			if(filterElement.field==='employe'){
				angular.forEach(filterElement.value,function(value){
					userIds.push(value.name);
				});
				
			}
		});
		ActivityReportREST.getDetails({
			employeIds : userIds.join(),
			startDate : $scope.dateRange.startDate.format('YYYY-MM-DD'),
			endDate : $scope.dateRange.endDate.format('YYYY-MM-DD')
		}).$promise.then(function(result) {
			$scope.craDetails=result;
		});

	};
	$scope.formatCraDetailDuration=function(duration){
		return $filter('date')(duration*1000, 'shortTime');
	}
	
	$scope.updateCurrentview('week');
	
	$scope.$on('craViewChanged', function(event, args) {
		$scope.retrieveCraDetails($scope.currentFilter);
		//$scope.retrieveCra();
	});
	
	$scope.isCurrentEmploye=function(employeId){
		return employeId==_userId;
	}
	
	$scope.taskRowTotal=function(taskRow,days){
		var rowTotal=0;
		angular.forEach(days,function(day){
			rowTotal=rowTotal+taskRow.duration[day.date]
		});
		return rowTotal;
	};
	
	$scope.dayColumnTotal=function(employeCraDetail,day){
		var columnTotal=0;
		angular.forEach(employeCraDetail.taskRows,function(taskRow){
			columnTotal+=taskRow.duration[day.date]
		});
		columnTotal+=employeCraDetail.craAbsenceDetail.duration[day.date];
		return columnTotal;
	}
	
	$scope.craTotal=function(employeCraDetail){
		var craTotal=0;
		angular.forEach(employeCraDetail.days,function(day){
			craTotal+=$scope.dayColumnTotal(employeCraDetail,day);
		});
		return craTotal;
	};
	
	$scope.updateCraValue = function(newValue, taskRow, day,days) {
		newValue=parseInt(newValue);
		var diff = newValue - taskRow.duration[day.date];
		if (diff < 0) {
			return 'Cannot reduce worklog';
		}
		var worklog = {};
		worklog.title = null;
		worklog.type = 'DURATION';
		worklog.start = day.date;
		worklog.end = null;
		worklog.duration = diff;
		worklog.taskId = taskRow.task.id;
		worklog.taskName = taskRow.task.name;
		worklog.description = 'Created from activity report view';
		worklog.employeId = _userId;
		return WorkLogREST.save(worklog).$promise.then(function(result) {
			alertService.show('success', 'Confirmation', 'Donn� sauvegard�');
		});
	};
	
	
	$scope.sendForApproval=function(employeId){
		ActivityReportREST.submit({
			employeId : employeId,
			startDate : $scope.dateRange.startDate.format('YYYY-MM-DD'),
			endDate : $scope.dateRange.endDate.format('YYYY-MM-DD')
		},{}).$promise.then(function(result) {
			$scope.retrieveCraDetails($scope.currentFilter);
			alertService.show('success','Activity report','The request has been sent!');
		});
	};
	
	$scope.cancelActivityReport=function(employeId,activityReportId){
		ActivityReportREST.cancel({
			employeId : employeId,
			activityReportId : activityReportId
		},{}).$promise.then(function(result) {
			$scope.retrieveCraDetails($scope.currentFilter);
			alertService.show('success','Activity report has been canceled','The request has been sent!');
		});
	};
	
	$scope.approveActivityReport=function(activityReportId){
		ActivityReportREST.approve({
			employeId : _userId,
			activityReportId : activityReportId
		},{}).$promise.then(function(result) {
			$scope.retrieveCraDetails($scope.currentFilter);
			alertService.show('success','Activity report has been approved','The request has been sent!');
		});
	};
	
}
