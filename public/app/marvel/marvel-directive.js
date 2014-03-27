angular.module('marvelapp.marvel')
.directive('marvelHero', ['MarvelService', function(MarvelService) {
    return {
      restrict: 'E', //AEC
      scope: {
        marvelHero: '=data',
        link: '@link',
      },
      link: function($scope, element, attrs) {
        $scope.getThumbnail = function(data){
            return MarvelService.getThumbnail(data, MarvelService.sizes.small);
        }
      },
      templateUrl: '/assets/app/marvel/marvelHero.html'
    };
}]);