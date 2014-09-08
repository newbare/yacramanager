App.factory("AuthenticationREST", function($resource) {
	return $resource(_contextPath + "/app/api/company/:id", {}, {
		activateAccount : {
			url : _contextPath + "/auth/api/activate:key",
			method : 'GET',
			params : {
				key : '@key'
			}
		},
		recoverPassword : {
			url : _contextPath + "auth/api//password-recovery:email",
			method : 'POST'
			
		}
	});
});

App.factory("AbsenceREST", function($resource) {
	return $resource(_contextPath + "/app/api/absences/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		},
		getTypes : {
			url : _contextPath + "/app/api/absences/types",
			method : 'GET',
			isArray : true

		}
	});
});

App.factory("CompanyREST", function($resource) {
	return $resource(_contextPath + "/app/api/company/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
});

App.factory('LogsService', function($resource) {
	return $resource(_contextPath + "/app/admin/logs", {}, {
		'findAll' : {
			method : 'GET',
			isArray : true
		},
		'changeLevel' : {
			method : 'PUT'
		}
	});
});

App.factory('MetricsService', function($resource) {
	return $resource(_contextPath + "/app/admin/metrics", {}, {
		'get' : {
			method : 'GET'
		}
	});
});

// jhipsterApp.factory('ThreadDumpService', function ($http) {
// return {
// dump: function() {
// var promise = $http.get('dump').then(function(response){
// return response.data;
// });
// return promise;
// }
// };
// });
//
// jhipsterApp.factory('HealthCheckService', function ($rootScope, $http) {
// return {
// check: function() {
// var promise = $http.get('health').then(function(response){
// return response.data;
// });
// return promise;
// }
// };
// });

App.factory("EmployeesREST", function($resource) {
	return $resource(_contextPath + "/app/api/users/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
});

App.factory("ClientsREST", function($resource) {
	return $resource(_contextPath + "/app/api/:companyId/client/:id", {
		companyId : '@companyId'
	}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
});

App.factory("ProjectsREST", function($resource) {
	return $resource(_contextPath + "/app/api/:companyId/project/:id", {
		companyId : '@companyId'
	}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
});

App.factory("TasksREST", function($resource) {
	return $resource(_contextPath + "/app/api/:companyId/task/:id", {
		companyId : '@companyId'
	}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
});

App.factory("CompanySettingsREST", function($resource) {
	return $resource(_contextPath + "/app/api/settings/company/"
			+ _userCompanyId + "/:id", {}, {
		update : {
			method : 'PUT'
		}
	});
});

App.factory("CraREST", function($resource) {
	return $resource(_contextPath + "/app/api/cra");
});

App.factory("NoteREST", function($resource) {
	return $resource(_contextPath + "/app/api/frais/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
});

App.factory("UsersREST", function($resource) {
	return $resource(_contextPath + "/app/api/users/:service",{},{
		changePassword:{
			url:_contextPath + "/app/api/users/change-password",
			method : 'POST',
		}
	});
});

App.factory("UserSettingsREST", function($resource) {
	return $resource(_contextPath + "/app/api/settings/user/" + _userId
			+ "/:id", {}, {
		update : {
			method : 'PUT'
		}
	});
});

App.factory("WorkLogREST", function($resource) {
	return $resource(_contextPath + "/app/api/worklog/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
});