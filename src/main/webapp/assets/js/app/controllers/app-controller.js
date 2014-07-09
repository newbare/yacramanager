App.config(function($datepickerProvider) {
	angular.extend($datepickerProvider.defaults, {
		dateFormat : 'dd/MM/yyyy',
		startWeek : 1,
		template : '../assets/others/datepicker/datepicker.tpl.html',
		autoclose : true,
		modelDateFormat : 'dd/MM/yyyy',
		todayHighlight : true
	});
});

App.config(function($tooltipProvider) {
  angular.extend($tooltipProvider.defaults, {
    animation: 'am-flip-x',
    trigger: 'hover',
    template:'../assets/others/tooltip/tooltip.tpl.html'
  });
})

App.controller('AppCtrl', [ '$scope', '$location', 'UsersREST',
		function($scope, $location, UsersREST) {
			$scope.navClass = function(page) {
				var currentRoute = $location.path().substring(1) || 'home';
				return page === currentRoute ? 'active' : '';
			};
			var loadUserInfo = function() {
				UsersREST.get({
					service : 'user-info'
				}, function(data) {
					$scope.userInfo = data;
					$scope.$broadcast('userInfo', data);
				});
			};
			loadUserInfo();
			

		} ]);

App.config([ '$stateProvider', '$urlRouterProvider',
		function($stateProvider, $urlRouterProvider) {

			// Use $urlRouterProvider to configure any redirects (when) and
			// invalid urls (otherwise).
			$urlRouterProvider
			.when('/entreprise', '/entreprise/home')
			.when('/admin', '/admin/home')

			// The `when` method says if the url is ever the 1st param, then
			// redirect to the 2nd param
			// Here we are just setting up some convenience urls.
			//.when('/c?id', '/contacts/:id').when('/user/:id', '/contacts/:id')

			// If the url is ever invalid, e.g. '/asdf', then redirect to '/'
			// aka the home state
			.otherwise('/');
			$stateProvider.state('home', {
				url : "/",
				templateUrl : '../views/app/home.html',
				controller : HomeController
			}).state('frais', {
				url : "/frais",
				templateUrl : '../views/app/frais.html',
				controller : FraisController
			}).state('cra', {
				url : "/cra",
				templateUrl : '../views/app/cra.html',
				controller : CraController
			}).state('absences', {
				url : "/absences",
				templateUrl : '../views/app/absences.html',
				controller : AbsencesController
			}).state('notifications', {
				url : "/notifications",
				templateUrl : '../views/app/notifications.html',
				controller : NotificationsController
			}).state('user-settings', {
				url : "/user-settings",
				templateUrl : '../views/app/user-settings.html',
				controller : UserSettingsController
			}).state('user-profile', {
				url : "/user-profile",
				templateUrl : '../views/app/user-profile.html',
				controller : UserProfileController
			}).state('entreprise', {
				url : "/entreprise",
				templateUrl : '../views/app/entreprise.html',
				controller : EntrepriseController,
			}).state('entreprise.home', {
				url : "/home",
				templateUrl : '../views/app/entreprise/entreprise-home.html'
				//controller : EntrepriseController,
			}).state('entreprise.salaries', {
				url : "/salaries",
				templateUrl : '../views/app/entreprise/entreprise-salaries.html'
				//controller : EntrepriseController,
			}).state('entreprise.messages', {
				url : "/messages",
				templateUrl : '../views/app/entreprise/entreprise-messages.html'
				//controller : EntrepriseController,
			}).state('entreprise.settings', {
				url : "/settings",
				templateUrl : '../views/app/entreprise/entreprise-settings.html'
				//controller : EntrepriseController,
			}).state('admin', {
				url : "/admin",
				templateUrl : '../views/app/admin.html',
				controller : AdminController
			}).state('admin.home', {
				url : "/home",
				templateUrl : '../views/app/admin/admin-home.html',
				//controller : AdminController
			}).state('admin.messages', {
				url : "/messages",
				templateUrl : '../views/app/admin/admin-messages.html',
				//controller : AdminController
			}).state('admin.settings', {
				url : "/settings",
				templateUrl : '../views/app/admin/admin-settings.html',
				//controller : AdminController
			})

		} ]);

function NotificationsController($scope,$rootScope) {
	$rootScope.page={"title":"Notification","description":"Stay aware..."}
}
