/**
 * Adolfo Alvarez Jr
 */

package isola;

/**
 * Factory class to create different types of agents.
 */
public class AgentFactory {
  public static Agent random() {
    return new RandomAgent();
  }
  public static Agent heuristicOne() {
    return new MinMaxAgent(new HeuristicOne(), 3, true);
  }
  public static Agent heuristicTwo() {
    return new MinMaxAgent(new HeuristicTwo(), 3, true);
  }
}
