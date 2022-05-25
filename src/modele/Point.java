package modele;

public class Point {
    int x, y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(int i, int j) {
        this.x = i;
        this.y = j;
    }

    public void SetCoords(int i, int j) {
        this.x = i;
        this.y = j;
    }
}
