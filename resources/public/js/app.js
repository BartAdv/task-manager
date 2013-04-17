function TasksCtrl($scope, $http) {
    $scope.newDesc = "";
    $scope.createTask = function() {
        $http.put("/task/" + $scope.newDesc)
        .success(function(data) {
            $scope.tasks = data;
        });
    };
}
