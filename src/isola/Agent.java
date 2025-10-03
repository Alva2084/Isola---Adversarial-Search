/**
 * Adolfo Alvarez Jr
 */

package isola;

/**
 * An agent that can play the game of Isola.
 */
public interface Agent {
  Action choose(State state);
  String name();
}
