'use strict';
$(function() {
	var stompClient = null;
	function connect() {
		var socket = new SockJS('/yacramanager/yacra');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			console.log('Connected: ' + frame);
			var user = frame.headers['user-name'];
			stompClient.subscribe('/topic/yacra', function(msg) {
				$.gritter.add({
					title: 'Hey!!',
					text: msg.body,
					class_name: 'gritter-info gritter-light'
				});
//				showGreeting(JSON.parse(greeting.body).content);
			});
			stompClient.subscribe("/user/queue/errors", function(msg) {
				console.log(msg);
				$.gritter.add({
					title: 'An error occured',
					text: msg.body,
					class_name: 'gritter-error gritter-light'
				});
			  });
		});
	}
	connect();
});
var yaCRAApp = {};

var App = angular.module('yaCRAApp', ['ngResource','mgcrea.ngStrap','ngRoute','ngAnimate','ngTable']);

App.run(function($rootScope) {
    $rootScope.page = ''; 
 });

App.config(function($datepickerProvider) {
  angular.extend($datepickerProvider.defaults, {
    dateFormat: 'dd/MM/yyyy',
    startWeek: 1,
    template: 'assets/others/datepicker/datepicker.tpl.html',
    autoclose : true,
    modelDateFormat: 'dd/MM/yyyy',
    todayHighlight: true
  });
});

App.controller('NavCtrl', 
		['$scope', '$location', function ($scope, $location) {
		 $scope.navClass = function (page) {
		    var currentRoute = $location.path().substring(1) || 'home';
		    return page === currentRoute ? 'active' : '';
		  };
		  $scope.loadHome = function () {
		        $location.url('/home');
		    };
		    
		      $scope.loadCRA = function () {
		        $location.url('/cra');
		    };
		    
		      $scope.loadAbsences= function () {
		        $location.url('/absences');
		    };
		    
		}]);

App.config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider
			.when('/', {
				templateUrl : 'views/home.html',
				controller : HomeController
			})
			.when('/cra', {
				templateUrl : 'views/cra.html',
				controller : CraController
			})
			.when('/absences', {
				templateUrl : 'views/absences.html',
				controller : AbsencesController
			})
			.when('/user-settings', {
				templateUrl : 'views/user-settings.html',
				controller : UserSettingsController
			}).when('/user-profile', {
				templateUrl : 'views/user-profile.html',
				controller : UserProfileController
			}).otherwise({
				redirectTo : '/'
			});
		} ]);
