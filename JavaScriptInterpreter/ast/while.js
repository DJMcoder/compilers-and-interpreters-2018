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
var While = /** @class */ (function (_super) {
    __extends(While, _super);
    function While(condition, program) {
        var _this = _super.call(this) || this;
        _this.condition = condition;
        _this.program = program;
        return _this;
    }
    While.prototype.exec = function (env) {
        while (this.condition.eval(env)) {
            this.program.exec(env);
        }
    };
    return While;
}(statement_1.default));
exports.default = While;
