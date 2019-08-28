package demo.service;

public class Password {
    public static String check(String password) {
        String result = "";
        if (password.length() < 8) {
            result = result + "Password is not eight characters long. ";
            
        }
        String upperCase = "(.*[A-Z].*)";
        if (!password.matches(upperCase)) {
            result = result + "Password must contain at least one capital letter. ";
            
        }
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers)) {
            result = result + "Password must contain at least one number. ";
        }
        String specialChars = "(.*[ ! # @ $ % ].*)";
        if (!password.matches(specialChars)) {
            result = result + "Password must contain at least one special character like ! # @ $ %  ";
            
        }
        String space = "(.*[   ].*)";
        if (password.matches(space)) {
            result = result + "Password cannot contain a space. ";
        }
        if (password.startsWith("?")) {
            result = result + "Password cannot start with '?'. ";
            
        }
        if (password.startsWith("!")) {
            result = result + "Password cannot start with '!'. ";
            
        }
        return result;
    }
    
    private Password() {
    }
    
}
