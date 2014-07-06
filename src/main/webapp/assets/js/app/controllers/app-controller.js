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
		['$scope', '$location','UsersREST', function ($scope, $location,UsersREST) {
			$scope.navClass = function (page) {
		    var currentRoute = $location.path().substring(1) || 'home';
		    return page === currentRoute ? 'active' : '';
		  };
		  console.log('create AppCtrl');
		  var loadUserInfo = function() {
				UsersREST.get({
					service : 'user-info'
				}, function(data) {
					$scope.userInfo = data;
					$scope.$broadcast('userInfo',data);
				});
			};
			loadUserInfo();
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
				templateUrl : 'views/app/home.html',
				controller : HomeController
			})
			.when('/cra', {
				templateUrl : 'views/app/cra.html',
				controller : CraController
			})
			.when('/absences', {
				templateUrl : 'views/app/absences.html',
				controller : AbsencesController
			})
			.when('/user-settings', {
				templateUrl : 'views/app/user-settings.html',
				controller : UserSettingsController
			}).when('/user-profile', {
				templateUrl : 'views/app/user-profile.html',
				controller : UserProfileController
			}).otherwise({
				redirectTo : '/'
			});
		} ]);
