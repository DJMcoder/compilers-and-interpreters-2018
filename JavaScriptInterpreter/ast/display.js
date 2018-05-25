"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
var statement_1 = __importDefault(require("./statement"));
var readline_sync_1 = __importDefault(require("readline-sync"));
var Display = /** @class */ (function (_super) {
    __extends(Display, _super);
    function Display(exp, readToVar) {
        var _this = _super.call(this) || this;
        _this.exp = exp;
        _this.readToVar = readToVar;
        return _this;
    }
    Display.prototype.exec = function (env) {
        console.log(this.exp.eval(env));
        if (this.readToVar) {
            var toRead = readline_sync_1.default.question('');
            if (toRead == 'true') {
                env.setVariable(this.readToVar, true);
                return;
            }
            if (toRead == 'false') {
                env.setVariable(this.readToVar, false);
                return;
            }
            var toReadInt = parseInt(toRead);
            if (isNaN(toReadInt)) {
                throw new TypeError('Inputs must be integers; could not read input \'' + toRead + '\'');
            }
            env.setVariable(this.readToVar, toReadInt);
        }
    };
    return Display;
}(statement_1.default));
exports.default = Display;
