angular.module('marvelapp', [
    'marvelapp.menu',
    'marvelapp.chat',
    'marvelapp.marvel',
    'marvelapp.marvel.detail',
    'ui.bootstrap',
    'ngRoute'])
.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/search', {
        templateUrl: '/assets/app/marvel/marvel.html',
        controller: 'MarvelCtrl',
        controllerAs :'marvel'
      }).
      when('/detail/:id', {
          templateUrl: '/assets/app/marvel/detail/detail.html',
          controller: 'DetailCtrl',
          controllerAs :'detail'
        }).
      when('/chat', {
          templateUrl: '/assets/app/chat/chat.html',
          controller: 'ChatCtrl',
          controllerAs :'chat'
        }).
      otherwise({
        redirectTo: '/search'
      });
}]);