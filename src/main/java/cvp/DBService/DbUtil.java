package cvp.DBService;

/**
 * Created by stepa on 21.05.15.
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class DbUtil {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialize(){
        DataSource dataSource = getDataSource();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS STUDENTS (ID INTEGER, STUDENT VARCHAR(50), GROUP_NUMB VARCHAR(20), LABS_COMPLETED INTEGER, FINAL_ASSESSMENT VARCHAR(20))");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
