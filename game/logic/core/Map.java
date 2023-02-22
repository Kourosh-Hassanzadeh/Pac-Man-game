package game.logic.core;

import game.logic.model.*;
import game.logic.model.Limit;

import javax.swing.*;

//******************************************
//Niloufar Zarabian 9912762256
//Kourosh Hassanzadeh 9912762552
//Mohammad Amin Afsharian Shandiz 9912762790
//******************************************

public class Map {

    private static Map instance;

    private final int rowCount;
    private final int colCount;

    private Player firstPlayer;
    private Player secondPlayer;

    private boolean isFirstPlayerTurn = true;

    private final Element[][] board;

    public static Map getInstance(int row, int col) {
        if (instance == null)
            instance = new Map(row, col);
        return instance;
    }

    private Map(int row, int col) {
        rowCount = row;
        colCount = col;

        board = new Element[row][col];

        firstPlayer = new Player();
        secondPlayer = new Player();
    }

    public void putElement(Element element, Position position) {
        board[position.getRow()][position.getCol()] = element;
        element.setPosition(position);
    }

    public Element getElementAt(Position position) {
        return getElementAt(position.getRow(), position.getCol());
    }

    public Element getElementAt(int row, int col) {
        return board[row][col];
    }

    public boolean isEmpty(Position position) {
        return getElementAt(position) == null;
    }

    public boolean canMove(Bead bead, Position dest) {
        if (getElementAt(dest) instanceof Bead) return false;

        Position source = bead.getPosition();
        int count = Math.abs(source.getRow() - dest.getRow()) + Math.abs(source.getCol() - dest.getCol());
        int limit = calculateLimit();
        if (count > limit) return false;

        if (source.getCol() == dest.getCol()) {

            boolean isUp = dest.getRow() < source.getRow();
            if (isUp) {
                for (int row = source.getRow(); row >= dest.getRow(); row--)
                    if (board[row][source.getCol()] instanceof Wall) return false;
            } else {
                for (int row = source.getRow(); row <= dest.getRow(); row++)
                    if (board[row][source.getCol()] instanceof Wall) return false;
            }
            return true;

        } else if (source.getRow() == dest.getRow()) {

            boolean isRight = dest.getCol() > source.getCol();
            if (isRight) {
                for (int col = source.getCol(); col <= dest.getCol(); col++)
                    if (board[source.getRow()][col] instanceof Wall) return false;
            } else {
                for (int col = source.getCol(); col >= dest.getCol(); col--)
                    if (board[source.getRow()][col] instanceof Wall) return false;
            }
            return true;

        } else return false;
    }

    private int calculateLimit() {
        Player opponent = getOpponent();
        int limit = Integer.MAX_VALUE;
        if (opponent.hasAnyEatenLimit()) {
            limit = opponent.getFirstLimit().getValue();
        }
        return limit;
    }

    private Player getOpponent() {
        return isFirstPlayerTurn ? secondPlayer : firstPlayer;
    }

    public void move(Bead bead, Position dest) {
        if (!canMove(bead, dest)) return;

        Position source = bead.getPosition();
        clear(source);

        if (source.getCol() == dest.getCol()) {
            boolean isUp = dest.getRow() < source.getRow();
            if (isUp) {
                for (int row = source.getRow(); row >= dest.getRow(); row--) {
                    handleMovement(board[row][source.getCol()]);
                }


            } else {
                for (int row = source.getRow(); row <= dest.getRow(); row++) {
                    handleMovement(board[row][source.getCol()]);
                }

            }
        } else if (source.getRow() == dest.getRow()) {
            boolean isRight = dest.getCol() > source.getCol();
            if (isRight) {
                for (int col = source.getCol(); col <= dest.getCol(); col++) {
                    handleMovement(board[source.getRow()][col]);
                }

            } else {
                for (int col = source.getCol(); col >= dest.getCol(); col--) {
                    handleMovement(board[source.getRow()][col]);


                }
            }
        }

        putElement(bead, dest);

        Player opponent = getOpponent();
        if (opponent.hasAnyEatenLimit())
            opponent.removeFirstLimit();

    }

    private void handleMovement(Element element) {
        if (element == null) return;

        Position position = element.getPosition();


        if (element instanceof Star) {

            getCurrentPlayer().incrementScore(1);
            clear(position);
            show();


        } else if (element instanceof Limit) {
            getCurrentPlayer().addLimit((Limit) element);
            clear(position);

        }
    }

    private void clear(int row, int col) {
        board[row][col] = null;
    }

    private void clear(Position position) {
        clear(position.getRow(), position.getCol());
    }

    public boolean isFinished() {
        for (int row = 0; row < getRowCount(); row++) {
            for (int col = 0; col < getColCount(); col++) {
                if (board[row][col] instanceof Star) {
                    return false;
                }
            }
        }
        return true;
    }

    public Player getWinner() {
        if (getFirstPlayer().getScore() > getSecondPlayer().getScore()) {
            return firstPlayer;
        } else if (getSecondPlayer().getScore() > getFirstPlayer().getScore()) {
            return secondPlayer;
        } else return null;     //agar mosavi shodan
    }

    public void turn() {
        isFirstPlayerTurn = !isFirstPlayerTurn;
    }

    public Player getCurrentPlayer() {
        return isFirstPlayerTurn ? firstPlayer : secondPlayer;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public boolean isFirstPlayerTurn() {
        return isFirstPlayerTurn;
    }

    public void show() {

        if (isFirstPlayerTurn()) {
            JOptionPane.showMessageDialog(null, String.format("Red score is: %d", getCurrentPlayer().getScore()));
        } else {
            JOptionPane.showMessageDialog(null, String.format("Blue score is: %d", getCurrentPlayer().getScore()));
        }
    }

}