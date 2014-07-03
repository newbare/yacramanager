'use strict';

function ConsultationAbsencesController($scope,$rootScope,AbsenceCRUDREST,AbsenceTypeREST) {
	$rootScope.page={"title":"Absences","description":"Consulter les absences"};
	$scope.selectedActionLabel="Sélectionnez le type d'absence";
	AbsenceTypeREST.query(function(data){
		$scope.absencesType=data;
	});
	$scope.changeActionSelection=function(data){
		$scope.selectedActionLabel=data.label;
		$scope.selectedActionName=data.name;
	};
}
