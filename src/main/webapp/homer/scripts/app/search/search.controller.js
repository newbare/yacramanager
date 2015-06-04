/**
 * 
 */
App.controller('SearchController',function ($rootScope,$scope,$http,searchResult){
	$scope.searchResult=searchResult.data;
	
});