import java.awt.event.KeyEvent;

public class GreedyAI {
    public int getNextMove(int[][] board) {
        Move bestFirstMove = null;
        int maxScore = Integer.MIN_VALUE;
        
        // Try all possible first moves
        for (Move firstMove : Move.values()) {
            int[][] boardAfterFirstMove = simulateMove(board, firstMove);
            int firstMoveScore = 0;
            
            // For each first move, try all possible second moves
            for (Move secondMove : Move.values()) {
                int[][] boardAfterSecondMove = simulateMove(boardAfterFirstMove, secondMove);
                int score = evaluateBoard(boardAfterSecondMove);
                
                // Update best first move if this combination yields a higher score
                if (score > maxScore) {
                    maxScore = score;
                    bestFirstMove = firstMove;
                }
            }
        }
        
        // Return the key for the best first move
        return moveKey(bestFirstMove);
    }
    
    private int evaluateBoard(int[][] board) {
        int score = 0;
        int emptyTiles = 0;
        int maxTile = 0;
        
        // Advanced board evaluation
        for (int[] row : board) {
            for (int tile : row) {
                if (tile > 0) {
                    // Score increases with larger tiles
                    score += tile * tile;
                    maxTile = Math.max(maxTile, tile);
                } else {
                    // Encourage keeping empty spaces
                    emptyTiles++;
                }
            }
        }
        
        // Bonus for having empty tiles and large tiles
        score += emptyTiles * 10;
        score += maxTile * 5;
        
        return score;
    }
    
    private int[][] simulateMove(int[][] board, Move move) {
        int[][] newBoard = cloneBoard(board);
        int size = newBoard.length;
        
        switch (move) {
            case UP:
                for (int col = 0; col < size; col++) {
                    // Compress and merge tiles upwards
                    int[] column = new int[size];
                    int index = 0;
                    
                    // Collect non-zero tiles
                    for (int row = 0; row < size; row++) {
                        if (newBoard[row][col] != 0) {
                            column[index++] = newBoard[row][col];
                        }
                    }
                    
                    // Merge adjacent equal tiles
                    for (int i = 0; i < index - 1; i++) {
                        if (column[i] == column[i+1]) {
                            column[i] *= 2;
                            column[i+1] = 0;
                        }
                    }
                    
                    // Compress again after merging
                    index = 0;
                    for (int row = 0; row < size; row++) {
                        if (column[row] != 0) {
                            newBoard[index++][col] = column[row];
                        }
                    }
                    
                    // Fill remaining with zeros
                    while (index < size) {
                        newBoard[index++][col] = 0;
                    }
                }
                break;
            
            case DOWN:
                for (int col = 0; col < size; col++) {
                    // Similar to UP, but reversed
                    int[] column = new int[size];
                    int index = size - 1;
                    
                    // Collect non-zero tiles from bottom
                    for (int row = size - 1; row >= 0; row--) {
                        if (newBoard[row][col] != 0) {
                            column[index--] = newBoard[row][col];
                        }
                    }
                    
                    // Merge adjacent equal tiles
                    for (int i = size - 1; i > 0; i--) {
                        if (column[i] == column[i-1]) {
                            column[i] *= 2;
                            column[i-1] = 0;
                        }
                    }
                    
                    // Compress again after merging
                    index = size - 1;
                    for (int row = size - 1; row >= 0; row--) {
                        if (column[row] != 0) {
                            newBoard[index--][col] = column[row];
                        }
                    }
                    
                    // Fill remaining with zeros
                    while (index >= 0) {
                        newBoard[index--][col] = 0;
                    }
                }
                break;
            
            case LEFT:
                // Similar logic to UP, but for rows
                for (int row = 0; row < size; row++) {
                    int[] line = new int[size];
                    int index = 0;
                    
                    // Collect non-zero tiles
                    for (int col = 0; col < size; col++) {
                        if (newBoard[row][col] != 0) {
                            line[index++] = newBoard[row][col];
                        }
                    }
                    
                    // Merge adjacent equal tiles
                    for (int i = 0; i < index - 1; i++) {
                        if (line[i] == line[i+1]) {
                            line[i] *= 2;
                            line[i+1] = 0;
                        }
                    }
                    
                    // Compress again after merging
                    index = 0;
                    for (int col = 0; col < size; col++) {
                        if (line[col] != 0) {
                            newBoard[row][index++] = line[col];
                        }
                    }
                    
                    // Fill remaining with zeros
                    while (index < size) {
                        newBoard[row][index++] = 0;
                    }
                }
                break;
            
            case RIGHT:
                // Similar to LEFT, but reversed
                for (int row = 0; row < size; row++) {
                    int[] line = new int[size];
                    int index = size - 1;
                    
                    // Collect non-zero tiles from right
                    for (int col = size - 1; col >= 0; col--) {
                        if (newBoard[row][col] != 0) {
                            line[index--] = newBoard[row][col];
                        }
                    }
                    
                    // Merge adjacent equal tiles
                    for (int i = size - 1; i > 0; i--) {
                        if (line[i] == line[i-1]) {
                            line[i] *= 2;
                            line[i-1] = 0;
                        }
                    }
                    
                    // Compress again after merging
                    index = size - 1;
                    for (int col = size - 1; col >= 0; col--) {
                        if (line[col] != 0) {
                            newBoard[row][index--] = line[col];
                        }
                    }
                    
                    // Fill remaining with zeros
                    while (index >= 0) {
                        newBoard[row][index--] = 0;
                    }
                }
                break;
        }
        
        return newBoard;
    }
    
    private int[][] cloneBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, board[i].length);
        }
        return newBoard;
    }
    
    private int moveKey(Move move) {
        switch (move) {
            case UP:
                return KeyEvent.VK_W;
            case DOWN:
                return KeyEvent.VK_S;
            case LEFT:
                return KeyEvent.VK_A;
            case RIGHT:
                return KeyEvent.VK_D;
            default:
                throw new IllegalArgumentException("Invalid move");
        }
    }
    
    public enum Move {
        UP, DOWN, LEFT, RIGHT
    }
}