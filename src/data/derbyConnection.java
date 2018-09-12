package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class derbyConnection {

    private derbyConnection instance = null;
    private final String USERNAME = "root";
    private final String PASSWORD = "Root";
    private final String  MYSQL_CONN_STRING="jdbc:derby://localhost:1527/parkingDB" ;
    private Connection conn = null;

  public derbyConnection getInstance() {
        if (instance == null) {
            instance = new derbyConnection();
        }
        return instance;
    }

    public Connection openConnection() {
                    try {
            if (isOpenConnection() == false) {
                //System.out.println("okokokk");
                try {
                    Class.forName("org.apache.derby.jdbc.ClientDriver");
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            conn = DriverManager.getConnection(MYSQL_CONN_STRING, USERNAME, PASSWORD);
            System.out.println("opeconnection : Connection Opened");
            }
        } catch (SQLException e) {
            System.err.println("openConnection() >> "+e);
            JOptionPane.showMessageDialog(null,"Problem With The network Please Contact the Network Administrator \n"+ e.getMessage());
            System.exit(1);
        }
        return conn;
    }

    public boolean isOpenConnection() {
        boolean res = false;
        if(conn != null){
            res = true;
            //System.out.println("isOpenConnection : Conexion Open");
        }
        return res;
    }

    public Connection getConnection() {
        try {
            openConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return conn;
    }

 
    public Connection close() {
        try {
            if(isOpenConnection() ==true){
            conn.close();
            //System.out.println("close : Conexion Closed");
            }
            
        } catch (Exception e) {
           // System.out.println(" close() >> "+e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return conn;
    }
    
    public boolean isClosed() {
        boolean result = false;
        try {
            if(conn != null){
             //   System.out.println("isClosed : Conexion Opened"); 
                result = true;
            }
            else{
             //   System.out.println("isClosed : ConexionClosed"); 
            }
            
        } catch (Exception e) {
            //System.out.println(" close() >> "+e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return result;
    }
   
}
