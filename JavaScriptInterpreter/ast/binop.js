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
var expression_1 = __importDefault(require("./expression"));
function trunc(v) {
    v = +v;
    if (!isFinite(v))
        return v;
    return (v - v % 1) || (v < 0 ? -0 : v === 0 ? v : 0);
}
;
var BinOp = /** @class */ (function (_super) {
    __extends(BinOp, _super);
    function BinOp(exp1, op, exp2) {
        var _this = _super.call(this) || this;
        _this.exp1 = exp1;
        _this.op = op;
        _this.exp2 = exp2;
        _this.single = (typeof op === 'undefined' || typeof exp2 === 'undefined');
        return _this;
    }
    BinOp.prototype.eval = function (env) {
        if (this.single)
            return this.exp1.eval(env);
        var num1 = this.exp1.eval(env);
        var num2 = this.exp2.eval(env);
        //console.log('exp2 =', this.exp2)
        //console.log('num2 =', num2)
        if (this.op == "<")
            return true == (num1 < num2);
        if (this.op == ">")
            return true == (num1 > num2);
        if (this.op == ">=")
            return true == (num1 >= num2);
        if (this.op == "<=")
            return true == (num1 <= num2);
        if (this.op == "<>")
            return true == (num1 != num2);
        if (this.op == "=")
            return true == (num1 == num2);
        if (this.op == "*")
            return trunc(trunc(Number(num1)) * trunc(Number(num2)));
        if (this.op == "/")
            return trunc(trunc(Number(num1)) / trunc(Number(num2)));
        if (this.op == "+")
            return trunc(trunc(Number(num1)) + trunc(Number(num2)));
        if (this.op == "-")
            return trunc(trunc(Number(num1)) - trunc(Number(num2)));
        throw new Error("Invalid operator " + this.op);
    };
    return BinOp;
}(expression_1.default));
exports.default = BinOp;
