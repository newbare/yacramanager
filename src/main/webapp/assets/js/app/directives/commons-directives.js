App.directive('webSocket',['$timeout','WebSocketService','notifService', function (timer,WebSocketService,notifService) {
        return {
        	restrict: 'AEC',
            link: function (scope, elem, attrs, ctrl) {
            	timer(function(){
            		WebSocketService.connect();
            		notifService.notify('info','WebSocket', 'Connected')
            	}, 0);
            }
        }
    }]);