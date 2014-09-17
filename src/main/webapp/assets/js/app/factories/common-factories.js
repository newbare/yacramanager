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

		},
		getApprovals : {
			url : _contextPath + "/app/api/absences/approval",
			method : 'GET',
			isArray : false

		},
		getPortfolio : {
			url : _contextPath + "/app/api/absences/portfolio",
			method : 'GET',
			isArray : false

		}
	});
});

App.factory('LanguageService', function ($http, $translate, LANGUAGES) {
    return {
        getBy: function(language) {
            if (language == undefined) {
                language = $translate.storage().get('NG_TRANSLATE_LANG_KEY') || window.navigator.language;
            }

            var promise =  $http.get( _contextPath+'/assets/i18n/' + language + '.json').then(function(response) {
                return LANGUAGES;
            });
            return promise;
        }
    };
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
			isArray : false
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
App.factory('HealthCheckService', function($rootScope, $http) {
	return {
		check : function() {
			var promise = $http.get(_contextPath + '/app/admin/health').then(function(response) {
				return response.data;
			});
			return promise;
		}
	};
});

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

App.factory("ActivityReportREST", function($resource) {
	return $resource(_contextPath + "/app/api/activity-report",{},{
		getDetails : {
			url : _contextPath + "/app/api/activity-report/details",
			method : 'GET',
			isArray : false

		},
		submit : {
			url : _contextPath + "/app/api/activity-report/submit",
			method : 'POST'
		}
	});
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