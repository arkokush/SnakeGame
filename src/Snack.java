import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Arkady Kokush
//Date: Oct 29, 2024

public class Snack
{
    private int x, y, w, h;
    private BufferedImage snack;

    public Snack(int x, int y, int w, int h)
    {
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
    }

    public Rectangle getHitbox()
    {
        return new Rectangle(x, y, w, h);
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getW()
    {
        return w;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public void draw(Graphics2D g2)
    {
        try
        {
            snack = ImageIO.read(getClass().getResourceAsStream("/OrangeSnack.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        g2.drawImage(snack, x, y, 25, 25, null);
    }
}
