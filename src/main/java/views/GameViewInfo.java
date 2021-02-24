package views;

public class GameViewInfo {

    private final boolean showGrid;

    public GameViewInfo(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public boolean isShowGrid() {
        return this.showGrid;
    }
}
