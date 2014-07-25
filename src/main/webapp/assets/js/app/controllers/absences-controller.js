'use strict';

function AbsencesController($scope, $rootScope, AbsenceCRUDREST,
		AbsenceTypeREST, alertService,ngTableParams,notifService,$http) {
	$rootScope.page = {
		"title" : "Absences",
		"description" : "Declarez vos absences"
	};
	/*
	 * criteria config
	 */
	
	$scope.tableFilter="";
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
	$scope.typeCriteriaConfig={
			name:"type",
			defaultButtonLabel:"Type",
			filterType:"ARRAY",
			closeable:true,
			filterValue:
				[{name:"RTT",label:"RTT",ticked:false},{name:"CP",label:"Conge paye",ticked:false}]
				,
			onFilter: function(value) {
				console.log('Filter checkbox ['+value.field+'] selected items '+value.value.length);
			},
			currentFilter:{},
			displayed: true
	};
	$scope.booleanCriteriaConfig={
			name:"validated",
			defaultButtonLabel:"Validated",
			filterType:"BOOLEAN",
			closeable:true,
			onFilter: function(value) {
				console.log('Filter boolean ['+value.field+']='+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	$scope.descriptionCriteriaConfig={
			name:"description",
			defaultButtonLabel:"Description",
			filterType:"TEXT",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.dateCriteriaConfig={
			name:"date",
			defaultButtonLabel:"Date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.criteriaBarConfig={
		criterions:[$scope.employeCriteriaConfig,$scope.typeCriteriaConfig,$scope.descriptionCriteriaConfig,$scope.dateCriteriaConfig,$scope.booleanCriteriaConfig],
		autoFilter:true,
		filters:[]
	};
	
	$scope.doFilter=function(data){
		console.log("Server filer launch with: "+JSON.stringify(data));
		var serverFilter={filter:data};
		$scope.tableFilter=JSON.stringify(serverFilter);
		$scope.refreshDatas();
	};
	
	$scope.initialActionLabel = "Ajouter une absence";
	$scope.dateFormat = "dd MMMM yyyy";
	$scope.hasDatas=false;
	$scope.selectedAbsences=[];
	$scope.ids=[];
	var allAbsence=[];
	$scope.selectedAction={};
	
	AbsenceTypeREST.query(function(data) {
		$scope.absencesType = data;
	});
	var absence = $scope.currentAbsence = {};
	var today = new Date();
	$scope.selectAbsence=function(absence){
		var index = $scope.selectedAbsences.indexOf(absence);
		if(index==-1){
			$scope.selectedAbsences.push(absence);
		}
	};
	
	$scope.deSelectAbsence=function(absence){
		var index = $scope.selectedAbsences.indexOf(absence);
		if(index!=-1){
			$scope.selectedAbsences.splice(index, 1);
		};
		
	};
	
	$scope.reset = function() {
		$scope.initialSelectionChanged = false;
		$scope.selectedActionLabel = $scope.initialActionLabel;
		absence.id=undefined;
		absence.typeAbsence = $scope.selectedActionName;
		absence.startDate = today;
		absence.endDate = today;
		absence.description = '';
		absence.startAfternoon = false;
		absence.endMorning = false;
		$scope.edition=false;
	};
	$scope.changeActionSelection = function() {
		absence.typeAbsence = $scope.selectedAction;
		$scope.initialSelectionChanged = true;
	};

	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	}
	
	function clone(obj) {
		if (null == obj || "object" != typeof obj)
			return obj;
		var copy = obj.constructor();
		for ( var attr in obj) {
			if (obj.hasOwnProperty(attr))
				copy[attr] = obj[attr];
		}
		return copy;
	}

	$scope.postAbsence = function(hideFn) {
		absence.typeAbsence=absence.typeAbsence.name;
		AbsenceCRUDREST.save(clone(absence)).$promise.then(function(result) {
			alertService.showInfo('Confirmation', 'Donn� sauvegard�');
			notifService.notify('info','Created','Nouvelle absence enregistr�');
			$scope.reset();
			$scope.tableParams.reload();
			hideFn();
		});
	};
	$scope.putAbsence = function() {
		AbsenceCRUDREST.update(clone(absence)).$promise.then(function(result) {
			notifService.notify('info','Created','Nouvelle absence enregistr�');
			$scope.reset();
			$scope.tableParams.reload();
			hideFn();
		});
	};
	$scope.editAbsence=function(id){
		$scope.edition=true;
		AbsenceCRUDREST.get(
				{id:id},function(data) {
					absence.id=data.id;
					absence.typeAbsence = data.typeAbsence;
					absence.startDate = data.startDate;
					absence.endDate = data.endDate;
					absence.description = data.description;
					absence.startAfternoon = data.startAfternoon;
					absence.endMorning = data.endMorning;
				},function(error){
					console.log(error);
					$scope.reset();
				});
	};
	
	$scope.deleteAbsence = function(id) {
		AbsenceCRUDREST.remove({
			id : id
		}).$promise.then(function(result) {
			$scope.tableParams.reload();
			notifService.notify('info','Confirmation', 'Absence supprimé');
		}, function(error) {
			console.log(error);
			notifService.notify('error','' + error.status, error.data);
		});

	};
	$scope.reset();
	$scope.startIndex=0;
	$scope.endIndex=0;
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			date : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			
			AbsenceCRUDREST.get(
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
				allAbsence=data.result;
				// set new data
				$defer.resolve(data.result);
			});
		}});
	
	$scope.checkboxes = { 'checked': false, items: {} };

	$scope.getAbsenceForId=function(array,id){
		var findOne={};
		angular.forEach(array, function(item) {
			if(id==item.id){
				findOne= item;
			};
		});
		return findOne;
	};
    // watch for check all checkbox
    $scope.$watch('checkboxes.checked', function(value) {
        angular.forEach(allAbsence, function(item) {
            if (angular.isDefined(item.id)) {
                $scope.checkboxes.items[item.id] = value;
            }
        });
    });

    // watch for data checkboxes
    $scope.$watch('checkboxes.items', function(values) {
        if (!allAbsence) {
            return;
        }
        var checked = 0, unchecked = 0,
            total = allAbsence.length;
        angular.forEach(allAbsence, function(item) {
            checked   +=  ($scope.checkboxes.items[item.id]) || 0;
            unchecked += (!$scope.checkboxes.items[item.id]) || 0;
            if($scope.checkboxes.items[item.id]){
            	$scope.selectAbsence($scope.getAbsenceForId(allAbsence,item.id));
            }else {
            	$scope.deSelectAbsence($scope.getAbsenceForId(allAbsence,item.id));
			}
        });
        if ((unchecked == 0) || (checked == 0)) {
            $scope.checkboxes.checked = (checked == total);
        }
        if(total==0){
        	$scope.checkboxes.checked =false;
        }
        // grayed checkbox
        angular.element(document.getElementById("select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));
    }, true);
	
	};
