package Game;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;


public class MemoryGame extends JFrame implements ActionListener {
    int startPointX, startPointY, countFunctionImage;
    int widthIcon = 120, heightIcon = 170, borderIcon = 5;
    double dividingScaleIcon = 0.8;
    int clickCount;
    JButton buttonHelp;
    int timesRemoveCouple = 0;
    int count = 0, id, preX, preY, X, Y;
    int level = 0, hit = 0, h;
    int sizeX[] = { 2, 2, 2, 3, 4, 4, 4, 4, 4, 4,2 };  // number row of level i = sizeX[i]
    int sizeY[] = { 3, 4, 6, 6, 6, 7, 8, 9, 10, 11,3 }; // number column of level i = sizeY[i]
    int maxTime = 30, time = 0;
    int row = 0, column = 0;
    private JProgressBar progressTime;
    private JButton matrixIconButtons[][];
    private boolean tick[][];
    private int matrixNumber[][];
    private JButton score_bt;
    private JPanel pn, pn2;
    Container cn;
    Timer timer, timer2;
    private int totalBackground = 9, totalIcon = 50;

    public MemoryGame(int k, int score, int timeRemove) {

        dividingScaleIcon= (k>3)?0.8:1;
        widthIcon =(int) (widthIcon * dividingScaleIcon);
        heightIcon =(int) (heightIcon * dividingScaleIcon);
        this.setTitle("Memory game");
        level = k;
        maxTime =sizeX[k]*sizeY[k]*2;
        cn = init(k, score, timeRemove);
        timer = new Timer(240, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                open();
                timer.stop();
            }
        });

        timer2 = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                time++;
                progressTime.setValue(maxTime - time);
                if (maxTime == time - 20) {
                    timer2.stop();
                    showDialogNewGame("Game over.\n" + "Score: " + score_bt.getText() + "\n" + "Do you want to retry?",
                            "Notification");
                }
            }
        });
    }

    public void open() {

        // If the icon is after 50, 300 or 100 points will be added
        if (matrixNumber[X][Y] > totalIcon) {
            clickCount--;
            score_bt.setText(
                    String.valueOf(Integer.parseInt(score_bt.getText()) + (matrixNumber[X][Y] == 51 ? 300 : 100)));
            RemoveTwoIcon(X, Y, X, Y);
            tick[X][Y] = false;
            hit++;
            if (hit == row * column / 2 + countFunctionImage / 2) {
                timer.stop();
                timer2.stop();

                buttonHelp.setEnabled(true);
                timesRemoveCouple++;
                buttonHelp.setText("Remove 1 couple" + "(" + (timesRemoveCouple) + ")");
                nextGame();
            }

        } else {
            clickCount = 0;
            if (id == matrixNumber[X][Y]) {
                RemoveTwoIcon(X, Y, preX, preY);
                tick[X][Y] = tick[preX][preY] = false;
                matrixIconButtons[X][Y].setVisible(false);
                score_bt.setText(String.valueOf(Integer.parseInt(score_bt.getText()) + 10));
                hit++;
                if (hit == row * column / 2 + countFunctionImage / 2) {
                    timer.stop();
                    timer2.stop();

                    buttonHelp.setEnabled(true);
                    timesRemoveCouple++;
                    buttonHelp.setText("Remove 1 couple" + "(" + (timesRemoveCouple) + ")");
                    nextGame();
                }
            } else {
                matrixIconButtons[preX][preY].setIcon(getIcon(-1));
                matrixIconButtons[X][Y].setIcon(getIcon(-1));
                tick[preX][preY] = true;
                tick[X][Y] = true;
                score_bt.setText(String.valueOf(Integer.parseInt(score_bt.getText()) - 5));
            }
        }
    }

    public Container init(int k, int score, int timeRemove) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        timesRemoveCouple = timeRemove;
        startPointX = 50;
        startPointY = 20;
        row = sizeX[k];
        column = sizeY[k];
        maxTime = column*row*2 * 10;
        matrixIconButtons = new JButton[row][column];
        tick = new boolean[row][column];
        matrixNumber = new int[row][column];
        time = 0;
        Container cn = this.getContentPane();

        this.setSize(screenSize.width, screenSize.height);
        int startPointX = (screenSize.width - column * widthIcon - borderIcon * (column - 1) - 40) / 2;
        int startPointY = (screenSize.height - row * heightIcon - borderIcon * (row - 1) - 40) / 2;

        pn = new JPanel();
        pn.setLayout(null);

        pn.setOpaque(false);

        for (int i = 0; i < row; i++)
            for (int j = 0; j < column; j++) {

                matrixIconButtons[i][j] = new JButton();
                pn.add(matrixIconButtons[i][j]);
                matrixIconButtons[i][j].setActionCommand(i + " " + j);
                matrixIconButtons[i][j].setBounds(j * (widthIcon + borderIcon) + startPointX,
                        i * (heightIcon + borderIcon) + startPointY, widthIcon, heightIcon);
                matrixIconButtons[i][j].addActionListener(this);
                matrixNumber[i][j] = (int) (Math.random() * 2 + 1);
                matrixIconButtons[i][j].setIcon(getIcon(-1));
                matrixIconButtons[i][j].setVisible(true);
                tick[i][j] = true;
            }

        pn.add(SetBackground(screenSize));
        pn.setBounds(startPointX, startPointY, column * widthIcon + borderIcon * (column - 1),
                row * heightIcon + borderIcon * (row - 1));
        pn2 = new JPanel();
        pn2.setLayout(new FlowLayout());
        score_bt = new JButton(String.valueOf(score));
        score_bt.setFont(new Font("UTM Nokia", 1, 20));
        score_bt.setBackground(Color.white);
        JLabel score_lb = new JLabel("Score: ");
        score_lb.setFont(new Font("UTM Nokia", 1, 20));

        buttonHelp = new JButton("Remove 1 couple");
        if (timesRemoveCouple == 0)
            buttonHelp.setEnabled(false);
        buttonHelp.setText("Remove 1 couple" + "(" + String.valueOf(timesRemoveCouple) + ")");
        buttonHelp.setFont(new Font("UTM Nokia", 1, 20));
        buttonHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = -2, tempX = 0, tempY = 0;
                for (int i = 0; i < row; i++)
                    for (int j = 0; j < column; j++)
                        if (matrixNumber[i][j] > 0 && matrixNumber[i][j] <= totalIcon) {
                            if (index == -2) {
                                index = matrixNumber[i][j];
                                tempX = i;
                                tempY = j;
                            } else if (index == matrixNumber[i][j]) {
                                if (id == matrixNumber[i][j]) {
                                    count = 0;
                                    clickCount = 0;
                                }
                                RemoveTwoIcon(i, j, tempX, tempY);
                                tick[i][j] = tick[tempX][tempY] = false;

                                score_bt.setText(String.valueOf(Integer.parseInt(score_bt.getText()) + 100));
                                hit++;

                                timesRemoveCouple--;
                                if (timesRemoveCouple == 0)
                                    buttonHelp.setEnabled(false);
                                buttonHelp.setText("Remove 1 couple" + "(" + String.valueOf(timesRemoveCouple) + ")");

                                if (hit == row * column / 2 + countFunctionImage / 2) {
                                    timer.stop();
                                    timer2.stop();

                                    buttonHelp.setEnabled(true);
                                    timesRemoveCouple++;
                                    buttonHelp
                                            .setText("Remove 1 couple" + "(" + String.valueOf(timesRemoveCouple) + ")");
                                    nextGame();
                                }
                                return;
                            }
                        }
                JOptionPane.showMessageDialog(new JFrame(), "There was no couple");
            }
        });

        pn2.add(score_lb);
        pn2.add(score_bt);
        pn2.add(buttonHelp);
        progressTime = new JProgressBar(0, maxTime);
        progressTime.setValue(maxTime);
        progressTime.setForeground(Color.orange);

        matrixNumber = new CreatMatrixNumber(row, column, totalIcon).getMatrix();
        countFunctionImage=CountFuctionImage(matrixNumber);
        cn.add(pn);
        cn.add(progressTime, "North");
        cn.add(pn2, "South");
        this.setVisible(true);
        this.setResizable(false);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        return cn;
    }


    public void showMatrix() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++)
                System.out.printf("%3d", matrixNumber[i][j]);
            System.out.println();
        }
        System.out.println("-----------------");
        System.out.println();
    }

    private Icon getIcon(int index) {
        int width = widthIcon, height = heightIcon;
        Image image = new ImageIcon(getClass().getResource("/Game/icon/icon" + index + ".jpg")).getImage();
        Icon icon = new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return icon;
    }

    public void newGame() {
        this.dispose();
        new  MemoryGame(0, 100, 1);
    }

    public void nextGame() {

        if(level >= sizeX.length-1 || level >= sizeY.length){
            showDialogNewGame("You win.\n" + "Score: " + score_bt.getText() + "\n" + "Do you want to retry?",
                    "Notification");
            this.dispose();
            return;
        }
        this.dispose();
        new MemoryGame(level + 1, Integer.parseInt(score_bt.getText()) + (maxTime - time), timesRemoveCouple);
    }

    public void showDialogNewGame(String message, String title) {
        int select = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (select == 0) {
            newGame();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (clickCount > 1 || maxTime <= time - 10)
            return;
        timer2.start();
        int i, j;
        String s = e.getActionCommand();
        int k = s.indexOf(32);
        i = Integer.parseInt(s.substring(0, k));
        j = Integer.parseInt(s.substring(k + 1, s.length()));
        if (tick[i][j]) {
            tick[i][j] = false;
            clickCount++;
            if (count == 0) {

                if (matrixNumber[i][j] > totalIcon) {
                    matrixIconButtons[i][j].setIcon(getIcon(matrixNumber[i][j]));
                    X = i;
                    Y = j;
                    timer.start();
                    return;
                }
                matrixIconButtons[i][j].setIcon(getIcon(matrixNumber[i][j]));
                id = matrixNumber[i][j];
                preX = i;
                preY = j;
            } else {
                if (matrixNumber[i][j] > totalIcon) {
                    matrixIconButtons[i][j].setIcon(getIcon(matrixNumber[i][j]));
                    X = i;
                    Y = j;
                    timer.start();
                    return;
                }
                matrixIconButtons[i][j].setIcon(getIcon(matrixNumber[i][j]));
                X = i;
                Y = j;
                timer.start();
            }
            count = 1 - count;
        }
    }

    public JLabel SetBackground(Dimension screenSize) {
        Image img;
        JLabel labelImage = null;

        try {

            Random random = new Random();
            random.setSeed(java.time.LocalTime.now().getNano());
            int icon = random.nextInt(totalBackground);

            img = ImageIO.read(getClass().getResource("/Game/background/background" + icon + ".jpg"));
            BufferedImage bffImage = (BufferedImage) img;
            bffImage = cropImageIcon(bffImage, screenSize.width, screenSize.height);
            labelImage = new JLabel(
                    new ImageIcon(bffImage.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH)));

            labelImage.setBounds(0, 0, screenSize.width, screenSize.height);
            return labelImage;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return labelImage;

    }

    public BufferedImage cropImageIcon(BufferedImage bffImage, int widthScale, int heightScale) {
        int widthImage = bffImage.getWidth();
        int heightImage = bffImage.getHeight();
        int newHeightImage, newWidthImage;
        if (heightImage * widthScale > widthImage * heightScale) {
            newHeightImage = widthImage * heightScale / widthScale;
            bffImage = bffImage.getSubimage(0, (heightImage - newHeightImage) / 2, widthImage, newHeightImage);
        } else {
            newWidthImage = heightImage * widthScale / heightScale;
            bffImage = bffImage.getSubimage((widthImage - newWidthImage) / 2, 0, newWidthImage, heightImage);
        }
        return bffImage;
    }

    private void RemoveTwoIcon(int i, int j, int preI, int preJ) {
        matrixIconButtons[i][j].setVisible(false);
        matrixIconButtons[preI][preJ].setVisible(false);
        matrixNumber[i][j] = -1;
        matrixNumber[preI][preJ] = -1;
    }

    public int CountFuctionImage(int a[][]) {
        int count=0;
        for(int i=0;i<row;i++)
            for(int j=0;j<column;j++)
                if(a[i][j]> totalIcon)
                    count++;
        return count;
    }
    public static void main(String[] args) {
        new  MemoryGame(0, 100, 1);
    }
}
