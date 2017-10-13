/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

/**
 *
 * @author darth
 */
public class ArgumentsController {
    public static Boolean argumentsController(String ARGUMENT) {
        try {
            int VALUE = Integer.parseInt(ARGUMENT);
            return VALUE == 1;
        }
        catch (NumberFormatException E) {
            System.err.println("Argument" + ARGUMENT + " must be an integer.");
            System.exit(1);
            return false;
        }
    }
}
