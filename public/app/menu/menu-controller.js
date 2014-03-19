angular.module('marvelapp.menu', []).controller('MenuCtrl', ['$scope', '$location', function ($scope, $location) {

    $scope.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };

}]);