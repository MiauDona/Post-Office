package miau.dona;

/*
El flujo de la aplicación se basa en:

1. Login
   Puedes introducir cualquier nombre de usuario con o sin contraseña, que se registrará en el hashmap de usuarios junto a la contraseña.
   Si el usuario no tiene contraseña no podrá recoger paquetes.
   Si un usuario es el receptor de un paquete, se le añadirá esa contraseña a su listado de contraseñas válidas para entrar
   Para iniciar sesion como admin, escribe las credenciales usuario: "root", contraseña: "root"

2. Opciones
   1. Como admin puedes crear un paquete que puede haber sido enviado por alguien que "no se ha presentado fisicamente a la oficina" o crearlo
      "para dejarlo en el almacén"
   2. Puedes eliminar paquetes que estén en la oficina
   3. Llama al siguiente cliente a la cola. Si el root no lo llama, el cliente no podrá realizar la acción 4. Si lo llama y su turno se pasa,
      tampoco (hacer más de 1 llamada)
   4. Enviar o recibir un paquete siendo cliente (las demas opciones solo están disponibles para el admin)
   0. Logout

3. Extras
   La ID de los paquetes comienzan desde 1, siendo este el primero que se crea, tanto al ser creado para enviar como al ser creado por el root.
   La aplicación es escalable para añadir varios usuarios administradores, búsquedas por id de usuario

4. Para salir de la aplicación
   Detenla con Ctrl + C ó pulsando el botón de detener del IDE.
*/

public class Main {
    public static void main(String[] args) {
        PostOffice po = new PostOffice();

        while (true) {
            po.login();
            po.showMenu();
        }


    }
}