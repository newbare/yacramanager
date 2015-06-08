App.config(function ($stateProvider) {
	$stateProvider.state('company.settings', {
		url : "/settings",
		templateUrl : _contextPath+'scripts/app/company/settings/company-settings.html',
		controller : 'CompanySettingsController',
		data : {
			ncyBreadcrumbLabel : 'Settings',
			pageDesc: 'Company settings'
		}
	});
});