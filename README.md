# Isola---Adversarial-Search
This project implements the game Isolation with a GUI and AI agents. Players (human or computer) move pawns and remove tiles to trap the opponent. Two heuristics, Jerry and Casey, guide AI decisions using minimax with alpha–beta pruning. Experiments compare strategies by tracking wins and average moves.

## Features
- **GUI Gameplay** using Java Swing for an interactive experience.
- **Multiple Modes**:
  - Player vs Player
  - Player vs Computer (Random)
  - Heuristic vs Heuristic (Jerry vs Casey)
- **Adversarial Search**:
  - Minimax search with alpha–beta pruning
  - Configurable depth
- **Experiment Runner** for automated testing and reporting of heuristic performance

## Heuristics
- **Jerry (HeuristicOne):**  
  Prioritizes maintaining mobility and lightly penalizes reducing the opponent’s moves. Rewards immediate isolation heavily.
- **Casey (HeuristicTwo):**  
  Considers board “tightness” by counting blocked neighboring cells. Rewards strategies that constrict space and force the opponent into traps.

## How to Run
1. Open the project in IntelliJ (or another Java IDE).
2. Ensure JDK 11+ is installed.
3. Run the `Play` class to launch the GUI.
4. Select the desired mode (Player vs Player, Player vs Computer, or Heuristic battles).
5. Run the `ExperimentRunner` class to conduct automated experiments.

## Experiment Results
The experiment runner outputs per-game results and overall summaries:
- Wins and losses per player
- Average moves per game
- Comparative performance of heuristics 1 & 1
- Comparative performance of heuristics 2 & 2
- Comparative performance of heuristics 1 & 2

## Future Work
- Fix the tests to make them cleaner and more optimal.  
- There had been some issues when creating tests.

