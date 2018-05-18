/**
 * Environment stores variables and the input stream.
 *
 * @version 3/16/18
 * @author David Melisso
 *
 */
class Environment
{
    /**
     * Creates an environment with a default scanner
     */
    constructor()
    {
      this.vars = new Map();
    }

    /**
     * Gets a list of variable identifiers stored in this environment (and not the parent)
     *
     * @return the list of variable identifiers as an array of Strings
     */
    getLocalVariables()
    {
        return vars.keys();
    }

    /**
     * Associates the given variable name with the given value
     *
     * @param variable
     *  the name of the variable
     * @param value
     *  the value to associate with the variable
     */
    setVariable(variable, value)
    {
        this.vars.set(variable, value);
    }

    /**
     * Returns the value associated with the given variable name
     *
     * @return the value associated with the given variable
     * @param variable
     *  the variable to get
     */
    getVariable(variable)
    {
        return this.vars.get(variable);
    }
}

module.exports = Environment;
