import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

//Arkady Kokush
//Date: Oct 28, 2024

public class SnakeLimb
{
    private int x, y, dx, dy, w, h;
    private int upKey, downKey, leftKey, rightKey;
    private int direction;
     static final int UP = 1;
     static final int LEFT = 2;
     static final int DOWN = 3;
     static final int RIGHT = 4;

    public SnakeLimb()
    {
        x = 400;
        y = 300;
        w = 25;
        h = 25;
        dx = w;
        dy = h;
    }

    public SnakeLimb(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        dx = w;
        dy = h;
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

    public int getDx()
    {
        return dx;
    }

    public void setDx(int dx)
    {
        this.dx = dx;
    }

    public int getDy()
    {
        return dy;
    }

    public void setDy(int dy)
    {
        this.dy = dy;
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

    public int getUpKey()
    {
        return upKey;
    }

    public void setUpKey(int upKey)
    {
        this.upKey = upKey;
    }

    public int getLeftKey()
    {
        return leftKey;
    }

    public void setLeftKey(int leftKey)
    {
        this.leftKey = leftKey;
    }

    public int getRightKey()
    {
        return rightKey;
    }

    public void setRightKey(int rightKey)
    {
        this.rightKey = rightKey;
    }


    public int getDirection()
    {
        return direction;
    }

    public void setDirection(int direction)
    {
        this.direction = direction;
    }

    public void draw(Graphics2D g2)
    {
        g2.fillRect(x, y, 25, 25);
    }

    public boolean equals(Object obj)
    {
        SnakeLimb limb = (SnakeLimb) obj;
        if (limb.getX() == this.getX() && limb.getY() == this.getY())
            return true;
        return false;
    }

}
