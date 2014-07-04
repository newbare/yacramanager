'use strict';

function AbsencesController($scope, $rootScope, AbsenceCRUDREST,
		AbsenceTypeREST, alertService,ngTableParams) {
	$rootScope.page = {
		"title" : "Absences",
		"description" : "D�clarer vos absences"
	};
	$scope.initialActionLabel = "Ajouter une absence";
	$scope.dateFormat = "dd MMMM yyyy";
	AbsenceTypeREST.query(function(data) {
		$scope.absencesType = data;
	});

	var absence = $scope.currentAbsence = {};
	var today = new Date();

	/*$scope.fetchTableDatas = function() {
		AbsenceCRUDREST.query(function(data) {
			$scope.absences = data;
		});
	};*/

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
			alertService.showInfo('Confirmation', 'Donn� sauvegard�');
			$scope.reset();
			$scope.tableParams.reload();
		});
	};
	$scope.deleteAbsence = function(id) {
		AbsenceCRUDREST.remove({
			id : id
		}).$promise.then(function(result) {
			$scope.tableParams.reload();
			alertService.showInfo('Confirmation', 'Absence supprimé');
		}, function(error) {
			// console.log(error);
			alertService.showError('' + error.status, error.data);
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
				// set new data
				$defer.resolve(data.result);
				/*data.$promise.then(function(data){
					$scope.absences=data;
				});*/
			});
			// ajax request to api
			/*Api.get(params.url(), function(data) {
				$timeout(function() {
					// update table params
					params.total(data.total);
					// set new data
					$defer.resolve(data.result);
				}, 500);
			});*/
		}
	});

};
