var args = process.argv.slice(2);

var gplay = require('google-play-scraper');

gplay.reviews({
  appId: args[0],
  page: 0,
  sort: gplay.sort.RATING
}).then(console.log, console.log);