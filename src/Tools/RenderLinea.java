package Tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import  javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author josephandyfeidje
 */


public class RenderLinea extends DefaultTableCellRenderer
{
    Font normal = new Font( "Arial",Font.BOLD,16);
   @Override
   public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column)
   
   {
      
        String sts = "";
        table.setRowSelectionAllowed(true); 
        setBackground(table.getBackground());
        setForeground(table.getForeground());
        
        try {
           sts = table.getValueAt(row, 3).toString();
            try {
              sts = sts.substring(0, sts.indexOf(" "));  
            } catch (StringIndexOutOfBoundsException e) {
            }
           
       } catch (NullPointerException e) {
       }
      
      if ( sts.equals("Entro") && column ==3 )
      {
         setBackground(Color.YELLOW);
          setFont(normal);
      } 
      else if(sts.equals("Salio") && column ==3){
          setBackground(Color.GREEN);
          setFont(normal);
      }

        super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
        return this;
   }
}