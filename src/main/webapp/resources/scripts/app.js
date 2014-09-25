var soccerNgApp = angular.module('soccerNgApp', ['ngRoute', 'ngAnimate']);

// ROUTING ===============================================
// set our routing for this application
// each route will pull in a different controller
soccerNgApp.config(function($routeProvider) {

    $routeProvider

    	// home page
    	.when('/', {
    		templateUrl: 'resources/soccer/soccer.html',
            controller: 'soccerController'
    	});

});


// CONTROLLERS ============================================
// home page controller
//animateApp.controller('soccerController', function($scope) {
 //   $scope.pageClass = 'soccer';
//});

