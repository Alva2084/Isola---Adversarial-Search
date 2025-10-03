/**
 * Adolfo Alvarez Jr
 */
package isola;

/**
 * A heuristic that evaluates actions based on the number of legal moves available
 * before and after the action, as well as the impact on the opponent's legal moves.
 */
public class HeuristicOne implements Heuristic {

  /**
   * Evaluate the given action in the context of the current state for the specified player.
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
    int movesAfter = nextState.legalDestinationsFrom(nextPos).size();

    int H_move;
    if (movesAfter > movesBefore) {
      H_move = +1;
    } else if (movesAfter == movesBefore) {
      H_move = 0;
    } else {
      H_move = -1;
    }

    int opponentMoves = nextState.legalDestinationsFrom(opponentPos).size();

    int H_token;
    if (opponentMoves == 0) {
      H_token = +100;
    } else {
      int opponentMovesBefore = currentState.legalDestinationsFrom(opponentPos).size();
      if (opponentMoves < opponentMovesBefore) {
        H_token = +2;
      } else {
        H_token = 0;
      }
    }

    return H_move + H_token;
  }
}
