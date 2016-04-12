package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by mathias on 21/03/16.
 */
public class ComponentDAO implements IComponentDAO {
    private final Connection CONN;
    private final String DATABASE_NAME = "Component";

    public ComponentDAO(Connection conn) {
        this.CONN = conn;
    }

    @Override
    public int createComponent(ComponentDTO component) {
        if (component.getComponentNumber() == 0 && component.getComponentGroupId() == 0 && component.getBarcode() == null && component.getStatus() == 0)
            return -1;

        String sql = "INSERT INTO + " + DATABASE_NAME + "(";
        String sqlValues = "";

        if (component.getComponentGroupId() != 0) {
            sql += "componentGroupId";
            sqlValues += "?";
        }

        if (component.getComponentNumber() != 0) {
            if (!sqlValues.equals("")) {
                sql += ", componentNumber";
                sqlValues += ", ?";
            } else {
                sql += "componentNumber";
                sqlValues += "?";
            }
        }

        if (component.getBarcode() != null) {
            if (!sqlValues.equals("")) {
                sql += ", barcode";
                sqlValues += ", ?";
            } else {
                sql += "barcode";
                sqlValues += "?";
            }
        }

        if (component.getStatus() != 0) {
            if (!sqlValues.equals("")) {
                sql += ", status";
                sqlValues += ", ?";
            } else {
                sql += "status";
                sqlValues += "?";
            }
        }

        sql += ") VALUES(";
        sql += sqlValues + ")";

        try {
            int param = 1;
            PreparedStatement stm = CONN.prepareStatement(sql);

            if (component.getComponentGroupId() != 0)
                stm.setInt(param++, component.getComponentGroupId());

            if (component.getComponentNumber() != 0)
                stm.setInt(param++, component.getComponentNumber());

            if (component.getBarcode() != null)
                stm.setString(param++, component.getBarcode());

            if (component.getStatus() != 0)
                stm.setInt(param++, component.getStatus());

            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    @Override
    public ComponentDTO getComponent(int componentId) {
        try {
            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE componentId = ?");
            stm.setInt(1, componentId);
            ResultSet result = stm.executeQuery();
            while (result.next())
                return new ComponentDTO(result.getInt("componentId"), result.getInt("componentGroupId"), result.getInt("componentNumber"), result.getString("barcode"), result.getInt("status"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ComponentDTO[] getComponents() {
        try {
            ResultSet result = CONN.createStatement().executeQuery("SELECT * FROM " + DATABASE_NAME);
            ArrayList<ComponentDTO> components = new ArrayList<>();
            while (result.next())
                components.add(new ComponentDTO(result.getInt("componentId"), result.getInt("componentGroupId"), result.getInt("componentNumber"), result.getString("barcode"), result.getInt("status")));
            return components.toArray(new ComponentDTO[components.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateComponent(ComponentDTO component) {
        if (component.getComponentId() == 0 || (component.getBarcode() == null && component.getComponentNumber() == 0 && component.getComponentGroupId() == 0 && component.getStatus() == 0))
            return -1;

        String sql = "UPDATE " + DATABASE_NAME + " set ";
        String sqlValues = "";

        if (component.getComponentGroupId() != 0)
            sqlValues += "componentGroupId = ?";

        if (component.getComponentNumber() != 0) {
            if (sqlValues.equals("")) {
                sqlValues += "componentNumber = ?";
            } else {
                sqlValues += ", componentNumber = ?";
            }
        }

        if (component.getBarcode() != null) {
            if (sqlValues.equals("")) {
                sqlValues += "barcode = ?";
            } else {
                sqlValues += ", barcode = ?";
            }
        }

        if (component.getStatus() != 0) {
            if (sqlValues.equals(""))
                sqlValues += "status = ?";
            else
                sqlValues += ", status = ?";
        }

        sql += sqlValues + " WHERE componentId = ?";

        try {
            PreparedStatement stm = CONN.prepareStatement(sql);
            int param = 1;
            if (component.getComponentNumber() != 0)
                stm.setInt(param++, component.getComponentNumber());
            if (component.getComponentGroupId() != 0)
                stm.setInt(param++, component.getComponentGroupId());
            if (component.getBarcode() != null)
                stm.setString(param++, component.getBarcode());
            if (component.getStatus() != 0)
                stm.setInt(param++, component.getStatus());

            stm.setInt(param++, component.getComponentId());

            stm.execute();

            if (stm.getUpdateCount() == 1)
                return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int deleteComponent(int componentId) {

        try {
            String sql = "DELETE FROM " + DATABASE_NAME + "WHERE componentId = ?";
            PreparedStatement stm = CONN.prepareStatement(sql);
            stm.setInt(1, componentId);
            if (stm.getUpdateCount() == 1) {
                return 1;
            } else {
                return -2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;

    }
}
