angular.module('services', ['ngResource'])
    .factory('Task', function($resource) {
	return $resource('task/:number');
    })
    .factory('Comment', function($resource) {
        return $resource('task/:number/comments/:text', {number: '@number', text: '@text' }, {
            'save': { method: 'POST' }
        });
    });
