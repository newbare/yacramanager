/**
 * 
 */
App.service('alertService', function($alert) {
	this.show = function(type,title, content) {
		// Service usage
		var myAlert = $alert({
			title : title,
			content : content,
			type : type,
			//placement: 'top',
			keyboard : true,
			show : false,
			duration: 10,
			template:  _contextPath+'/assets/others/alert/alert.tpl.html',
			container: '#alerts-container'
			//container: 'body'
			//container: '#main-view'
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
	var subscribeToInfoQueue=function(){
		stompClient.subscribe("/user/queue/info", function(msg) {
			notifService.notify('info','Info',msg.body)
		});
	};
	
	var subscribeToCompanyQueue=function(){
		stompClient.subscribe("/user/queue/"+_userCompanyName, function(msg) {
			notifService.notify('info','From company queue',msg.body)
		});
	};
	
	var subscribeToCompanyTopic=function(){
		stompClient.subscribe("/topic/"+_userCompanyName, function(msg) {
			notifService.notify('info','From company topic',msg.body)
		});
	};
	
	this.connect = function connect() {
			var socket = new SockJS('/yacramanager/yacra');
			stompClient = Stomp.over(socket);
			stompClient.debug=function(){
				//do nothing
			};
			stompClient.connect('guest','guest', function(frame) {
				console.log('Connected: ' + frame);
				var user = frame.headers['user-name'];
				subscribeToCommonTopic();
				subscribeToErrorQueue();
				subscribeToInfoQueue();
				subscribeToCompanyQueue();
				subscribeToCompanyTopic();
				});
	};
});
	