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
			template: _contextPath+'/assets/others/alert/alert.tpl.html',
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
			template:  _contextPath+'/assets/others/alert/alert.tpl.html',
			container: 'body'
		});
		myAlert.$promise.then(myAlert.show);
	};
});

App.service('notifService', function() {
	this.notify = function(type,title, content) {
		$.gritter.add({
			title: title,
			text: content,
			class_name: 'gritter-'+type+' gritter-light'
		});
	};
});
	
App.service('WebSocketService', function($timeout,notifService) {
	var stompClient = null;	
	
	var subscribeToCommonTopic=function(){
		stompClient.subscribe('/topic/yacra', function(msg) {
			notifService.notify('info','From topic',msg.body)
		});
	};
	var subscribeToErrorQueue=function(){
		stompClient.subscribe("/user/queue/errors", function(msg) {
			notifService.notify('error','Error',msg.body)
		});
	};
	
	this.connect = function connect() {
			var socket = new SockJS('/yacramanager/yacra');
			stompClient = Stomp.over(socket);
			
			stompClient.connect('guest','guest', function(frame) {
				console.log('Connected: ' + frame);
				var user = frame.headers['user-name'];
				subscribeToCommonTopic();
				subscribeToErrorQueue();
				});
	};
	
});
	