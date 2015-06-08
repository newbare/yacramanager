/**
 * 
 */
App.controller('SearchController',function ($rootScope,$scope,$http,searchResult){
	$rootScope.globalSearchText="";
	$scope.searchResult=searchResult.data;
	
});