angular.module('rateShareApp').factory("User", ["$firebaseObject",
  function($firebaseObject) {
    // create a reference to the database where we will store our data
    var ref = firebase.database().ref("users/");

    return $firebaseObject(ref);
  }
]);