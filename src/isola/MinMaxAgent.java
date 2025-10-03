/**
 * Adolfo Alvarez Jr
 */
package isola;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * An implementation of the Minimax algorithm with alpha-beta pruning for the game of Isola.
 * The agent uses a specified heuristic to evaluate non-terminal game states at a given search depth.
 */
public class MinMaxAgent implements Agent {

  private final Heuristic heuristic;
  private final int maximumSearchDepth;
  private final boolean randomizeExpansionOrder;
  private final Random random = new Random();

  /**
   * Constructs a MinMaxAgent with the specified heuristic, search depth, and expansion order randomization.
   */
  public MinMaxAgent(Heuristic heuristic,
                      int maximumSearchDepth,
                      boolean randomizeExpansionOrder) {
    this.heuristic = heuristic;
    this.maximumSearchDepth = Math.max(1, maximumSearchDepth);
    this.randomizeExpansionOrder = randomizeExpansionOrder;
  }

  /**
   * Constructs a MinMaxAgent with the specified heuristic and search depth.
   * The order of action expansion is not randomized.
   */
  @Override
  public String name() {
    return "Minimax(d=" + maximumSearchDepth + ", " + heuristic.getClass().getSimpleName() + ")";
  }

  /**
   * Chooses the best action for the current player using the Minimax algorithm with alpha-beta pruning.
   * @param currentState The current state of the game.
   * @return The chosen action.
   */
  @Override
  public Action choose(State currentState) {
    final int maximizingPlayer = currentState.currentPlayer();
    double alpha = Double.NEGATIVE_INFINITY;
    double beta  = Double.POSITIVE_INFINITY;

    List<Action> rootActions = new ArrayList<>(currentState.legalActions());
    if (randomizeExpansionOrder) Collections.shuffle(rootActions, random);

    double bestScore = Double.NEGATIVE_INFINITY;
    Action bestAction = rootActions.get(0); // safe default

    for (Action candidate : rootActions) {
      State next = currentState.apply(candidate);
      double score = minimize(next, 1, alpha, beta, maximizingPlayer);
      if (score > bestScore || (score == bestScore && random.nextBoolean())) {
        bestScore = score;
        bestAction = candidate;
      }
      alpha = Math.max(alpha, bestScore);
      if (alpha >= beta) break;
    }
    return bestAction;
  }

  /**
   * Minimize function for the Minimax algorithm with alpha-beta pruning.
   * @param state The current game state.
   * @param depth The current depth in the search tree.
   * @param alpha The alpha value for pruning.
   * @param beta The beta value for pruning.
   * @param maximizingPlayer The player for whom we are maximizing the score.
   * @return The minimum score achievable from this state.
   */
  private double minimize(State state, int depth, double alpha, double beta, int maximizingPlayer) {
    int winner = state.winnerIfTerminal();
    if (winner != 0) return terminalUtility(winner, maximizingPlayer);
    if (depth >= maximumSearchDepth) return evaluateLeaf(state, maximizingPlayer);

    double best = Double.POSITIVE_INFINITY;
    List<Action> actions = new ArrayList<>(state.legalActions());
    if (randomizeExpansionOrder) Collections.shuffle(actions, random);

    for (Action action : actions) {
      State next = state.apply(action);
      double score = maximize(next, depth + 1, alpha, beta, maximizingPlayer);
      if (score < best) best = score;
      beta = Math.min(beta, best);
      if (alpha >= beta) break;
    }
    return best;
  }

  /**
   * Maximize function for the Minimax algorithm with alpha-beta pruning.
   * @param state The current game state.
   * @param depth The current depth in the search tree.
   * @param alpha The alpha value for pruning.
   * @param beta The beta value for pruning.
   * @param maximizingPlayer The player for whom we are maximizing the score.
   * @return The maximum score achievable from this state.
   */
  private double maximize(State state, int depth, double alpha, double beta, int maximizingPlayer) {
    int winner = state.winnerIfTerminal();
    if (winner != 0) return terminalUtility(winner, maximizingPlayer);
    if (depth >= maximumSearchDepth) return evaluateLeaf(state, maximizingPlayer);

    double best = Double.NEGATIVE_INFINITY;
    List<Action> actions = new ArrayList<>(state.legalActions());
    if (randomizeExpansionOrder) Collections.shuffle(actions, random);

    for (Action action : actions) {
      State next = state.apply(action);
      double score = minimize(next, depth + 1, alpha, beta, maximizingPlayer);
      if (score > best) best = score;
      alpha = Math.max(alpha, best);
      if (alpha >= beta) break;
    }
    return best;
  }

  /**
   * Returns a large positive or negative utility value based on whether the maximizing player has won.
   * @param winner The player who has won (0 if no winner).
   * @param maximizingPlayer The player for whom we are maximizing the score.
   * @return A large positive value if the maximizing player has won, otherwise a large negative value.
   */
  private double terminalUtility(int winner, int maximizingPlayer) {
    return (winner == maximizingPlayer) ? 1e9 : -1e9;
  }

  /**
   * Evaluates a non-terminal game state using the provided heuristic.
   * @param stateAtDepth The game state to evaluate.
   * @param maximizingPlayer The player for whom we are maximizing the score.
   * @return The heuristic score of the state.
   */
  private double evaluateLeaf(State stateAtDepth, int maximizingPlayer) {
    List<Action> actions = stateAtDepth.legalActions();
    if (actions.isEmpty()) {

      return terminalUtility(stateAtDepth.winnerIfTerminal(), maximizingPlayer);
    }
    int toMove = stateAtDepth.currentPlayer();
    int best = Integer.MIN_VALUE;
    for (Action a : actions) {
      int score = heuristic.evaluate(stateAtDepth, a, toMove);
      if (score > best) best = score;
    }
    return (toMove == maximizingPlayer) ? best : -best;
  }
}
