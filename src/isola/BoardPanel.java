/**
 * Adolfo Alvarez Jr
 */

package isola;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import javax.swing.*;

/**
 * A JPanel that displays the game board and pieces.
 */
public class BoardPanel extends JPanel {
  private State state;
  private final int cellWidth = 64, cellHeight = 64, pad = 16;
  private final Set<Integer> highlights = new HashSet<>();
  private java.util.function.Consumer<Coordinates> onClick;

  /**
   * Create a new BoardPanel to display the given initial state.
   * @param init the initial game state
   */
  public BoardPanel(State init) {
    this.state = init;
    setPreferredSize(new Dimension(pad*2 + State.COLUMN_COUNT*cellWidth, pad*2 + State.ROW_COUNT*cellHeight));
    setBackground(new Color(245,245,245));
    addMouseListener(new MouseAdapter() {
      @Override public void mousePressed(MouseEvent e){
        int c = (e.getX()-pad)/cellWidth;
        int r = (e.getY()-pad)/cellHeight;
        if (State.isInBounds(r,c) && onClick!=null) onClick.accept(new Coordinates(r,c));
      }
    });
  }

  /**
   * Set a function to be called when a cell is clicked.
   * @param fn
   */
  public void setOnCellClick(java.util.function.Consumer<Coordinates> fn){ this.onClick = fn; }
  public void setState(State s){ this.state = s; repaint(); }
  public void setHighlights(Collection<Coordinates> cells){
    highlights.clear();
    for (Coordinates coord : cells) highlights.add(coord.rowIndex*100+coord.columnIndex);
    repaint();
  }
  public void clearHighlights(){ highlights.clear(); repaint(); }

  /**
   * Paint the board and pieces.
   */
  @Override protected void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2=(Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    for(int r=0;r<State.ROW_COUNT;r++) for(int c=0;c<State.COLUMN_COUNT;c++){
      int x=pad+c*cellWidth, y=pad+r*cellHeight;
      g2.setColor(new Color(245,238,195));
      g2.fillRect(x,y,cellWidth,cellHeight);
      if(!state.isCellAvailable(r,c)){
        g2.setColor(new Color(230,230,230));
        g2.fillRect(x+6,y+6,cellWidth-12,cellHeight-12);
      } else if(highlights.contains(r*100+c)){
        g2.setColor(new Color(200,230,255));
        g2.fillRect(x+4,y+4,cellWidth-8,cellHeight-8);
      }
      g2.setColor(Color.GRAY);
      g2.drawRect(x,y,cellWidth,cellHeight);
    }
    drawPawn(g2,state.playerOnePosition(),new Color(30,180,80));
    drawPawn(g2,state.playerTwoPosition(),new Color(60,120,220));
  }

  /**
   * Draw a pawn at the given cell with the given color.
   */
  private void drawPawn(Graphics2D g2, Coordinates p, Color color){
    int x=pad+p.columnIndex*cellWidth, y=pad+p.rowIndex*cellHeight;
    int cx=x+cellWidth/2, cy=y+cellHeight/2;
    g2.setColor(color);
    g2.fillOval(cx-14,cy-22,28,28);
    g2.fillRect(cx-6,cy-6,12,16);
    g2.setColor(Color.DARK_GRAY);
    g2.drawOval(cx-14,cy-22,28,28);
    g2.drawRect(cx-6,cy-6,12,16);
  }
}
