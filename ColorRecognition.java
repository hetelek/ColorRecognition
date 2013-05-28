import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ColorRecognition
{
    private CountedItem<EnhancedColor>[] allColors = null, edgeColors = null;
    private EnhancedColor backgroundColor, primaryColor, secondaryColor, detailColor;
    private String imagePath;
    private int disctinctMin = 50;
    
    public ColorRecognition(String imagePath)
    {
        this.imagePath = imagePath;
    }
    
    public ColorRecognition(String imagePath, int disctinctMin)
    {
        this.imagePath = imagePath;
        this.disctinctMin = disctinctMin;
    }
    
    public void setDistinctMin(int disctinctMin)
    {
        this.disctinctMin = disctinctMin;
    }
    
    public int getDistinctMin()
    {
    	return this.disctinctMin;
    }
    
    public void analyzeImage() throws IOException
    {
    	if (allColors == null || edgeColors == null)
    	{
	        BufferedImage image = ImageIO.read(new File(imagePath));
	        
	        // get the width and height
	        int width = image.getWidth();
	        int height = image.getHeight();
	        
	        CountedLinkedList<EnhancedColor> edgeColorsList = new CountedLinkedList<EnhancedColor>();
	        CountedLinkedList<EnhancedColor> allColorsList = new CountedLinkedList<EnhancedColor>();
	        
	        int widthJump = (width <= 100) ? 1 : width / 100;
	        int heightJump = (height <= 100) ? 1 : height / 100;
	        
	        // load all the colors
	        for (int x = 0; x < width; x += widthJump)
	        {
	            for (int y = 0; y < height; y += heightJump)
	            {
	                EnhancedColor color = new EnhancedColor(image.getRGB(x, y));
	                
	                // add all left-most side colors to get the background
	                if (x == 0)
	                    edgeColorsList.add(color);
	                allColorsList.add(color);
	            }
	        }
	        
	        image = null;
	        
	        edgeColors = edgeColorsList.getSortedArray();
	        allColors = allColorsList.getSortedArray();
    	}
        
        CountedItem<EnhancedColor> backgroundColor = null;
        
        // get the background color
        if (edgeColors.length > 0)
        {
            backgroundColor = edgeColors[1];
            
            // try and avoid black/white backgrounds
            if (backgroundColor.getItem().isBlackOrWhite())
            {
                for (int i = 1; i < edgeColors.length; i++)
                {
                    // make sure that the current color occures at least 1/3 of the previous color
                    double occurenceDifference = (double)edgeColors[i].getCount() / (double)backgroundColor.getCount();
                    
                    // make sure it's not black or white, and do comparisons
                    if (!edgeColors[i].getItem().isBlackOrWhite() && occurenceDifference > .3)
                        backgroundColor = edgeColors[i];
                    else
                        break;
                }
            }
        }
        
        EnhancedColor enhancedBackgroundColor = backgroundColor.getItem();
        this.backgroundColor = enhancedBackgroundColor;
        
        this.primaryColor = null;
        this.secondaryColor = null;
        this.detailColor = null;
        
        // loop through all the colors, and get the top 3 colors
        for (CountedItem<EnhancedColor> color : allColors)
        {
            boolean isDistinctFromBackground = enhancedBackgroundColor.isDistinct(color.getItem(), disctinctMin);
            // do the primary first, secondary and then detailed (so the primary is most common)
            if (this.primaryColor == null && isDistinctFromBackground)
                this.primaryColor = color.getItem();
            else if (this.secondaryColor == null && this.primaryColor != null && isDistinctFromBackground && primaryColor.isDistinct(color.getItem(), disctinctMin))
                this.secondaryColor = color.getItem();
            else if (this.detailColor == null && this.secondaryColor != null && isDistinctFromBackground && secondaryColor.isDistinct(color.getItem(), disctinctMin) && primaryColor.isDistinct(color.getItem(), disctinctMin))
            {
                this.detailColor = color.getItem();
                break;
            }
        }
        
        if (this.primaryColor == null)
            System.out.println("missed primary");
            
        if (this.secondaryColor == null)
            System.out.println("missed secondary");
            
        if (this.detailColor == null)
            System.out.println("missed detail");
    }
    
    public void smartAnalyze() throws IOException
    {
    	this.disctinctMin = 255;
    	analyzeImage();
    	
    	while (this.disctinctMin-- > 0 && primaryColor == null || secondaryColor == null || detailColor == null)
    		analyzeImage();
    }
    
    public Color getBackgroundColor()
    {
        return this.backgroundColor;
    }
    
    public Color getPrimaryColor()
    {
        return this.primaryColor;
    }
    
    public Color getSecondaryColor()
    {
        return this.secondaryColor;
    }
    
    public Color getDetailColor()
    {
        return this.detailColor;
    }
}