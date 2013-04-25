angular.module('services', ['ngResource'])
	.factory('Task', function($resource) {
		return $resource('task/:number');
});