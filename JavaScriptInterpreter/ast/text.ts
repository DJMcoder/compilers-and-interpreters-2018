import Expression from "./expression";
import Environment from "../environment/environment";

export default class Text extends Expression {
  text: string;

  constructor(text: string) {
    super();
    this.text = text;
  }

  eval(env: Environment): any {
    return this.text;
  }
}
