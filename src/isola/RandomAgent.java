/**
 * Adolfo Alvarez Jr
 */
package isola;

import java.util.List;
import java.util.Random;

/**
 * An agent that selects actions uniformly at random from the set of legal actions.
 */
public class RandomAgent implements Agent {
  private final Random rng = new Random();

  /**
   * Chooses an action uniformly at random from the set of legal actions.
   * @param state the current state of the game
   * @return a randomly selected legal action
   */
  @Override
  public Action choose(State state) {
    List<Action> actions = state.legalActions();
    return actions.get(rng.nextInt(actions.size()));
  }

/**
   * Returns the name of the agent.
   * @return the name of the agent
   */
  @Override
  public String name() {
    return "Random Agent";
  }
}
