package web;

import datos.ClienteDAO;
import datos.IClienteDAO;
import datos.resultados.Resultado;
import dominio.Cliente;
import java.io.*;
import java.util.*;
import javax.json.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {

    private final JsonObjectBuilder ob = Json.createObjectBuilder();
    private final JsonArrayBuilder ab = Json.createArrayBuilder();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        // obtenemos los parametros para crear un nuevo cliente
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String saldo = request.getParameter("saldo");
        //crear una nueva lista de errores sin elementos
        List<String> errores = new ArrayList<>();
        // verifica cada uno de los campos y agrega un error en caso de faltar propiedad
        if (nombre == null || nombre.equals("")) {
            errores.add("El campo 'nombre' es obligatorio");
        }
        if (apellido == null || apellido.equals("")) {
            errores.add("El campo 'apellido' es obligatorio");
        }
        if (email == null || email.equals("")) {
            errores.add("El campo 'email' es obligatorio");
        }
        if (telefono == null || telefono.equals("")) {
            errores.add("El campo 'telefono' es obligatorio");
        }
        // si errores no esta vacio se envia de respuesta un error
        if (!errores.isEmpty()) {
            this.enviarError(response, errores);
        } else {
            // si el saldo viene vacio o no se usa un constructor deiferente
            Cliente cliente;
            if (saldo == null) {
                cliente = new Cliente(nombre, apellido, email, telefono);
            } else {
                cliente = new Cliente(nombre, apellido, email, telefono, Double.parseDouble(saldo));
            }
            //se instanica un nuevo objeto de tipo Data Acces Object
            IClienteDAO clienteDAO = new ClienteDAO();
            Resultado resultado = clienteDAO.insert(cliente);
            // definimos el tipo de respuesta para enviar al cliente
            if (resultado.isExitoso()) {
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
            PrintWriter out;
            try {
                out = response.getWriter();
                ob.add("ok", resultado.isExitoso());
                ob.add("msg", resultado.getMensaje());
                Writer writer = new StringWriter();
                Json.createWriter(writer).writeObject(ob.build());
                String jsonString = writer.toString();
                out.print(jsonString);
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        // Si el parametro viene vacio se envia un error
        if (request.getParameter("idCliente") == null) {
            this.enviarError(response, Arrays.asList("El Parametro idCliente es Necesario"));
        } else {
            // convertimos en entero el id
            int id = Integer.parseInt(request.getParameter("idCliente"));
            // nuevo objeto de cliente - constructor del id
            Cliente cliente = new Cliente(id);
            // nueva instancia de data access object
            IClienteDAO clienteDAO = new ClienteDAO();
            // obtenemos el resultado de la operacion delete
            Resultado resultado = clienteDAO.delete(cliente);        
            if (resultado.isExitoso()) {
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
            PrintWriter out;
            try {
                // preparamos la salida
                out = response.getWriter();
                // creamos un nuevo JSON
                ob.add("ok", resultado.isExitoso());
                ob.add("msg", resultado.getMensaje());
                Writer writer = new StringWriter();
                Json.createWriter(writer).writeObject(ob.build());
                String jsonString = writer.toString();
                out.print(jsonString);
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String idCliente = request.getParameter("idCliente");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String saldo = request.getParameter("saldo");

        if (idCliente == null || idCliente.equals("")) {
            this.enviarError(response, Arrays.asList("El Campo Id Es Necesario"));
        } else {
            IClienteDAO clienteDAO = new ClienteDAO();
            // recuperamos de la base de datos el cliente por id
            Cliente clienteDB = clienteDAO.selectById(new Cliente(Integer.parseInt(idCliente)));
            // asignamos al objecto nuevos valores en caso de que existan en la peticion
            if (nombre != null && !nombre.equals("")) {
                clienteDB.setNombre(nombre);
            }
            if (apellido != null && !apellido.equals("")) {
                clienteDB.setApellido(apellido);
            }
            if (telefono != null && !telefono.equals("")) {
                clienteDB.setTelefono(telefono);
            }
            if (email != null && !email.equals("")) {
                clienteDB.setEmail(email);
            }
            if (saldo != null && !saldo.equals("")) {
                clienteDB.setSaldo(Double.parseDouble(saldo));
            }
            Resultado resultado = clienteDAO.update(clienteDB);
            if (resultado.isExitoso()) {
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
            PrintWriter out;
            try {
                out = response.getWriter();
                ob.add("ok", resultado.isExitoso());
                ob.add("msg", resultado.getMensaje());
                Writer writer = new StringWriter();
                Json.createWriter(writer).writeObject(ob.build());
                String jsonString = writer.toString();
                out.print(jsonString);
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        String idCliente = request.getParameter("idCliente");
        if (idCliente == null || idCliente.equals("")) {
            this.enviarError(response, Arrays.asList("El campo \"idCliente\" es obligatorio"));
        } else {
            IClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = new Cliente(Integer.parseInt(idCliente));
            Cliente clienteDB = clienteDAO.selectById(cliente);
            PrintWriter out;
            try {
                out = response.getWriter();
                ob.add("idCliente", clienteDB.getIdCliente());
                ob.add("nombre", clienteDB.getNombre());
                ob.add("apellido", clienteDB.getApellido());
                ob.add("email", clienteDB.getEmail());
                ob.add("telefono", clienteDB.getTelefono());
                ob.add("saldo", clienteDB.getSaldo());
                Writer writer = new StringWriter();
                Json.createWriter(writer).writeObject(ob.build());
                String jsonString = writer.toString();
                out.print(jsonString);
                out.close();
            } catch(IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }
    private void enviarError(HttpServletResponse response, List<String> errores) {
        response.setStatus(400);
        PrintWriter out;
        try {
            out = response.getWriter();
            ob.add("ok", false);
            errores.forEach(error -> {
                ab.add(error);
            });
            ob.add("errores", ab.build());
            Writer writer = new StringWriter();
            Json.createWriter(writer).writeObject(ob.build());
            String jsonString = writer.toString();
            out.print(jsonString);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

}
