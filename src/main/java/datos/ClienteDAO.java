package datos;

import datos.resultados.Resultado;
import dominio.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO {
    private static final String SELECT = "SELECT idCliente, nombre, apellido, email, telefono, saldo FROM clientes";
    private static final String SELECT_BY_ID = "SELECT idCliente, nombre, apellido, email, telefono, saldo FROM clientes WHERE idCliente = ?";
    private static final String INSERT = "INSERT INTO clientes (nombre, apellido, email, telefono, saldo) VALUES (?,?,?,?,?)";
    private static final String UPDATE = "UPDATE clientes SET nombre=?, apellido=?, email=?, telefono=?, saldo=? WHERE idCliente=?";
    private static final String DELETE = "DELETE FROM clientes WHERE idCliente = ?";
    
    @Override
    public List<Cliente> select() {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Cliente> clientes = null;
        try {
            conexion = Conexion.getConnection();
            preparedStatement = conexion.prepareStatement(SELECT);
            resultSet = preparedStatement.executeQuery();
            clientes = new ArrayList<>();
            while(resultSet.next()){
                int idCliente = resultSet.getInt("idCliente");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String email = resultSet.getString("email");
                String telefono = resultSet.getString("telefono");
                double saldo = resultSet.getDouble("saldo");
                Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
                clientes.add(cliente);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(resultSet);
            Conexion.close(preparedStatement);
            Conexion.close(conexion);
        }
        return clientes;
    }

    @Override
    public Cliente selectById(Cliente cliente) {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conexion = Conexion.getConnection();
            preparedStatement = conexion.prepareStatement(SELECT_BY_ID);
            preparedStatement.setInt(1, cliente.getIdCliente());
            resultSet = preparedStatement.executeQuery();
            resultSet.absolute(1);
            cliente.setNombre(resultSet.getString("nombre"));
            cliente.setApellido(resultSet.getString("apellido"));
            cliente.setEmail(resultSet.getString("email"));
            cliente.setTelefono(resultSet.getString("telefono"));
            cliente.setSaldo(resultSet.getDouble("saldo"));
        } catch(SQLException ex){
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(resultSet);
            Conexion.close(preparedStatement);
            Conexion.close(conexion);
        }
        return cliente;
    }

    @Override
    public Resultado insert(Cliente cliente) {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        Resultado resultado = null;
        try {
            conexion = Conexion.getConnection();
            preparedStatement = conexion.prepareStatement(INSERT);
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getApellido());
            preparedStatement.setString(3, cliente.getEmail());
            preparedStatement.setString(4, cliente.getTelefono());
            preparedStatement.setDouble(5, cliente.getSaldo());
            int contador = preparedStatement.executeUpdate();
            resultado = new Resultado(true, "registro exitoso, rows afectados: " + contador);
        } catch(SQLException ex){
            resultado = new Resultado(false, ex.getMessage());
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(preparedStatement);
            Conexion.close(conexion);
        }
        return resultado;
    }

    @Override
    public Resultado update(Cliente cliente) {
                Connection conexion = null;
        PreparedStatement preparedStatement = null;
        Resultado resultado = null;
        try {
            conexion = Conexion.getConnection();
            preparedStatement = conexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getApellido());
            preparedStatement.setString(3, cliente.getEmail());
            preparedStatement.setString(4, cliente.getTelefono());
            preparedStatement.setDouble(5, cliente.getSaldo());
            preparedStatement.setInt(6, cliente.getIdCliente());
            int contador = preparedStatement.executeUpdate();
            if (contador > 0) {
                resultado = new Resultado(true, "Actualizacion exitosa; rows afectados: " + contador);
            } else {
                resultado = new Resultado(false, "El id Introducido no existe, rows afectados: " + contador);
            }
        } catch(SQLException ex){
            resultado = new Resultado(false, ex.getMessage());
//            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(preparedStatement);
            Conexion.close(conexion);
        }
        return resultado;
    }

    @Override
    public Resultado delete(Cliente cliente) {
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        Resultado resultado = null;
        try {
            conexion = Conexion.getConnection();
            preparedStatement = conexion.prepareStatement(DELETE);
            preparedStatement.setInt(1, cliente.getIdCliente());
            int contador = preparedStatement.executeUpdate();
            if(contador == 0) {
                resultado = new Resultado(true, "El idIntroducido no existe, rows afectados: " + contador);    
            } else {
                resultado = new Resultado(true, "operacion exitosa, rows afectados: " + contador);
            }
        }catch(SQLException ex) {
            resultado = new Resultado(false, ex.getMessage());
        } finally {
            Conexion.close(preparedStatement);
            Conexion.close(conexion);
        }
        return resultado;
    }
}
