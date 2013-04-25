angular.module('task-manager', ['services'])
.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/index', { templateUrl: 'partials/task-list.html', controller: TasksCtrl})
        .when('/task/:number', { templateUrl: 'partials/task-details.html', controller: TaskDetailsCtrl})
        .otherwise({ redirectTo: '/index' });
}]);
