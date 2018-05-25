import Statement from "./statement";
import Expression from "./expression";
import Program from "./program";
import Environment from "../environment/environment";

export default class If extends Statement {
  condition: Expression;
  program: Program;
  else: Program | void;

  constructor(condition: Expression, program: Program, els: Program | void = undefined) {
    super();
    this.condition = condition;
    this.program = program;
    this.else = els;
  }

  exec(env: Environment) {
    if (this.condition.eval(env)) {
      this.program.exec(env);
    }
    else if (this.else) {
      this.else.exec(env);
    }
  }
}
