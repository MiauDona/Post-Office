package miau.dona;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PostOffice {
    private HashMap<User, String> users = new HashMap(); // Introducir el username del User
    private List<Package> packages = new ArrayList();
    private Queue<User> clientQueue = new ArrayDeque();
    private Scanner scanner = new Scanner(System.in);

    private boolean adminManagement = false;
    
    // Pide usuario y contraseña
    public boolean login() {
        System.out.println("Please enter your username (if you are not logged in, it will create you a new user): ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        
        return tryLogin(username, password) || login(); // Intenta logearse y si devuelve false, vuelve a pedir credenciales
    }
    
    // Si encuentra el usuario intenta logearse con las credenciales, 
    // si no, crea el usuario con las credenciales nuevas
    private boolean tryLogin(String username, String password) {
        boolean userFound = false;
        
        for (User user : users.keySet()) {
            if (user.getUsername().equals(username)) {
                userFound = true;
                    
                if (users.get(user).equals(password) || password.isEmpty() || password.equals(" ")) {
                    System.out.println("Successfully logged in");
                    return true;
                } else if (users.get(user).equals(password) && user.getUsername().equals(username) && user.isAdmin() || user.getUsername().equals("admin") && password.equals("admin")) {
                    System.out.println("Successfully logged in as admin");
                    adminManagement = true;
                    return true;
                }
            }
        }
        
         if (!userFound) {
                System.out.println("New user, didn't exist before");
                createUser(username, password);
                return true;
        }
        
        System.out.println("Wrong password");
        return false;
    }
    
    // Crea usuario si no existe
    private boolean createUser(String username, String password) {
        for (User user : users.keySet()) {
            if (user.getUsername().equals(username)) {
                System.out.println("That user already exists, cannot create user");
                return true;
            }
        }
        
        users.put(new User(username, false), password);
        return false;
    }
    
    public void showMenu() {
        int option = -1;
        if(adminManagement) {
            showAdminMenu();
            option = selectOption();
            switch(option) {
                case 0:
                    logOutOption();
                    break;
                case 1:
                    option1();
                    break;
                case 2:
                    option2();
                    break;
                case 3:
                    option3();
                    break;
                default:
                    System.out.println("Invalid option");
            }
            showMenu();
        } else {
            showClientMenu();
            option = selectOption();
            switch(option) {
                case 0:
                    logOutOption();
                    break;
                case 4:
                    option4();
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
            showMenu();
        }
    }
    
    private void showAdminMenu() {
        System.out.println("Admin Menu");
        System.out.println("Option 0 Logout");
        System.out.println("Option 1");
        System.out.println("Option 2");
        System.out.println("Option 3");
    }
    
    private void showClientMenu() {
        System.out.println("Client Menu");
        System.out.println("Option 0 Logout");
        System.out.println("Option 4");
    }
    
    private void logOutOption() {
        System.out.println("Option 0");
        System.out.println("You are logged out");
        login();
    }
     
    private int selectOption() {
        System.out.println("Select an option");
        String option = scanner.nextLine();
        
        return Integer.parseInt(option);
    }
    
    private void option1() {
        
        System.out.println("Let's add a package");
        System.out.println("Introduce the next data");
        System.out.println("Weight: ");
        String sWeight = scanner.nextLine();

        System.out.println("Height: ");
        String sHeight = scanner.nextLine();

        System.out.println("Width: "); 
        String sWidth = scanner.nextLine();

        System.out.println("Is the packet sent? If not you can leave this alone");
        String answerIsSent = scanner.nextLine();

        double weight = Double.parseDouble(sWeight);
        double height = Double.parseDouble(sHeight);
        double width = Double.parseDouble(sWidth);
        
        Package op1package = new Package(weight, height, width);

        if (answerIsSent.equalsIgnoreCase("Yes") || answerIsSent.equalsIgnoreCase("y")) {
            System.out.println("Who sent it?");
            String stringSender = scanner.nextLine();

            System.out.println("Who is the receiver?");
            String stringReceiver = scanner.nextLine();

            System.out.println("Where is sent?");
            String address = scanner.nextLine();

            
            System.out.println("When is the packet arriving? Date format(YYYY-MM-DD)");
            String arrival = scanner.nextLine();
            
            Date shippingDate = askShippingDate();
            if (lookForUser(stringSender) != null && lookForUser(stringReceiver) != null) {
                User sender = new User(stringSender, false);
                User receiver = new User(stringReceiver, false);
                op1package.sendPackage(sender, receiver, address, shippingDate);
            } else {
                System.out.println("Not valid username of sender or receiver, packet cancelled");
            }
            
            
            
            
        }
    }
    
    private Date askShippingDate() {
        System.out.println("Enter shipping date");
        String userDate = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateFormat.parse(userDate);
            System.out.println("Funcionó");
            return dateFormat.parse(userDate);
        } catch (ParseException ignored) {
            System.out.println("Invalid format, try again with something like 31/12/2025");
            return askShippingDate();
        }
    }
    
    private User lookForUser(String username) {
        for (User user : users.keySet()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        System.out.println("User not found");
        return null;
    }
    
    
    
    private void option2() {
        System.out.println("Remove a package");
    }
    
    private void option3() {
        System.out.println("Call a client to pickup or to send a package");
    }
    
    private void option4() {
        System.out.println("Send or receive a package");
    }
}
