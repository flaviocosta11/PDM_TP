package pt.ipp.estg.pdm_tp.Tables;

public class PointCat {
    private int id;
    private int id_point;
    private int id_category;

    public PointCat() {

    }

    public PointCat(int id, int id_point, int id_category) {
        this.id = id;
        this.id_point = id_point;
        this.id_category = id_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_point() {
        return id_point;
    }

    public void setId_point(int id_point) {
        this.id_point = id_point;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    @Override
    public String toString() {
        return "PointCat{" +
                "id=" + id +
                ", id_point=" + id_point +
                ", id_category=" + id_category +
                '}';
    }
}
