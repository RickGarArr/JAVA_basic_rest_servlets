package web;

import datos.*;
import dominio.Cliente;
import java.io.*;
import java.util.*;
import javax.json.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/clientes")
public class ListaClientes extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        IClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientes = clienteDAO.select();
        response.setContentType("application/json");
        response.setStatus(200);
        response.addHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out;
        try {
            out = response.getWriter();
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            JsonArrayBuilder clientesJSON = Json.createArrayBuilder();
            clientes.forEach(cliente -> {
                objectBuilder.add("idCliente", cliente.getIdCliente());
                objectBuilder.add("nombre", cliente.getNombre());
                objectBuilder.add("apellido", cliente.getApellido());
                objectBuilder.add("email", cliente.getEmail());
                objectBuilder.add("telefono", cliente.getTelefono());
                objectBuilder.add("saldo", cliente.getSaldo());
                JsonObject jsonObject = objectBuilder.build();
                clientesJSON.add(jsonObject);
            });
            objectBuilder.add("saldoTotal", this.calcularSaldo(clientes));
            objectBuilder.add("clientes", clientesJSON);
            Writer writer = new StringWriter();
            Json.createWriter(writer).writeObject(objectBuilder.build());
            String jsonArrayString = writer.toString();
            out.print(jsonArrayString);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }  
    }
    
    private double calcularSaldo(List<Cliente> clientes) {
        double saldoTotal = 0;
        for(Cliente cliente: clientes) {
            saldoTotal += cliente.getSaldo();
        }
        return saldoTotal;
    }
}
