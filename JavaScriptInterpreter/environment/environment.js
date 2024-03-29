"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
/**
 * Environment stores variables and the input stream.
 *
 * @version 3/16/18
 * @author David Melisso
 *
 */
var Environment = /** @class */ (function () {
    /**
     * Creates an environment with a default scanner
     */
    function Environment() {
        this.vars = {};
    }
    /**
     * Associates the given variable name with the given value
     *
     * @param variable
     *  the name of the variable
     * @param value
     *  the value to associate with the variable
     */
    Environment.prototype.setVariable = function (variable, value) {
        this.vars[variable] = value;
    };
    /**
     * Returns the value associated with the given variable name
     *
     * @return the value associated with the given variable
     * @param variable
     *  the variable to get
     */
    Environment.prototype.getVariable = function (variable) {
        return this.vars[variable];
    };
    return Environment;
}());
exports.default = Environment;
