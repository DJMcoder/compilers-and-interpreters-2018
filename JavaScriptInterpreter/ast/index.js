"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
var assignment_1 = __importDefault(require("./assignment"));
var binop_1 = __importDefault(require("./binop"));
var boolean_1 = __importDefault(require("./boolean"));
var display_1 = __importDefault(require("./display"));
var expression_1 = __importDefault(require("./expression"));
var if_1 = __importDefault(require("./if"));
var number_1 = __importDefault(require("./number"));
var program_1 = __importDefault(require("./program"));
var statement_1 = __importDefault(require("./statement"));
var text_1 = __importDefault(require("./text"));
var variable_1 = __importDefault(require("./variable"));
var while_1 = __importDefault(require("./while"));
exports.default = {
    Assignment: assignment_1.default,
    BinOp: binop_1.default,
    Boolean: boolean_1.default,
    Display: display_1.default,
    Expression: expression_1.default,
    If: if_1.default,
    Number: number_1.default,
    Program: program_1.default,
    Statement: statement_1.default,
    Text: text_1.default,
    Variable: variable_1.default,
    While: while_1.default
};
