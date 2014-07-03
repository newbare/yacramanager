'use strict';

function DeclarationAbsencesController($scope,$rootScope,AbsenceCRUDREST,AbsenceTypeREST,alertService) {
	$rootScope.page={"title":"Absences","description":"Déclarer vos absences"};
	$scope.initialActionLabel="Ajouter une absence";
	$scope.dateFormat="dd MMMM yyyy";
	AbsenceTypeREST.query(function(data){
		$scope.absencesType=data;
	});
	
	var absence=$scope.currentAbsence={};
	var today=new Date();
	
	$scope.fetchTableDatas=function(){
		AbsenceCRUDREST.query(function(data) {
			$scope.absences = data;
		  });
	};
	
	$scope.reset=function(){
		$scope.initialSelectionChanged=false;
		$scope.selectedActionLabel=$scope.initialActionLabel
		absence.type=$scope.selectedActionName;
		absence.startDate=today;
		absence.endDate=today;
		absence.description='';
		absence.startAfternoon=false;
		absence.endMorning=false;
		$scope.fetchTableDatas();
	};
	$scope.changeActionSelection=function(data){
		$scope.reset();
		$scope.selectedActionLabel=data.label;
		$scope.selectedActionName=data.name;
		absence.type=data.name;
		$scope.initialSelectionChanged=true;
	}
	
	function clone(obj) {
	    if (null == obj || "object" != typeof obj) return obj;
	    var copy = obj.constructor();
	    for (var attr in obj) {
	        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
	    }
	    return copy;
	}
	
	$scope.postAbsence=function(){
		AbsenceCRUDREST.save(clone(absence));
		alertService.showInfo('Confirmation','Donné sauvegardé');
		$scope.reset();
	}
	$scope.reset();
	
	
	
}
