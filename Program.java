import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;

public class Program extends JFrame implements ChangeListener
{
    public static void main(String[] args) throws IOException
    {
        Program prog = new Program();
        prog.analyzeImage();
    }
    
    JLabel image = new JLabel(), primary = new JLabel("Primary Color"), secondary = new JLabel("Secondary Color"), detail = new JLabel("Detailed Color");
    JSlider distinctMin = new JSlider(JSlider.HORIZONTAL, 0, 255, 50);
    JButton button = new JButton("Load Image");
    String path;
    ColorRecognition rec;
    
    public Program() throws IOException
    {
        super("Color Recognition");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(400, 400);
        super.setLayout(new FlowLayout());
       
        super.getContentPane().add(primary);
        super.getContentPane().add(secondary);
        super.getContentPane().add(detail);
        
        ActionListener listener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    path = fc.getSelectedFile().getAbsolutePath();
                    
                    rec = new ColorRecognition(path);
                    try
                    {
                        rec.smartAnalyze();
                        setComponentColorsAndProperties(true);
                    }
                    catch(Exception ex)
                    {
                    }
                }
            }
        };
        button.addActionListener(listener);
        super.getContentPane().add(button);
                
        distinctMin.setMajorTickSpacing(10);
        distinctMin.setMinorTickSpacing(1);
        distinctMin.addChangeListener(this);
        super.getContentPane().add(distinctMin);
        
        super.getContentPane().add(image); 
        
        super.setVisible(true);
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        int distinct = (int)source.getValue();
        rec.setDistinctMin(distinct);
        
        try
        {
            analyzeImage();
        }
        catch (IOException e1)
        {
            System.out.println("Failed to analyze image. Error: " + e1.getMessage());
        }
    }
    
    private void setComponentColorsAndProperties(boolean updateAll)
    {
        if (updateAll)
        {
            ImageIcon imageIcon = new ImageIcon(path);
            image.setIcon(imageIcon);
    
            super.setSize(rec.getWidth() + 100, rec.getHeight() + 100);
        }
        
        super.getContentPane().setBackground(rec.getBackgroundColor());
        primary.setForeground(rec.getPrimaryColor());
        secondary.setForeground(rec.getSecondaryColor());
        detail.setForeground(rec.getDetailColor());
        
        distinctMin.setBackground(rec.getBackgroundColor());
        distinctMin.setValue(rec.getDistinctMin());
    }
    
    public void analyzeImage() throws IOException
    {
        if (rec == null)
            return;
            
        rec.analyzeImage();
        setComponentColorsAndProperties(false);
    }
}
