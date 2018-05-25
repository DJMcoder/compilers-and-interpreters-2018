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
var If = /** @class */ (function (_super) {
    __extends(If, _super);
    function If(condition, program, els) {
        if (els === void 0) { els = undefined; }
        var _this = _super.call(this) || this;
        _this.condition = condition;
        _this.program = program;
        _this.else = els;
        return _this;
    }
    If.prototype.exec = function (env) {
        if (this.condition.eval(env)) {
            this.program.exec(env);
        }
        else if (this.else) {
            this.else.exec(env);
        }
    };
    return If;
}(statement_1.default));
exports.default = If;
