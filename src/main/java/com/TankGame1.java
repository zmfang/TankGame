package java.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * Created by 方贞铭 on 2017/5/31.
 */
public class TankGame1 extends JFrame{
    MyPanel mp=null;
    public static void main(String[] args)
    {
        TankGame1 tankGame1=new TankGame1();
    }
    public TankGame1() {
        mp=new MyPanel();
        Thread thread1=new Thread(mp);
        thread1.start();
        this.add(mp);

        this.addKeyListener(mp);
        this.setSize(400,300);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }
}

class MyPanel extends JPanel implements KeyListener,Runnable
{

   //判断子弹是否或者
    boolean islive =true;
    //定义一个我的坦克



    Hero hero=null;

    //定义敌人的坦克
    Vector<EnemyTank> enemyTanks=new Vector<EnemyTank>();
    int enSize=3;
    //定义炸弹集合
    Vector<Bomb> bombs=new Vector<Bomb>();

    //定义三战图片，组合成一个炸弹
    Image image1=null;
    Image image2=null;
    Image image3=null;

    //构造函数
    public MyPanel()
    {
        hero=new Hero(100,100);
        for(int i=0;i<enSize;i++)
        {
            EnemyTank enTn=new EnemyTank((i+1)*50,0);
            enTn.setColor(0);
            enTn.setDirect(2);
            //启动敌人的坦克
            Thread thread=new Thread(enTn);
            thread.start();
            //给敌人坦克添加一颗子弹
            Shot shot=new Shot(enTn.x+10,enTn.y+30,2);
            //加入给敌人
            enTn.shots.add(shot);
            Thread thread1=new Thread(shot);
            thread1.start();
            enemyTanks.add(enTn);

        }
        //初始化图片
        image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/4059765_102221944000_2.jpg"));
        image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/8889139_234605328146_2.jpg"));
        image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/200841510636189_2.jpg"));
    }
    //重写paint函数
    public void paint(Graphics graphics)
    {
        super.paint(graphics);
        graphics.fillRect(0,0,400,300);
        this.drawTank(this.hero.getX(),this.hero.getY(),graphics,this.hero.getDirect(),1);
        //画出子弹


        for(int i=0;i<this.hero.shotArray.size();i++)
        {
            Shot myShot=hero.shotArray.get(i);
            if(myShot!=null&&myShot.isLive==true)
            {
            graphics.draw3DRect(myShot.x,myShot.y,1,1,false);

            }
            if(myShot.isLive==false)
            {
                this.hero.shotArray.remove(myShot);
            }
        }

        //画出敌人坦克，再画出敌人的子弹
        for(int i=0;i<enemyTanks.size();i++)
        {
            EnemyTank enemyTank=enemyTanks.get(i);
            if(enemyTank.isLive)
            {

                this.drawTank(enemyTank.getX(),enemyTank.getY(),graphics,enemyTank.getDirect(),0);

                for(int j=0;j<enemyTank.shots.size();j++)
                {
                    //取出子弹
                    Shot enemyShot=enemyTank.shots.get(j);
                    if(enemyShot.isLive)
                    {
                        graphics.draw3DRect(enemyShot.x,enemyShot.y,1,1,false);
                    }
                    else
                    {
                        enemyTank.shots.remove(enemyShot);
                    }
                }
            }
        }
        //画出炸弹
        for(int i=0;i<bombs.size();i++)
        {
            //取出炸弹
            Bomb bomb=bombs.get(i);

            if(bomb.life>6)
            {
                graphics.drawImage(image1,bomb.x,bomb.y,30,30,this);

            }else if (bomb.life>4){
                graphics.drawImage(image2,bomb.x,bomb.y,30,30,this);
            }
            else
            {
                graphics.drawImage(image3,bomb.x,bomb.y,30,30,this);
            }
            //让bomb的生命减少
            bomb.lifeDown();
            //如果炸弹生命值==0，死亡
            if(bomb.life==0)
            {
                bombs.remove(bomb);
            }
        }

    }
    //是否击中敌人坦克
    public void hitTank (Shot shot,EnemyTank enemyTank)
    {
        //判断该坦克方向
        switch (enemyTank.direct)
        {
            //向上或下
            case 0:
            case 2:
                if(shot.x>enemyTank.x&&shot.x<enemyTank.x+20&&shot.y<enemyTank.y+30)
                {
                    Bomb bomb=new Bomb(enemyTank.x,enemyTank.y);
                    bombs.add(bomb);
                    //子弹死亡
                    //敌人死亡
                    shot.isLive=false;
                     enemyTank.isLive=false;

                    //创建一颗炸弹，放入Vector

                }
            case 1:
            case 3:
                if(shot.x>enemyTank.x&&shot.x<enemyTank.x+30&&shot.y>enemyTank.y&&shot.y<enemyTank.y+20)
                {
                    Bomb bomb=new Bomb(enemyTank.x,enemyTank.y);
                    bombs.add(bomb);
                    shot.isLive=false;
                    enemyTank.isLive=false;

                    //创建一颗炸弹，放入Vector

                }
        }
    }
        //画出坦克的函数
        public void drawTank(int x,int y,Graphics graphics,int direct,int type)
        {
            switch (type)
            {
                case 0:
                    graphics.setColor(Color.cyan);
                    break;
                case 1:
                    graphics.setColor(Color.YELLOW);
                    break;
            }
            //判断方向
            switch (direct)
            {
                //向上
                case 0:

                    //画出我的坦克(到时再封装)
                    //1，画出左边的矩形
                    graphics.fill3DRect(x,y,5,30,false);
                    //2.画出右边的矩形
                    graphics.fill3DRect(x+15,y,5,30,false);
                    //画出中间矩形
                    graphics.fill3DRect(x+5,y+5,10,20,false);
                    graphics.fillOval(x+5,y+10,10,10);
                    graphics.drawLine(x+10,y+15,x+10,y);
                    break;
                case 1:
                    //炮筒向右
                    graphics.fill3DRect(x,y,30,5,false);
                    graphics.fill3DRect(x,y+15,30,5,false);
                    graphics.fill3DRect(x+5,y+5,20,10,false);
                    graphics.fillOval(x+10,y+5,10,10);
                    graphics.drawLine(x+15,y+10,x+30,y+10);
                    break;
                case 2:
                    //向下
                    //画出我的坦克(到时再封装)
                    //1，画出左边的矩形
                    graphics.fill3DRect(x,y,5,30,false);
                    //2.画出右边的矩形
                    graphics.fill3DRect(x+15,y,5,30,false);
                    //画出中间矩形
                    graphics.fill3DRect(x+5,y+5,10,20,false);
                    graphics.fillOval(x+5,y+10,10,10);
                    graphics.drawLine(x+10,y+15,x+10,y+30);
                    break;
                case 3:
                    //炮筒向zuo
                    graphics.fill3DRect(x,y,30,5,false);
                    graphics.fill3DRect(x,y+15,30,5,false);
                    graphics.fill3DRect(x+5,y+5,20,10,false);
                    graphics.fillOval(x+10,y+5,10,10);
                    graphics.drawLine(x+15,y+10,x,y+10);
                    break;

            }
        }

    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_W)
        {
            //设置我的坦克的反向
            this.hero.setDirect(0);
            this.hero.moveUp();

        }
        else  if(e.getKeyCode()==KeyEvent.VK_D)
        {
            this.hero.setDirect(1);
            this.hero.moveRight();
        }
        else if(e.getKeyCode()==KeyEvent.VK_S)
        {
            this.hero.setDirect(2);
            this.hero.moveDown();
        }
        else if(e.getKeyCode()==KeyEvent.VK_A)
        {
            this.hero.setDirect(3);
            this.hero.moveLeft();
        }
        if(e.getKeyCode()==KeyEvent.VK_J)
        {
            //判断玩家是否按下J
            if(this.hero.shotArray.size()<=4)
            {
            this.hero.shotEnemy();
            }
        }
       repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //判断是否击中
            for(int i=0;i<this.hero.shotArray.size();i++)
            {
                //取出子弹
                Shot myShot =hero.shotArray.get(i);
                //判断子弹是否有效
                if(myShot!=null&&myShot.isLive)
                {
                    //取出每一个敌人坦克，与之判断
                    for(int j=0;j<enemyTanks.size();j++)
                    {
                        //取出坦克
                        EnemyTank enemyTank=enemyTanks.get(j);
                        if(enemyTank.isLive)
                        {
                            this.hitTank(myShot,enemyTank);
                        }
                    }
                }
            }
            //判断是否需要给坦克加入新的坦克
//            for(int i=0;i<enemyTanks.size();i++)
//            {
//                EnemyTank enemyTank=enemyTanks.get(i);
//              if(enemyTank.isLive)
//              {
//                  if(enemyTank.shots.size()<5)
//                  {
//                      Shot shot=null;
//                      //没有子弹
//                      //添加
//                      switch (enemyTank.direct)
//                      {
//                          case 0:
//                              shot=new Shot(enemyTank.x+10,enemyTank.y,0);
//                              enemyTank.shots.add(shot);
//                              break;
//                          case 1:
//                              shot=new Shot(enemyTank.x+30,enemyTank.y+10,1);
//                              enemyTank.shots.add(shot);
//                              break;
//                          case 2:
//                              shot=new Shot(enemyTank.x+10,enemyTank.y+30,2);
//                              enemyTank.shots.add(shot);
//                              break;
//                          case 3:
//                              shot=new Shot(enemyTank.x,enemyTank.y+10,3);
//                              enemyTank.shots.add(shot);
//                              break;
//                      }
//                      //启动线程
//                      Thread thread=new Thread(shot);
//                      thread.start();
//                  }
//              }
//            }

            this.repaint();
        }
    }
}

//坦克类

