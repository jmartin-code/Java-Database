package jon.codes.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\jmartinez\\IdeaProjects\\music\\" + DB_NAME;

    //Create constants for tables and column
    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
//    public static final int INDEX_ALBUM_ID = 1;
//    public static final int INDEX_ALBUM_NAME = 2;
//    public static final int INDEX_ALBUM_ARTIST = 3;


    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
//    public static final int INDEX_SONG_ID = 1;
//    public static final int INDEX_SONG_TRACK = 2;
//    public static final int INDEX_SONG_TITLE = 3;
//    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    //Create constants to query the SQLite database using SQL CLI commands
    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
    public static final String CREATE_ARTIST_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW +
            " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
            " AS " + COLUMN_SONG_ALBUM + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS +
            " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " INNER JOIN " +
            TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
            TABLE_SONGS + "." + COLUMN_SONG_TRACK;

    //SQL command to query the view
    public static final String QUERY_ARTIST_SONG_VIEW = "SELECT " + COLUMN_ARTIST_NAME + ", " + COLUMN_SONG_ALBUM + ", " +
            COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONG_TITLE + " =\"";

    public static final String QUERY_ALBUMS_ARTIST_START = "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME
            + " FROM " + TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS
            + " ON " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_ID +
            " WHERE " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME +
            " = \"";

    public static final String QUERY_ALBUMS_ARTIST_SORT = " ORDER BY " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME +
            " COLLATE NOCASE ";

    public static final String QUERY_ARTIST_START = "SELECT * FROM " + TABLE_ARTISTS;
    public static final String QUERY_ARTIST_SORT = " ORDER BY " + INDEX_ARTIST_NAME + " COLLATE NOCASE ";

    public static final String QUERY_SONG_START = "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", "
            + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " +
            TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = "
            + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "."
            + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " WHERE " + TABLE_SONGS + "."
            + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_SONG_SORT = " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME +
            "," + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    //Constants to update the data in the SQLite database are below
    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS + '(' + COLUMN_ARTIST_NAME + ") VALUES(?)";
    public static final String INSERT_ALBUM = "INSERT INTO " + TABLE_ALBUMS + '(' + COLUMN_ALBUM_NAME +
            ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";
    public static final String INSERT_SONG = "INSERT INTO " + TABLE_SONGS + '(' + COLUMN_SONG_TRACK + ", " +
            COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM + ") VALUES(?, ?, ?)";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " +
            TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " +
            TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";

    public static final String QUERY_ALBUMS_BY_ARTIST_ID = "SELECT * FROM " + TABLE_ALBUMS +
            " WHERE " + COLUMN_ALBUM_ARTIST + " = ? ORDER BY " + COLUMN_ALBUM_NAME + " COLLATE NOCASE";

    //Use prepared statements to avoid some type of SQL injection attacks
    private PreparedStatement insertArtist;
    private PreparedStatement insertAlbum;
    private PreparedStatement insertSong;

    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;
    private PreparedStatement queryAlbumsByArtistId;

    private Connection conn;

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            insertArtist = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertAlbum = conn.prepareStatement(INSERT_ALBUM, Statement.RETURN_GENERATED_KEYS);
            insertSong = conn.prepareStatement(INSERT_SONG);
            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);
            queryAlbumsByArtistId = conn.prepareStatement(QUERY_ALBUMS_BY_ARTIST_ID);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to the database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
            if(insertArtist != null){
                insertArtist.close();
            }
            if(insertAlbum != null){
                insertAlbum.close();
            }
            if(insertSong != null){
                insertSong.close();
            }

        } catch (SQLException e) {
            System.out.println("Could not close: " + e.getMessage());
        }
    }

    public List<Artist> queryArtists(int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_START);
//        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ARTIST_SORT);

            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC ");
            } else {
                sb.append(" ASC ");
            }
        }

        System.out.println(sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString());) {

            List<Artist> artists = new ArrayList<>();

            while (results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }
            return artists;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_ARTIST_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC ");
            } else {
                sb.append(" ASC ");
            }
        }
        System.out.println("SQL statement = " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<String> albums = new ArrayList<>();
            while (results.next()) {
                albums.add(results.getString(1));
            }
            return albums;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public List<SongArtist> querySongArtist(String songName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_SONG_START);
        sb.append(songName);
        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_SONG_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC ");
            } else {
                sb.append(" ASC ");
            }
        }
        System.out.println("SQL query = " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<SongArtist> songArtists = new ArrayList<>();

            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }
            return songArtists;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    //Count the number of records in a particular table
    public int getCount(String table) {
        String sqlCommand = "SELECT COUNT(*) AS count FROM " + table;

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sqlCommand)) {
            int count = results.getInt("count");
//            int minId = results.getInt("minId");

            System.out.format("Count is %d ", count);
            return count;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return -1;
        }
    }

    //Method to create the Song Artist view
    public boolean viewForSongArtist() {
        try (Statement statement = conn.createStatement()) {

            //Execute the SQL command to create the Artist song view
            statement.execute(CREATE_ARTIST_SONG_VIEW);
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to create view " + e.getMessage());
            return false;
        }
    }

    //Method to query the song artist view
    public List<SongArtist> querySongArtistView(String title) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_SONG_VIEW);
        sb.append(title);
        sb.append("\"");

        System.out.println(sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery(sb.toString())) {
            List<SongArtist> songArtists = new ArrayList<>();
            while (result.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(result.getString(1));
                songArtist.setAlbumName(result.getString(2));
                songArtist.setTrack(result.getInt(3));
                songArtists.add(songArtist);
            }
            return songArtists;

        } catch (SQLException e) {
            System.out.println("Failed to query " + e.getMessage());
            return null;
        }
    }

    //Method to insert Album name into the database
    public void insertSong(String title, String artist, String album, int track) {
        try {
            conn.setAutoCommit(false);

            int artistId = insertArtist(artist);
            int albumId = insertAlbum(album, artistId);
            insertSong.setInt(1, track);
            insertSong.setString(2, title);
            insertSong.setInt(3, albumId);
            int affectedRows = insertSong.executeUpdate();
            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The song insert has failed");
            }

        } catch (Exception e) {
            System.out.println("Failed to insert song: " + e.getMessage());
            try {
                System.out.println("Performing a rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Activating auto commit");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }


    public int insertAlbum(String name, int artistId) throws SQLException {

        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        } else {
            // Insert the album
            insertAlbum.setString(1, name);
            insertAlbum.setInt(2, artistId);
            int affectedRows = insertAlbum.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert album!");
            }

            ResultSet generatedKeys = insertAlbum.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for album");
            }
        }
    }

    private int insertArtist(String name) throws SQLException {

        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        } else {
            // Insert the artist
            insertArtist.setString(1, name);
            int affectedRows = insertArtist.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert artist!");
            }

            ResultSet generatedKeys = insertArtist.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
    }

}
