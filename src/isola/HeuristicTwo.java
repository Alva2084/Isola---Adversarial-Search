/**
 * Adolfo Alvarez Jr
 */

package isola;

/**
 * HeuristicTwo evaluates a game state based on two factors:
 */
public class HeuristicTwo implements Heuristic {

  /**
   * H_move: The difference in the number of legal moves available to the player
   * @param currentState
   * @param action
   * @param player
   * @return
   */
  @Override
  public int evaluate(State currentState, Action action, int player) {
    State nextState = currentState.apply(action);

    Coordinates currentPos = (player == 1)
            ? currentState.playerOnePosition()
            : currentState.playerTwoPosition();

    Coordinates nextPos = (player == 1)
            ? nextState.playerOnePosition()
            : nextState.playerTwoPosition();

    Coordinates opponentPos = (player == 1)
            ? nextState.playerTwoPosition()
            : nextState.playerOnePosition();

    int movesBefore = currentState.legalDestinationsFrom(currentPos).size();
    int movesAfter  = nextState.legalDestinationsFrom(nextPos).size();
    int H_move = movesBefore - movesAfter;

    int opponentMoves = nextState.legalDestinationsFrom(opponentPos).size();
    int H_token;

    if (opponentMoves == 0) {
      H_token = 100;
    } else {
      H_token = countTightCells(nextState);
    }

    return H_move + H_token;
  }

  private int countTightCells(State state) {
    int count = 0;
    for (int r = 0; r < State.ROW_COUNT; r++) {
      for (int c = 0; c < State.COLUMN_COUNT; c++) {
        if (!state.isCellAvailable(r, c)) continue;
        int blocked = 0;
        for (Coordinates nbr : state.neighborsOf(new Coordinates(r, c))) {
          if (!state.isCellAvailable(nbr.rowIndex, nbr.columnIndex)) {
            blocked++;
          }
        }
        if (blocked >= 3) count++;
      }
    }
    return count;
  }
}

