var yaCRAApp = {};

var App = angular.module('yaCRAApp', ['ngResource','mgcrea.ngStrap','ngRoute','ngAnimate','ngTable','ui.router']);

App.run(function($rootScope) {
    $rootScope.page = ''; 
 });

