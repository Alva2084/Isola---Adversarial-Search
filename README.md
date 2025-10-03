# Isola---Adversarial-Search
This project implements the game Isolation with a GUI and AI agents. Players (human or computer) move pawns and remove tiles to trap the opponent. Two heuristics, Jerry and Casey, guide AI decisions using minimax with alpha–beta pruning. Experiments compare strategies by tracking wins and average moves.

Features
GUI Gameplay using Java Swing for an interactive experience.
Multiple Modes:
-Player vs Player
-Player vs Computer
-Heuristic vs Heuristic (Jerry vs Casey)
Adversarial Search:
-Minimax search with alpha–beta pruning.
-Configurable depth.
Experiment Runner for automated testing and reporting of heuristic performance.

Heuristics
-Jerry (HeuristicOne):
 Prioritizes maintaining mobility and lightly penalizes reducing the opponent’s moves. Rewards immediate isolation heavily.
-Casey (HeuristicTwo):
 Considers board “tightness” by counting blocked neighboring cells. Rewards strategies that constrict space and force the
 opponent into traps.

How to Run
-Open the project in IntelliJ (or another Java IDE).
-Ensure JDK 11+ is installed.
-Run the Play class to launch the GUI
-Select the desired mode (Player vs Player, Player vs Computer, or Heuristic battles).
-Run the ExperimentRunner class to conduct automated experiments

Experiment Results
The experiment runner outputs per-game results and overall summaries:
-Wins and losses per player.
-Average moves per game.
-Comparative performance of heuristics Jerry vs Casey

Future Work
Fix the tests to make it cleaner and more optimal. There had been some issues when creating tests.
