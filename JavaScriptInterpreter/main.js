const Parser = require('./parser/parser');
const Environment = require('./environment/environment');
const colors = require('colors/safe');
const fs = require('fs');
const path = require('path');

const filename = process.argv[2];

try {
  if (typeof filename == 'undefined') {
    throw new Error('File input not given');
  }
  if (!fs.existsSync(filename)) {
    throw new Error('File Not Found: Could not locate the file ' + __dirname + '/' + filename);
  }
  new Parser(filename)
  .parseProgram()
  .exec(new Environment());

}
catch (e) {
  console.error(colors.red(e.toString()));
  console.error(colors.red('Fatal error; terminating interpretation...'))
  process.exit(1);
}
