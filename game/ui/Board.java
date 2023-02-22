package game.ui;

import game.core.Utils;
import game.logic.core.Map;
import game.logic.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//******************************************
//Niloufar Zarabian 9912762256
//Kourosh Hassanzadeh 9912762552
//Mohammad Amin Afsharian Shandiz 9912762790
//******************************************

public class Board {

    private boolean canPutRedBead = true;
    private boolean canPutBlueBead = true;
    boolean check = true;

    private Map map;

    private JFrame frame = new JFrame("Game");
    private JButton[][] buttons;

    public Board() {
        JOptionPane.showMessageDialog(null, "Hello, Welcome to the game.");

        String widthStr = JOptionPane.showInputDialog(frame, "Enter the number of rows : ");
        if (widthStr.length() == 0) {
            widthStr = JOptionPane.showInputDialog(frame, "Enter the number of rows again: ");
        }
        final int width = Integer.parseInt(widthStr);

        String heightStr = JOptionPane.showInputDialog(frame, "Enter the number of columns : ");
        if (heightStr.length() == 0) {
            heightStr = JOptionPane.showInputDialog(frame, "Enter the number of columns again: ");
        }
        final int height = Integer.parseInt(heightStr);
        JOptionPane.showMessageDialog(frame, "You must right click to put an element!");
        map = Map.getInstance(width, height);
        frame.setLayout(new GridLayout(width, height));

        buttons = new JButton[width][height];

        JOptionPane.showMessageDialog(null, "Put the red bead");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                final JButton currentButton = new JButton();
                initButton(currentButton);

                buttons[i][j] = currentButton;
                final Position currentPosition = new Position(i, j);

                frame.add(currentButton);

                currentButton.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                            if (canPutRedBead) {
                                Bead redBead = new Bead(true);

                                map.getFirstPlayer().setBead(redBead);

                                putBead(currentButton, redBead, currentPosition);

                                map.turn();

                                canPutRedBead = false;

                                JOptionPane.showMessageDialog(null, "Put the blue bead");
                            } else if (canPutBlueBead) {
                                Bead blueBead = new Bead(false);

                                map.getSecondPlayer().setBead(blueBead);

                                putBead(currentButton, blueBead, currentPosition);

                                map.turn();

                                canPutBlueBead = false;
                            } else if (!canPutRedBead && !canPutBlueBead && !check) {
                                Position dest = currentPosition;

                                Bead bead = map.getCurrentPlayer().getBead();
                                boolean canMove = map.canMove(bead, dest);

                                if (canMove) {

                                    map.move(bead, dest);
                                    map.turn();
                                    refresh();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Can not move, choose another position");
                                }

                                boolean isFinished = map.isFinished();
                                if (isFinished) {
                                    Player winner = map.getWinner();
                                    if (winner == null) {
                                        JOptionPane.showMessageDialog(null, "Equal");
                                    } else if (winner == map.getFirstPlayer()) {
                                        JOptionPane.showMessageDialog(null, "Red player won!");

                                    } else {
                                        JOptionPane.showMessageDialog(null, "Blue player won!");
                                    }
                                    System.exit(0);
                                }

                            }

                        }

                        if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1 && check) {
                            JFrame container = new JFrame("Choose what you want: ");
                            container.setBounds(850, 350, 410, 140);
                            JButton Start = new JButton("Start");
                            Start.setBounds(300, 0, 100, 100);
                            container.add(Start);
                            JButton wallButton = new JButton("Wall");
                            wallButton.setBounds(0, 0, 100, 100);
                            container.add(wallButton);
                            JButton starButton = new JButton("Star");
                            starButton.setBounds(100, 0, 100, 100);
                            container.add(starButton);
                            JButton limitButton = new JButton("Limit");
                            limitButton.setBounds(200, 0, 100, 100);
                            container.add(limitButton);


                            Start.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    check = false;
                                }
                            });
                            if (check) {
                                wallButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent w) {
                                        putWall(currentButton, currentPosition);
                                    }
                                });

                                starButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent s) {
                                        putStar(currentButton, currentPosition);
                                    }
                                });

                                limitButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent l) {
                                        String valueStr = JOptionPane.showInputDialog(frame, "Enter the limit: ");
                                        if (valueStr.length() == 0) {
                                            valueStr = JOptionPane.showInputDialog(frame, "Enter the limit again: ");
                                        }
                                        int value = Integer.parseInt(valueStr);

                                        if (value < width && value < height) {
                                            putLimit(currentButton, currentPosition, value);
                                        }
                                        else {
                                            JOptionPane.showMessageDialog(null, "This value is invalid. Please try again");
                                            clear(currentButton);
                                        }
                                    }
                                });
                            }


                            container.setLayout(null);
                            container.setVisible(true);
                        }
                    }
                });
            }
        }

        frame.setVisible(true);
        frame.setBounds(500, 100, width * 50 + 15, height * 50 + 37);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void clear(JButton button) {
        button.setIcon(null);
        button.removeAll();
    }

    private void putBead(JButton button, Bead bead , Position position) {
        map.putElement(bead, position);
        ImageIcon icon = Utils.getImage(bead.isRed() ? "red.png" : "blue.png");
        button.setIcon(icon);
    }

    private void putLimit(JButton button, Position position, int value) {
        String strvalue = Integer.toString(value);
        JLabel label = new JLabel(strvalue);
        label.setLocation(15, 15);
        label.setLayout(null);
        button.add(label);
        button.setIcon(Utils.getImage("limit.png"));
        map.putElement(new Limit(value), position);

    }

    private void putStar(JButton button, Position position) {
        button.setIcon(Utils.getImage("star.png"));
        map.putElement(new Star(), position);
    }

    private void putWall(JButton button, Position position) {
        button.setIcon(Utils.getImage("wall.png"));
        map.putElement(new Wall(), position);
    }

    private void initButton(JButton button) {
        button.setBackground(Color.white);
        button.setVisible(true);
    }

    private void refresh() {
        for (int row = 0; row < map.getRowCount(); row++) {
            for (int col = 0; col < map.getColCount(); col++) {
                Element element = map.getElementAt(row, col);

                String fileName = null;
                if (element instanceof Star) {
                    fileName = "star.png";
                } else if (element instanceof Wall) {
                    fileName = "wall.png";
                } else if (element instanceof Limit) {
                    fileName = "limit.png";
                } else if (element instanceof Bead) {
                    fileName = ((Bead) element).isRed() ? "red.png" : "blue.png";
                }

                if (fileName == null)
                    buttons[row][col].setIcon(null);
                else
                    buttons[row][col].setIcon(Utils.getImage(fileName));

            }
        }
    }

}