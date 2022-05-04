package modele;

public class Point {
    int x, y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(int i, int j) {
        x = i;
        y = j;
    }

    public void SetCoords(int i, int j) {
        x = i;
        y = j;
    }
}
