package pt.ipp.estg.pdm_tp.Tables;

public class PointPhoto {
    private int id;
    private int id_point;
    private int id_photo;

    public PointPhoto() {

    }
    public PointPhoto(int id, int id_point, int id_photo) {
        this.id = id;
        this.id_point = id_point;
        this.id_photo = id_photo;
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

    public int getId_photo() {
        return id_photo;
    }

    public void setId_photo(int id_photo) {
        this.id_photo = id_photo;
    }

    @Override
    public String toString() {
        return "PointPhoto{" +
                "id=" + id +
                ", id_point=" + id_point +
                ", id_photo=" + id_photo +
                '}';
    }
}
