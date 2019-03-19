angular.module('rateShareApp').factory("Categorias", ["$firebaseArray",
  function($firebaseArray) {
    // create a reference to the database where we will store our data
    var ref = firebase.database().ref("categorias");

    return $firebaseArray(ref);
  }
]);