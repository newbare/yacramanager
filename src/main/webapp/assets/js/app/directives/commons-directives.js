App.directive('webSocket', [ '$timeout', 'WebSocketService', 'notifService',
		function(timer, WebSocketService, notifService) {
			return {
				restrict : 'AEC',
				link : function(scope, elem, attrs, ctrl) {
					timer(function() {
						WebSocketService.connect();
						//notifService.notify('info', 'WebSocket', 'Connected');
					}, 0);
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
				var collapsed=true;
				var title=attrs.collapsibleFieldset;
				var bodyElement=elem.find('div');
				bodyElement.addClass("am-fade");
				elem.addClass("collapsible");
				var legendImgElement="<img class=\"tool-img tool-toggle\"  src=\"data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==\">";
				var legendElement="<legend>"+legendImgElement+" <span> "+title+"</span></legend>";
				elem.append(legendElement);
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