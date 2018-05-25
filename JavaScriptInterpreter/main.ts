import fs from "fs";
import colors from "colors";
import Parser from "./parser/parser";
import Environment from "./environment/environment";

const filename = process.argv[2];

try {
  if (typeof filename !== 'string' || filename == "") {
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
