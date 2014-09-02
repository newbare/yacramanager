App.directive('webSocket', [ '$timeout', 'WebSocketService', 'notifService',
		function(timer, WebSocketService, notifService) {
			return {
				restrict : 'AEC',
				link : function(scope, elem, attrs, ctrl) {
					scope.$on('application-loaded', function() {
						WebSocketService.connect();
					})
					
//					timer(function() {
//						WebSocketService.connect();
//						//notifService.notify('info', 'WebSocket', 'Connected');
//					}, 0);
				}
			};
		} ]);

App.directive('updateTitle', function($rootScope) {
	  return {
	    link: function(scope, element) {

	      var listener = function(event, toState, toParams, fromState, fromParams) {
	        var title = 'YACRA Manager';
	        if (toState.data && toState.data.pageTitle) title = title+ ' | '+toState.data.pageTitle;
	        element.text(title)
	      };

	      $rootScope.$on('$stateChangeStart', listener);
	    }
	  }
	});
App.directive('httpRequestError', [ '$rootScope', 'alertService', 'notifService',
                     		function($rootScope, alertService, notifService) {
                     			return {
                     				restrict : 'AEC',
                     				link : function(scope, elem, attrs, ctrl) {
                     					scope.$on('event:http-request-error', function(event, error) {
                     						//notifService.notify('error',error.title+': <strong>'+error.status+'</strong>',error.data);
                     						alertService.show('danger',error.title+': '+error.status+')',error.data);
                     					});
                     				}
                     			};
} ]);

App.directive('fileInput',['$compile',function($compile) {
                     			return {
                     				restrict : 'AE',
                     				scope: true,
                     				link : function($scope, elem, attrs) {
                     					elem.bootstrapFileInput();
                     					elem.bind("change", function (changeEvent) {
                     						$scope.$apply(function () {
                     							$scope.onFileSelect(changeEvent.target.files[0]);
                     		                });
                     		            });
                     				}
                     			}
                     		}]);
App.directive('colResizable',function($compile) {
		return {
			restrict : 'A',
			link : function($scope, elem, attrs) {
				elem.colResizable();
			}
		}
	});

App.directive('organigram',function($compile) {
	return {
		restrict : 'A',
		link : function($scope, elem, attrs) {
			elem.jOrgChart({
	            chartElement : '#'+attrs.organigram,
	            dragAndDrop  : true
	        });
		}
	}
});

App.directive('hasRole', ['notifService',
                     		function(notifService) {
                     			return {
                     				restrict : 'AEC',
                     				link : function($scope, elem, attrs, ctrl) {
                     					var attr_roles = attrs.hasRole.split(",");
                     					var elementRef=elem;
                     					var hasTheRole=function(roles,role){
                     						result=false;
                     						roles.forEach(function(entry) {
                     						    if(entry.role==role) {
                     						    	result=true;
                     						    }
                     						});
                     						return result;
                     					};
                     					var hasAnyOneOfRole=function(userRoles,definedRoles){
                     						result=false;
                     						definedRoles.forEach(function(entry) {
                     						    if(hasTheRole(userRoles,entry)) {
                     						    	result=true;
                     						    }
                     						});
                     						return result;
                     					};
                     					$scope.$on('userInfo', function(event, args) {
                     						if(!hasAnyOneOfRole(args.roles,attr_roles)){
                     							elementRef.hide();
                     						}
                     					  });
                     					
                     				}
                     			};
                     		} ]);
App.directive('collapsibleFieldset',	function() {
		return {
			restrict : 'A',
			replace: 'true',
			link : function(scope, elem, attrs, ctrl) {
				var collapsed=(attrs.collapsibleFieldsetCollapsed ===undefined)?true:false;
				var title=attrs.collapsibleFieldset;
				var bodyElement=elem.find('div');
				bodyElement.addClass("am-fade");
				elem.addClass("collapsible");
				var legendImgElement="<img class=\"tool-img tool-toggle\"  src=\"data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==\">";
				var legendElement="<legend>"+legendImgElement+" <span> "+title+"</span></legend>";
				elem.prepend(legendElement);
				if(collapsed){
					elem.addClass("collapsed");
					elem.find('.tool-img').addClass("collapsed");
				}
				elem.children('legend').bind('click', function(event) {
					collapsed=!collapsed;
					if(collapsed){
						elem.addClass("collapsed");
						elem.find('.tool-img').addClass("collapsed");
					}else {
						elem.removeClass("collapsed");
						elem.find('.tool-img').removeClass("collapsed");
					}
				});
			}
		};
});
App.directive('authApplicationSupport', function($timeout) {
    return {
        restrict: 'A',
        link: function(scope, elem, attrs) {
          var login = elem.find('#app-login-content');
          var main = elem.find('#app-content');
          
          login.hide();
          
          scope.$on('event:auth-loginRequired', function() {
            login.slideDown('slow', function() {
              main.hide();
            });
          });
          scope.$on('event:auth-loginConfirmed', function() {
            main.show();
            login.slideUp();
          });
        }
      }
    });

App.directive('applicationLoadingSupport', function($timeout) {
    return {
        restrict: 'A',
        scope : true,
        link: function(scope, elem, attrs) {
        	var eventsToWait=scope.eventsToWait;
        	if(eventsToWait!== undefined){
        		angular.forEach(eventsToWait,function(event){
        			scope.$on(event, function() {
        				for (var i in eventsToWait) {
							  if(eventsToWait[i]===event){
								  eventsToWait.splice(i, 1);
								  debug("event "+event+' finished');
								}
							};
						if(eventsToWait.length==0){
	        				 debug('All event finished');
	        				 scope.$broadcast('application-loaded');
	        				//once Angular is started, remove class:
	    		        	$timeout(function() {
	    		        		elem.removeClass('waiting-for-angular');
	    					},0);
	        			}
        	          });
        			
        		});
        	}
        }
      }
    });

App.directive('connectionLostSupport', function($modal) {
	return {
		restrict : 'A',
		scope : true,
		link : function(scope, elem, attrs) {
			scope.$on('event:http-connection-lost', function() {
				//Show a basic modal from a controller
				var connectionLostModal = $modal({
					title : 'Network error',
					content : 'Connection lost with server :(',
					show : true,
					placement : 'center',
					backdrop : 'static'
				});
			});
			
		}
	}
});

App.directive('ngConfirm',function($modal) {
	return {
		priority : -1,
						link : function(scope, element, attr) {
							var msg = attr.ngConfirm || "Are you sure?";
							var clickAction = attr.ngClick;
							// scope.$on('application-loaded', function() {
							var confirmModal = $modal({
								scope : scope,
								template : _contextPath
										+ '/views/app/templates/partials/confirm-dialog.tpl.html',
								placement : 'center',
								backdrop : 'static',
								show : false
							});
							// Show when some event occurs (use $promise
							// property to ensure the template has been loaded)
							scope.showModal = function() {
								confirmModal.$promise.then(confirmModal.show);
							};
							scope.confirmationMessage = msg;
							scope.yes = function(hide) {
								scope.$eval(clickAction);
								hide();
							};
							scope.no = function(hide) {
								hide();
							}
							element.bind('click', function(event) {
								scope.showModal();
								event.stopImmediatePropagation();
								event.preventDefault();
							});
						}
	};
});


