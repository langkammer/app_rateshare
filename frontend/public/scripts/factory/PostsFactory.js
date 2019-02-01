angular.module('rateShareApp').factory("Postagens", ["$firebaseArray",
  function($firebaseArray) {
    // create a reference to the database where we will store our data
    var ref = firebase.database().ref("posts");

    return $firebaseArray(ref);
  }
]);