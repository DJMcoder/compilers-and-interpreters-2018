import Expression from "./expression";
import Environment from "../environment/environment";

export default class Variable extends Expression {
  name: string;

  constructor(name: string) {
    super();
    this.name = name;
  }

  eval(env: Environment) {
    return env.getVariable(this.name);
  }
}
