import java.awt.FlowLayout;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Program extends JFrame implements ChangeListener
{
	public static void main(String[] args) throws IOException
	{
        Program prog = new Program();
        prog.analyzeImage();
	}
	
	JLabel primary = new JLabel("Primary Color"), secondary = new JLabel("Secondary Color"), detail = new JLabel("Detailed Color");
	JSlider distinctMin = new JSlider(JSlider.HORIZONTAL, 0, 255, 50);
	ColorRecognition rec = new ColorRecognition("C:\\Users\\Stevie\\Desktop\\images.jpeg");
	
	public Program() throws IOException
	{
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(400, 400);
        super.setLayout(new FlowLayout());
       
        super.getContentPane().add(primary);
        super.getContentPane().add(secondary);
        super.getContentPane().add(detail);
                
        distinctMin.setMajorTickSpacing(10);
		distinctMin.setMinorTickSpacing(1);
		distinctMin.addChangeListener(this);
		super.getContentPane().add(distinctMin);
		
		rec.smartAnalyze();
		setComponentColorsAndProperties();
		
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
	
	private void setComponentColorsAndProperties()
	{
		super.getContentPane().setBackground(rec.getBackgroundColor());
        primary.setForeground(rec.getPrimaryColor());
        secondary.setForeground(rec.getSecondaryColor());
		detail.setForeground(rec.getDetailColor());
		
		distinctMin.setBackground(rec.getBackgroundColor());
		distinctMin.setValue(rec.getDistinctMin());
	}
	
	public void analyzeImage() throws IOException
	{
		rec.analyzeImage();
		setComponentColorsAndProperties();
	}
}
