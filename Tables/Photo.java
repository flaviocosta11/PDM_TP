package pt.ipp.estg.pdm_tp.Tables;

public class Photo {
    private int id;
    private String photo;


    public Photo() {

    }

    public Photo(int id, String photo) {
        this.id = id;
        this.photo = photo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", photo='" + photo + '\'' +
                '}';
    }
}
