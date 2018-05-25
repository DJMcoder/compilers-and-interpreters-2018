import Statement from "./statement";
import Expression from "./expression";
import Program from "./program";
import Environment from "../environment/environment";

export default class While extends Statement {
  condition: Expression;
  program: Program;

  constructor(condition: Expression, program: Program) {
    super();
    this.condition = condition;
    this.program = program;
  }

  exec(env: Environment) {
    while (this.condition.eval(env)) {
      this.program.exec(env);
    }
  }
}
