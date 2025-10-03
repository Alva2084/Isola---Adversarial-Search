/**
 * Adolfo Alvarez Jr
 */
package isola;

/**
 * A pair of coordinates (rowIndex, columnIndex) representing a cell on the board.
 */
public final class Coordinates {
  public final int rowIndex;
  public final int columnIndex;

  /**
   * Create a new Coordinates object.
   * @param rowIndex the row index (0-based)
   * @param columnIndex the column index (0-based)
   */
  public Coordinates(int rowIndex, int columnIndex) {
    this.rowIndex = rowIndex;
    this.columnIndex = columnIndex;
  }

  /**
   * Two Coordinates are equal if their row and column indices are equal.
   */
  @Override public String toString() {
    return "(" + rowIndex + "," + columnIndex + ")";
  }
}
