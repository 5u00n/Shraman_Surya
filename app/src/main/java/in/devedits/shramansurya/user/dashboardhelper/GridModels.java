package in.devedits.shramansurya.user.dashboardhelper;

public class GridModels {
    String id ,title, color;
    int icon;

    public GridModels(String id, String title, int icon, String color) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
