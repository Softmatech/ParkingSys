package data;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import static java.awt.Toolkit.getDefaultToolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.jdesktop.swingx.JXTextArea;

public class querryDerby {

    public int longueur;
    public JFileChooser ls;
    public InputStream lp;
    
    ConnectionManager dba = new ConnectionManager();
    private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
       //metodo que retorna un resultset pasandole el query
    public ResultSet Rezulta(String query) {
        ResultSet rs = null;
        try {
            Statement comando = dba.openConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = comando.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("** Error Coneksyon #1 **\n" + ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return rs;
    }

    public void dimensyon (JInternalFrame frame){
//este metodo devuelve el tamaño de la pantalla
Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
//obtenemos el tamaño de la ventana
Dimension ventana = frame.getSize();
//para centrar la ventana lo hacemos con el siguiente calculo
int aa = (pantalla.width - ventana.width)/2;
int bb = (pantalla.height - ventana.height)/4;
frame.setLocation(aa,bb);
  
}
    
    //metodo que retorna un resultset pasandole el query
    public int ResultsetCount(String query) {
        int tot = 0;
        try {
            Statement comando = dba.openConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = comando.executeQuery(query);
            if (rs.last()) {
                tot = rs.getRow();
            }
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("** Error Coneksyon #1 **\n" + ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return tot;
    }

    public class MyDefaultTableModelo extends DefaultTableModel {

        public MyDefaultTableModelo() {
            super();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    public class MyDefaultTableModelo_ extends DefaultTableModel {

        public MyDefaultTableModelo_() {
            super();
        }

        @Override
        public boolean isCellEditable(int row, int col) {

            return true;
        }
    }

    // metodo para ejecutar update, insert o delete
    public int ejecutarSql(String query) {
        int status = 0;
        try {
            dba.openConnection();
            Statement stmt = dba.openConnection().createStatement();
            stmt.executeUpdate(query);
            status = 1;
        } catch (SQLException et) {
            status = 0 ;
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, et);
            System.err.println("** Error Coneksyon #2 **\n" + et.getMessage());
            JOptionPane.showMessageDialog(null, et.getMessage());
        }
        return status;
    }

    //metodo para obtener la cantidad de registros
    public int CantRegistros(ResultSet ResultSet) {
        int int_cantreg = 0;
        try {
            while (ResultSet.next()) {
                int_cantreg++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return int_cantreg;
    }

    public void dimensyon(JFrame jframe,JInternalFrame frame) {
        //este metodo devuelve el tamaño de la pantalla
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        //Dimension pantalla = frame.getSize();
        //obtenemos el tamaño de la ventana
        Dimension ventana = frame.getSize();
        //para centrar la ventana lo hacemos con el siguiente calculo

        int aa = ((pantalla.width - ventana.width) / 3);
        int bb = ((pantalla.height - ventana.height) / 12);
        frame.setLocation(aa, bb);
    }

    public MyDefaultTableModelo llenar_grid(String a, String[] words) {
        
        MyDefaultTableModelo modelo = new MyDefaultTableModelo();
        
        List<String> wordList = Arrays.asList(words);

        for (String e : wordList) {

            modelo.addColumn(e);

        }

        try {
            ResultSet consulta = Rezulta(a);

            ResultSetMetaData estructura_de_la_consulta = consulta.getMetaData();
            int colNo = estructura_de_la_consulta.getColumnCount();
            Object[] columnas = new Object[colNo];

            while (consulta.next()) {
                for (int i = 0; i < colNo; i++) {
                    columnas[i] = consulta.getObject(i + 1);
                }

                modelo.addRow(columnas);

            }

        } catch (SQLException ex) {
            Logger.getLogger("Tab la pagen paramet sa yo ").log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        return modelo;
    }

    public void llenar_grid(String a, DefaultTableModel tabla) {
        DefaultTableModel modelo = tabla;

        int colunmas = modelo.getRowCount() - 1;

        for (int j = colunmas; j >= 0; j--) {
            modelo.removeRow(j);
        }

        try {
            ResultSet consulta = Rezulta(a);

            ResultSetMetaData estructura_de_la_consulta = consulta.getMetaData();
            int colNo = estructura_de_la_consulta.getColumnCount();
            Object[] columnas = new Object[colNo];
            while (consulta.next()) {
                for (int i = 0; i < colNo; i++) {
                    columnas[i] = consulta.getObject(i + 1);
                }
                modelo.addRow(columnas);
            }

        } catch (SQLException ex) {
            Logger.getLogger("Tab la pagen paramet sa yo ").log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }

    public String dateToMysqlDate(Date fecha) {
        String fechaReturn = "0000-00-00";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if(fecha != null){
            fechaReturn = sdf.format(fecha);
        }
        return fechaReturn;
    }

    public String StringToMysqlDateFormat(String fecha) {
        DateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
        Date dat = null;
        try {
            dat = formater.parse(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dat);
    }

    public String StringToMysqlDateFormatSpecial(String fecha) {
        DateFormat formater = new SimpleDateFormat("M/dd/yy");
        Date dat = null;
        try {
            dat = formater.parse(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dat);
    }

    public String dateHourToMysqlDate(Date fecha) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd H:m:s");
        return sdf.format(fecha);
    }
    

    public String PrepaCad(String cadena) {
        String Direccion = "";
        for (int i = 0; i < cadena.trim().length(); i++) {
            if (cadena.charAt(i) == '\'') {
                Direccion = Direccion + '\'';
            }
            Direccion = Direccion + cadena.charAt(i);
        }

        return (Direccion);
    }

    public InputStream Photos(JLabel label, String id) {
        Integer targetWidth = 400;
        Integer targetHeight = 600;

        ImageIcon imgcon = null;
        label.setText(null);
        ls = new JFileChooser();
        ls.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //-------------------------------------------------
        javax.swing.filechooser.FileFilter filter = new FileNameExtensionFilter("JPG & GIF & PNG", "jpg", "gif", "png");
        ls.setFileFilter(filter);
        //--------------------------------------------------
        int gt = ls.showOpenDialog(null);
        if (gt == JFileChooser.APPROVE_OPTION) {
            try {
                lp = new FileInputStream(ls.getSelectedFile());
                longueur = (int) ls.getSelectedFile().length();
                General.longeur = longueur;
                BufferedImage icn = ImageIO.read(ls.getSelectedFile());
                BufferedImage thumbImg = Scalr.resize(icn, Method.QUALITY, Mode.FIT_EXACT, label.getWidth(), label.getHeight(), Scalr.OP_ANTIALIAS);

                imgcon = new ImageIcon(CrearImage(thumbImg, label));
                label.setIcon(imgcon);
                //InsertPhotos(id, lp, longueur);
                label.updateUI();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

        }
        return lp;
    }

    public void InsertPhotos(String code, InputStream direct, int longitud) {
        //InputStream Foto = null;
        try {
            String sql = "call insertimg (?,?)";
            PreparedStatement statement = dba.openConnection().prepareStatement(sql);
            statement.setString(1, code);
            statement.setBlob(2, direct, longitud);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void ShowPhotos(String a, JLabel label) {

        try {
            ResultSet dz = Rezulta(a);
            if (dz.first()) {
                Blob bl = (Blob) dz.getBlob("photo");
                byte[] data = bl.getBytes(1, (int) bl.length());
                try {
                    Image img = ImageIO.read(new ByteArrayInputStream(data)).getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(img));
                    label.setText(null);
                    label.updateUI();
                } catch (IOException ex) {
                    Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            } else {
                label.setIcon(null);
                label.updateUI();
            }
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public Image CrearImage(Image image, JLabel label) {
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = new BufferedImage(label.getWidth(), label.getHeight(), BufferedImage.SCALE_DEFAULT);
        Graphics g = bimage.createGraphics();

        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    public Date StringToDate(String Fecha) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Fecha != null) {
                d = sdf.parse(Fecha);
            }
        } catch (NullPointerException | ParseException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return d;
    }

    //.................................................................................

    public Date StringToDateDos(String Fecha) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            if (Fecha != null) {
                d = sdf.parse(Fecha);
            }
        } catch (NullPointerException | ParseException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return d;
    }

    //'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    public MyDefaultTableModelo llenar_grid_With_Field(String a) {
        MyDefaultTableModelo modelo = new MyDefaultTableModelo();
        try {
            ResultSet consulta = Rezulta(a);
            ResultSetMetaData estructura_de_la_consulta = consulta.getMetaData();
            int colNo = estructura_de_la_consulta.getColumnCount() - 2;
            Object[] columnas = new Object[colNo];
            String[] col = new String[colNo];

            for (int i = 0; i < col.length; i++) {
                col[i] = estructura_de_la_consulta.getColumnLabel(i + 3).toUpperCase();
            }
            List<String> wordl = Arrays.asList(col);
            for (String e : wordl) {
                modelo.addColumn(e);
            }
            int j = 1;
            while (consulta.next()) {
                for (int i = 0; i < colNo; i++) {
                    columnas[i] = consulta.getObject(i + 3);
                }
                modelo.addRow(columnas);
                j++;
            }

        } catch (SQLException ex) {
            Logger.getLogger("Tab la pagen paramet sa yo ").log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return modelo;
    }
    //...........................................................................

    public MyDefaultTableModelo llenar_grid_With_Field_(String a) {
        MyDefaultTableModelo modelo = new MyDefaultTableModelo();
        try {
            ResultSet consulta = Rezulta(a);
            ResultSetMetaData estructura_de_la_consulta = consulta.getMetaData();
            int colNo = estructura_de_la_consulta.getColumnCount();
            Object[] columnas = new Object[colNo];
            String[] col = new String[colNo];

            for (int i = 0; i < col.length; i++) {
                col[i] = estructura_de_la_consulta.getColumnLabel(i + 1).toUpperCase();
            }
            List<String> wordl = Arrays.asList(col);
            for (String e : wordl) {
                modelo.addColumn(e);
            }
            int j = 1;
            while (consulta.next()) {
                for (int i = 0; i < colNo; i++) {
                    columnas[i] = consulta.getObject(i + 1);
                }
                modelo.addRow(columnas);

                j++;
            }

        } catch (SQLException ex) {
            Logger.getLogger("Tab la pagen paramet sa yo ").log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return modelo;
    }
    //...........................................................................

    public MyDefaultTableModelo llenar_grid_With_Field_jProgress3(String a, JProgressBar bar) throws InterruptedException {
        MyDefaultTableModelo modelo = new MyDefaultTableModelo();
        int sa = ResultsetCount(a);
        try {
            ResultSet consulta = Rezulta(a);
            ResultSetMetaData estructura_de_la_consulta = consulta.getMetaData();
            int colNo = estructura_de_la_consulta.getColumnCount() - 2;
            Object[] columnas = new Object[colNo];
            String[] col = new String[colNo];

            for (int i = 0; i < col.length; i++) {
                col[i] = estructura_de_la_consulta.getColumnLabel(i + 3).toUpperCase();
            }
            List<String> wordl = Arrays.asList(col);
            for (String e : wordl) {
                modelo.addColumn(e);
            }
            while (consulta.next()) {
                for (int i = 0; i < colNo; i++) {
                    columnas[i] = consulta.getObject(i + 3);
                }
                int da = consulta.getRow();
                int sl = ((da * 100) / sa);
                //  System.out.println(sl+"%");
                bar.setValue(sl);
                bar.setStringPainted(true);
                modelo.addRow(columnas);
                Thread.sleep(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger("Tab la pagen paramet sa yo ").log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return modelo;
    }

    //...........................................................................
    //'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

    public MyDefaultTableModelo llenar_grid_With_Field_jProgress1(String a, JProgressBar bar) throws InterruptedException  {
        MyDefaultTableModelo modelo = new MyDefaultTableModelo();
        int sa = ResultsetCount(a);
        try {
            ResultSet consulta = Rezulta(a);
            ResultSetMetaData estructura_de_la_consulta = consulta.getMetaData();
            int colNo = estructura_de_la_consulta.getColumnCount();
            Object[] columnas = new Object[colNo];
            String[] col = new String[colNo];

            for (int i = 0; i < col.length; i++) {
                col[i] = estructura_de_la_consulta.getColumnLabel(i + 1).toUpperCase();
            }
            List<String> wordl = Arrays.asList(col);
            for (String e : wordl) {
                modelo.addColumn(e);
            }
            while (consulta.next()) {
                for (int i = 0; i < colNo; i++) {
                    columnas[i] = consulta.getObject(i + 1);
                }
                int da = consulta.getRow();
                int sl = ((da * 100) / sa);
                bar.setValue(sl);
                bar.setStringPainted(true);
                modelo.addRow(columnas);
                Thread.sleep(1);
            }
            consulta.close();
            
        } catch (SQLException |ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger("Tab la pagen paramet sa yo ").log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return modelo;
    }
    //...........................................................................

    public void Combo(JComboBox combo, String select) {
        combo.removeAllItems();
        String Descripcion;
        int codigo;
        try {
            ResultSet rs = Rezulta(select);
            combo.addItem("Seleksyone");
            while (rs.next()) {
                Descripcion = rs.getString(1);
                combo.addItem(Descripcion);
            }
            combo.addItem("Lòt");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }


    public void hostRegister() {
        try {
            InetAddress st = InetAddress.getLocalHost();
            String hst = st.getHostName();
            //JOptionPane.showMessageDialog(null,"men ni papa "+ hst);
            String ml = "call host_register (NULL,'" + hst + "') ";
            System.out.println("host " + ml);
            ejecutarSql(ml);
        } catch (UnknownHostException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void letterInterdiction(KeyEvent ke, JTextField j) {
        char c = ke.getKeyChar();
        if (!Character.isDigit(c)) {
            getDefaultToolkit().beep();
            ke.consume();
            JOptionPane.showMessageDialog(null, "This field only accept number digit [0-9]");
        }
    }

     public void autoComplete(String txt,String tabla,String campo,JTextField textField){
         ArrayList name = new ArrayList();
        ResultSet rs = Rezulta("select distinct "+campo+" from "+tabla+" where "+campo+" is not null and "+campo+" not in(\"\")");
        try {
            while (rs.next()) {
                String silo = rs.getString(campo);
                name.add(silo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        String complete = "";
        int start = txt.length(); 
        int last = txt.length();
        int a;
        
        for (a = 0; a < name.size(); a++) {
            if(name.get(a).toString().startsWith(txt) || name.get(a).toString().toLowerCase().startsWith(txt) || name.get(a).toString().toUpperCase().startsWith(txt)){
                complete = name.get(a).toString();
                last = complete.length();
                break;
            }
        }
        if(last > start){
            textField.setBackground(Color.WHITE);
            textField.setSelectedTextColor(Color.WHITE);
            textField.setSelectionColor(new Color(255, 105, 0));
            textField.setText(complete);
            textField.setCaretPosition(last);
            textField.moveCaretPosition(start);
        }
    }
    
     public void autoComplete(String txt,String tabla,String campo,JXTextArea textField)throws NullPointerException{
         ArrayList name = new ArrayList();
         //where commente is not null and commente not in("")
         String sql = "select distinct "+campo+" from "+tabla+" where "+campo+" is not null and "+campo+" not in(\"\") ";
         System.out.println(sql);
        ResultSet rs = Rezulta(sql);
        try {
            while (rs.next()) {
                String silo = rs.getString(campo);
                name.add(silo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        String complete = "";
        int start = txt.length(); 
        int last = txt.length();
        int a;
        
        for (a = 0; a < name.size(); a++) {
            System.out.println("texxtt "+txt);
            if(name.get(a).toString().startsWith(txt) || name.get(a).toString().toLowerCase().startsWith(txt) || name.get(a).toString().toUpperCase().startsWith(txt)){
                complete = name.get(a).toString();
                last = complete.length();
                break;
            }
        }
        if(last > start){
            textField.setBackground(Color.WHITE);
            textField.setText(complete);
            textField.setCaretPosition(last);
            textField.moveCaretPosition(start);
        }
    }
     
     public String isNull(String string){
         if("null".equals(string)){
             string = null;
         }
         return string;
     }
     
     public void Salir(JButton button, Container component){
         
         button.setText("Quit");
         button.setToolTipText("Quit the Form");
         
         int rep = JOptionPane.showConfirmDialog(null, "Are you Sure you want to quit", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
         if(rep == JOptionPane.YES_OPTION){
             if(component instanceof JDialog){
                 JOptionPane.showMessageDialog(null, "1");
                 
             }
             else if (component instanceof JInternalFrame){
                 JOptionPane.showMessageDialog(null, "2");
             }
         }
     }

     
     public DefaultTableModel tablemodel(String sqls,int init){
         MyDefaultTableModelo modelo = new MyDefaultTableModelo();
        try {
            ResultSet consulta = Rezulta(sqls);
            ResultSetMetaData estructura_de_la_consulta = consulta.getMetaData();
            int colNo = estructura_de_la_consulta.getColumnCount()-2; 
            Object[] columnas = new Object[colNo];
            String[] col = new String[colNo];
            
            for (int i = 0; i < col.length; i++) {
                col[i] = estructura_de_la_consulta.getColumnLabel(i+init).toUpperCase();
            }
            List<String> wordl = Arrays.asList(col);
            for (String e : wordl){
                modelo.addColumn(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modelo;
     }
     
     public void removeDatosOnTable(DefaultTableModel model){
         int a = 0;
         
         try {
           a = model.getRowCount() -1;  System.out.println("rowc "+a);
         } catch (NullPointerException |ArrayIndexOutOfBoundsException ex) {
         }
         
         if(a >= 0){
         for (int i = a; i >= 0; i--) {
             try {
               model.removeRow(i);  
             } catch (NullPointerException e) {
             }
             
         }
         }
     }
     
     public DefaultTableModel tablemodelo(String sqls,int init){
         MyDefaultTableModelo modelo = new MyDefaultTableModelo();
        try {
            ResultSet consulta = Rezulta(sqls);
            ResultSetMetaData estructura_de_la_consulta = consulta.getMetaData();
            int colNo = estructura_de_la_consulta.getColumnCount(); 
            Object[] columnas = new Object[colNo];
            String[] col = new String[colNo];
            
            for (int i = 0; i < col.length; i++) {
                col[i] = estructura_de_la_consulta.getColumnLabel(i+init).toUpperCase();
            }
            List<String> wordl = Arrays.asList(col);
            for (String e : wordl){
                modelo.addColumn(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modelo;
     }
     
     public int getCodeFromCombo(String table,String campoAbuscar,JComboBox combo){
        int code = 0;
        String sql = "select "+campoAbuscar+" from "+table+" where description = '"+PrepaCad(combo.getSelectedItem().toString().trim())+"' ";
        System.out.println(sql);
        ResultSet rs = Rezulta(sql);
        try {
            if(rs.last())
                code = rs.getInt(campoAbuscar);
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return code;
    }
     
     public int getCodeFromString(String table,String campoAbuscar,String campo){
        int code = 0;
        String sql = "select "+campoAbuscar+" from "+table+" where description = '"+PrepaCad(campo.trim())+"' ";
        System.out.println(sql);
        ResultSet rs = Rezulta(sql);
        try {
            if(rs.last())
                code = rs.getInt(campoAbuscar);
        } catch (SQLException ex) {
            Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return code;
    }
     
     public String Date_To_String(Date h) {
       Date date = h;
       DateFormat df =DateFormat.getDateInstance(DateFormat.FULL, new Locale("FR", "fr"));
       SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd  MMMM  yyyy " , new Locale("FR", "fr"));
       String s = df.format(date);
       String x = sdf.format(date);
       return x;
    }
     
     public String Date_Hour_To_String(Date h) {
       Date date = h;
       DateFormat df =DateFormat.getDateInstance(DateFormat.FULL, new Locale("FR", "fr"));
       SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd  MMMM  yyyy ' ' hh':'mm':'ss' 'a" , new Locale("FR", "fr"));
       String s = df.format(date);
       String x = sdf.format(date);
       return x;
    }
     
     public String abrege(String abreges){
         //for create abreviature
         int x = abreges.length(); int xx = x/2;   int xxx = xx/2;
         String letra = "";
         for (int i = 0; i < x; i++) {
             if(!abreges.contains(" ")){
                 letra = abreges.substring(0,xx);
             }
             else{
                 System.out.println("yesss");
               int pos = abreges.indexOf(' ')+1; System.out.println("pos "+pos);
                 String comp1 = abreges.substring(0, xxx);
                 String comp2 = abreges.substring(pos, pos + xxx);
                 letra = comp1 + comp2;
               
             }
             
             String let = letra+"#";
             String sql = "select abrege from marchandise where abrege = '"+let+"' ";
             System.out.println("sql "+sql);
             ResultSet rs = Rezulta(sql);
             int j = 0;
             try {
                 if (rs.last()) {
                     String abr = rs.getString("abrege");
                     try {
                  j =Integer.parseInt(abr.substring(abr.length()-2,abr.length()-1));
                  j++;
                     } catch (NumberFormatException e) {
                         
                     }
                     
                     letra += j;
                     j++;
                 }
             } catch (SQLException ex) {
                 Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         return letra+"#";
     } 
     
public boolean Verificar (String campo, String tabla,JTextField j) throws SQLException{
    boolean res = false;
        String sql = "select * from " + tabla + " where " +campo+ " = '"+PrepaCad(j.getText().trim())+"' " ;
        System.out.println("sqsqqsqsq "+sql);
    ResultSet gb = Rezulta(sql);
    System.out.println(sql);
    if (gb.first()){
         String fv = gb.getString(campo);
            res=true;
    }
    return res;
}

public boolean isElementExistInGrid(JTable table, String[] text) {
                
                boolean result = false;
        for (String text1 : text) {
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    try {
                        if (table.getValueAt(i, j).toString().equals(text1)) {
                            result =true;
                        }
                    }catch (NullPointerException e) {                        
                    }
                }
            }
        }
                return result;
            }
     
//........................................................................................................

public void afich (JTextField a, String tex){
    if(a.getText().isEmpty()){
    a.setText(tex);
    
}
}

public void selection(JTextField bv){
    int a = bv.getText().length();
       bv.setSelectionStart(0); bv.setSelectionEnd(a);
}

public void Consulter (JButton boton, JDialog consulta,JTextField campo){
   
     boton.setText("Consulter");   
     boton.setToolTipText("Cliquer pour Consulter ");
     consulta.setVisible(true);
    if (General.codigo!=0){
                    campo.setText(General.codigo+"");
                    
                }
  }

public void InsertPhotos(int idpersonne, InputStream direct, int longitud,JLabel label) {

                try {
                    if (label.getIcon() != null) {
                        String sql = "call image_procedure (?,?)";
                        PreparedStatement statement = dba.openConnection().prepareStatement(sql);
                        statement.setInt(1, idpersonne);
                        statement.setBlob(2, direct, longitud);
                        statement.executeUpdate();
                    } else {
                        JOptionPane.showMessageDialog(null, "Désolé Vous Le champ matricule ne doit pas etre vide");
                        return;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

    
 
//valida correo electronico
public boolean isEmailValidate(String email) {
    boolean rep = false;
    try{
	    // Compiles the given regular expression into a pattern.
	    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	    // Match the given input against this pattern
	    Matcher matcher = pattern.matcher(email);
	    if (matcher.matches()){
                rep = true;
            }

	}catch(Exception e){
		e.printStackTrace();
	}
        return rep;
       
}

public Date HORA(String dat) {
                SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss");
                Date tim = null;
                try {
                    tim = fm.parse(dat);
                } catch (ParseException ex) {
                    Logger.getLogger(querryDerby.class.getName()).log(Level.SEVERE, null, ex);
                }
                return tim;
            }

public String HOURFormat(JSpinner spin) {
    System.out.println("SPIN "+spin.getValue());
                SimpleDateFormat sdff = new SimpleDateFormat("HH:mm:ss", Locale.FRENCH);
                Calendar cal = Calendar.getInstance();
                String hr = sdff.format(spin.getValue());
                return hr;
            }

}
