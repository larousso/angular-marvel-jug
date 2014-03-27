angular-marvel
==============

Projet de démo d'angular js avec les apis de marvel.


Répository utilisé pendant le jug de niort [http://www.poitoucharentesjug.org/](http://www.poitoucharentesjug.org/).

Démo visible ici :
[http://angular-marvel-jug.herokuapp.com/](http://angular-marvel-jug.herokuapp.com/)


Ce projet est écrit avec les frameworks [play 2](http://www.playframework.com/), [angularjs](http://angularjs.org), [twitter bootstrap](http://getbootstrap.com/) et [angular ui bootstrap](http://angular-ui.github.io/bootstrap/).

Ce projet consomme les [api de marvel](http://developer.marvel.com/).

##Concepts utilisés :
* organisation en modules
* injection de dépendances
* appels REST
* router
* directives
* utilisation de directives existantes
* realtime


## Détails

Les api de marvel nécessitent une authentification.
L'application play permet de wrapper les services marvel et d'ajouter l'authentification requise.
Les services marvel sont accessibles sur /marvel/**.

On aura donc :

    http//localhost:9000/marvel/characters/1009718 <=> http://gateway.marvel.com/v1/public/characters/1009718?apikey=f310c9bc6a47ef6a242ea0686616bd54

L'application play expose également deux services :

* POST /chat : permet de poster un message de chat
* GET /chat/feeds : permet d'écouter les messages en utilisant le [server-sent events](http://en.wikipedia.org/wiki/Server-sent_events).


##Lancer l'application :

Télécharger et installer play => [http://www.playframework.com/documentation/2.2.x/Installing](http://www.playframework.com/documentation/2.2.x/Installing)

    git clone https://github.com/larousso/angular-marvel.git
    cd angular-marvel
    play run

