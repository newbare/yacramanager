App.config(function ($stateProvider) {
	$stateProvider.state('company.settings', {
		url : "/settings",
		templateUrl : _contextPath+'views/app/company/company-settings.html',
		controller : 'CompanySettingsController'
	});
});