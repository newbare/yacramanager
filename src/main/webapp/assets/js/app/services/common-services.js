/**
 * 
 */
App.service('alertService', function($alert,$templateCache, $timeout) {
	this.showInfo = function(title, content) {
		// Service usage
		var myAlert = $alert({
			title : title,
			content : content,
			placement : 'top',
			type : 'info',
			keyboard : true,
			show : false,
			duration: 3,
			template: 'assets/others/alert/alert.tpl.html',
			container: 'body'
		});
		myAlert.$promise.then(myAlert.show);
	};
	this.showError = function(title, content) {
		// Service usage
		var myAlert = $alert({
			title : title,
			content : content,
			placement : 'top',
			type : 'danger',
			keyboard : true,
			show : false,
			duration: 3,
			template: 'assets/others/alert/alert.tpl.html',
			container: 'body'
		});
		myAlert.$promise.then(myAlert.show);
	};
});