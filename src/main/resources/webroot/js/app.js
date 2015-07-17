
var validationApp = angular.module('sendEmailApp', []);

validationApp.controller('mainController', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    $scope.submitForm = function () {

        $http.post($location.protocol() + '://' + $location.host() + ':' + $location.port() + '/mail/send', {
                'from': $scope.from,
                'to': $scope.to,
                'subject': $scope.subject,
                'text': $scope.message,
                'name': $scope.name | 'unknown'
            }
        ).
            success(function (data, status, headers, config) {
                console.log(data);
                console.log(status);
                console.log(headers);
            }).
            error(function (data, status, headers, config) {
                console.log("FAILURE !!!!");
            });
    };

}]);
