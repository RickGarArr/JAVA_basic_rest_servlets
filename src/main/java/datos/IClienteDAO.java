package datos;

import datos.resultados.Resultado;
import dominio.Cliente;
import java.util.List;

public interface IClienteDAO {
    List<Cliente> select();
    
    Cliente selectById(Cliente cliente);
    
    Resultado insert(Cliente cliente);
    
    Resultado update(Cliente cliente);
    
    Resultado delete(Cliente cliente);
}
