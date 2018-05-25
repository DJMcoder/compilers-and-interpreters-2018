import Environment from "../environment/environment";
import Expression from "./expression";

export default class Num extends Expression {
  num: number;

  constructor(num: number) {
    super();
    this.num = num;
  }

  eval(env: Environment) {
    return this.num;
  }
}
