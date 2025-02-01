package miau.dona;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PostOffice {
    // Declaracion de las listas
    private final HashMap<User, List<String>> users = new HashMap<>(); // Introducir el username del User
    private final List<Package> packages = new ArrayList<>();
    private final Queue<User> clientQueue = new ArrayDeque<>();
    private final Scanner scanner = new Scanner(System.in);

    // Atributos para el menú
    private boolean adminManagement = false;
    private boolean receivable = false;

    // Creación del usuario root
    private final User root = addUserToHashMap(new User("root", true), "root");

    // Usuarios para comprobaciones
    private User nextUser = root; // Usuario root para que no sea igual que loginUser
    private User loginUser = null; // Usuario con el que se está logeado

    // Pide usuario y contraseña
    public boolean login() {
        System.out.println("Please enter your username (if you are not logged in, it will create you a new user): ");
        String username = scanner.nextLine();

        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();

        return tryLogin(username, password) || login(); // Intenta logearse y si devuelve false, vuelve a pedir credenciales
    }
    
    // Si encuentra el usuario intenta logearse con las credenciales, si no, crea el usuario con las credenciales nuevas
    private boolean tryLogin(String username, String password) {
        loginUser = getOrCreateUser(username, password);

        // Contraseñas del usuario
        List<String> userPasswords = users.get(loginUser);

        if (userPasswords.contains(password) || password.isEmpty()) {
            if (loginUser.isAdmin()) {
                System.out.println("Successfully logged in as admin");
                adminManagement = true;
            } else {
                System.out.println("Successfully logged in");
            }

            if (userPasswords.contains(password) && password != null && !password.isEmpty()) {
                receivable = true;
            }

            return true;
        }

        System.out.println("Wrong password");
        return false;
    }
    
    // Crea usuario si no existe
    private User getOrCreateUser(String username, String password) {
        for (User user : users.keySet()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        System.out.println("New user, didn't exist before");
        User user = new User(username, false);
        addUserToHashMap(user, password);
        return user;
    }

    // Muestra un menú según seas admin o usuario
    public void showMenu() {
        if (adminManagement) {
            System.out.println("""
                Admin Menu
                Option 0 Logout
                Option 1
                Option 2
                Option 3
                """);
        } else {
            System.out.println("""
                Client Menu
                Option 0 Logout
                Option 4
                """);
        }

        System.out.println("Select an option");
        int option = Integer.parseInt(scanner.nextLine());

        switch(option) {
            case 0:
                System.out.println("You are logged out");
                adminManagement = false;
                receivable = false;
                break;
            case 1:
                if (adminManagement) option1();
                break;
            case 2:
                if (adminManagement) option2();
                break;
            case 3:
                if (adminManagement) option3();
                break;
            case 4:
                if (!adminManagement) option4();
                break;
            default:
                System.out.println("Invalid option");
                showMenu();
                break;
        }
    }

    // Crea un paquete y lo añade a la lista de paquetes
    private void option1() {
        System.out.println("Let's add a package");
        System.out.println("Introduce the next data");
        System.out.println("Weight: ");
        String sWeight = scanner.nextLine();

        System.out.println("Height: ");
        String sHeight = scanner.nextLine();

        System.out.println("Width: "); 
        String sWidth = scanner.nextLine();

        System.out.println("Is the packet been sent? (y/n) If not you can leave this alone");
        String answerIsSent = scanner.nextLine();

        double weight = Double.parseDouble(sWeight);
        double height = Double.parseDouble(sHeight);
        double width = Double.parseDouble(sWidth);
        
        Package newPackage = new Package(weight, height, width);
        packages.add(newPackage);
        System.out.println("New package created with this id: " + newPackage.getIdPackage());


        // Si el paquete se ha enviado de forma ficticia el usuario rellenara estos datos
        if (answerIsSent.equalsIgnoreCase("Yes") || answerIsSent.equalsIgnoreCase("y")) {
            System.out.println("Who sent it?");
            String stringSender = scanner.nextLine();

            System.out.println("Who is the receiver?");
            String stringReceiver = scanner.nextLine();

            System.out.println("Where is sent?");
            String address = scanner.nextLine();
            
            Date shippingDate = askShippingDate();

            // Si alguno de los dos usuarios no existe, se crean
            User sender = lookForUser(stringSender) == null ? new User(stringSender, false) : lookForUser(stringSender);
            User receiver = lookForUser(stringReceiver) == null ? new User(stringReceiver, false) : lookForUser(stringReceiver);
            newPackage.sendPackage(sender, receiver, address, shippingDate);
        }

        showMenu();
    }

    // Pregunta la fecha de entrega prevista
    private Date askShippingDate() {
        System.out.println("Enter shipping date");
        String userDate = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            return dateFormat.parse(userDate);
        } catch (ParseException ignored) {
            System.out.println("Invalid format, try again with something like 31/12/2025");
            return askShippingDate();
        }
    }

    // Busca a un usuario a través de su nombre
    private User lookForUser(String username) {
        for (User user : users.keySet()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    // Eliminar un paquete de la lista de paquetes
    private void option2() {
        System.out.println("Which is the ID of the package you want to remove?");
        String sId = scanner.nextLine();
        Long id = Long.parseLong(sId);

        if (getPackageById(id) != null) {
            packages.remove(getPackageById(id));
            System.out.println("Package removed");
        } else {
            System.out.println("Not a valid package");
        }

        showMenu();
    }

    // Llama a un cliente para que pueda hacer alguna acción en su turno
    private void option3() {
        System.out.println("Call a client to pickup or to send a package");
        nextUser = clientQueue.poll();
        System.out.println("Next client: " + nextUser);

        showMenu();
    }

    // Opcion 4: Permite al usuario recibir o enviar paquete si es su turno
    private void option4() {
        System.out.println("You chose to send or to receive a package. In case you logged in without password, you'll only be able to send");

        if (nextUser == loginUser) {
            if (receivable) {
                System.out.println("What do you want to do? (Receive/Send)");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("Receive")) {
                    receivePackage();
                } else if (command.equalsIgnoreCase("Send")) {
                    sendPackage();
                }
            } else {
                sendPackage();
            }
        } else if (clientQueue.contains(loginUser)) {
            System.out.println("Keep waiting, you are not the next");
        } else {
            clientQueue.add(loginUser);
            System.out.println("Added to the waiting list");
        }

        showMenu();
    }

    // Hace la accion de recibir un paquete
    private void receivePackage() {
        System.out.println("Introduce the packet id you want to receive");
        String id = scanner.nextLine();
        Package pack = getPackageById(Long.parseLong(id));

        if (pack == null) {
            System.out.println("Package not found");
        } else {
            pack.setReceiver(loginUser);
            pack.setPickupDate(askShippingDate());
        }
    }

    // Envia un paquete a un usuario y settea al receptor
    private void sendPackage() {
        System.out.println("Is the packet existing? (yes/no)");
        String answer = scanner.nextLine();

        // Si user dice que existe
        if (answer.equalsIgnoreCase("yes")) {
            System.out.println("Which ID has the package?");
            String sid = scanner.nextLine();
            Long id = Long.parseLong(sid);

            // Si existe de verdad el paquete settea el receptor y el emisor
            Package pack = getPackageById(id);

            if (pack != null) {
                pack.setSender(loginUser);

                // Si el receptor no existe, le crea las credenciales
                setReceiver(pack);
            } else {
                System.out.println("Package not found");
            }

        } else if (answer.equalsIgnoreCase("no")) {
            System.out.println("What are its dimensions?");

            System.out.println("Weight: ");
            String sWeight = scanner.nextLine();

            System.out.println("Height: ");
            String sHeight = scanner.nextLine();

            System.out.println("Width: ");
            String sWidth = scanner.nextLine();

            // Crea el paquete y le settea el receptor y el emisor
            Package newPackage = new Package(Double.parseDouble(sWeight),Double.parseDouble(sHeight), Double.parseDouble(sWidth));
            newPackage.setSender(loginUser);
            System.out.println("New package created with this id: " + newPackage.getIdPackage());
            setReceiver(newPackage);
            packages.add(newPackage);
        }
    }

    // Settea el receptor y le crea las credenciales si no las tiene
    private void setReceiver(Package pack) {
        // Pregunta para quien es el paquete
        System.out.println("Who is this packet for?");
        String receiverName = scanner.nextLine();

        User receiver = lookForUser(receiverName);
        // Si el receptor existe
        if (receiver != null) {
            // Settea el receptor como receptor
            pack.setReceiver(receiver);

            // Añade la contraseña como password
            addPassword(receiver, pack.getIdPackage().toString());
        } else {
            // Crea el usuario y lo añade al hashmap
            User newUser = new User(receiverName, false);
            addUserToHashMap(newUser, pack.getIdPackage().toString());
        }
    }

    // Busca un package por la id
    private Package getPackageById(Long id) {
        for (Package p : packages) {
            if (p.getIdPackage().equals(id)) {
                return p;
            }
        }

        return null;
    }

    // Le añade una contraseña a un usuario
    public void addPassword(User user, String newPassword) {
        if (users.containsKey(user)) {
            List<String> passwords = users.get(user);
            passwords.add(newPassword);
        } else {
            System.out.println("User not found.");
        }
    }

    // Añade un usuario junto a su primera contraseña
    public User addUserToHashMap(User user, String password) {
        if (users.get(user) != null) {
            return user;
        }

        List<String> passwords = new ArrayList<>();
        passwords.add(password);

        users.put(user, passwords);
        return user;
    }
}
