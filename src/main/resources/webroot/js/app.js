// app.js
// create angular app


var validationApp = angular.module('sendEmailApp', []);
console.log("!!!!!! HERE000000!!!!!!");
// create angular controller
validationApp.controller('mainController', [ '$scope', '$http', '$location', function($scope, $http, $location) {
    console.log("!!!!!! HERE!!!!!!");
    // function to submit the form after all validation has occurred
    $scope.submitForm = function () {

        //// check to make sure the form is completely valid
        //if (isValid) {
        //    alert('our form is amazing');
        //}
        console.log("!!!!!! SENDING!!!!!!");
        console.log("!!!!!! Host!!!!!!"  + $location.host()  );

        $http.post('http://' + $location.host() + ':8080/mail/send', {'from' : $scope.from,
                                                         'to' : $scope.to,
                                                    'subject' : $scope.subject,
                                                       'text' : $scope.message,
                                                       'name' : $scope.aname | 'unknown'}
            ).
            success(function (data, status, headers, config) {
                console.log(data);
                console.log(status);
                console.log(headers);
                console.log("SUCCESS !!!!");
            }).
            error(function (data, status, headers, config) {
                console.log("FAILURE !!!!");
            });

    };

}]);
