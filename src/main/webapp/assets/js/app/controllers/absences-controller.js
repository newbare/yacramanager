'use strict';

function AbsencesController($scope, $rootScope, AbsenceCRUDREST,
		AbsenceTypeREST, alertService,ngTableParams,notifService) {
	$rootScope.page = {
		"title" : "Absences",
		"description" : "Dï¿½clarer vos absences"
	};
	$scope.initialActionLabel = "Ajouter une absence";
	$scope.dateFormat = "dd MMMM yyyy";
	$scope.hasDatas=false;
	AbsenceTypeREST.query(function(data) {
		$scope.absencesType = data;
	});

	var absence = $scope.currentAbsence = {};
	var today = new Date();

	$scope.reset = function() {
		$scope.initialSelectionChanged = false;
		$scope.selectedActionLabel = $scope.initialActionLabel;
		absence.type = $scope.selectedActionName;
		absence.startDate = today;
		absence.endDate = today;
		absence.description = '';
		absence.startAfternoon = false;
		absence.endMorning = false;
	};
	$scope.changeActionSelection = function(data) {
		$scope.reset();
		$scope.selectedActionLabel = data.label;
		$scope.selectedActionName = data.name;
		absence.type = data.name;
		$scope.initialSelectionChanged = true;
	};

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

	$scope.postAbsence = function() {
		AbsenceCRUDREST.save(clone(absence)).$promise.then(function(result) {
			//alertService.showInfo('Confirmation', 'Donnï¿½ sauvegardï¿½');
			notifService.notify('info','Created','Nouvelle absence enregistré')
			$scope.reset();
			$scope.tableParams.reload();
		});
	};
	$scope.deleteAbsence = function(id) {
		AbsenceCRUDREST.remove({
			id : id
		}).$promise.then(function(result) {
			$scope.tableParams.reload();
			notifService.notify('info','Confirmation', 'Absence supprimÃ©');
		}, function(error) {
			// console.log(error);
			notifService.notify('error','' + error.status, error.data);
		});

	};
	$scope.reset();
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			name : 'asc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			
			AbsenceCRUDREST.get(
					{
						page:params.$params.page-1,
						size:params.$params.count
					},function(data) {
				params.total(data.totalCount);
				if(data.totalCount>=1){
					$scope.hasDatas=true;
				}else {
					$scope.hasDatas=false;
					
				}
				
				// set new data
				$defer.resolve(data.result);
				/*data.$promise.then(function(data){
					$scope.absences=data;
				});*/
			});
		}
	});

};
