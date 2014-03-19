angular.module('marvelapp.marvel', [])
.controller('MarvelCtrl', ['MarvelService', function(MarvelService){

    var marvel = this;

    this.rechercher = function(pageNum){
         MarvelService.findSuperHeroes(this.name, 1)
            .then(function(result){
                marvel.searchResult = result;
            }, function(error){
                alert('Oups');
            });
    };

    this.displayPage = function(pageNum){
        MarvelService.findSuperHeroes(this.name, pageNum)
        .then(function(result){
            marvel.searchResult.data = result.data;
        }, function(error){
            alert('Oups');
        });
    };

    this.getThumbnail = function(character){
        return MarvelService.getThumbnail(character, MarvelService.sizes.small);
    };

    this.rechercher();

}]);