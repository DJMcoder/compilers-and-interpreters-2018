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
var Num = /** @class */ (function (_super) {
    __extends(Num, _super);
    function Num(num) {
        var _this = _super.call(this) || this;
        _this.num = num;
        return _this;
    }
    Num.prototype.eval = function (env) {
        return this.num;
    };
    return Num;
}(expression_1.default));
exports.default = Num;
