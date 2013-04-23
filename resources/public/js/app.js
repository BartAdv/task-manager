function ApplyToElementScope(el, f) {
    window.onload = function() {
	var scope = angular.element(el).scope();
    	scope.$apply(function() { f(scope); });
    };
};

function TasksCtrl($scope, $http) {
    $scope.selectedTask = 0;
    $scope.newTask = {};
    $scope.selectTask = function(num) {
        $scope.selectedTask = num;
    };
    $scope.isSelected = function(task) { return $scope.selectedTask === task['task/number']; };
    $scope.create = function() {
        $http.put("/task/" + $scope.newTask.description)
        .success(function(data) {
            $scope.tasks = data;
        });
    };
    $scope.updateStatus = function() {
	$http.post("/task/" + $scope.task.number + "/status/" + $scope.task['task/status'])
	.success(function(data) {
	    $scope.tasks = data;
	});
    };
    $scope.update = function(task) {
	$http.post("/task" , task)
	.success(function(data) {
	    $scope.tasks = data;
	    $scope.selectedTask = 0;
	});
    };
}

function TaskDetailsCtrl($scope, $http) {
    $scope.task = {};
}
