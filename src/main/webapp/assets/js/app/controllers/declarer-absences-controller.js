'use strict';

function DeclarationAbsencesController($scope,$rootScope,AbsenceCRUDREST,AbsenceTypeREST) {
	$rootScope.page={"title":"Absences","description":"Déclarer vos absences"};
	$scope.selectedActionLabel="Selectionnez le type d'absence";
	AbsenceTypeREST.query(function(data){
		$scope.absencesType=data;
	});
	$scope.changeActionSelection=function(data){
		$scope.selectedActionLabel=data.label;
		$scope.selectedActionName=data.name;
	}
}
