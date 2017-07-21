package java.com;

import java.util.Vector;

/**
 * Created by 方贞铭 on 2017/6/4.
 */
//炸弹类
class Bomb
{
    //定义炸弹坐标
    int x;
    int y;

    //炸弹的生命
    int life=9;
    boolean islive=true;
    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //减少生命值
    public void lifeDown()
    {
        if(life>0)
        {
            life--;

        }
        else
        {
            this.islive=false;
        }
    }
}

class Tank
{
    //表示坦克的横坐标
    int x=0;
    //坦克的纵坐标
    int y=0;
    //0表示上
    //1表示右 2表示下 3表示左
    int direct=0;
    //坦克的速度
    int speed=1;

    //颜色
    int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }
}

class Hero extends Tank
{
    //坦克的速度
    //子弹
    //Shot shot=null;
    Vector<Shot> shotArray=new Vector<Shot>();
    Shot shot=null;
    public Hero(int x, int y) {
        super(x, y);

    }
    //开火
    public void shotEnemy()
    {

       switch (this.direct)
       {
           case 0:
               shot=new Shot(x+10,y,0);
               shotArray.add(shot);
               break;
           case 1:
               shot=new Shot(x+30,y+10,1);
               shotArray.add(shot);
               break;
           case 2:
               shot=new Shot(x+10,y+30,2);
               shotArray.add(shot);
               break;
           case 3:
               shot=new Shot(x,y+10,3);
               shotArray.add(shot);
               break;
       }
       //启动子弹线程
        Thread thread=new Thread(shot);
       thread.start();

    }



    //坦克向上移动
    public void moveUp()
    {
        y-=speed;
    }
    public void moveRight()
    {
        x+=speed;
    }
    public void moveDown()
    {
        y+=speed;
    }
    public void moveLeft()
    {
        x-=speed;
    }

}

class EnemyTank extends Tank implements Runnable
{
    Vector<EnemyTank> enemyTanks=new Vector<EnemyTank>();
    //定义一个向量，可以存放敌人的子弹
    Vector<Shot> shots=new Vector<Shot>();
    //敌人添加子弹，应当在刚刚创建坦克和敌人的坦克子弹死亡了过后


    boolean b0=true;

    boolean b1=true;

    boolean b2=true;

    boolean b3=true;
    int times=0;

    Shot shot=null;
    boolean isLive=true;
    public EnemyTank(int x, int y) {
        super(x, y);
    }
    public void setEnemyTanks(Vector<EnemyTank> vv)
    {
        this.enemyTanks=vv;
    }


    public void run() {
        while (true)
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (this.direct) {
                case 0:
                    //说明坦克正在向上走
                    for (int i = 0; i < 30; i++) {
                        if (y > 0) {
                            y -= speed;
                        }

                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }


                    break;
                case 1:
                    //向右边
                    for (int i = 0; i < 30; i++) {

                        if (x < 400) {
                            x += speed;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


                    break;
                case 2:
                    //向下
                    for (int i = 0; i < 30; i++) {
                        if (y < 300) {
                            y += speed;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


                    break;
                case 3:
                    //向左边
                    for (int i = 0; i < 30; i++) {
                        if (x > 0) {
                            x -= speed;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    break;
            }
                //让坦克随机产生一个新的方向
                this.direct=(int)(Math.random()*4);

                //判断敌人坦克是否死亡
                if(this.isLive==false)
                {
                    //让坦克死亡后退出线程
                    break;
                }
            times++;
            if(times%2==0){

                if(shots.size()<=5){
                    shotEnemy();

                }

            }

            }



        }





    public void shotEnemy(){

        switch(this.direct){

            case 0:

                shot=new Shot(x+10, y,0);

                shots.add(shot);

                break;

            case 1:

                shot=new Shot(x+30, y+10,1);

                shots.add(shot);

                break;

            case 2:

                shot=new Shot(x+10, y+30,2);

                shots.add(shot);

                break;

            case 3:

                shot=new Shot(x, y+10,3);

                shots.add(shot);

                break;

        }

        Thread t=new Thread(shot);

        t.start();

    }





}


//子弹类
class Shot implements Runnable
{
    int x;
    int y;
    int direct;
    int speed=1;
    //是否活着
    boolean isLive=true;

    public void run() {
        while (true)
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (direct)
            {
                case 0:
                    //上
                        y-=speed;
                    break;
                case 1:
                    x+=speed;
                    break;
                case 2:
                    y+=speed;
                    break;
                case 3:
                    x-=speed;
                    break;
            }
            //子弹何时死亡
            //判断该子弹是否碰到边缘。
            if(x<0||x>400||y<0||y>300)
            {

                this.isLive=false;
                break;
            }

        }

    }

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }
}
