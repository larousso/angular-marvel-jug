angular.module('marvelapp.marvel', ['marvellapp.marvel.service'])
.controller('MarvelCtrl', ['MarvelService', function(MarvelService){

    var marvel = this;
    var currentPageNum = 0;

    this.rechercher = function(pageNum){
         MarvelService.findSuperHeroes(this.name, currentPageNum)
            .then(function(result){
                marvel.searchResult = result;
            }, function(error){
                alert('Oups');
            });
    };

    this.next = function(){
        currentPageNum = currentPageNum + 1;
        this.rechercher(currentPageNum);
    };

    this.getThumbnail = function(character){
        return MarvelService.getThumbnail(character);
    };

}]);