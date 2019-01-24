package pt.ipp.estg.pdm_tp.Tables;

public class RoutePoint {
    private int id;
    private int id_point;
    private int id_route;

    public RoutePoint() {

    }
    public RoutePoint(int id, int id_point, int id_route) {
        this.id = id;
        this.id_point = id_point;
        this.id_route = id_route;
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

    public int getId_route() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
    }


    @Override
    public String toString() {
        return "RoutePoint{" +
                "id=" + id +
                ", id_point=" + id_point +
                ", id_route=" + id_route +
                '}';
    }
}
