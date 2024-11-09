import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

//Arkady Kokush
//Date: Oct 28, 2024

public class Snake
{
    private int direction;
    static final int UP = 1;
    static final int LEFT = 2;
    static final int DOWN = 3;
    static final int RIGHT = 4;
    private int upKey, downKey, leftKey, rightKey;
    private ArrayList<SnakeLimb> limbs;
    private int extend = 3;

    public Snake(int x, int y, int w, int h)
    {
        this(x, y, w, h, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);
    }

    public Snake(int x, int y, int w, int h, int upKey, int downKey, int leftKey, int rightKey)
    {
        this.direction = 0;
        this.limbs = new ArrayList<>();
        this.upKey = upKey;
        this.downKey = downKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        initializeSnake(x, y, w, h);
    }

    private void initializeSnake(int x, int y, int w, int h)
    {
        for (int i = 0; i < 3; i++)
        {
            limbs.add(new SnakeLimb(x, y, w, h));
        }
    }

    public boolean checkSelf()
    {
        if (direction != 0)
        {
            SnakeLimb head = limbs.get(limbs.size() - 1);
            for (int i = 0; i < limbs.size() - 2; i++)
            {
                if (head.equals(limbs.get(i)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hitOther(Snake otherSnake)
    {
        if (direction != 0)
        {
            SnakeLimb head = limbs.get(limbs.size() - 1);
            for (int i = 0; i < otherSnake.size() - 1; i++)
            {
                if (head.equals(otherSnake.get(i)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkCrossBorder(int x, int y)
    {
        return limbs.get(limbs.size() - 1).getX() > x - limbs.get(limbs.size() - 1).getW()
                || limbs.get(limbs.size() - 1).getX() < 0
                || limbs.get(limbs.size() - 1).getY() > y - limbs.get(limbs.size() - 1).getH()
                || limbs.get(limbs.size() - 1).getY() < 0;
    }

    public boolean onSnack(Snack s)
    {
        for (int i = 0; i < limbs.size() - 1; i++)
        {
            if (limbs.get(i).getX() == s.getX() && limbs.get(i).getY() == s.getY())
            {
                return true;
            }
        }
        return false;
    }

    public Rectangle getHitbox()
    {
        return new Rectangle(this.getX(), this.getY(), this.getW(), this.getH());
    }

    public boolean ateSnack(Snack s)
    {
        if (this.getHitbox().intersects(s.getHitbox()))
        {
            extend++;
            return true;
        }
        return false;
    }

    public void keyPress(int key)
    {
        if (key == upKey)// Up
        {
            direction = UP;
        }
        if (key == leftKey)// Left
        {
            direction = LEFT;
        }
        if (key == downKey)// Down
        {
            direction = DOWN;
        }
        if (key == rightKey)// Right
        {
            direction = RIGHT;
        }
    }

    public void draw(Graphics2D g2)
    {
        for (SnakeLimb limb : limbs)
        {
            limb.draw(g2);
        }
    }

 
    public void moveSnake()
    {
        SnakeLimb head = limbs.get(limbs.size() - 1);
        SnakeLimb newHead = new SnakeLimb(head.getX(), head.getY(), head.getW(), head.getH());

        switch (direction)
        {
        case UP:
            newHead.setY(newHead.getY() - 25);
            break;
        case DOWN:
            newHead.setY(newHead.getY() + 25);
            break;
        case LEFT:
            newHead.setX(newHead.getX() - 25);
            break;
        case RIGHT:
            newHead.setX(newHead.getX() + 25);
            break;
        }

        limbs.add(newHead);
        if (extend < limbs.size())
        {
            limbs.remove(0);
        }
    }

    public int getExtend()
    {
        return extend;
    }

    public void setExtend(int extend)
    {
        this.extend = extend;
    }

    public int getDirection()
    {
        return direction;
    }

    public void setDirection(int direction)
    {
        this.direction = direction;
    }

    public int getX()
    {
        return limbs.get(limbs.size() - 1).getX();
    }

    public void setX(int x)
    {
        limbs.get(limbs.size() - 1).setX(x);
    }

    public int getY()
    {
        return limbs.get(limbs.size() - 1).getY();
    }

    public void setY(int y)
    {
        limbs.get(limbs.size() - 1).setX(y);
    }

    public int getW()
    {
        return limbs.get(limbs.size() - 1).getW();
    }

    public void setW(int w)
    {
        limbs.get(limbs.size() - 1).setW(w);
    }

    public int getH()
    {
        return limbs.get(limbs.size() - 1).getH();
    }

    public void setH(int h)
    {
        limbs.get(limbs.size() - 1).setH(h);
    }

    public void clear()
    {
        limbs.clear();
    }

    public int size()
    {
        return limbs.size();
    }

    public SnakeLimb get(int i)
    {
        return limbs.get(i);
    }
}
