package pt.ipp.estg.pdm_tp.Tables;

public class PointInterest {

    private int id;
    private String latitude;
    private String longitude;
    private String titulo;
    private String descricao;
    private int rating;
    private String cidade;
    private int id_like;
    private int id_marker;

    public PointInterest(){}

    public PointInterest(int id, String latitude, String longitude, String titulo, String descricao, int rating, String cidade, int id_like, int id_marker) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.titulo = titulo;
        this.descricao = descricao;
        this.rating = rating;
        this.cidade = cidade;
        this.id_like = id_like;
        this.id_marker = id_marker;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getId_like() {
        return id_like;
    }

    public void setId_like(int id_like) {
        this.id_like = id_like;
    }

    public int getId_marker() {
        return id_marker;
    }

    public void setId_marker(int id_marker) {
        this.id_marker = id_marker;
    }

    @Override
    public String toString() {
        return "PointInterest{" +
                "id=" + id +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", rating=" + rating +
                ", cidade='" + cidade + '\'' +
                ", id_like=" + id_like +
                ", id_marker=" + id_marker +
                '}';
    }
}
