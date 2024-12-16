# 2048 Game Project

## Overview
This project is a Java implementation of the popular 2048 game. The game allows players to combine tiles of the same value on a 4x4 grid to achieve the 2048 tile or a higher score. The project includes both manual and AI-controlled gameplay.

---

## Features

### Gameplay
- **Manual Play**: Use keyboard inputs (arrow keys) to move tiles.
- **AI Play**: Two AI modes:
  - **Random AI**: Randomly chooses moves.
  - **Greedy AI**: Selects the best possible move based on heuristic evaluations.

### Game Logic
- Spawning of tiles (`2` or `4`) in random empty positions.
- Tile movement and merging logic.
- Score tracking.
- Win/lose condition management.

---

## Project Structure

### Main Classes
1. **`Main`**:
   - Entry point of the application.
   - Handles the game loop, rendering, and AI interactions.
2. **`Game`**:
   - Core game logic.
   - Manages the game board and tile updates.
3. **`GameObject`**:
   - Represents individual tiles on the board.
   - Manages tile movement and rendering.
4. **`Sprite`**:
   - Handles graphical representation of tiles.
5. **`Renderer`**:
   - Renders the game board and tiles.
6. **`Keyboard`**:
   - Captures and processes keyboard inputs.
7. **`RandomAI`**:
   - Implements random move logic for AI.
8. **`GreedyAI`**:
   - Implements heuristic-based decision-making for AI.

---

## How to Run
1. **Requirements**:
   - Java 8 or higher.
2. **Steps**:
   - Compile the code: `javac *.java`
   - Run the game: `java Main`

---

## Controls
- **Manual Play**:
  - `W` / Up Arrow: Move tiles up.
  - `A` / Left Arrow: Move tiles left.
  - `S` / Down Arrow: Move tiles down.
  - `D` / Right Arrow: Move tiles right.
  - `R`: Restart the game.
- **AI Play**:
  - Use buttons in the UI to toggle between Random AI, Greedy AI, and stop autoplay.

---

## Key Algorithms

### Tile Movement
- Implements movement for four directions.
- Combines adjacent tiles with the same value.

### AI Logic
- **Random AI**: Selects a random move, ensuring simplicity.
- **Greedy AI**: Evaluates all possible moves and picks the one maximizing the immediate score.
  - Simulates future states for optimization.
  