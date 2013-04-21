function TasksCtrl($scope, $http) {
    $scope.task = {};
    $scope.selectTask = function(task) {
        $scope.task = task;
    };
    $scope.createTask = function() {
        $http.put("/task/" + $scope.task.description)
        .success(function(data) {
            $scope.tasks = data;
        });
    };
    $scope.updateStatus = function() {
	$http.post("task/" + $scope.task.number + "/status/" + $scope.task.status)
	.success(function(data) {
	    $scope.tasks = data;
	});
    };
}
