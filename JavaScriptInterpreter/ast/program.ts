import Statement from "./statement";
import Environment from "../environment/environment";

export default class Program extends Statement {
  stmts: Statement[];

  constructor(stmts: Statement[]) {
    super();
    this.stmts = stmts;
  }

  exec(env: Environment) {
    for (var stmt of this.stmts) {
      stmt.exec(env);
    }

  }
}