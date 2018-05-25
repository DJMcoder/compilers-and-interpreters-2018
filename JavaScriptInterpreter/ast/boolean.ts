import Expression from './expression';
import Environment from '../environment/environment';

export default class Bool extends Expression {
  bool: boolean;
  constructor(bool: boolean) {
    super();
    this.bool = bool;
  }

  eval(env: Environment) {
    return this.bool;
  }
}
