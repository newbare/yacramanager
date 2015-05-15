App.factory("AppConfigREST", function($resource) {
	return $resource(_contextPath + "conf/env/", {}, {
		getConf: {
			url : _contextPath + "conf/env/"
		}
	});
});
App.factory("AuthenticationREST", function($resource) {
	return $resource(_contextPath + "app/api/company/:id", {}, {
		activateAccount : {
			url : _contextPath + "auth/api/activate:key",
			method : 'GET',
			params : {
				key : '@key'
			}
		},
		recoverPassword : {
			url : _contextPath + "auth/api/password-recovery:email",
			method : 'POST'
			
		}
	});
});

App.factory("AbsenceREST", function($resource) {
	return $resource(_contextPath + "app/api/absences/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		},
		getTypes : {
			url : _contextPath + "app/api/absences/types",
			method : 'GET',
			isArray : true

		},
		getApprovals : {
			url : _contextPath + "app/api/absences/approval",
			method : 'GET',
			isArray : false

		},
		getPortfolio : {
			url : _contextPath + "app/api/absences/portfolio",
			method : 'GET',
			isArray : false

		}
	});
});

App.factory('LanguageService', function ($q,$translate, LANGUAGES) {
    return {
        getCurrent: function () {
            var deferred = $q.defer();
            var language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');

            if (angular.isUndefined(language)) {
                language = 'en';
            }

            deferred.resolve(language);
            return deferred.promise;
        },
        getAll: function () {
            var deferred = $q.defer();
            deferred.resolve(LANGUAGES);
            return deferred.promise;
        }
    };
});

App.factory("CompanyREST", function($resource) {
	return $resource(_contextPath + "app/api/company/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		},
		inviteEmployee : {
			url : _contextPath + "app/api/company/:id/invite",
			method : 'POST'			
		}
	});
});

App.factory('LogsService', function($resource) {
	return $resource(_contextPath + "app/admin/logs", {}, {
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
	return $resource(_contextPath + "app/admin/metrics", {}, {
		'get' : {
			method : 'GET'
		}
	});
});

App.factory('HealthCheckService', function($rootScope, $http) {
	return {
		check : function() {
			var promise = $http.get(_contextPath + 'app/admin/management/health').then(function(response) {
				return response.data;
			});
			return promise;
		}
	};
});

App.factory("EmployeesREST", function($resource) {
	return $resource(_contextPath + "app/api/users/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		},
		updateManager:{
			url:_contextPath + "app/api/users/update-manager/:employeeId",
			method : 'POST',
		},
		updateUserRights:{
			url:_contextPath + "app/api/users/update-rights/:employeeId",
			method : 'POST',
		}
	});
});

App.factory("ClientsREST", function($resource) {
	return $resource(_contextPath + "app/api/:companyId/client/:id", {
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
	return $resource(_contextPath + "app/api/:companyId/project/:id", {
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
	return $resource(_contextPath + "app/api/:companyId/task/:id", {
		companyId : '@companyId'
	}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		},
		getAll:{
			url : _contextPath + "app/api/:companyId/task/:employeId/all",
			method : 'GET',
			isArray : true
		},
		getAssignedEmployee:{
			url : _contextPath + "app/api/:companyId/task/:taskId/assigned/:employeId",
			method : 'GET',
			isArray : false
		},
		assignEmployeeToTask:{
			url : _contextPath + "app/api/:companyId/task/:taskId/assign-employees",
			method : 'POST'
		},
		unAssignEmployeeToTask:{
			url : _contextPath + "app/api/:companyId/task/:taskId/unassign-employees",
			method : 'POST'
		}
	});
});

App.factory("CompanySettingsREST", function($resource,USERINFO) {
	return $resource(_contextPath + "app/api/settings/company/"+ USERINFO.company.id + "/:id", {}, {
		update : {
			method : 'PUT'
		}
	});
});

App.factory("ActivityReportREST", function($resource) {
	return $resource(_contextPath + "app/api/activity-report",{},{
		getDetails : {
			url : _contextPath + "app/api/activity-report/details",
			method : 'GET',
			isArray : false

		},
		submit : {
			url : _contextPath + "app/api/activity-report/submit",
			method : 'POST'
		},
		approve : {
			url : _contextPath + "app/api/activity-report/approve",
			method : 'POST'
		},
		reject : {
			url : _contextPath + "app/api/activity-report/reject",
			method : 'POST'
		},
		cancel : {
			url : _contextPath + "app/api/activity-report/cancel",
			method : 'POST'
		},
		getApprovals : {
			url : _contextPath + "app/api/activity-report/approval/:id",
			method : 'GET',
			isArray : true

		}
	});
});

App.factory("NoteREST", function($resource) {
	return $resource(_contextPath + "app/api/frais/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
});

App.factory("UsersREST", function($resource) {
	return $resource(_contextPath + "app/api/users/:service",{},{
		changePassword:{
			url:_contextPath + "app/api/users/change-password",
			method : 'POST',
		}
	});
});

App.factory("UserSettingsREST", function($resource,USERINFO) {
	return $resource(_contextPath + "app/api/settings/user/" + USERINFO.id	+ "/:id", {}, {
		update : {
			method : 'PUT'
		}
	});
});

App.factory("WorkLogREST", function($resource) {
	return $resource(_contextPath + "app/api/worklog/:id", {}, {
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
});

App.factory("ActivitiesREST", function($resource) {
	return $resource(_contextPath + "app/api/:companyId/task/:id", {
		companyId : '@companyId'
	}, {
		forUser:{
			url : _contextPath + "app/api/activities/user/:id",
			method : 'GET',
			isArray : false
		},
		forCompany:{
			url : _contextPath + "app/api/activities/company/:id",
			method : 'GET',
			isArray : false
		},
		forProject:{
			url : _contextPath + "app/api/activities/project/:id",
			method : 'GET',
			isArray : false
		},
		forClient:{
			url : _contextPath + "app/api/activities/client/:id",
			method : 'GET',
			isArray : false
		}
	});
});

App.factory('NgStomp', function($rootScope) {
    var stompClient = {};

    function NGStomp(url) {
    	var socket = new SockJS(url);
        this.stompClient = Stomp.over(socket);
    }

    NGStomp.prototype.subscribe = function(queue, callback) {
        this.stompClient.subscribe(queue, function() {
            var args = arguments;
            $rootScope.$apply(function() {
                callback(JSON.parse(args[0].body));
            })
        })
    }

    NGStomp.prototype.send = function(queue, headers, data) {
        this.stompClient.send(queue, headers, data);
    }

    NGStomp.prototype.connect = function( on_connect, on_error) {
    	this.stompClient.debug=function(){
			//do nothing
		};
    	this.stompClient.connect({},
            function(frame) {
                $rootScope.$apply(function() {
                    on_connect.apply(stompClient, frame);
                })
            },
            function(frame) {
                $rootScope.$apply(function() {
                    on_error.apply(stompClient, frame);
                })
            });
    }

    NGStomp.prototype.disconnect = function(callback) {
        this.stompClient.disconnect(function() {
            var args = arguments;
            $rootScope.$apply(function() {
                callback.apply(args);
            })
        })
    }

    return function(url) {
        return new NGStomp(url);
    }
});