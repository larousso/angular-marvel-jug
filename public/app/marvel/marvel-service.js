angular.module('marvellapp.marvel.service', [])
.factory('MarvelService', ['$http', '$q', function($http, $q) {

    var nbParPages = 15;
    var currentPage = 0;

    return {
        findSuperHeroes : function(name, pageNum) {
            var offset = pageNum * nbParPages;
            var url = '/marvel/characters?offset='+offset+'&limit='+nbParPages;
            if(name){
                url = url + '&name='+name;
            }
            var deferred = $q.defer();
            $http.get(url).success(function(response){
                var resultat = {
                    currentPage : pageNum
                };

                if(response && response.data){
                    resultat.total = response.data.total;
                    resultat.nbPages = Math.round(response.data.total / nbParPages);
                    resultat.data = response.data.results;
                }
                deferred.resolve(resultat);

            }).error(function(data){
                deferred.reject( data );
            });
            return deferred.promise;
        },
        getThumbnail : function(character){
            return character.thumbnail.path+'/portrait_small.'+character.thumbnail.extension;
        }
    };
}]);