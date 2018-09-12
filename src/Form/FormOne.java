/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Form;

import Tools.RenderLinea;
import com.ezware.oxbow.swingbits.table.filter.TableRowFilterSupport;
import data.querryDerby;
import java.awt.Color;
import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author josephandyfeidje
 */
public class FormOne extends javax.swing.JFrame {

    String[] columnas = {"Tiempo", "Precio"};
    String[][] Datos = {};
    String[] columnasDos = {"CODIGO", "PLACA", "HORA DE ENTRADA", "ESTADO", "TIEMPO CONSUMIDO"};
    String[][] DatosDos = {};
    String[] columnasTres = {"CODIGO", "PLACA", "HORA DE ENTRADA", "ESTADO", "TIEMPO CONSUMIDO", "MONTO", "HORA DE SALIDA"};
    String[][] DatosTres = {};
    String[] columnasQUatros = {"CODIGO", "PLACA", "HORA DE ENTRADA", "ESTADO", "TIEMPO CONSUMIDO", "MONTO", "HORA DE SALIDA"};
    String[][] DatosQuatros = {};
    DefaultTableModel tablemodel;
    DefaultTableModel tablemodelDos;
    DefaultTableModel tablemodelTres;
    DefaultTableModel tablemodelQuatros;
    querryDerby kerry = new querryDerby();
    String time = "CURRENT_TIMESTAMP";

    /**
     * Creates new form FormOne
     */
    public FormOne() {
        initComponents();
        setLocationRelativeTo(this);
        tablemodel = new DefaultTableModel(Datos, columnas);
        tablemodelDos = new DefaultTableModel(DatosDos, columnasDos);
        tablemodelTres = new DefaultTableModel(DatosTres, columnasTres);
        tablemodelQuatros = new DefaultTableModel(DatosQuatros, columnasQUatros);
        jTable1.setModel(tablemodel);
        jTable2.setModel(tablemodelDos);
        jTable3.setModel(tablemodelTres);
        jTable4.setModel(tablemodelQuatros);
        estado();
        jCheckBox1.setSelected(true);
        horaChecked();
//        jDateChooser2.setDate(new Date());
        mytimer.scheduleAtFixedRate(Task, 0, 60000);
        TableRowFilterSupport.forTable(jTable2).searchable(true).apply();
        TableRowFilterSupport.forTable(jTable3).searchable(true).apply();
        TableRowFilterSupport.forTable(jTable4).searchable(true).apply();
        jTable2.setDefaultRenderer(Object.class,new RenderLinea());
        jTable3.setDefaultRenderer(Object.class,new RenderLinea());
        jTable4.setDefaultRenderer(Object.class,new RenderLinea());
        fullDatos();
        jButton1.setEnabled(isTextieldEmpty(jTextField1));
        jButton2.setEnabled(isTableSelected(jTable3));
        jtable3ChangeListener();
        dateListeners();
        
        Calendar cal = Calendar.getInstance();
            cal.setTime(kerry.HORA("08:00:00"));
            jSpinner1.setModel(new SpinnerDateModel(cal.getTime(), null, null, Calendar.HOUR));
            JSpinner.DateEditor in = new JSpinner.DateEditor(jSpinner1, "HH:mm:ss a");
            jSpinner1.setEditor(in);
            
        jSpinner1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                horaChecked();
            }
        });
    }

    int secondPassed = 0;
    Timer mytimer = new Timer();
    TimerTask Task = new TimerTask() {

        @Override
        public void run() {
            secondPassed += 60;
//            System.out.println("60 Second");
            
            fullDatosDos();
            fullDatosTres();
            fullDatosQuatro();
        }
    };
    
    private void dateListeners(){
        
        jDateChooser1.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {

             if(jDateChooser1.getDate() != null){
                 fullDatosQuatro();
        }
        
            }
        });
    }
    
    private void jtable3ChangeListener() {
        jTable3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
               jButton2.setEnabled(isTableSelected(jTable3));
            }
        });
    }
    
    private boolean isTableSelected(JTable table){
        boolean state = false;
        System.out.println("-->> "+jTable3.getSelectedRow());
        if(table.getSelectedRow() >= 0){
            state = true;
        }
        return state;
    }
    
    private boolean isTextieldEmpty(JTextField text){
        boolean state = false;
        if(!text.getText().isEmpty() && text.getText().length() > 4){
            state = true; 
        }
        return state;
        
    }
    
    private double totlAmountOut(JTable table,int tableCol){
        double x = 0.00;
        
        
        for (int i = 0; i < table.getRowCount(); i++) {
            String pr = table.getValueAt(i, tableCol).toString();
            String col = table.getValueAt(i, tableCol - 2).toString().trim();
            
            if(col.equals("Salio")){
            try {
              x += Double.valueOf(pr.substring(0, pr.indexOf(" ")));  
            } catch (ArrayIndexOutOfBoundsException e) {
                x  += Double.valueOf(pr);
            }
        }
        }
        return x;
    }
    
    private double totlAmountIn(JTable table,int tableCol){
        double x = 0.00;
        
        for (int i = 0; i < table.getRowCount(); i++) {
            String pr = table.getValueAt(i, tableCol).toString();
            
            try {
              x += Double.valueOf(pr.substring(0, pr.indexOf(" ")));  
            } catch (ArrayIndexOutOfBoundsException e) {
                x  += Double.valueOf(pr);
            }
        }
        return x;
    }
    
    private double checkMonto(String placa, String dia) {
        double monto = 0.00;
        String sql = "select monto from SALIDA_VS_MONTO where placa = '" + placa + "' and HORA_ENTRADA LIKE '%" + dia.trim() + "%' ";
//        System.out.println("sql -->> " + sql);
        ResultSet rs = kerry.Rezulta(sql);
        try {
            if (rs.last()) {
                monto = rs.getDouble("monto");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormOne.class.getName()).log(Level.SEVERE, null, ex);
        }
        return monto;
    }
    
    private String checkTiempoConsumido(String placa, String dia) {
        String tiempo = "";
        String sql = "select tiempo_consumido from SALIDA_VS_MONTO where placa = '" + placa + "' and HORA_ENTRADA like '%" + dia.trim() + "%' ";
//        System.out.println("sql -->> " + sql);
        ResultSet rs = kerry.Rezulta(sql);
        try {
            if (rs.last()) {
                tiempo = rs.getString("tiempo_consumido");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormOne.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tiempo;
    }
    
    private void fullDatosQuatro() {
        try {
            kerry.removeDatosOnTable(tablemodelQuatros);
            
//        SwingWorker<Void, Void> workers;
//        workers = new SwingWorker<Void, Void>() {
//            @Override
//            protected Void doInBackground() throws Exception {
            String sql = "";
            System.out.println("jh "+kerry.dateToMysqlDate(jDateChooser1.getDate()));
            if(kerry.dateToMysqlDate(jDateChooser1.getDate()).equals("0000-00-00")){
                sql = "select codigo,placa,hora_entrada,hora_salida from ENTRADA_VS_SALIDA_VIEW where  placa like '%" + search1.getText().trim() + "%' ";
            }
            else{
                sql = "select codigo,placa,hora_entrada,hora_salida from ENTRADA_VS_SALIDA_VIEW where hora_entrada like '%"+kerry.dateToMysqlDate(jDateChooser1.getDate())+"%' and  placa like '%" + search1.getText().trim() + "%' ";
            }
//                System.out.println("sql " + sql);
            ResultSet rs = kerry.Rezulta(sql);
            while (rs.next()) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                String codigo = rs.getString("codigo");
//                    String dia = rs.getString("dia") + " ";
                String placa = rs.getString("placa");
                String hora_entrada = rs.getString("hora_entrada");
                Date fechaInicio = kerry.StringToDate(hora_entrada);
                String dia = hora_entrada.substring(0, hora_entrada.indexOf(" "));
                String horaCons = null;
                
                if(checkEstado(placa, dia).equals("Entro")){
                    horaCons = calcHourMin(getDateDiff(fechaInicio, TimeUnit.MINUTES));
                }
                else{
                    horaCons = checkTiempoConsumido(placa, dia);
                }
                
                String estado = checkEstado(placa, dia);
                
                double monto = 0.00;
                if(checkEstado(placa, dia).equals("Entro")){
                    monto = montoCount(horaCons);
                }
                else{
                    monto = checkMonto(placa, dia);
                }
//                    String diasalida = rs.getString("dia_salida") + " ";
                String salida = rs.getString("hora_salida");
                Object[] DatosTres = {codigo, placa, hora_entrada, estado, horaCons, monto + " $$", salida};
                tablemodelQuatros.addRow(DatosTres);
            }
            jLabel9.setText("TOTAL : "+totlAmountOut(jTable4, 5)+" $$");
            setCursor(Cursor.getDefaultCursor());
            
//                return null;
//            }
//        };
//        workers.execute();
        } catch (SQLException ex) {
            Logger.getLogger(FormOne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String checkEstado(String placa, String dia) {
        String situacion = "Entro";
        String sql = "select hora_salida from SALIDA where placa = '" + placa + "' and HORA_ENTRADA like '%" + dia.trim() + "%' ";
//        System.out.println("sql -->> " + sql);
        ResultSet rs = kerry.Rezulta(sql);
        try {
            if (rs.last()) {
                situacion = "Salio";
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormOne.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("checkEstado -->> "+situacion);
        return situacion;
    }

    private void salida() {
        int cod = Integer.parseInt(jTable3.getValueAt(jTable3.getSelectedRow(), 0).toString().trim());
        String d = jTable3.getValueAt(jTable3.getSelectedRow(), 2).toString().trim();
//        String dia = d.substring(0, d.indexOf(" "));
        String hora = d.substring(d.indexOf(" "));
        String plac = jTable3.getValueAt(jTable3.getSelectedRow(), 1).toString().trim();
        String tiempo = jTable3.getValueAt(jTable3.getSelectedRow(), 4).toString().trim();
        String mon = jTable3.getValueAt(jTable3.getSelectedRow(), 5).toString().trim();
        double monto = Double.valueOf(mon.substring(0, mon.indexOf("$")));
        
        String sql = "INSERT INTO SALIDA (CODIGO,HORA_ENTRADA, PLACA)VALUES (" + cod + ",'"+d+"', '" + plac + "')";
//        System.out.println("sql -->> " + sql);
        kerry.ejecutarSql(sql);
        
        sql = "INSERT INTO SALIDA_VS_MONTO (CODIGO,HORA_ENTRADA, PLACA, TIEMPO_CONSUMIDO,MONTO)VALUES (" + cod + ",'"+d+"' ,'" + plac + "','"+tiempo+"',"+monto+")";
//        System.out.println("sql -->> "+sql);
        if(kerry.ejecutarSql(sql) ==1){
        JOptionPane.showMessageDialog(null, "La placa '"+plac+"' salio del parqueo ");
        fullDatosTres();
        }
    }

    private double montoMinuto(String tiempoConsumido, int indexH) {
        int indexM = tiempoConsumido.indexOf("m");
        double precios = 0.00, montoM;
        String sql = "";
        if(indexH > 0){
            indexH++;
        }
        int minutos = Integer.parseInt(tiempoConsumido.substring(indexH, indexM)); //JOptionPane.showMessageDialog(null, minutos);
        
        if(minutos <= 15){
        sql = "select precio from TIEMPO_VS_PRECIO where tiempo = '15 minutos'";
                }
        else if( minutos <=30){
        sql = "select precio from TIEMPO_VS_PRECIO where tiempo = '30 minutos'";
                }
           else {
        sql = "select precio from TIEMPO_VS_PRECIO where tiempo = '60 minutos'";
        }
      
//        System.out.println("sql -->> "+sql);
        ResultSet rs = kerry.Rezulta(sql);
        try {
            if (rs.last()) {
                precios = rs.getDouble("precio");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormOne.class.getName()).log(Level.SEVERE, null, ex);
        }
        montoM = (precios);
        return montoM;

    }

    private double montoCount(String tiempoConsumido) {
        double montoH = 0.00;
        if (tiempoConsumido.contains("H")) {
            int indexH = tiempoConsumido.indexOf("H");
            int hora = Integer.parseInt(tiempoConsumido.substring(0, indexH));
            double precios = 0.00;
            String sql = "select precio from TIEMPO_VS_PRECIO where tiempo = '60 minutos' ";
//            System.out.println("sql ->> " + sql);
            ResultSet rs = kerry.Rezulta(sql);
            try {
                if (rs.last()) {
                    precios = rs.getDouble("precio");
//                    System.out.println("prc -->> " + precios);
                }
            } catch (SQLException ex) {
                Logger.getLogger(FormOne.class.getName()).log(Level.SEVERE, null, ex);
            }
            montoH = (precios * hora);
//            System.out.println("mon 1 -->> " + montoH);

            if (tiempoConsumido.length() > indexH) {
                int indexM = tiempoConsumido.indexOf("m");
//                int minutos = Integer.parseInt(tiempoConsumido.substring(indexH + 1, indexM));
                montoH += montoMinuto(tiempoConsumido, indexH);
//                System.out.println("mon 2 -->> " + montoH);
            }

        } else if (tiempoConsumido.contains("m")) {
            int indexM = tiempoConsumido.indexOf("m");
//            int minutos = Integer.parseInt(tiempoConsumido.substring(0, indexM)); System.out.println("min-->> "+minutos);
            montoH += montoMinuto(tiempoConsumido, 0); 
//            System.out.println("monto-->> "+montoH);
        }

        return montoH;
    }

    private String calcHourMin(long min) {
        long numHora = 0;
        long minMod = 0;
        long numMin = 0;
        String equi = "";
        if (min >= 60) {
            numHora = (min / 60);
//            System.out.println("numH " + numHora);
            minMod = (min % 60);
//            System.out.println("minM " + minMod);
            if (minMod == 0) {
                equi = numHora + "H";
            } else {
                equi = numHora + "H" + minMod + "mn";
            }
        } else {
            minMod = min;
            equi = minMod + "mn";
        }
        return equi;
    }

    private void fullDatosTres() {
        kerry.removeDatosOnTable(tablemodelTres);
        SwingWorker<Void, Void> workers;
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
String sql = "select codigo,placa,hora_entrada,hora_salida from ENTRADA_VS_SALIDA_VIEW where HORA_SALIDA is null and  placa like '%" + search.getText().trim() + "%' ";
//                System.out.println("sql " + sql);
                ResultSet rs = kerry.Rezulta(sql);
                while (rs.next()) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    String codigo = rs.getString("codigo");
//                    String dia = rs.getString("dia") + " ";
                    String placa = rs.getString("placa");
                    String hora_entrada = rs.getString("hora_entrada");
                    Date fechaInicio = kerry.StringToDate(hora_entrada);
                    String dia = hora_entrada.substring(0, hora_entrada.indexOf(" "));
                    
                    String horaCons = null;
                    
                    if(checkEstado(placa, dia).equals("Entro")){
                    horaCons = calcHourMin(getDateDiff(fechaInicio, TimeUnit.MINUTES));
                    }
                    else{
                    horaCons = checkTiempoConsumido(placa, dia);
                    }
                    
                    String estado = checkEstado(placa, dia);
                    
                    double monto = 0.00;
                    if(checkEstado(placa, dia).equals("Entro")){
                    monto = montoCount(horaCons);
                    }
                    else{
                    monto = checkMonto(placa, dia);
                    }
                    
                    String salida = rs.getString("hora_salida");
                    Object[] DatosTres = {codigo, placa, hora_entrada, estado, horaCons, monto + " $$", salida};
//                    System.out.println("-- "+Arrays.toString(DatosTres));
                    tablemodelTres.addRow(DatosTres);
                }
                jLabel10.setText("TOTAL : "+totlAmountIn(jTable3, 5)+" $$");
                setCursor(Cursor.getDefaultCursor());

                return null;
            }
        };
        workers.execute();
    }

    public static long getDateDiff(Date date1, TimeUnit timeUnit) {
        Date date2 = new Date();
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    private void saveDos() {
        if (jTextField1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(monedaCombo, "Ingrese el una informacion valida");
            jTextField1.setBackground(Color.red);
            jTextField1.requestFocus();
            return;
        }
        
        String [] text = {jTextField1.getText().trim()};
        if(kerry.isElementExistInGrid(jTable2, text)){
            JOptionPane.showMessageDialog(rootPane, "Esta Placa ha sido registrada en el sistema por favor verifique");
            jTextField1.setBackground(Color.red);
            jTextField1.requestFocus();
            return;
        }

            String sql = "";
            String placa = jTextField1.getText().trim();
            if (jCheckBox1.isSelected()) {
                sql = "INSERT INTO ENTRADA (PLACA, HORA_ENTRADA)VALUES ( '" + placa + "', " + time + ")";
            } else {
                sql = "INSERT INTO ENTRADA (PLACA, HORA_ENTRADA)VALUES ('" + placa + "', '" + time + "')";
            }
//            System.out.println(" sql -->> " + sql);
            
            if(kerry.ejecutarSql(sql) ==1){
            JOptionPane.showMessageDialog(null, "La Placa '"+placa+"' ha sido registrado con successo");
            fullDatosDos();
            jTextField1.setText(null);
             }

            
    }

    private void fullDatosDos() {
        kerry.removeDatosOnTable(tablemodelDos);
        SwingWorker<Void, Void> workers;
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                String sql = "select codigo,placa,hora_entrada from ENTRADA_VS_SALIDA_VIEW where hora_salida is null ";
//                System.out.println("sql " + sql);
                ResultSet rs = kerry.Rezulta(sql);
                while (rs.next()) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    String codigo = rs.getString("codigo");
//                    String dia = rs.getString("dia") + " ";
                    String placa = rs.getString("placa");
                    String hora_entrada = rs.getString("hora_entrada");
                    Date fechaInicio = kerry.StringToDate(hora_entrada);
                    String horaCons = calcHourMin(getDateDiff(fechaInicio, TimeUnit.MINUTES));
                    String dia = hora_entrada.substring(0, hora_entrada.indexOf(" "));
                    String estado = checkEstado(placa, dia);
                    Object[] DatosDos = {codigo, placa, hora_entrada, estado, horaCons};
                    tablemodelDos.addRow(DatosDos);
                }
                setCursor(Cursor.getDefaultCursor());

                return null;
            }
        };
        workers.execute();
    }

    private void horaChecked() {
        if (jCheckBox1.isSelected()) {
            jCheckBox1.setText("Ahora");
            jSpinner1.setVisible(false);
            time = "CURRENT_TIMESTAMP";
        } else {
            jCheckBox1.setText("Mencionar Hora");
            jSpinner1.setVisible(true);

            
            String day = kerry.dateToMysqlDate(new Date());
            System.out.println("SPINNER SET "+jSpinner1.getValue());
            time = day+" " + kerry.HOURFormat(jSpinner1);
        }
    }

    private void estado() {
        if (precioTextField.getText().isEmpty()) {
            save.setEnabled(false);
            exit.setEnabled(false);
        } else {
            save.setEnabled(true);
            exit.setEnabled(true);
        }
    }

    private void nuevo() {
        tiempoCombo.setSelectedIndex(0);
        precioTextField.setText(null);
        monedaCombo.setSelectedIndex(1);
    }

    private void save() {

        if (tiempoCombo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(rootPane, "Seleccione un intevalo valido por favor");
            tiempoCombo.setBackground(Color.red);
            tiempoCombo.requestFocus();
            return;
        }

        if (precioTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Digite un precio valido por favor");
            precioTextField.setBackground(Color.red);
            precioTextField.requestFocus();
            return;
        }

        if (monedaCombo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(rootPane, "Seleccione una moneda valida por favor");
            monedaCombo.setBackground(Color.red);
            monedaCombo.requestFocus();
            return;
        }

            String tiempo = tiempoCombo.getSelectedItem().toString().trim();
            double precio = Double.valueOf(precioTextField.getText().trim());
            String moneda = monedaCombo.getSelectedItem().toString().trim();

            String sql = "INSERT INTO TIEMPO_VS_PRECIO (TIEMPO, PRECIO, MONEDA) VALUES ('" + tiempo + "', " + precio + ", '" + moneda + "')";
//            System.out.println("sql ->> " + sql);
            kerry.ejecutarSql(sql);

            nuevo.doClick();
    }

    private void fullDatos() {
        kerry.removeDatosOnTable(tablemodel);
        SwingWorker<Void, Void> workers;
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                String sql = "select * from TIEMPO_VS_PRECIO";
//                System.out.println("sql " + sql);
                ResultSet rs = kerry.Rezulta(sql);
                while (rs.next()) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    String tiempo = rs.getString("tiempo");
                    String precio = rs.getString("precio") + " ";
                    String moneda = rs.getString("moneda");

                    String[] Datos = {tiempo, precio + moneda};
                    tablemodel.addRow(Datos);
                }
                setCursor(Cursor.getDefaultCursor());

                return null;
            }
        };
        workers.execute();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPopupMenu4 = new javax.swing.JPopupMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tiempoCombo = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        precioTextField = new javax.swing.JTextField();
        monedaCombo = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        save = new javax.swing.JButton();
        nuevo = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        jPanel2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSpinner1 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        jPanel16 = new javax.swing.JPanel();
        search = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        jPanel21 = new javax.swing.JPanel();
        search1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-synchronize_1.png"))); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-synchronize_1.png"))); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem2);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-synchronize_1.png"))); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem3);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-synchronize_1.png"))); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu4.add(jMenuItem4);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setComponentPopupMenu(jPopupMenu1);

        jPanel4.setBackground(new java.awt.Color(0, 204, 204));
        jPanel4.setBorder(new org.edisoncor.gui.util.DropShadowBorder());
        jPanel4.setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-expensive_2_filled.png"))); // NOI18N
        jLabel1.setText("Precio por intervalo de Tiempo");
        jPanel4.add(jLabel1, "card2");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Tiempo : ");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        tiempoCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar Tiempo", "15 minutos", "30 minutos", "60 minutos", "Otros" }));
        tiempoCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tiempoComboMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tiempoCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(tiempoCombo)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Precio : ");

        precioTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                precioTextFieldMouseClicked(evt);
            }
        });
        precioTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                precioTextFieldActionPerformed(evt);
            }
        });
        precioTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                precioTextFieldKeyTyped(evt);
            }
        });

        monedaCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "$$", "$", "Otros" }));
        monedaCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monedaComboMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(precioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(monedaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(monedaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(precioTextField)))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-save.png"))); // NOI18N
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        jPanel9.add(save);

        nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-plus.png"))); // NOI18N
        nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoActionPerformed(evt);
            }
        });
        jPanel9.add(nuevo);

        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-cancel.png"))); // NOI18N
        jPanel9.add(exit);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(174, 174, 174)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(356, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(jTable1);

        jPanel6.add(jScrollPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Precio", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setComponentPopupMenu(jPopupMenu2);

        jPanel10.setBackground(new java.awt.Color(0, 204, 204));
        jPanel10.setBorder(new org.edisoncor.gui.util.DropShadowBorder());

        jLabel4.setFont(new java.awt.Font("Lucida Sans", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-garage_filled.png"))); // NOI18N
        jLabel4.setText("Control de Entrada");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        jLabel5.setText("Numero de placa : ");
        jPanel11.add(jLabel5);

        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel11.add(jTextField1);

        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jPanel11.add(jCheckBox1);

        jSpinner1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSpinner1KeyReleased(evt);
            }
        });
        jPanel11.add(jSpinner1);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-login_rounded.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton1);

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable2.setComponentPopupMenu(jPopupMenu2);
        jScrollPane2.setViewportView(jTable2);

        jPanel12.add(jScrollPane2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 867, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Control de Entrada", jPanel2);

        jPanel13.setComponentPopupMenu(jPopupMenu3);

        jPanel14.setBackground(new java.awt.Color(0, 204, 204));
        jPanel14.setBorder(new org.edisoncor.gui.util.DropShadowBorder());

        jLabel6.setFont(new java.awt.Font("Lucida Sans", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-garage_filled.png"))); // NOI18N
        jLabel6.setText("Control de Salida");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(519, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.LINE_AXIS));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable3.setComponentPopupMenu(jPopupMenu3);
        jScrollPane3.setViewportView(jTable3);

        jPanel15.add(jScrollPane3);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel16.setLayout(new java.awt.GridLayout(1, 0));

        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchKeyTyped(evt);
            }
        });
        jPanel16.add(search);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-logout_rounded.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton2);

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("jLabel10");
        jLabel10.setBorder(new javax.swing.border.LineBorder(java.awt.Color.green, 2, true));
        jLabel10.setBounds(new java.awt.Rectangle(0, 10, 0, 10));
        jPanel16.add(jLabel10);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Control de Salida", jPanel13);

        jPanel18.setComponentPopupMenu(jPopupMenu3);

        jPanel19.setBackground(new java.awt.Color(0, 204, 204));
        jPanel19.setBorder(new org.edisoncor.gui.util.DropShadowBorder());

        jLabel7.setFont(new java.awt.Font("Lucida Sans", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-garage_filled.png"))); // NOI18N
        jLabel7.setText("Control General");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(519, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.LINE_AXIS));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable4.setComponentPopupMenu(jPopupMenu4);
        jScrollPane4.setViewportView(jTable4);

        jPanel20.add(jScrollPane4);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel21.setLayout(new java.awt.GridLayout(1, 0));

        search1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                search1KeyTyped(evt);
            }
        });
        jPanel21.add(search1);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search.png"))); // NOI18N
        jPanel21.add(jLabel8);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("jLabel9");
        jLabel9.setBorder(new javax.swing.border.LineBorder(java.awt.Color.green, 2, true));
        jPanel21.add(jLabel9);
        jPanel21.add(jDateChooser1);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE))
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(422, Short.MAX_VALUE))
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                    .addGap(0, 93, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                    .addContainerGap(58, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(390, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Control General", jPanel17);

        jPanel3.add(jTabbedPane1);

        getContentPane().add(jPanel3);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void precioTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precioTextFieldActionPerformed
        save.doClick();
    }//GEN-LAST:event_precioTextFieldActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        save();
        fullDatos();
    }//GEN-LAST:event_saveActionPerformed

    private void nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_nuevoActionPerformed

    private void tiempoComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tiempoComboMouseClicked
        tiempoCombo.setBackground(Color.WHITE);
    }//GEN-LAST:event_tiempoComboMouseClicked

    private void precioTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_precioTextFieldMouseClicked
        precioTextField.setBackground(Color.WHITE);
    }//GEN-LAST:event_precioTextFieldMouseClicked

    private void precioTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precioTextFieldKeyTyped
        precioTextField.setBackground(Color.WHITE);
        estado();
    }//GEN-LAST:event_precioTextFieldKeyTyped

    private void monedaComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_monedaComboMouseClicked
        monedaCombo.setBackground(Color.WHITE);
    }//GEN-LAST:event_monedaComboMouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
         jButton1.doClick();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jSpinner1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSpinner1KeyReleased
//        horaAutomated();
    }//GEN-LAST:event_jSpinner1KeyReleased

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        horaChecked();
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveDos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void searchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyTyped
        fullDatosTres();
    }//GEN-LAST:event_searchKeyTyped

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        fullDatos();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        fullDatosDos();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        fullDatosTres();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        salida();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void search1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search1KeyTyped
        fullDatosQuatro();
    }//GEN-LAST:event_search1KeyTyped

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        fullDatosQuatro();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        jTextField1.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        jTextField1.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        jButton1.setEnabled(isTextieldEmpty(jTextField1));
    }//GEN-LAST:event_jTextField1KeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormOne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormOne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormOne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormOne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormOne().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JPopupMenu jPopupMenu4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JComboBox monedaCombo;
    private javax.swing.JButton nuevo;
    private javax.swing.JTextField precioTextField;
    private javax.swing.JButton save;
    private javax.swing.JTextField search;
    private javax.swing.JTextField search1;
    private javax.swing.JComboBox tiempoCombo;
    // End of variables declaration//GEN-END:variables
}
