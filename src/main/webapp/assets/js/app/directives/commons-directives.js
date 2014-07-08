App.directive('webSocket', [ '$timeout', 'WebSocketService', 'notifService',
		function(timer, WebSocketService, notifService) {
			return {
				restrict : 'AEC',
				link : function(scope, elem, attrs, ctrl) {
					timer(function() {
						WebSocketService.connect();
						notifService.notify('info', 'WebSocket', 'Connected');
					}, 0);
				}
			};
		} ]);


App.directive('fileInput',	function() {
                     			return {
                     				restrict : 'AEC',
                     				link : function(scope, elem, attrs, ctrl) {
                     					elem.bootstrapFileInput ();
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