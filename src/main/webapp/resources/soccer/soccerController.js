soccerNgApp.controller("soccerController",

  function($scope, $window,  soccerService) {

	soccerService.getSoccer(
            function(responseData) {
                $scope.populatePage(responseData);
            }
        );

        $scope.populatePage = function(iBean) {

            $scope.soccer = iBean;
            $scope.sort = {
                    column: '',
                    descending: false
                };
                $scope.changeSorting = function(column) {

                    var sort = $scope.sort;
         
                    if (sort.column == column) {
                        sort.descending = !sort.descending;
                    } else {
                        sort.column = column;
                        sort.descending = false;
                    }
                };
        };
	}
)