'use strict';

angular.module('yaCRAService', ['ngResource']).
        factory('Todo', function ($resource) {
            return $resource('rest/todo/:id', {}, {
                'save': {method:'PUT'}
            });
        });
