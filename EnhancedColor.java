import java.awt.Color;

public class EnhancedColor extends Color
{
    private static final int whiteRgbMin = 200;
    private static final int blackRgbMax = 55;
    private static final int indiviualColorDiffMin = 50;
    
    public EnhancedColor(int rgb)
    {
        super(rgb);     
    }
    
    public EnhancedColor(int r, int g, int b, int a)
    {
        super(r, g, b, a);
    }
    
    public boolean isBlackOrWhite()
    {
        int color1 = getRGB();
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = (color1 >> 0) & 0xFF;
        
        return (r1 >= whiteRgbMin && g1 >= whiteRgbMin && b1 >= whiteRgbMin) || (r1 <= blackRgbMax && g1 <= blackRgbMax && b1 <= blackRgbMax);
    }
    
    public boolean isDistinct(EnhancedColor color)
    {
        int color1 = getRGB();
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = (color1 >> 0) & 0xFF;
        
        int color2 = color.getRGB();
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = (color2 >> 0) & 0xFF;

        int rDiff = Math.abs(r1 - r2);
        int gDiff = Math.abs(g1 - g2);
        int bDiff = Math.abs(b1 - b2);
        
        return (rDiff > indiviualColorDiffMin) || (gDiff > indiviualColorDiffMin) || (bDiff > indiviualColorDiffMin);
    }
}
