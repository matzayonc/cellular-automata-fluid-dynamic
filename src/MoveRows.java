public class MoveRows implements Runnable {
    private int first;
    private int last;

    private Point[][] points;

    public MoveRows(int first, int last, Point[][] points) {
        this.first = first;
        this.last = last;
        this.points = points;
    }

    public void run() {
        for (int x = first; x < last; ++x)
            for (int y = 0; y < points[x].length; ++y)
                points[x][y].move();
    }
}
