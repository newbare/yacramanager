App.directive('webSocket', [ '$timeout', 'WebSocketService', 'notifService',
		function(timer, WebSocketService, notifService) {
			return {
				restrict : 'AEC',
				link : function(scope, elem, attrs, ctrl) {
					scope.$on('application-loaded', function() {
						WebSocketService.connect();
					});
					
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
	        element.text(title);
	      };

	      $rootScope.$on('$stateChangeStart', listener);
	    }
	  };
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
});
App.directive('httpRequestError', [ '$rootScope', 'alertService', 'notifService',
                     		function($rootScope, alertService, notifService) {
                     			return {
                     				restrict : 'AEC',
                     				link : function(scope, elem, attrs, ctrl) {
                     					scope.$on('event:http-request-error', function(event, error) {
                     						//notifService.notify('error',error.title+': <strong>'+error.status+'</strong>',error.data);
                     						alertService.show('danger',error.title+': '+error.status,JSON.stringify(error.data.errorMessage));
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
                     			};
                     		}]);
App.directive('colResizable',function($compile) {
		return {
			restrict : 'A',
			link : function($scope, elem, attrs) {
				elem.colResizable();
			}
		};
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
			});
		}
	};
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
	};
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
				scope.fieldsetOpen=collapsed;
				bodyElement.addClass("am-fade");
				elem.addClass("collapsible");
				var legendImgElement="<span class=\"fa-stack\"><i class=\"fa fa-circle-thin fa-stack-2x\"></i><i class=\"fa  fa-stack-1x\"></i></span>";
				var legendElement="<legend>"+legendImgElement+" <span> "+title+"</span></legend>";
				elem.prepend(legendElement);
				var toggle=function(){
					if(collapsed){
						elem.addClass("collapsed");
						elem.find('.tool-img').addClass("collapsed");
						elem.find('legend>span>i:nth-child(2)').addClass("fa-chevron-down");
						elem.find('legend>span>i:nth-child(2)').removeClass("fa-chevron-up");
					}else {
						elem.removeClass("collapsed");
						elem.find('.tool-img').removeClass("collapsed");
						elem.find('legend>span>i:nth-child(2)').addClass("fa-chevron-up");
						elem.find('legend>span>i:nth-child(2)').removeClass("fa-chevron-down");
					}
				};
				
				toggle();
				elem.children('legend').bind('click', function(event) {
					collapsed=!collapsed;
					toggle();
				});
			}
		};
});
App.directive('authApplicationSupport', function($timeout,$modal) {
    return {
        restrict: 'A',
        priority : -1,
        link: function(scope, elem, attrs) {
          var login = elem.find('#app-login-content');
          var main = elem.find('#app-content');
          var mainFooter = elem.find('.footer-v1');
		  var loginModal = undefined;
		  var modalInitialized=false;
          var initModal=function(){
        	  loginModal = $modal({
  				scope : scope,
  				template : _contextPath	+ 'scripts/templates/partials/login-modal.tpl.html',
  				show : false,
  				backdrop : 'static',
  				placement : 'center'
  			});
        	  modalInitialized=true;
          }
          initModal();
          scope.$on('event:auth-loginRequired', function() {
//        	  if(!modalInitialized){
//        		  initModal();
//        	  }
        	  //if(!loginModal.$element.is(':visible')){
        		  loginModal.$promise.then(loginModal.show);
        		  if(main.is(':visible')){
        			  main.hide();
        			  mainFooter.hide();
        		  }
        	  //}
          });
          scope.$on('event:auth-loginConfirmed', function() {
//        	  if(!modalInitialized){
//        		  initModal();
//        	  }
//        	  if(loginModal.$element.is(':visible')){
        		  loginModal.$promise.then(loginModal.hide);
        		  if(!main.is(':visible')){
        			  main.show();
        			  mainFooter.show();
        		  }
        	  //}
          });
        }
      };
    });

App.directive('absencePortfolio',['AbsenceREST','USERINFO', function(AbsenceREST,USERINFO) {
    return {
    	replace:true,
        restrict: 'E',
//        template:'<fieldset data-collapsible-fieldset="Available time off" data-collapsible-fieldset-collapsed="true">'+
//					'<div>'+
//					'<div class="progress">'+
//					'  <div class="progress-bar progress-bar-striped " data-ng-class="{\'progress-bar-success\':$index==1,\'progress-bar-warning\':$index==2,\'progress-bar-info\':$index==3,\'progress-bar-danger\':$index==4}" '+
//					'	  		style="width: {{portfolio.remaining*100/totalPortfolioRemaining}}%" data-ng-repeat="portfolio in absencePortfolio" title="{{portfolio.typeAbsenceDTO.label}} {{portfolio.remaining*100/totalPortfolioRemaining |number:0 }}%">'+
//					'	    <span class="sr-only">portfolio.remaining*100/totalPortfolioRemaining</span>'+
//					'	    {{portfolio.typeAbsenceDTO.label | characters:15}} {{portfolio.remaining*100/totalPortfolioRemaining |number:0 }}%'+
//					'	  </div>'+
//					'	</div>'+
//					'</div>'+
//					'<div class="row">'+
//					'	<div class="col-md-4 col-xs-12" data-ng-repeat="portfolio in absencePortfolio">'+
//					'		<div class="col-md-8 col-xs-6"><span>{{portfolio.typeAbsenceDTO.label}}: </span></div>'+
//					'		<div class="col-md-4 col-xs-2">'+
//					'			<span>'+
//					'				<strong>{{portfolio.remaining}}</strong>'+
//					'			</span>'+
//					'			</div>'+
//					'	</div>'+
//					'</div>'+
//					'</fieldset>',
        templateUrl: _contextPath	+ 'scripts/templates/partials/absence-portfolio.tpl.html',
        link: function(scope, elem, attrs) {
        	scope.absencePortfolio={};
        	var userID=attrs.userId || USERINFO.id;
        	scope.$on('absence-portfolio-changed',function(){
        		scope.refreshPortfolio();
        	});
        	var initialisation=true;
        	scope.refreshPortfolio=function(){
        		AbsenceREST.getPortfolio({
        			"requesterId" : userID
        		}).$promise.then(function(result) {
        			scope.absencePortfolio=result.result;
        			scope.totalPortfolioRemaining=scope.countTotalPortfolio(scope.absencePortfolio);
        			initialisation=false;
        		});
        	};
        	scope.countTotalPortfolio=function(absencePortfolios){
        		var total=0;
        		angular.forEach(absencePortfolios,function(item){
        			total+=item.remaining;
        		});
        		return total;
        	};
        	scope.refreshPortfolio();
        }
      };
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
								}
							}
						if(eventsToWait.length===0){
	        				 debug('All event finished');
	        				 scope.$broadcast('application-loaded');
	        				//once Angular is started, remove class:
	    		        	$timeout(function() {
	    		        		elem.removeClass('waiting-for-angular');
	    		        		elem.css('display', 'none');
	    					},0);
	        			}
        	          });
        			
        		});
        	}
        }
      };
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
	};
});

App.directive('ngConfirm',function($modal,sweetAlert) {
	return {
		priority : -1,
						link : function(scope, element, attr) {
							var msg = attr.ngConfirm || "Are you sure?";
							var clickAction = attr.ngClick;
							// scope.$on('application-loaded', function() {
							var confirmModal = $modal({
								scope : scope,
								template : _contextPath	+ 'scripts/templates/partials/confirm-dialog.tpl.html',
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
							};
							element.bind('click', function(event) {

								sweetAlert.swal({
					                title: msg,
					                text: "Your will not be able to recover this imaginary file!",
					                type: "warning",
					                showCancelButton: true,
					                confirmButtonColor: "#DD6B55",
					                confirmButtonText: "Yes, delete it!",
					                cancelButtonText: "No, cancel plx!",
					                closeOnConfirm: true,
					                closeOnCancel: true },
					            function (isConfirm) {
					                if (isConfirm) {
					                	scope.$eval(clickAction);
					                    sweetAlert.swal("Done !", "", "success");
					                } else {
					                    sweetAlert.swal("Cancelled", "Your imaginary file is safe :)", "error");
					                }
					            });
								//scope.showModal();
								event.stopImmediatePropagation();
								event.preventDefault();
							});
						}
	};
});

App.filter('range', function() {
	  return function(input, total) {
	    total = parseInt(total);
	    for (var i=0; i<total; i++)
	      input.push(i);
	    return input;
	  };
	});

App.filter('partition', function() {
	  var cache = {};
	  var filter = function(arr, size) {
	    if (!arr) { return; }
	    var newArr = [];
	    for (var i=0; i<arr.length; i+=size) {
	      newArr.push(arr.slice(i, i+size));
	    }
	    var arrString = JSON.stringify(arr);
	    var fromCache = cache[arrString+size];
	    if (JSON.stringify(fromCache) === JSON.stringify(newArr)) {
	      return fromCache;
	    }
	    cache[arrString+size] = newArr;
	    return newArr;
	  };
	  return filter;
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
    };
});

App.directive('autoSelect', function () {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            element.on('click', function () {
                this.select();
            });
        }
    };
});

App
		.directive(
				'ngJspdf',
				function($compile) {
					return {
						restrict : 'E',
						replace : true,
						scope:true,
						transclude : true,
						template : '<a href="" data-ng-click="exportToPDF()" ng-transclude></a>"',
						link : function($scope, elem, attrs) {
							$scope.targetFragmentId = attrs.target;
							$scope.fileName = attrs.filename;
							
							$scope.exportToPDF = function() {
								//var doc = new jsPDF('landscape');
								var pdf = new jsPDF('landscape', 'pt', 'letter');
								var source=$('#'+$scope.targetFragmentId).get(0);
								// All units are in the set measurement for the
								// document
								// This can be changed to "pt" (points), "mm"
								// (Default), "cm", "in"
//								doc.fromHTML($('#'+$scope.targetFragmentId).get(0), 15, 15, {
//									'width' : 170
//								});
//								doc.save($scope.fileName);
								
								//TEST NEW
								specialElementHandlers = {
								        // element with id of "bypass" - jQuery style selector
								        '#bypassme': function (element, renderer) {
								            // true = "handled elsewhere, bypass text extraction"
								            return true
								        },
								        'fieldset':function (element, renderer) {
								            return true
								        }
								    };
								    margins = {
								        top: 80,
								        bottom: 60,
								        left: 40,
								        width: 522
								    };
								    // all coords and widths are in jsPDF instance's declared units
								    // 'inches' in this case
								    pdf.fromHTML(
								    source, // HTML string or DOM elem ref.
								    margins.left, // x coord
								    margins.top, { // y coord
								        'width': margins.width, // max width of content on PDF
								        'elementHandlers': specialElementHandlers
								    },

								    function (dispose) {
								        // dispose: object with X, Y of the last line add to the PDF 
								        //          this allow the insertion of new lines after html
								        pdf.save('Test.pdf');
								    }, margins);
								
							};
							
							
							
						}
					};
				});


App.directive('contactsManager',['$sce',
         						'$timeout',
        						'$templateCache',
        						'$q',
        						'$http',
        						'$compile',
        						'$filter', function($sce, $timeout, $templateCache, $q, $http,
        								$compile, $filter) {
    return {
    	replace:true,
        restrict: 'AE',
        scope : {
			// models
        	contactsManagerConfig : '='
        },
        template : 
			'<div ng-html-compile="filterContentHTML">'+
			'</div>',
        link: function($scope, elem, attrs) {
        	var textFilterTemplate = _contextPath + 'scripts/templates/contact-manager.tpl.html';
        	$scope.filterContentHTML=undefined;
        	
        	$scope.contactsManagerConfig.dataObject.$promise.then(function(value) {
        		$scope.dataObject=value;
			});
        	$scope.update=$scope.contactsManagerConfig.update;
        	function fetchTemplate(template) {
				return $q.when(	$templateCache.get(template)|| $http.get(template))
						.then(function(res) {
									if (angular.isObject(res)) {
										$templateCache.put(template,res.data);
										return res.data;
									}
									return res;
								});
			};
        	fetchTemplate(textFilterTemplate)
			.then(
					function(content) {
						$scope.filterContentHTML = content;
					});
        	$scope.addContact=function(dataObject){
        		var newContact={name:undefined,email:undefined,phoneNumbers:[],adresse:{adress:undefined,postCode:undefined,city:undefined,country:undefined}};
        		dataObject.contacts.push(newContact);
        	};
        	
        	$scope.deleteContact=function(dataObject,index){
        		dataObject.contacts.splice(index,1);
        		$scope.update();
        	};
        	$scope.addPhoneNumbers=function(contact){
        		contact.phoneNumbers.push('');
        	};
        	$scope.removePhoneNumbers=function(contact,index){
        		contact.phoneNumbers.splice(index,1);
        	};
        }
      };
    }]);

App.directive('showValidation', function() {
    return {
        restrict: 'A',
        require: 'form',
        link: function (scope, element) {
            element.find('.form-group').each(function() {
                var $formGroup = $(this);
                var $inputs = $formGroup.find('input[ng-model],input[data-ng-model],textarea[ng-model],textarea[data-ng-model],select[ng-model],select[data-ng-model]');

                if ($inputs.length > 0) {
                    $inputs.each(function() {
                        var $input = $(this);
                        scope.$watch(function() {
                            return $input.hasClass('ng-invalid') && $input.hasClass('ng-dirty');
                        }, function(isInvalid) {
                            $formGroup.toggleClass('has-error', isInvalid);
                        });
                    });
                }
            });
        }
    };
});

App.directive('ngFocus', [function() {
	  var FOCUS_CLASS = "ng-focused";
	  return {
	    restrict: 'A',
	    require: 'ngModel',
	    link: function(scope, element, attrs, ctrl) {
	      ctrl.$focused = false;
	      element.bind('focus', function(evt) {
	        element.addClass(FOCUS_CLASS);
	        scope.$apply(function() {ctrl.$focused = true;});
	      }).bind('blur', function(evt) {
	        element.removeClass(FOCUS_CLASS);
	        scope.$apply(function() {ctrl.$focused = false;});
	      });
	    }
	  }
	}]);
App.directive('activitiesTimeline', function() {
    return {
    	replace:true,
        restrict: 'E',
        scope : false,
        templateUrl: _contextPath	+ 'scripts/templates/timeline/timeline.tpl.html',
        link: function (scope, element,attrs) {
        	 scope.$watch(attrs['source'], function(newValue, oldValue) {
        		    if (scope[attrs['source']]!==undefined) {
        		    	var resource= scope[attrs['source']];
        		    	resource.$promise.then(function(result){
        	        		scope.timelineData=result;
        	        	});
        		    }
        		  });
        	
        }
    };
});

App.directive('subMenuNavPill', function($rootScope) {
	  return {
		restrict: 'A',
	    link: function(scope, element,attrs) {
	    	element.addClass('sub-menu');
	    	var parent=element.parent();
	    	scope.$watch(function(){
	    		return parent;
	    	},function(newValue, oldValue){
	    		if(parent.hasClass('collapse') && parent.hasClass('in')){
	    			element.toggleClass('nav-stacked');
	    		}
	    	});
	    }
	  };
	});

App.directive('yacraStatus', function($rootScope) {
	  return {
		restrict: 'A',
		replace: true,
		template: '<span class="label" data-ng-class="{\'label-success\' : isValidated() , \'label-warning\' : isWaiting(), \'label-danger\' : isRejected()}">'
			+'{{status}}</span>',
	    link: function(scope, element,attrs) {
	    	scope.isValidated=function(){
				return 'APPROVED'==scope.status || 'OPEN'==scope.status;
			};
			scope.isWaiting=function(){
				return 'PENDING'==scope.status;
			};
			scope.isRejected=function(){
				return 'REJECTED'==scope.status;
			};
	    	scope.status=attrs['yacraStatus'];
	    }
	  };
	});

App.directive('scrollToTop', function() {
	  return {
		restrict: 'A',
	    link: function(scope, element,attrs) {
	    	var scrollToTop=function () {
	    		verticalOffset = typeof(verticalOffset) != 'undefined' ? verticalOffset : 0;
	    		body = $('body');
	    		offset = body.offset();
	    		offsetTop = offset.top;
	    		$('html, body').animate({scrollTop: offsetTop}, 500, 'linear');
	    	};
	    	$(document).on('scroll', function(){
		    	if ($(window).scrollTop() > attrs['scrollToTop']) {
		    		element.addClass('show');
				} else {
					element.removeClass('show');
				}
	    	});
	    	element.on('click', scrollToTop);
	    }
	  };
	});

App.directive('widgetContainer', function() {
	  return {
		restrict: 'A',
	    link: function(scope, element,attrs) {
	    	element.sortable({
	    	    connectWith: '.widget-container-col',
	    	    items: '> .widget-box',
	    	    opacity: 0.8,
	    	    revert: true,
	    	    forceHelperSize: true,
	    	    placeholder: 'widget-placeholder',
	    	    forcePlaceholderSize: true,
	    	    tolerance: 'pointer',
	    	    start: function(event, ui){
	    	        //when an element is moved, its parent (.widget-container-col) becomes empty with almost zero height
	    	        //we set a "min-height" for it to be large enough so that later we can easily drop elements back onto it
	    	        ui.item.parent().css({'min-height': ui.item.height()})
	    	    },
	    	    update: function(event, ui) {
	    	        //the previously set "min-height" is not needed anymore
	    	        //now the parent (.widget-container-col) should take the height of its child (.wiget-box)
	    	        ui.item.parent({'min-height':''})
	    	    }
	    	 });
	    }
	  };
	});

App.directive('widgetBox', function($q) {
	  return {
		restrict: 'A',
	    link: function(scope, element,attrs) {
	    	this.scope=scope;
	    	this.attrs=attrs;
	    	this.element=element;
	    	var Widget_Box = function(box, options) {
	    		this.$box = $(box);
	    		var that = this;
	    		//this.options = $.extend({}, $.fn.widget_box.defaults, options);

	    		this.reload = function() {
	    			var $box = this.$box;
	    			var $remove_position = false;
	    			if($box.css('position') == 'static') {
	    				$remove_position = true;
	    				$box.addClass('position-relative');
	    			}
	    			$box.append('<div class="widget-box-overlay"><i class="'+ ace.vars['icon'] + 'loading-icon fa fa-spinner fa-spin fa-2x white"></i></div>');

	    			refreshAction=$q.when(scope[attrs.refreshAction]()).then(
			    			function(){
			    				$box.trigger('reloaded.ace.widget')
			    			});
	    			
	    			$box.one('reloaded.ace.widget', function() {
	    				$box.find('.widget-box-overlay').remove();
	    				if($remove_position) $box.removeClass('position-relative');
	    			});
	    		}

	    		this.close = function() {
	    			var $box = this.$box;
	    			var closeSpeed = 300;
	    			$box.fadeOut(closeSpeed , function(){
	    					$box.trigger('closed.ace.widget');
	    					$box.remove();
	    				}
	    			)
	    		}
	    		
	    		this.toggle = function(type, button) {
	    			var $box = this.$box;
	    			var $body = $box.find('.widget-body').eq(0);
	    			var $icon = null;
	    			
	    			var event_name = typeof type !== 'undefined' ? type : ($box.hasClass('collapsed') ? 'show' : 'hide');
	    			var event_complete_name = event_name == 'show' ? 'shown' : 'hidden';

	    			if(typeof button === 'undefined') {
	    				button = $box.find('> .widget-header a[data-action=collapse]').eq(0);
	    				if(button.length == 0) button = null;
	    			}

	    			if(button) {
	    				$icon = button.find(ace.vars['.icon']).eq(0);

	    				var $match
	    				var $icon_down = null
	    				var $icon_up = null
	    				if( ($icon_down = $icon.attr('data-icon-show')) ) {
	    					$icon_up = $icon.attr('data-icon-hide')
	    				}
	    				else if( $match = $icon.attr('class').match(/fa\-(.*)\-(up|down)/) ) {
	    					$icon_down = 'fa-'+$match[1]+'-down'
	    					$icon_up = 'fa-'+$match[1]+'-up'
	    				}
	    			}

	    			var expandSpeed   = 250;
	    			var collapseSpeed = 200;

	    			if( event_name == 'show' ) {
	    				if($icon) $icon.removeClass($icon_down).addClass($icon_up);

	    				$body.hide();
	    				$box.removeClass('collapsed');
	    				$body.slideDown(expandSpeed, function(){
	    					$box.trigger(event_complete_name+'.ace.widget')
	    				})
	    			}
	    			else {
	    				if($icon) $icon.removeClass($icon_up).addClass($icon_down);
	    				$body.slideUp(collapseSpeed, function(){
	    						$box.addClass('collapsed')
	    						$box.trigger(event_complete_name+'.ace.widget')
	    					}
	    				);
	    			}
	    		}
	    		
	    		this.hide = function() {
	    			this.toggle('hide');
	    		}
	    		this.show = function() {
	    			this.toggle('show');
	    		}
	    		
	    		
	    		this.fullscreen = function() {
	    			var $icon = this.$box.find('> .widget-header a[data-action=fullscreen]').find(ace.vars['.icon']).eq(0);
	    			var $icon_expand = null
	    			var $icon_compress = null
	    			if( ($icon_expand = $icon.attr('data-icon1')) ) {
	    				$icon_compress = $icon.attr('data-icon2')
	    			}
	    			else {
	    				$icon_expand = 'fa-expand';
	    				$icon_compress = 'fa-compress';
	    			}
	    			
	    			
	    			if(!this.$box.hasClass('fullscreen')) {
	    				$icon.removeClass($icon_expand).addClass($icon_compress);
	    				this.$box.addClass('fullscreen');
	    				
	    				applyScrollbars(this.$box, true);
	    			}
	    			else {
	    				$icon.addClass($icon_expand).removeClass($icon_compress);
	    				this.$box.removeClass('fullscreen');
	    				
	    				applyScrollbars(this.$box, false);
	    			}
	    			
	    			this.$box.trigger('fullscreened.ace.widget')
	    		}
	    	};
	    	$.fn.widget_box = function (option, value) {
	    		var method_call;

	    		var $set = this.each(function () {
	    			var $this = $(this);
	    			var data = $this.data('widget_box');
	    			var options = typeof option === 'object' && option;

	    			if (!data) $this.data('widget_box', (data = new Widget_Box(this, options)));
	    			if (typeof option === 'string') method_call = data[option](value);
	    		});

	    		return (method_call === undefined) ? $set : method_call;
	    	};


	    	$(document).on('click.ace.widget', '.widget-header a[data-action]', function (ev) {
	    		ev.preventDefault();

	    		var $this = $(this);
	    		var $box = $this.closest('.widget-box');
	    		if( $box.length == 0 || $box.hasClass('ui-sortable-helper') ) return;

	    		var $widget_box = $box.data('widget_box');
	    		if (!$widget_box) {
	    			$box.data('widget_box', ($widget_box = new Widget_Box($box.get(0))));
	    		}

	    		var $action = $this.data('action');
	    		if($action == 'collapse') {
	    			var event_name = $box.hasClass('collapsed') ? 'show' : 'hide';

	    			var event
	    			$box.trigger(event = $.Event(event_name+'.ace.widget'))
	    			if (event.isDefaultPrevented()) return

	    			$widget_box.toggle(event_name, $this);
	    		}
	    		else if($action == 'close') {
	    			var event
	    			$box.trigger(event = $.Event('close.ace.widget'))
	    			if (event.isDefaultPrevented()) return

	    			$widget_box.close();
	    		}
	    		else if($action == 'reload') {
	    			$this.blur();
	    			var event
	    			$box.trigger(event = $.Event('reload.ace.widget'))
	    			if (event.isDefaultPrevented()) return

	    			$widget_box.reload();
	    		}
	    		else if($action == 'fullscreen') {
	    			var event
	    			$box.trigger(event = $.Event('fullscreen.ace.widget'))
	    			if (event.isDefaultPrevented()) return
	    		
	    			$widget_box.fullscreen();
	    		}
	    		else if($action == 'settings') {
	    			$box.trigger('setting.ace.widget')
	    		}

	    	});
	    	
	    	function applyScrollbars($widget, enable) {
	    		var $main = $widget.find('.widget-main').eq(0);
	    		$(window).off('resize.widget.scroll');
	    		
	    		//IE8 has an unresolvable issue!!! re-scrollbaring with unknown values?!
	    		var nativeScrollbars = ace.vars['old_ie'] || ace.vars['touch'];
	    		
	    		if(enable) {
	    			var ace_scroll = $main.data('ace_scroll');
	    			if( ace_scroll ) {
	    				$main.data('save_scroll', {size: ace_scroll['size'], lock: ace_scroll['lock'], lock_anyway: ace_scroll['lock_anyway']});
	    			}
	    			
	    			var size = $widget.height() - $widget.find('.widget-header').height() - 10;//extra paddings
	    			size = parseInt(size);
	    			
	    			$main.css('min-height', size);
	    			if( !nativeScrollbars ) {
	    				if( ace_scroll ) {
	    					$main.ace_scroll('update', {'size': size, 'mouseWheelLock': true, 'lockAnyway': true});
	    				}
	    				else {
	    					$main.ace_scroll({'size': size, 'mouseWheelLock': true, 'lockAnyway': true});
	    				}
	    				$main.ace_scroll('enable').ace_scroll('reset');
	    			}
	    			else {
	    				if( ace_scroll ) $main.ace_scroll('disable');
	    				$main.css('max-height', size).addClass('overflow-scroll');
	    			}
	    			
	    			
	    			$(window)
	    			.on('resize.widget.scroll', function() {
	    				var size = $widget.height() - $widget.find('.widget-header').height() - 10;//extra paddings
	    				size = parseInt(size);
	    				
	    				$main.css('min-height', size);
	    				if( !nativeScrollbars ) {
	    					$main.ace_scroll('update', {'size': size}).ace_scroll('reset');
	    				}
	    				else {
	    					$main.css('max-height', size).addClass('overflow-scroll');
	    				}
	    			});
	    		}
	    		
	    		else  {
	    			$main.css('min-height', '');
	    			var saved_scroll = $main.data('save_scroll');
	    			if(saved_scroll) {
	    				$main
	    				.ace_scroll('update', {'size': saved_scroll['size'], 'mouseWheelLock': saved_scroll['lock'], 'lockAnyway': saved_scroll['lock_anyway']})
	    				.ace_scroll('enable')
	    				.ace_scroll('reset');
	    			}
	    			
	    			if( !nativeScrollbars ) {				
	    				if(!saved_scroll) $main.ace_scroll('disable');				
	    			}
	    			else {
	    				$main.css('max-height', '').removeClass('overflow-scroll');
	    			}
	    		}
	    	}
	    }
	  };
	});

