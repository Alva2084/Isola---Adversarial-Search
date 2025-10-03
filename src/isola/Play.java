/**
 * Adolfo Alvarez Jr
 */
package isola;

import javax.swing.*;
import java.awt.*;

/**
 * The main class for playing the game of Isola.
 * It sets up the GUI and manages the game state and turns.
 */
public class Play {
  private State state;
  private final JFrame frame = new JFrame("Isolation");
  private final JLabel status = new JLabel(" ");
  private final BoardPanel board;
  private final JButton resetBtn = new JButton("Reset");

  private enum Phase { SELECT_MOVE, SELECT_REMOVE, AI_THINKING }
  private Phase phase = Phase.SELECT_MOVE;
  private Coordinates pendingDestination = null;
  private Agent agent1;
  private Agent agent2;

  private boolean randomFirst = true;

  // AI scheduling guard
  private boolean aiMoveScheduled = false;
  private javax.swing.Timer aiTimer = null;

  /**
   * Create a new Play instance with the specified mode.
   * @param mode the game mode, e.g. "Player vs Computer"
   */
  public Play(String mode) {
    switch (mode) {
      case "Player vs Player":
        agent1 = null;
        agent2 = null;
        break;
      case "Player vs Computer":
        agent1 = null;
        agent2 = AgentFactory.random();
        break;
      case "Player vs Jerry":
        agent1 = null;
        agent2 = AgentFactory.heuristicOne();
        break;
      case "Human vs Casey":
        agent1 = null;
        agent2 = AgentFactory.heuristicTwo();
        break;
      case "Jerry vs Casey":
        agent1 = AgentFactory.heuristicOne();
        agent2 = AgentFactory.heuristicTwo();
        break;
      default:
        agent1 = null;
        agent2 = AgentFactory.random();
    }

    state = State.initial(randomFirst);
    board = new BoardPanel(state);

    resetBtn.addActionListener(e -> resetGame());

    JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
    top.add(resetBtn);
    top.add(status);

    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.add(top, BorderLayout.NORTH);
    frame.add(board, BorderLayout.CENTER);

    board.setOnCellClick(this::handleClick);

    frame.pack();
    frame.setVisible(true);

    resetGame();
  }

  /**
   * Reset the game to the initial state.
   */
  private void resetGame() {
    if (aiTimer != null) { aiTimer.stop(); aiTimer = null; }
    aiMoveScheduled = false;

    state = State.initial(randomFirst);
    phase = Phase.SELECT_MOVE;
    pendingDestination = null;
    board.setState(state);
    highlightDestinations();
    status.setText("New game. Player " + state.currentPlayer() + " to move.");
    maybeAiTurn();
  }

  /**
   * Handle a click on the board at the given cell coordinates.
   * @param cell the coordinates of the clicked cell
   */
  private void handleClick(Coordinates cell) {
    if (phase == Phase.AI_THINKING) return;

    if (phase == Phase.SELECT_MOVE) {
      Coordinates me = (state.currentPlayer() == 1)
              ? state.playerOnePosition()
              : state.playerTwoPosition();

      for (Coordinates dest : state.legalDestinationsFrom(me)) {
        if (dest.rowIndex == cell.rowIndex && dest.columnIndex == cell.columnIndex) {
          pendingDestination = cell;
          phase = Phase.SELECT_REMOVE;
          highlightRemovals();
          status.setText("Choose a token to remove.");
          return;
        }
      }
    } else if (phase == Phase.SELECT_REMOVE) {
      if (cell.rowIndex == pendingDestination.rowIndex
              && cell.columnIndex == pendingDestination.columnIndex) {
        status.setText("You cannot remove the tile you just moved onto. Choose another.");
        return;
      }

      boolean isLegalRemovalForThisDestination = false;
      for (Action a : state.legalActions()) {
        boolean sameDestination =
                (a.destinationCell.rowIndex == pendingDestination.rowIndex) &&
                        (a.destinationCell.columnIndex == pendingDestination.columnIndex);
        boolean sameRemoval =
                (a.tokenToRemove.rowIndex == cell.rowIndex) &&
                        (a.tokenToRemove.columnIndex == cell.columnIndex);
        if (sameDestination && sameRemoval) {
          isLegalRemovalForThisDestination = true;
          break;
        }
      }
      if (!isLegalRemovalForThisDestination) {
        status.setText("That token can't be removed after your move. Pick another.");
        return;
      }
      advance(new Action(pendingDestination, cell));
    }
  }

  /**
   * Advance the game state by applying the given action.
   * @param a the action to apply
   */
  private void advance(Action a) {
    state = state.apply(a);
    board.setState(state);
    pendingDestination = null;

    int w = state.winnerIfTerminal();
    if (w != 0) {
      board.clearHighlights();
      status.setText("Winner: Player " + w);
      return;
    }

    phase = Phase.SELECT_MOVE;
    highlightDestinations();
    status.setText("Player " + state.currentPlayer() + " to move.");
    maybeAiTurn();
  }

  /**
   * If it's an AI's turn, schedule its move after a short delay.
   */
  private void maybeAiTurn() {
    Agent currentAgent = (state.currentPlayer() == 1) ? agent1 : agent2;
    if (currentAgent == null) return;

    if (!aiMoveScheduled) {
      aiMoveScheduled = true;
      phase = Phase.AI_THINKING;
      board.clearHighlights();
      status.setText("AI thinking...");
      aiTimer = new javax.swing.Timer(350, e -> {
        ((Timer) e.getSource()).stop();
        aiMoveScheduled = false;
        advance(currentAgent.choose(state));
      });
      aiTimer.setRepeats(false);
      aiTimer.start();
    }
  }

  /**
   * Highlight the legal move destinations for the current player.
   */
  private void highlightDestinations() {
    Coordinates me = (state.currentPlayer() == 1)
            ? state.playerOnePosition()
            : state.playerTwoPosition();
    board.setHighlights(state.legalDestinationsFrom(me));
  }

  /**
   * Highlight the tokens that can be removed after moving to the pending destination.
   */
  private void highlightRemovals() {
    java.util.List<Coordinates> removable = new java.util.ArrayList<>();
    for (Action a : state.legalActions()) {
      boolean sameDestination =
              (a.destinationCell.rowIndex == pendingDestination.rowIndex) &&
                      (a.destinationCell.columnIndex == pendingDestination.columnIndex);
      if (sameDestination) {
        removable.add(a.tokenToRemove);
      }
    }
    board.setHighlights(removable);
  }

  /**
   * The main method to start the application.
   * @param args
   */
  public static void main(String[] args) {
    String[] opts = {"Player vs Player", "Player vs Computer", "Player vs Jerry", "Player vs Casey", "Jerry vs Casey"};
    int pick = JOptionPane.showOptionDialog(
            null, "Choose mode", "Isolation",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, opts, opts[0]
    );

    if (pick == JOptionPane.CLOSED_OPTION) System.exit(0);

    String chosenMode = opts[pick];
    SwingUtilities.invokeLater(() -> new Play(chosenMode));
  }
}
