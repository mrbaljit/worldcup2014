soccerNgApp.factory('soccerService',function($http){
    var soccerService = {};

    soccerService.getSoccer = function (response, status) {
    	
        return $http.get("getSoccer.json").success(response).error(status);
    };

    return soccerService;
});
