"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
var fs_1 = __importDefault(require("fs"));
var colors_1 = __importDefault(require("colors"));
var parser_1 = __importDefault(require("./parser/parser"));
var environment_1 = __importDefault(require("./environment/environment"));
var filename = process.argv[2];
try {
    if (typeof filename !== 'string' || filename == "") {
        throw new Error('File input not given');
    }
    if (!fs_1.default.existsSync(filename)) {
        throw new Error('File Not Found: Could not locate the file ' + __dirname + '/' + filename);
    }
    new parser_1.default(filename)
        .parseProgram()
        .exec(new environment_1.default());
}
catch (e) {
    console.error(colors_1.default.red(e.toString()));
    console.error(colors_1.default.red('Fatal error; terminating interpretation...'));
    process.exit(1);
}
