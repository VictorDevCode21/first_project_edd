package edd_project;
import com.datasource.DataSource;
import com.graph.Graph;
import java.io.IOException;
import com.interfaces.WelcomeInterface;


/**
 *
 * @author PC
 */
public class EDD_Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        DataSource dataSource = new DataSource();
//        try {
////            dataSource.loadNetworkFromFile("src/resources/Bogota.json"); // Carga el archivo de Bogotá
//            // O para Caracas:
//             dataSource.loadNetworkFromFile("src/resources/Caracas.json");
//            System.out.println(dataSource.getNetworkData()); // Muestra los datos cargados
//        } catch (IOException e) {
//            System.err.println("Error loading network data: " + e.getMessage());
//        }
        
          WelcomeInterface interface1 = new WelcomeInterface();
          interface1.show();
        
    }
    
}
