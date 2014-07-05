App.controller('LoginController', 
		['$scope', '$location', function ($scope, $location) {
			$scope.navClass = function (page) {
		    var currentRoute = $location.path().substring(1) || 'home';
		    return page === currentRoute ? 'active' : '';
		  };
		  $scope.loadLogin = function () {
		        $location.url('/home');
		    };
		    
		      $scope.loadRegister = function () {
		        $location.url('/register');
		    };
		    
		      $scope.loadForgotAccount= function () {
		        $location.url('/forgot-account');
		    };
		    
		}]);


App.config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider
			.when('/', {
				templateUrl : 'views/login.html',
				controller : LoginController
			})
//			.when('/register', {
//				templateUrl : 'views/register.html',
//				controller : CraController
//			})
//			.when('/forgot-account', {
//				templateUrl : 'views/forgot-account.html',
//				controller : AbsencesController
//			})
			.otherwise({
				redirectTo : '/'
			});
		} ]);