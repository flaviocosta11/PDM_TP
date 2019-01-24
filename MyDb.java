package pt.ipp.estg.pdm_tp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import pt.ipp.estg.pdm_tp.Tables.Category;
import pt.ipp.estg.pdm_tp.Tables.Photo;
import pt.ipp.estg.pdm_tp.Tables.PointInterest;
import pt.ipp.estg.pdm_tp.Tables.Route;

public class MyDb extends SQLiteOpenHelper {
    // Nome e versao da base de dados
    private static final String DATABASE_NAME = "PointInterest.db";
    private static final int DATABASE_VERSION = 3;

    // TABELA pontos
    public static final String TABLE_POINT = "tbl_pointinterest";
    public static final String KEY_ID = "id";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_TITULO = "titulo";
    public static final String KEY_DESCRICAO = "descricao";
    public static final String KEY_RATING = "rating";
    public static final String KEY_CIDADE = "cidade";
    public static final String KEY_DRAWBLE_LIKE = "id_like";
    public static final String KEY_DRAWBLE_MARKER = "id_marker";

    // TABELA Category
    public static final String TABLE_CATEGORIA = "tbl_category";
    public static final String CAT_ID = "id_categoria";
    public static final String CAT_TITULO = "nomecategoria";

    // TABELA PointCat
    public static final String TABLE_CATEGORY_POINT = "tbl_category_point";
    public static final String CATEGORY_POINT_ID = "id";
    public static final String CATEGORY_POINT_ID_P = "id_point";
    public static final String CATEGORY_POINT_ID_CAT = "id_category";


    // TABELA Route
    public static final String TABLE_ROTA = "tbl_rota";
    public static final String ROT_ID = "id";
    public static final String ROT_TITULO = "titulo";
    public static final String ROT_DESC = "descricao";

    // TABELA RoutePoint
    public static final String TABLE_ROUTE_POINT = "tbl_route_point";
    public static final String ROUTE_POINT_ID = "id";
    public static final String ROUTE_ID_POINT_ID = "id_point";
    public static final String ROUTE_POINT_ID_ROUTE = "id_route";

    // TABELA PHOTO
    public static final String TABLE_FOTO = "tbl_photo";
    public static final String FOTO_ID = "id";
    public static final String FOTO_LINK = "photo";

    // TABELA PHOTOPOINT
    public static final String TABLE_PHOTO_POINT = "tbl_photo_point";
    public static final String PHOTO_POINT_ID = "id";
    public static final String PHOTO_POINT_POINT_ID = "id_point";
    public static final String PHOTO_POINT_ID_PHOTO = "id_photo";

    private final String SQL_TABLE_POINT = "CREATE TABLE " + TABLE_POINT + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_LATITUDE + " VARCHAR(30) NOT NULL, "
            + KEY_LONGITUDE + " VARCHAR(30) NOT NULL, "
            + KEY_TITULO + " VARCHAR(20) NOT NULL, "
            + KEY_DESCRICAO + " VARCHAR(150) NOT NULL, "
            + KEY_RATING + " INTEGER(1) NOT NULL, "
            + KEY_CIDADE + " VARCHAR(20) NOT NULL, "
            + KEY_DRAWBLE_LIKE + " INTEGER(1) NOT NULL, "
            + KEY_DRAWBLE_MARKER + " INTEGER(1) NOT NULL)";

    private final String SQL_TABLE_CATEGORIA = "CREATE TABLE " + TABLE_CATEGORIA + " ("
            + CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CAT_TITULO + " VARCHAR(30) NOT NULL)";

    private final String SQL_TABLE_CATEGORY_POINT = "CREATE TABLE " + TABLE_CATEGORY_POINT + " ("
            + CATEGORY_POINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CATEGORY_POINT_ID_P + " INTEGER(10) NOT NULL, "
            + CATEGORY_POINT_ID_CAT + " INTEGER(10) NOT NULL)";


    private final String SQL_TABLE_ROTA = "CREATE TABLE " + TABLE_ROTA + " ("
            + ROUTE_POINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ROT_TITULO + " VARCHAR(30) NOT NULL,"
            + ROT_DESC + " VARCHAR(200))";


    private final String SQL_TABLE_ROUTE_POINT = "CREATE TABLE " + TABLE_ROUTE_POINT + " ("
            + ROT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ROUTE_ID_POINT_ID + " INTEGER(10) NOT NULL, "
            + ROUTE_POINT_ID_ROUTE + " INTEGER(10) NOT NULL)";

    private final String SQL_TABLE_FOTO = "CREATE TABLE " + TABLE_FOTO + " ("
            + FOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FOTO_LINK + " VARCHAR(550) NOT NULL)";

    private final String SQL_TABLE_PHOTO_POINT = "CREATE TABLE " + TABLE_PHOTO_POINT + " ("
            + PHOTO_POINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PHOTO_POINT_POINT_ID + " INTEGER(10) NOT NULL, "
            + PHOTO_POINT_ID_PHOTO + " INTEGER(10) NOT NULL)";


    public MyDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        // Criar a tabela na base de dados
        db.execSQL(SQL_TABLE_POINT);
        db.execSQL(SQL_TABLE_CATEGORIA);
        db.execSQL(SQL_TABLE_ROTA);
        db.execSQL(SQL_TABLE_CATEGORY_POINT);
        db.execSQL(SQL_TABLE_ROUTE_POINT);
        db.execSQL(SQL_TABLE_FOTO);
        db.execSQL(SQL_TABLE_PHOTO_POINT);

        inicializarDados(db);
    }


    public void inicializarDados(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_POINT + "(" + KEY_ID + "," + KEY_LATITUDE + "," + KEY_LONGITUDE + "," + KEY_TITULO + "," + KEY_DESCRICAO + "," + KEY_RATING + "," + KEY_CIDADE + "," + KEY_DRAWBLE_LIKE + "," + KEY_DRAWBLE_MARKER + ")" + " VALUES (1,'41.367131','-8.194706','ESTG','ESCOLA DE TECNOLOGIA E GESTÃO FELGUEIRAS',4,'Felgueiras',0,1);");
        db.execSQL("INSERT INTO " + TABLE_POINT + "(" + KEY_ID + "," + KEY_LATITUDE + "," + KEY_LONGITUDE + "," + KEY_TITULO + "," + KEY_DESCRICAO + "," + KEY_RATING + "," + KEY_CIDADE + "," + KEY_DRAWBLE_LIKE + "," + KEY_DRAWBLE_MARKER + ")" + " VALUES (2,'41.581111','-8.441614','Ctt Correios','Ctt Correios de Braga',4,'Braga',1,1);");
        db.execSQL("INSERT INTO " + TABLE_POINT + "(" + KEY_ID + "," + KEY_LATITUDE + "," + KEY_LONGITUDE + "," + KEY_TITULO + "," + KEY_DESCRICAO + "," + KEY_RATING + "," + KEY_CIDADE + "," + KEY_DRAWBLE_LIKE + "," + KEY_DRAWBLE_MARKER + ")" + " VALUES (3,'39.659398','-8.825518','Batalha','Mosteiro da Batalha',5,'Batalha',1,1);");


        db.execSQL("INSERT INTO " + TABLE_FOTO + "(" + FOTO_ID + "," + FOTO_LINK + ")" + " VALUES (1,'https://eu.ipp.pt/estgf/imagens/imgPortal/login8.jpg');");

        db.execSQL("INSERT INTO " + TABLE_FOTO + "(" + FOTO_ID + "," + FOTO_LINK + ")" + " VALUES (2,'https://c2.staticflickr.com/4/3527/3247630819_5f7f17a544_z.jpg?zz=1');");
        db.execSQL("INSERT INTO " + TABLE_FOTO + "(" + FOTO_ID + "," + FOTO_LINK + ")" + " VALUES (3,'https://www.angelustv.pt/pic/950x450/_233004_batalha_monastery_5ae79adf49722.jpg');");
        db.execSQL("INSERT INTO " + TABLE_FOTO + "(" + FOTO_ID + "," + FOTO_LINK + ")" + " VALUES (4,'https://www.viajarentreviagens.pt/wp-content/uploads/2017/06/IMG_4860-1170x550.jpg');");
        db.execSQL("INSERT INTO " + TABLE_FOTO + "(" + FOTO_ID + "," + FOTO_LINK + ")" + " VALUES (5,'https://static.noticiasaominuto.com/stockimages/1920/naom_545d23aa9030a.jpg?1459279143');");


        db.execSQL("INSERT INTO " + TABLE_PHOTO_POINT + "(" + PHOTO_POINT_POINT_ID + "," + PHOTO_POINT_ID_PHOTO + ")" + " VALUES (1,1);");

        db.execSQL("INSERT INTO " + TABLE_PHOTO_POINT + "(" + PHOTO_POINT_POINT_ID + "," + PHOTO_POINT_ID_PHOTO + ")" + " VALUES (1,2);");
        db.execSQL("INSERT INTO " + TABLE_PHOTO_POINT + "(" + PHOTO_POINT_POINT_ID + "," + PHOTO_POINT_ID_PHOTO + ")" + " VALUES (3,3);");
        db.execSQL("INSERT INTO " + TABLE_PHOTO_POINT + "(" + PHOTO_POINT_POINT_ID + "," + PHOTO_POINT_ID_PHOTO + ")" + " VALUES (3,4);");
        db.execSQL("INSERT INTO " + TABLE_PHOTO_POINT + "(" + PHOTO_POINT_POINT_ID + "," + PHOTO_POINT_ID_PHOTO + ")" + " VALUES (2,5);");


        db.execSQL("INSERT INTO " + TABLE_CATEGORY_POINT + "(" + CATEGORY_POINT_ID_P + "," + CATEGORY_POINT_ID_CAT + ")" + " VALUES (1,19);");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY_POINT + "(" + CATEGORY_POINT_ID_P + "," + CATEGORY_POINT_ID_CAT + ")" + " VALUES (1,18);");

        db.execSQL("INSERT INTO " + TABLE_CATEGORY_POINT + "(" + CATEGORY_POINT_ID_P + "," + CATEGORY_POINT_ID_CAT + ")" + " VALUES (3,1);");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY_POINT + "(" + CATEGORY_POINT_ID_P + "," + CATEGORY_POINT_ID_CAT + ")" + " VALUES (3,2);");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY_POINT + "(" + CATEGORY_POINT_ID_P + "," + CATEGORY_POINT_ID_CAT + ")" + " VALUES (2,20);");


        db.execSQL("INSERT INTO " + TABLE_ROTA + "(" + ROT_ID + "," + ROT_TITULO + "," + ROT_DESC + ")" + " VALUES (1,'Felgueiras - Braga','ida a aos ctt braga');");
        db.execSQL("INSERT INTO " + TABLE_ROUTE_POINT + "(" + ROUTE_ID_POINT_ID + "," + ROUTE_POINT_ID_ROUTE + ")" + " VALUES (1,1);");
        db.execSQL("INSERT INTO " + TABLE_ROUTE_POINT + "(" + ROUTE_ID_POINT_ID + "," + ROUTE_POINT_ID_ROUTE + ")" + " VALUES (2,1);");

        db.execSQL("INSERT INTO " + TABLE_ROTA + "(" + ROT_ID + "," + ROT_TITULO + "," + ROT_DESC + ")" + " VALUES (2,'Batalha - Braga','ida ao mosteiro da Batalha');");
        db.execSQL("INSERT INTO " + TABLE_ROUTE_POINT + "(" + ROUTE_ID_POINT_ID + "," + ROUTE_POINT_ID_ROUTE + ")" + " VALUES (2,2);");
        db.execSQL("INSERT INTO " + TABLE_ROUTE_POINT + "(" + ROUTE_ID_POINT_ID + "," + ROUTE_POINT_ID_ROUTE + ")" + " VALUES (3,2);");

        db.execSQL("INSERT INTO " + TABLE_ROTA + "(" + ROT_ID + "," + ROT_TITULO + "," + ROT_DESC + ")" + " VALUES (3,'Batalha - Braga - ESTG','ida ao mosteiro da Batalha e ESTG');");
        db.execSQL("INSERT INTO " + TABLE_ROUTE_POINT + "(" + ROUTE_ID_POINT_ID + "," + ROUTE_POINT_ID_ROUTE + ")" + " VALUES (2,3);");
        db.execSQL("INSERT INTO " + TABLE_ROUTE_POINT + "(" + ROUTE_ID_POINT_ID + "," + ROUTE_POINT_ID_ROUTE + ")" + " VALUES (3,3);");
        db.execSQL("INSERT INTO " + TABLE_ROUTE_POINT + "(" + ROUTE_ID_POINT_ID + "," + ROUTE_POINT_ID_ROUTE + ")" + " VALUES (1,3);");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Sitios histórico');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Monumentos');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Zoológicos');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Museus');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Galerias de arte');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Jardim Botânicos');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Castelos');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Bibliotecas');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Praias');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Parques nacionais');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Parques temáticos');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Miradouros');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Pontes');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Bars');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Discutecas');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Cafés');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Shoppings');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Escolas');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Universidades');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + CAT_TITULO + ")" + " VALUES ('Outros');");


    }

    protected void dropAll(SQLiteDatabase _db) {
        // Remove as tabelas da base de dados
        _db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINT + ";");
        _db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA + ";");
        _db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROTA + ";");
        _db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_POINT + ";");
        _db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE_POINT + ";");
        _db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOTO + ";");
        _db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO_POINT + ";");
    }

    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
        // Invocar o metodo que elimina as tabelas da base de dados
        dropAll(_db);
        // Invocar o metodo que cria as tabelas na base de dados e inicializa os dados
        onCreate(_db);
    }


    //  INSERIR PONTO DE INTERESSE
    public void InsertPointInterest(PointInterest point, int[] idCats, int[] idFots) {

        SQLiteDatabase datadb = this.getReadableDatabase();
        datadb.execSQL("INSERT INTO " + TABLE_POINT + "(" + KEY_LATITUDE + "," + KEY_LONGITUDE + "," + KEY_TITULO + "," + KEY_DESCRICAO + "," + KEY_RATING + "," + KEY_CIDADE + "," + KEY_DRAWBLE_LIKE + "," + KEY_DRAWBLE_MARKER + ")" + " VALUES ('" + point.getLatitude() + "','" + point.getLongitude() + "','" + point.getTitulo() + "','" + point.getDescricao() + "','" + point.getRating() + "','" + point.getCidade() + "',0,1);");

        //obter o id do ultimo ponto inserido
        String query = "SELECT * FROM " + TABLE_POINT;
        Cursor c = datadb.rawQuery(query, null);
        int iddoponto = 0;
        if (c != null && c.moveToFirst()) {
            do {
                iddoponto = c.getInt(c.getColumnIndex(KEY_ID));
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();
        datadb.close();


        for (int i = 0; i <= idCats.length; i++) {
            datadb.execSQL("INSERT INTO " + TABLE_CATEGORY_POINT + "(" + CATEGORY_POINT_ID_P + "," + CATEGORY_POINT_ID_CAT + ")" + " VALUES ('" + iddoponto + "','" + idCats[i] + "');");
        }

        for (int i = 0; i <= idFots.length; i++) {
            datadb.execSQL("INSERT INTO " + TABLE_PHOTO_POINT + "(" + PHOTO_POINT_POINT_ID + "," + PHOTO_POINT_ID_PHOTO + ")" + " VALUES ('" + iddoponto + "','" + idFots[i] + "');");
        }
    }

    //  EDITAR PONTO DE INTERESSE
    public void UpdatePointInterest(PointInterest point, int[] idCatsNew, int[] idFots, int[] idFotsNew) {

        SQLiteDatabase datadb = this.getReadableDatabase();
        datadb.execSQL("UPDATE " + TABLE_POINT + " SET (" + KEY_LATITUDE + " = " + point.getLatitude() + "," + KEY_LONGITUDE + " = " + point.getLongitude() + ","
                + KEY_TITULO + " = " + point.getTitulo() + ","
                + KEY_DESCRICAO + " = " + point.getDescricao() + ","
                + KEY_RATING + " = " + point.getRating() + ","
                + KEY_CIDADE + " = " + point.getCidade() +
                ")" + " WHERE  " + KEY_ID + " = " + point.getId() + " ;");

        DeleteCategoryPoint(point.getId());
        DeletePhotoPoint(point.getId(), idFots);

        for (int i = 0; i <= idCatsNew.length; i++) {
            datadb.execSQL("INSERT INTO  " + TABLE_CATEGORY_POINT + "(" + CATEGORY_POINT_ID_P + "," + CATEGORY_POINT_ID_CAT + ")" + " VALUES ('" + point.getId() + "','" + idCatsNew[i] + "');");
        }

        for (int i = 0; i <= idFotsNew.length; i++) {
            datadb.execSQL("INSERT INTO " + TABLE_PHOTO_POINT + "(" + PHOTO_POINT_POINT_ID + "," + PHOTO_POINT_ID_PHOTO + ")" + " VALUES ('" + point.getId() + "','" + idFotsNew[i] + "');");
        }
    }

    //  APAGAR CATEGORIAS DE UM PONTO
    public void DeleteCategoryPoint(int idpoint) {
        //  DELETE FROM table_name WHERE condition;
        SQLiteDatabase datadb = this.getReadableDatabase();
        datadb.execSQL("DELETE FROM " + TABLE_CATEGORY_POINT + " WHERE " + CATEGORY_POINT_ID_P + " = "
                + idpoint + ";");
    }

    //  APAGAR FOTOS DE UM PONTO
    public void DeletePhotoPoint(int idpoint, int[] idsfotos) {
        //  DELETE FROM table_name WHERE condition;
        SQLiteDatabase datadb = this.getReadableDatabase();
        datadb.execSQL("DELETE FROM " + TABLE_PHOTO_POINT + " WHERE " + PHOTO_POINT_POINT_ID + " = "
                + idpoint + ";");
        for (int i = 0; i <= idsfotos.length; i++) {
            datadb.execSQL("DELETE FROM " + TABLE_FOTO + " WHERE " + FOTO_ID + " = "
                    + idsfotos[i] + ";");
        }
    }


    //  INSERIR Foto
    public int InsertPhoto(Photo photo) {
        SQLiteDatabase datadb = this.getReadableDatabase();
        datadb.execSQL("INSERT INTO " + TABLE_FOTO + "(" + FOTO_LINK + ")" + " VALUES ('" + photo.getPhoto() + "');");

        //obter o id da ultima foto inserida
        String query = "SELECT * FROM " + TABLE_FOTO;
        Cursor c = datadb.rawQuery(query, null);
        int idfoto = 0;
        if (c != null && c.moveToFirst()) {
            do {
                idfoto = c.getInt(c.getColumnIndex(FOTO_ID));
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();
        datadb.close();

        return idfoto;
    }

    //INSERIR ROTA
    public void Inserir_Rota(int[] ids_points, String titulo) {

        SQLiteDatabase datadb = this.getReadableDatabase();
        datadb.execSQL("INSERT INTO " + TABLE_ROTA + "(" + ROT_TITULO + ")" + " VALUES ('" + titulo + "');");

        //obter o id do ultimo ponto inserido
        String query = "SELECT * FROM " + TABLE_ROTA;
        Cursor c = datadb.rawQuery(query, null);
        int idrota = 0;
        if (c != null && c.moveToFirst()) {
            do {
                idrota = c.getInt(c.getColumnIndex(KEY_ID));
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();
        datadb.close();


        for (int i = 0; i <= ids_points.length; i++) {
            datadb.execSQL("INSERT INTO " + TABLE_ROUTE_POINT + "(" + ROUTE_ID_POINT_ID + "," + ROUTE_POINT_ID_ROUTE + ")" + " VALUES ('" + ids_points[i] + "','" + idrota + "');");
        }


    }

    //APAGAR ROTA
    public void Delete_Rota(int idrota) {
        //  DELETE FROM table_name WHERE condition;
        SQLiteDatabase datadb = this.getReadableDatabase();
        datadb.execSQL("DELETE FROM " + TABLE_ROTA + " WHERE " + ROT_ID + " = " + idrota + ";");

        datadb.execSQL("DELETE FROM " + TABLE_ROUTE_POINT + " WHERE " + ROUTE_POINT_ID_ROUTE + " = " + idrota + ";");

    }


    // OBTER PONTOS DE INTERESSE
    public ArrayList<PointInterest> getPoints() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PointInterest> mListPoints = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_POINT;
        Cursor c = db.rawQuery(query, null);


        if (c != null && c.moveToFirst()) {
            do {
                PointInterest newpoint = new PointInterest();
                newpoint.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                newpoint.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));
                newpoint.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
                newpoint.setTitulo(c.getString(c.getColumnIndex(KEY_TITULO)));
                newpoint.setDescricao(c.getString(c.getColumnIndex(KEY_DESCRICAO)));
                newpoint.setRating(c.getInt(c.getColumnIndex(KEY_RATING)));
                newpoint.setCidade(c.getString(c.getColumnIndex(KEY_CIDADE)));
                newpoint.setId_like(c.getInt(c.getColumnIndex(KEY_DRAWBLE_LIKE)));
                newpoint.setId_marker(c.getInt(c.getColumnIndex(KEY_DRAWBLE_MARKER)));

                mListPoints.add(newpoint);
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        db.close();

        return mListPoints;
    }


    // OBTER PONTOS DE INTERESSE FAVORITOS
    public ArrayList<PointInterest> getPointsFavourites() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PointInterest> mListPoints = new ArrayList<>();
        int like = 1;

        String query = "SELECT * FROM " + TABLE_POINT + " WHERE " + KEY_DRAWBLE_LIKE + " = " + like;
        Cursor c = db.rawQuery(query, null);


        if (c != null && c.moveToFirst()) {
            do {
                PointInterest newpoint = new PointInterest();
                newpoint.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                newpoint.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));
                newpoint.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
                newpoint.setTitulo(c.getString(c.getColumnIndex(KEY_TITULO)));
                newpoint.setDescricao(c.getString(c.getColumnIndex(KEY_DESCRICAO)));
                newpoint.setRating(c.getInt(c.getColumnIndex(KEY_RATING)));
                newpoint.setCidade(c.getString(c.getColumnIndex(KEY_CIDADE)));
                newpoint.setId_like(c.getInt(c.getColumnIndex(KEY_DRAWBLE_LIKE)));
                newpoint.setId_marker(c.getInt(c.getColumnIndex(KEY_DRAWBLE_MARKER)));

                mListPoints.add(newpoint);
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        db.close();

        return mListPoints;
    }


    // OBTER PONTOS DE INTERESSE no MAPA
    public ArrayList<PointInterest> getPointsInMap() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PointInterest> mListPoints = new ArrayList<>();
        int marker = 1;

        String query = "SELECT * FROM " + TABLE_POINT + " WHERE " + KEY_DRAWBLE_MARKER + " = " + marker;
        Cursor c = db.rawQuery(query, null);


        if (c != null && c.moveToFirst()) {
            do {
                PointInterest newpoint = new PointInterest();
                newpoint.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                newpoint.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));
                newpoint.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
                newpoint.setTitulo(c.getString(c.getColumnIndex(KEY_TITULO)));
                newpoint.setDescricao(c.getString(c.getColumnIndex(KEY_DESCRICAO)));
                newpoint.setRating(c.getInt(c.getColumnIndex(KEY_RATING)));
                newpoint.setCidade(c.getString(c.getColumnIndex(KEY_CIDADE)));
                newpoint.setId_like(c.getInt(c.getColumnIndex(KEY_DRAWBLE_LIKE)));
                newpoint.setId_marker(c.getInt(c.getColumnIndex(KEY_DRAWBLE_MARKER)));

                mListPoints.add(newpoint);
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        db.close();

        return mListPoints;
    }


    // obter categorias de um ponto
    public ArrayList<Category> getCategoriasPonto(int id_ponto) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Category> mListCategorys = new ArrayList<>();

        int i = 0;

        String query = "SELECT * FROM " + TABLE_CATEGORY_POINT + " WHERE " + CATEGORY_POINT_ID_P + " = " + id_ponto;

        Cursor c = db.rawQuery(query, null);
        int ids[] = new int[c.getCount()];

        if (c != null && c.moveToFirst()) {
            do {
                ids[i] = c.getInt(c.getColumnIndex(CATEGORY_POINT_ID_CAT));
                i++;
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        for (int e = 0; e < ids.length; e++) {
            String querytwo = "SELECT * FROM " + TABLE_CATEGORIA + " WHERE id_categoria = " + ids[e];
            Cursor cur = db.rawQuery(querytwo, null);
            if (cur != null && cur.moveToFirst()) {

                Category category = new Category();
                category.setId_categoria(cur.getInt(cur.getColumnIndex(CAT_ID)));
                category.setNomecategoria(cur.getString(cur.getColumnIndex(CAT_TITULO)));

                mListCategorys.add(category);
            }

            if (cur != null)
                cur.close();
        }

        db.close();

        return mListCategorys;
    }

    // obter fotos de um ponto
    public ArrayList<Photo> getFotosPonto(int id_ponto) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Photo> mListFotos = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_PHOTO_POINT + " WHERE " + PHOTO_POINT_POINT_ID + " = " + id_ponto;
        Cursor c = db.rawQuery(query, null);

        int ids[] = new int[c.getCount()];
        int i = 0;
        if (c != null && c.moveToFirst()) {
            do {
                ids[i] = c.getInt(c.getColumnIndex(PHOTO_POINT_ID_PHOTO));
                i++;
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        for (int e = 0; e < ids.length; e++) {

            Photo photo = new Photo();

            String querytwo = "SELECT * FROM " + TABLE_FOTO + " WHERE id = " + ids[e] + ";";
            Cursor cur = db.rawQuery(querytwo, null);

            if (cur != null && cur.moveToFirst()) {
                photo.setId(cur.getInt(cur.getColumnIndex(FOTO_ID)));
                photo.setPhoto(cur.getString(cur.getColumnIndex(FOTO_LINK)));
                //mListFotos.add(photo);
            }

            mListFotos.add(photo);
        }

        db.close();

        return mListFotos;
    }

    // OBTER AS ROTA
    public ArrayList<PointInterest> getPontosRota(int id_rota) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PointInterest> mListRotas = new ArrayList<>();

        int i = 0;

        String query = "SELECT * FROM " + TABLE_ROUTE_POINT + " WHERE " + ROUTE_POINT_ID_ROUTE + " = " + id_rota;

        Cursor c = db.rawQuery(query, null);
        int ids[] = new int[c.getCount()];

        if (c != null && c.moveToFirst()) {
            do {
                ids[i] = c.getInt(c.getColumnIndex(ROUTE_ID_POINT_ID));
                i++;
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        for (int e = 0; e < ids.length; e++) {
            mListRotas.add(getPointInterest(ids[e]));
        }

        db.close();

        return mListRotas;
    }

    public Route getDescRota(int id_route) {

        SQLiteDatabase db = this.getReadableDatabase();
        Route route = new Route();

        String query = "SELECT * FROM " + TABLE_ROTA + " WHERE " + ROT_ID + " = " + id_route;

        Cursor c = db.rawQuery(query, null);

        if (c != null && c.moveToFirst()) {
            route.setId(c.getInt(c.getColumnIndex(ROT_ID)));
            route.setTitulo(c.getString(c.getColumnIndex(ROT_TITULO)));
            route.setDescricao(c.getString(c.getColumnIndex(ROT_DESC)));

        }
        if (c != null)
            c.close();

        db.close();

        return route;
    }


    public PointInterest getPointInterest(int id_point) {

        SQLiteDatabase db = this.getReadableDatabase();
        PointInterest point = new PointInterest();

        String query = "SELECT * FROM " + TABLE_POINT + " WHERE " + KEY_ID + " = " + id_point;

        Cursor c = db.rawQuery(query, null);

        if (c != null && c.moveToFirst()) {

            point.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            point.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));
            point.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
            point.setTitulo(c.getString(c.getColumnIndex(KEY_TITULO)));
            point.setDescricao(c.getString(c.getColumnIndex(KEY_DESCRICAO)));
            point.setRating(c.getInt(c.getColumnIndex(KEY_RATING)));
            point.setCidade(c.getString(c.getColumnIndex(KEY_CIDADE)));
            point.setId_like(c.getInt(c.getColumnIndex(KEY_DRAWBLE_LIKE)));
            point.setId_marker(c.getInt(c.getColumnIndex(KEY_DRAWBLE_MARKER)));

            Log.d("oi", c.getString(c.getColumnIndex(KEY_TITULO)));

        }
        if (c != null)
            c.close();

        db.close();

        return point;
    }


    public ArrayList<Category> getCategorias() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Category> mListCategories = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_CATEGORIA;
        Cursor c = db.rawQuery(query, null);


        if (c != null && c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId_categoria(c.getInt(c.getColumnIndex(CAT_ID)));
                category.setNomecategoria(c.getString(c.getColumnIndex(CAT_TITULO)));

                mListCategories.add(category);
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        db.close();

        return mListCategories;
    }

    //OBTER LISTA DE ROTAS
    public ArrayList<Route> getRotas() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Route> mListRotas = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_ROTA;
        Cursor c = db.rawQuery(query, null);


        if (c != null && c.moveToFirst()) {
            do {
                Route route = new Route();
                route.setId(c.getInt(c.getColumnIndex(ROT_ID)));
                route.setTitulo(c.getString(c.getColumnIndex(ROT_TITULO)));
                route.setDescricao(c.getString(c.getColumnIndex(ROT_DESC)));

                mListRotas.add(route);
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        db.close();

        return mListRotas;
    }

}

