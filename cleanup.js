/**
 * This will clean up the HTML file by removing some useless tags and mobipocket tags
 */
var fs = require('fs')
fs.readFile("output.html", 'utf8', function(err, data) {
    if (err) {
        return console.log(err);
    }
    var result = data.replace(/&nbsp;/g, ' ');
    result = result.replace(/<mbp:pagebreak\/>/g, '');
    result = result.replace(/<mbp:pagebreak \/>/g, '');
    result = result.replace(/<mbp:frameset>/g, '');
    result = result.replace(/<dom>/g, '');
    result = result.replace(/<\/dom>/g, '');

    fs.writeFile("output.html", result, 'utf8', function(err) {
        if (err) return console.log(err);
    });
});
