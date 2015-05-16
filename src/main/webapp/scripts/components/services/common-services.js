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
			duration: 60,
			template:  _contextPath+'templates/alert/alert.tpl.html',
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
	

App.service('WebSocketService', function($timeout,$rootScope,notifService,USERINFO) {
	var stompClient = null;	
	var companyEventListeners=[];
	var subscribeToCommonTopic=function(){
		stompClient.subscribe('/topic/yacra', function(msg) {
			notifService.notify('info','From topic',msg.body);
		});
	};
	var subscribeToErrorQueue=function(){
		stompClient.subscribe("/user/queue/errors", function(msg) {
			notifService.notify('error','Error',msg.body);
		});
	};
	var subscribeToInfoQueue=function(){
		stompClient.subscribe("/user/queue/info", function(msg) {
			notifService.notify('info','Info',msg.body);
		});
	};
	
	var subscribeToCompanyQueue=function(){
		stompClient.subscribe("/user/queue/"+USERINFO.company.id, function(msg) {
			notifService.notify('info','From company queue',msg.body);
		});
	};
	
	var subscribeToCompanyTopic=function(){
		stompClient.subscribe("/topic/company/"+USERINFO.company.id+"/event", function(msg) {
			event=JSON.parse(msg.body);
			//notifService.notify('info','From company topic',event);
			angular.forEach(companyEventListeners,function(listener){
				if(event.entityType===listener['entityType'] && USERINFO.id!=event.userId){
					listener['listener'](listener['scope'],event);
					//$rootScope.$broadcast(listener['name'], event);
				}
			});
		});
	};
	
	this.addCompanyEventListener=function(name,scope,entityType,listener){
		alreadyExist=false;
		angular.forEach(companyEventListeners,function(listener,i){
			if(name===listener['name']){
				alreadyExist=true;
			}
		});
		if(!alreadyExist){
			companyEventListeners.push({'name':name,'entityType':entityType,'listener':listener});
		}
	};
	this.removeCompanyEventListener=function(name){
		angular.forEach(companyEventListeners,function(listener,i){
			if(name===listener['name']){
				companyEventListeners.splice(i, 1);
				return;
			}
		});
	};
	
	this.connect = function connect() {
			var socket = new SockJS('/websocket/event');
			stompClient = Stomp.over(socket);
			stompClient.debug=function(){
				//do nothing
			};
			stompClient.connect({}, function(frame) {
				console.log('Connected: ' + frame);
				var user = frame.headers['user-name'];
//				subscribeToCommonTopic();
//				subscribeToErrorQueue();
//				subscribeToInfoQueue();
//				subscribeToCompanyQueue();
				subscribeToCompanyTopic();
				});
	};
});
	