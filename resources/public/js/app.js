function TasksCtrl($scope, $http) {
    $scope.selectedTask = 0;
    $scope.newTask = {};
    $scope.selectTask = function(num) {
        $scope.selectedTask = num;
    };
    $scope.isSelected = function(task) { return $scope.selectedTask === task.number; };
    $scope.createTask = function() {
        $http.put("/task/" + $scope.nweTask.description)
        .success(function(data) {
            $scope.tasks = data;
        });
    };
    $scope.updateStatus = function() {
	$http.post("/task/" + $scope.task.number + "/status/" + $scope.task.status)
	.success(function(data) {
	    $scope.tasks = data;
	});
    };
    $scope.update = function(task) {
	$http.post("/task" , task)
	.success(function(data) {
	    $scope.tasks = data;
	});
    };
}
