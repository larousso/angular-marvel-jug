angular.module('marvelapp.marvel.service', [])
.factory('MarvelService', ['$http', '$q', function($http, $q) {

    var nbParPages = 15;
    var currentPage = 0;

    function removePrefix(url){
        var prefix = "http://gateway.marvel.com/v1/public";
        return url.substring(prefix.length, url.length)
    }

    var service = {
        sizes : {
            small : 'portrait_small',
            medium : 'portrait_medium',
            xlarge : 'portrait_xlarge',
            fantastic: 'portrait_fantastic',
            uncanny : 'portrait_uncanny',
            incredible : 'portrait_incredible'
        },
        findSuperHeroes : function(name, pageNum) {
            var offset = (pageNum-1) * nbParPages;
            var url = '/marvel/characters?offset='+offset+'&limit='+nbParPages;
            if(name){
                url = url + '&name='+name;
            }

            console.log('GET '+url);
            var deferred = $q.defer();
            $http.get(url).success(function(response){
                var resultat = {
                    currentPage : pageNum,
                    nbParPages : nbParPages
                };

                if(response && response.data){
                    var nbPages = response.data.total / nbParPages + 0.51;
                    resultat.total = response.data.total;
                    resultat.nbPages = Math.round(nbPages);
                    resultat.data = response.data.results;
                }
                deferred.resolve(resultat);

            }).error(function(data){
                deferred.reject( data );
            });
            return deferred.promise;
        },
        getThumbnail : function(character, size){
            if(character && character.thumbnail){
                return character.thumbnail.path+'/'+size+'.'+character.thumbnail.extension;
            }
        },
        loadCharacter : function(id) {
           var url = '/marvel/characters/'+id;

           var deferred = $q.defer();
           $http.get(url).success(function(response){
                var result = {};
                if(response && response.data && response.data.results && response.data.results.length>0){
                    result = response.data.results[0];
                }
                deferred.resolve(result);
           }).error(function(error){
                deferred.reject(error);
           });
           return deferred.promise;
        },
        loadComics : function(character){
            var comicsUrl = '/marvel'+removePrefix(character.comics.collectionURI);
            var deferred = $q.defer();
            $http.get(comicsUrl).success(function(response){
                var comics = new Array();
                if(response && response.data && response.data.results){
                   comics = response.data.results;
                }
                deferred.resolve(comics);
            }).error(function(error){
                deferred.reject();
            });
            return deferred.promise;
        }
    };
    return service;
}]);