angular.module('rateShareApp').directive('googleAd', ['$timeout', function($timeout) {
    
    return {
      restrict: 'A',
      link: function(scope, element, attr) {
        return $timeout(function() {
          var adsbygoogle, html, rand;
          rand = Math.random();
          html = '<ins class="adsbygoogle" style="display:inline-block;width:300px;height:250px" data-ad-client="ca-pub-4447442076400719" data-adtest="on"  data-ad-slot="1234567890"  data-ad-region="page-' + rand + '"></ins>';
          $(element).append(html);
          return (adsbygoogle = window.adsbygoogle || []).push({});
        });
      }
    };
  }
]);