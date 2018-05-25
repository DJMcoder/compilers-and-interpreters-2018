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
Object.defineProperty(exports, "__esModule", { value: true });
/**
 * ScanErrorException is a sub class of Exception and is thrown to indicate a
 * scanning error.  Usually, the scanning error is the result of an illegal
 * character in the input stream.  The error is also thrown when the expected
 * value of the character stream does not match the actual value.
 * @author Mr. Page
 * @version 062014
 *
 */
var ScanErrorException = /** @class */ (function (_super) {
    __extends(ScanErrorException, _super);
    /**
       * Constructor for ScanErrorObjects that includes a reason for the error.
       * @param reason - the reason for the error
       */
    function ScanErrorException(reason) {
        return _super.call(this, reason) || this;
    }
    return ScanErrorException;
}(Error));
exports.default = ScanErrorException;
