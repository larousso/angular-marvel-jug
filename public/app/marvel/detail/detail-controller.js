angular.module('marvelapp.marvel.detail', [])
.controller('DetailCtrl', ['MarvelService', '$routeParams',
    function (MarvelService, $routeParams) {

        var detail = this;
        detail.id = $routeParams.id;

        MarvelService.loadCharacter(detail.id).then(function(result){
            detail.character = result;
            detail.image = MarvelService.getThumbnail(detail.character, MarvelService.sizes.xlarge);

            MarvelService.loadComics(detail.character).then(function(result){
                detail.comics = result;
            });

        });

        this.getThumbnailSmall = function(data){
           return MarvelService.getThumbnail(data, MarvelService.sizes.small);
        };

}]);