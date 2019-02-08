const functions = require('firebase-functions');

const admin = require('firebase-admin');


                                                                    
admin.initializeApp();

function buildHtmlWithPost (key,post,img) {

  console.log("IDDDDD : " , post)
  const title = post.titulo;

  var head = {
    title: title,
    meta: [
      // This may not be a valid combination of tags for the services you need to support;
      // they're only shown here as an example.
      { property: 'og:url', content: 'https://rateshareteste.firebaseapp.com/ver-postagem?key=' + key   },
      { property: 'og:locale', content: "pt_BR" },
      { property: 'og:title', content: title },
      { property: 'og:description', content: post.descricao },
      { property: 'og:image', content: img },
      { property: 'twitter:title', content: title },
      { property: 'twitter:description', content: post.descricao },
      { property: 'twitter:image', content: img }
    ],
    link: [
      { rel: 'icon', 'href': 'https://rateshareteste.firebaseapp.com/assets/logo_2_simple.png' },
    ],
  };

  // Is this crazy object-to-HTML parsing necessary? Of course not.
  // We could have just declared one long HTML string. But alas
  var string = '<!DOCTYPE html><head>';
  Object.keys(head).forEach(key => {
    if (typeof head[key] === 'string')
      string += '<' + key + '>' + head[key] + '</' + key + '>';
    else if (Array.isArray(head[key])) {
      for (const obj of head[key]) {
        string += '<' + key;
        Object.keys(obj).forEach(key2 => {
          string += ' ' + key2 + '="' + obj[key2] + '"';
        });
        string += '>';
      }
    }
  });
  string += '</head><body>';
  string += '<script>window.location="https://rateshareteste.firebaseapp.com/ver-postagem?key=' + key + '";</script>';
  string += '</body></html>';

  return string;
}





exports.posts = functions.https.onRequest((req, res) => {
  const path = req.path.split('/');

  const postId = path;
  console.log("POST [2]", postId[1] )
  console.log("POST [1]", path)

  const key = postId[2];

  admin.database().ref("/posts/"+key).once("value").then(
    function(snapshot) {
        const post = snapshot.val();
        if (!post) {
          res.status(404).send('Post not found :(');
        }else{
          const url =   "https://firebasestorage.googleapis.com/v0/b/rateshareteste.appspot.com/o/posts%2F" + snapshot.key + "?alt=media"
          const htmlString = buildHtmlWithPost(snapshot.key,post,url);
          res.status(200).end(htmlString);
        }
        return;
    }
   
   ).catch(error => { 
      res.status(500).end(error);
    }
   );
});


