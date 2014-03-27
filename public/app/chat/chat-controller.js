angular.module('marvelapp.chat', []).controller('ChatCtrl', ['$http', '$scope', function ($http, $scope) {

    var chat = this;

    this.messages = new Array();

    this.sendMessage = function(){
        $http.post('/chat', this.message).success(function(){
            chat.message.text = null;
        });
    };

    var source = new EventSource("/chat/feeds");
    source.addEventListener("message", function(event){
        $scope.$apply(function(){
            chat.messages.push(JSON.parse(event.data)));
        });
    });

}]);