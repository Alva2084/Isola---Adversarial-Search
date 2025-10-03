/**
 * Adolfo Alvarez Jr
 */

package isola;

public interface Heuristic {
  int evaluate(State state, Action action, int player);
}
