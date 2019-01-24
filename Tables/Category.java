package pt.ipp.estg.pdm_tp.Tables;

public class Category {
    private int id_categoria;
    private String nomecategoria;

    public Category(int id_categoria, String nomecategoria) {
        this.id_categoria = id_categoria;
        this.nomecategoria = nomecategoria;
    }

    public Category() {
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNomecategoria() {
        return nomecategoria;
    }

    public void setNomecategoria(String nomecategoria) {
        this.nomecategoria = nomecategoria;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id_categoria=" + id_categoria +
                ", nomecategoria='" + nomecategoria + '\'' +
                '}';
    }
}
