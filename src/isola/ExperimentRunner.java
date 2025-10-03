/**
 * Adolfo Alvarez Jr
 */
package isola;

public class ExperimentRunner {

  /**
   * Aggregate results for a series of games.
   */
  public static class Result {
    int winsPlayer1 = 0;
    int winsPlayer2 = 0;
    int totalMoves = 0;
    int totalMovesP1 = 0;
    int totalMovesP2 = 0;
    int games = 0;
  }

  /**
   * Main method to run experiments.
   * @param args
   */
  public static void main(String[] args) {
    Heuristic h1 = new HeuristicOne();
    Heuristic h2 = new HeuristicTwo();

    Agent H1Agent = new MinMaxAgent(h1, 3, true);
    Agent H2Agent = new MinMaxAgent(h2, 3, true);

    Result r1 = runGames("H1 vs H1", H1Agent, H1Agent, 50);
    printSeriesSummary("H1 vs H1", r1);

    Result r2 = runGames("H2 vs H2", H2Agent, H2Agent, 50);
    printSeriesSummary("H2 vs H2", r2);

    Result r3a = runGames("H1(P1) vs H2(P2)", H1Agent, H2Agent, 50);
    Result r3b = runGames("H2(P1) vs H1(P2)", H2Agent, H1Agent, 50);
    Result r3 = combine(r3a, r3b);
    printSeriesSummary("H1 vs H2 (combined 100, alternating first)", r3);
  }

  /**
   * Run a series of games between two agents.
   * @param seriesLabel
   * @param agent1
   * @param agent2
   * @param nGames
   * @return
   */
  private static Result runGames(String seriesLabel, Agent agent1, Agent agent2, int nGames) {
    Result res = new Result();
    for (int g = 0; g < nGames; g++) {
      State state = State.initial(true); // randomize who actually moves first on the board
      int total = 0, p1Moves = 0, p2Moves = 0;

      while (state.winnerIfTerminal() == 0) {
        int toMove = state.currentPlayer();                   // who moves now?
        Agent current = (toMove == 1) ? agent1 : agent2;
        Action act = current.choose(state);
        state = state.apply(act);

        if (toMove == 1) p1Moves++; else p2Moves++;
        total++;
      }

      int winner = state.winnerIfTerminal();
      if (winner == 1) res.winsPlayer1++; else if (winner == 2) res.winsPlayer2++;

      res.totalMoves  += total;
      res.totalMovesP1 += p1Moves;
      res.totalMovesP2 += p2Moves;
      res.games++;

      System.out.printf(
              "%s — Game %02d | Winner: P%d (%s vs %s) | P1 moves: %d, P2 moves: %d, Total: %d%n",
              seriesLabel, g + 1, winner, agent1.name(), agent2.name(), p1Moves, p2Moves, total
      );
    }
    return res;
  }

  /**
   * Print a summary of a series of games.
   * @param label
   * @param r
   */
  private static void printSeriesSummary(String label, Result r) {
    double avgTotal   = (r.games == 0) ? 0.0 : r.totalMoves / (double) r.games;
    double avgP1Moves = (r.games == 0) ? 0.0 : r.totalMovesP1 / (double) r.games;
    double avgP2Moves = (r.games == 0) ? 0.0 : r.totalMovesP2 / (double) r.games;

    System.out.println("----------------------------------------------------------------");
    System.out.printf(
            "%s → Games: %d | P1 wins: %d | P2 wins: %d | Avg total moves: %.2f | Avg P1 moves: %.2f | Avg P2 moves: %.2f%n",
            label, r.games, r.winsPlayer1, r.winsPlayer2, avgTotal, avgP1Moves, avgP2Moves
    );
    System.out.println("----------------------------------------------------------------");
  }

  /**
   * Combine two Result objects into one.
   * @param a
   * @param b
   * @return
   */
  private static Result combine(Result a, Result b) {
    Result r = new Result();
    r.winsPlayer1 = a.winsPlayer1 + b.winsPlayer1;
    r.winsPlayer2 = a.winsPlayer2 + b.winsPlayer2;
    r.totalMoves  = a.totalMoves + b.totalMoves;
    r.totalMovesP1 = a.totalMovesP1 + b.totalMovesP1;
    r.totalMovesP2 = a.totalMovesP2 + b.totalMovesP2;
    r.games       = a.games + b.games;
    return r;
  }
}
