/**
 * Environment stores variables and the input stream.
 *
 * @version 3/16/18
 * @author David Melisso
 *
 */
export default class Environment
{
    vars: { [key: string]: number | boolean };
    /**
     * Creates an environment with a default scanner
     */
    constructor()
    {
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
    setVariable(variable: string, value: number | boolean): void
    {
        this.vars[variable] = value;
    }

    /**
     * Returns the value associated with the given variable name
     *
     * @return the value associated with the given variable
     * @param variable
     *  the variable to get
     */
    getVariable(variable: string): number | boolean
    {
        return this.vars[variable];
    }
}
