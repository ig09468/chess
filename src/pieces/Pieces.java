package pieces;
/**
 * Pieces est la classe Mère correspondant aux pièces d'un plateau.
 *
 * @author Kitei
 *
 */
public class Pieces {

    private String color;
    private int posX;
    private int posY;
    private boolean onBoard;
    private
    /**
     * Constructor de la classe.
     *
     * @param color, est la couleur de la pièce
     * @param posX,  la position horizontale
     * @param posY,  la position verticale
     *
     */
    public Pieces(String color, int posX, int posY) {

        this.color = color;
        this.posX = posX;
        this.posY = posY;
        this.onBoard= true;
    }


    /**
     * Fonction permettant de retourner la couleur de la pièce.
     *
     * @return color, la couleur.
     */
    public String getColor() {
        return color;
    }

    /**
     * Fonction permettant de retourner la position horizontale de la pièce.
     *
     * @return posX
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Fonction permettant de retourner la position verticale de la pièce.
     *
     * @return posY
     */
    public int getPosY() {
        return posY;
    }

    public String toString() {
        return color + " " + posX + " " + posY;
    }
}
