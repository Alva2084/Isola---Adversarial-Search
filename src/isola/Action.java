/**
 * Adolfo Alvarez Jr
 */
package isola;

/**
 * An action in the game of Isola consists of moving a token to a destination cell
 * and then removing a token from the board.
 */
public final class Action {
  public final Coordinates destinationCell;
  public final Coordinates tokenToRemove;

  public Action(Coordinates destinationCell, Coordinates tokenToRemove) {
    this.destinationCell = destinationCell;
    this.tokenToRemove   = tokenToRemove;
  }

  @Override public String toString() {
    return "move " + destinationCell + ", remove " + tokenToRemove;
  }
}
