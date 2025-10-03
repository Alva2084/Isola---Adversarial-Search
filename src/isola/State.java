/**
 * Adolfo Alvarez Jr
 */
package isola;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Represents the state of an Isola game, including the board configuration,
 * player positions, and the current player's turn.
 */
public final class State {
  public static final int ROW_COUNT = 6;
  public static final int COLUMN_COUNT = 8;

  private final boolean[][] availableCells;
  private final Coordinates playerOnePosition;
  private final Coordinates playerTwoPosition;
  private final int currentPlayer;  // 1 or 2

  /**
   * Creates a new game state with the specified parameters.
   * @param availableCells
   * @param p1
   * @param p2
   * @param currentPlayer
   */
  public State(boolean[][] availableCells, Coordinates p1, Coordinates p2, int currentPlayer) {
    this.availableCells = availableCells;
    this.playerOnePosition = p1;
    this.playerTwoPosition = p2;
    this.currentPlayer = currentPlayer;
  }

  /**
   * Creates the initial game state, with all cells available and players in their starting positions.
   * @param randomizeFirstPlayer
   * @return
   */
  public static State initial(boolean randomizeFirstPlayer) {
    boolean[][] cells = new boolean[ROW_COUNT][COLUMN_COUNT];
    for (int row = 0; row < ROW_COUNT; row++) Arrays.fill(cells[row], true);
    int startingPlayer = randomizeFirstPlayer ? (new Random().nextBoolean() ? 1 : 2) : 1;
    return new State(cells, new Coordinates(0, 2), new Coordinates(5, 2), startingPlayer);
  }

  /**
   * Returns the current player (1 or 2).
   * @return
   */
  public int currentPlayer() { return currentPlayer; }
  public Coordinates playerOnePosition() { return playerOnePosition; }
  public Coordinates playerTwoPosition() { return playerTwoPosition; }

  /**
   * Checks if the specified cell is available (not removed).
   * @param rowIndex
   * @param columnIndex
   * @return
   */
  public boolean isCellAvailable(int rowIndex, int columnIndex) { return availableCells[rowIndex][columnIndex]; }

  /**
   * Checks if the specified cell is occupied by either player.
   * @param rowIndex
   * @param columnIndex
   * @return
   */
  public boolean isCellOccupied(int rowIndex, int columnIndex) {
    return (playerOnePosition.rowIndex == rowIndex && playerOnePosition.columnIndex == columnIndex)
            || (playerTwoPosition.rowIndex == rowIndex && playerTwoPosition.columnIndex == columnIndex);
  }

  /**
   * Checks if the specified row and column indices are within the bounds of the board.
   * @param r
   * @param c
   * @return
   */
  public static boolean isInBounds(int r, int c){ return 0<=r && r<ROW_COUNT && 0<=c && c<COLUMN_COUNT; }

  /**
   * Direction vectors for the 8 possible movement directions (N, NE, E, SE, S, SW, W, NW).
   */
  private static final int[] ROW_DELTAS    = {-1,-1,-1, 0,0, 1,1,1};

  /**
   * Direction vectors for the 8 possible movement directions (N, NE, E, SE, S, SW, W, NW).
   */
  private static final int[] COLUMN_DELTAS = {-1, 0, 1,-1,1,-1,0,1};

  /**
   * Returns a list of legal destination coordinates for a pawn from the given position.
   * @param fromPosition
   * @return
   */
  public List<Coordinates> legalDestinationsFrom(Coordinates fromPosition) {
    ArrayList<Coordinates> result = new ArrayList<>();
    for (int i=0; i<8; i++) {
      int newRow = fromPosition.rowIndex + ROW_DELTAS[i];
      int newCol = fromPosition.columnIndex + COLUMN_DELTAS[i];
      if (isInBounds(newRow,newCol) && isCellAvailable(newRow,newCol) && !isCellOccupied(newRow,newCol)) {
        result.add(new Coordinates(newRow,newCol));
      }
    }
    return result;
  }

  /**
   * Returns a list of all legal actions for the current player.
   * @return
   */
  public List<Action> legalActions() {
    Coordinates activePawn = (currentPlayer==1)? playerOnePosition : playerTwoPosition;
    List<Coordinates> destinations = legalDestinationsFrom(activePawn);
    ArrayList<Action> actions = new ArrayList<>();

    for (Coordinates destination : destinations) {
      for (int r=0; r<ROW_COUNT; r++) {
        for (int c=0; c<COLUMN_COUNT; c++) {
          if (!isCellAvailable(r, c)) continue;

          boolean occupiedByOpponent =
                  (currentPlayer==1)
                          ? (playerTwoPosition.rowIndex==r && playerTwoPosition.columnIndex==c)
                          : (playerOnePosition.rowIndex==r && playerOnePosition.columnIndex==c);

          boolean isDestination =
                  (r==destination.rowIndex && c==destination.columnIndex);

          if (!occupiedByOpponent && !isDestination) {
            actions.add(new Action(destination, new Coordinates(r, c)));
          }
        }
      }
    }
    return actions;
  }


  /**
   * Applies the given action to the current state and returns the resulting new state.
   * @param action
   * @return
   */
  public State apply(Action action) {
    boolean[][] nextCells = new boolean[ROW_COUNT][COLUMN_COUNT];
    for (int r=0;r<ROW_COUNT;r++) nextCells[r] = Arrays.copyOf(availableCells[r], COLUMN_COUNT);
    nextCells[action.tokenToRemove.rowIndex][action.tokenToRemove.columnIndex] = false;

    Coordinates nextP1 = playerOnePosition;
    Coordinates nextP2 = playerTwoPosition;
    if (currentPlayer==1) nextP1 = action.destinationCell; else nextP2 = action.destinationCell;
    int nextPlayer = (currentPlayer==1)?2:1;
    return new State(nextCells, nextP1, nextP2, nextPlayer);
  }

  /**
   * Determines if the game is in a terminal state and returns the winner.
   * @return 0 if the game is not over, 1 if player 1 wins, 2 if player 2 wins.
   */
  public int winnerIfTerminal() {
    Coordinates activePawn = (currentPlayer==1)? playerOnePosition : playerTwoPosition;
    if (legalDestinationsFrom(activePawn).isEmpty()) {
      return (currentPlayer==1)?2:1;
    }
    return 0;
  }

  /**
   * Returns a list of neighboring coordinates around the given center coordinate.
   * @param center
   * @return
   */
  public List<Coordinates> neighborsOf(Coordinates center) {
    ArrayList<Coordinates> result = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      int newRow = center.rowIndex + ROW_DELTAS[i];
      int newCol = center.columnIndex + COLUMN_DELTAS[i];
      if (isInBounds(newRow, newCol)) {
        result.add(new Coordinates(newRow, newCol));
      }
    }
    return result;
  }
}