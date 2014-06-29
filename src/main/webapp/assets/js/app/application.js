'use strict';

angular.module('yaCRAApp', ['yaCRAService']).
        config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
            when('/cra', {templateUrl:'views/cra.html', controller:TodoListController}).
            when('/user-settings', {templateUrl:'views/user-settings.html', controller:TodoNewController}).
            when('/todo/:id', {templateUrl:'views/todo-detail.html', controller:TodoDetailController}).
            otherwise({redirectTo:'/'});
}]);
