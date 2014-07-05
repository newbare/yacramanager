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

App.controller('AppCtrl', 
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
