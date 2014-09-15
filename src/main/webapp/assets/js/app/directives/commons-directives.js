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
App.directive('activeMenu', function($translate, $locale, tmhDynamicLocale) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs, controller) {
            var language = attrs.activeMenu;

            scope.$watch(function() {
                return $translate.use();
            }, function(selectedLanguage) {
                if (language === selectedLanguage) {
                    tmhDynamicLocale.set(language);
                    element.addClass('active');
                } else {
                    element.removeClass('active');
                }
            });
        }
    };
})
App.directive('httpRequestError', [ '$rootScope', 'alertService', 'notifService',
                     		function($rootScope, alertService, notifService) {
                     			return {
                     				restrict : 'AEC',
                     				link : function(scope, elem, attrs, ctrl) {
                     					scope.$on('event:http-request-error', function(event, error) {
                     						//notifService.notify('error',error.title+': <strong>'+error.status+'</strong>',error.data);
                     						alertService.show('danger',error.title+': '+error.status,JSON.stringify(error.data));
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

App.directive('fixedTableColumn', function($compile) {
	return {
		restrict : 'A',
		link : function($scope, elem, attrs) {
			$scope.$watch('$last', function(v) {
				if (v) {
					var $fixedColumn = elem.clone().insertBefore(elem)
							.addClass('fixed-column');
					$fixedColumn.find(
							'th:not(:first-child),td:not(:first-child)')
							.remove();

					$fixedColumn.find('tr').each(function(i, element) {
						$(this).height(elem.find('tr:eq(' + i + ')').height());
					});
				}
				;
			});
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

App.directive('absencePortfolio',['AbsenceREST', function(AbsenceREST) {
    return {
    	replace:true,
        restrict: 'E',
        template:'<fieldset data-collapsible-fieldset="Used on selected period" data-collapsible-fieldset-collapsed="true">'
					+'<div>'
					+'<div class="progress">'
					+'  <div class="progress-bar progress-bar-striped " data-ng-class="{\'progress-bar-success\':$index==1,\'progress-bar-warning\':$index==2,\'progress-bar-info\':$index==3,\'progress-bar-danger\':$index==4}" '
					+'	  		style="width: {{portfolio.remaining*100/totalPortfolioRemaining}}%" data-ng-repeat="portfolio in absencePortfolio" title="{{portfolio.typeAbsenceDTO.label}} {{portfolio.remaining*100/totalPortfolioRemaining |number:0 }}%">'
					+'	    <span class="sr-only">portfolio.remaining*100/totalPortfolioRemaining</span>'
					+'	    {{portfolio.typeAbsenceDTO.label | characters:15}} {{portfolio.remaining*100/totalPortfolioRemaining |number:0 }}%'
					+'	  </div>'
					+'	</div>'
					+'</div>'
					+'<div class="row">'
					+'	<div class="col-md-4" data-ng-repeat="portfolio in absencePortfolio">'
					+'		<div class="col-md-8"><span>{{portfolio.typeAbsenceDTO.label}}: </span></div>'
					+'		<div class="col-md-4">'
					+'			<span>'
					+'				<strong>{{portfolio.remaining}}</strong>'
					+'			</span>'
					+'			</div>'
					+'	</div>'
					+'</div>'
					+'</fieldset>',
        link: function(scope, elem, attrs) {
        	scope.absencePortfolio={};
        	scope.refreshPortfolio=function(){
        		AbsenceREST.getPortfolio({
        			"requesterId" : _userId
        		}).$promise.then(function(result) {
        			scope.absencePortfolio=result.result;
        			scope.totalPortfolioRemaining=scope.countTotalPortfolio(scope.absencePortfolio);
        		});
        	}
        	scope.countTotalPortfolio=function(absencePortfolios){
        		var total=0;
        		angular.forEach(absencePortfolios,function(item){
        			total+=item.remaining;
        		});
        		return total;
        	};
        	scope.refreshPortfolio();
        }
      }
    }]);

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

App.directive('passwordStrengthBar', function() {
    return {
        replace: true,
        restrict: 'E',
        template: '<div id="strength">' +
                  '<ul id="strengthBar" class="list-inline">' +
                    '<li><small  translate="global.messages.validate.newpassword.strength">Password strength:</small></li><li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li>' +
                  '</ul>' +
                '</div>',
        link: function(scope, iElement, attr) {
            var strength = {
                colors: ['#F00', '#F90', '#FF0', '#9F0', '#0F0'],
                mesureStrength: function (p) {

                    var _force = 0;
                    var _regex = /[$-/:-?{-~!"^_`\[\]]/g; // "
                                          
                    var _lowerLetters = /[a-z]+/.test(p);                    
                    var _upperLetters = /[A-Z]+/.test(p);
                    var _numbers = /[0-9]+/.test(p);
                    var _symbols = _regex.test(p);
                                          
                    var _flags = [_lowerLetters, _upperLetters, _numbers, _symbols];                    
                    var _passedMatches = $.grep(_flags, function (el) { return el === true; }).length;                                          
                    
                    _force += 2 * p.length + ((p.length >= 10) ? 1 : 0);
                    _force += _passedMatches * 10;
                        
                    // penality (short password)
                    _force = (p.length <= 6) ? Math.min(_force, 10) : _force;                                      
                    
                    // penality (poor variety of characters)
                    _force = (_passedMatches == 1) ? Math.min(_force, 10) : _force;
                    _force = (_passedMatches == 2) ? Math.min(_force, 20) : _force;
                    _force = (_passedMatches == 3) ? Math.min(_force, 40) : _force;
                    
                    return _force;

                },
                getColor: function (s) {

                    var idx = 0;
                    if (s <= 10) { idx = 0; }
                    else if (s <= 20) { idx = 1; }
                    else if (s <= 30) { idx = 2; }
                    else if (s <= 40) { idx = 3; }
                    else { idx = 4; }

                    return { idx: idx + 1, col: this.colors[idx] };
                }
            };  
            scope.$watch(attr.passwordToCheck, function(password) {
                if (password) {
                    var c = strength.getColor(strength.mesureStrength(password));
                    iElement.removeClass('ng-hide');
                    iElement.find('ul').children('li.point')
                        .css({ "background": "#DDD" })
                        .slice(0, c.idx)
                        .css({ "background": c.col });
                }
            });
        }
    }
});


