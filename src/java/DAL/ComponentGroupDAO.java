package DAL;

import DTO.ComponentGroupDTO;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by mathias on 21/03/16.
 */
public class ComponentGroupDAO implements IComponentGroupDAO {
    private final Connection CONN;
    private final String DATABASE_NAME = "ComponentGroup";

    public ComponentGroupDAO(Connection conn) {
        this.CONN = conn;
    }

    @Override
    public int createComponentGroup(ComponentGroupDTO componentGroup) {
        if (componentGroup.getName() == null)
            return -1;

        String sql = "INSERT INTO " + DATABASE_NAME + "(name, status";
        String sqlValues = "?, ?";

        if (componentGroup.getStandardLoanDuration() != null) {
            sql += ", standardLoanDuration";
            sqlValues += ", ?";
        }

        sql += ") VALUES(";
        sql += sqlValues + ")";

        try {
            int param = 1;
            PreparedStatement stm = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(param++, componentGroup.getName());

            if (componentGroup.getStatus() == -1)
                stm.setInt(param++, 0);
            else
                stm.setInt(param++, componentGroup.getStatus());

            if (componentGroup.getStandardLoanDuration() != null)
                stm.setString(param++, componentGroup.getStandardLoanDuration());

            stm.execute();

            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next())
                return generatedKeys.getInt(1);
            else
                return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public ComponentGroupDTO getComponentGroup(int componentGroupId) {
        try {
            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE componentGroupId = ?");
            stm.setInt(1, componentGroupId);
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                return new ComponentGroupDTO(result.getInt("componentGroupId"), result.getString("name"), result.getString("standardLoanDuration"), result.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ComponentGroupDTO[] getComponentGroups() {
        ResultSet result = null;
        ArrayList<ComponentGroupDTO> componentGroups = new ArrayList();
        try {
            result = CONN.createStatement().executeQuery("SELECT * FROM " + DATABASE_NAME);
            while (result.next()) {
                componentGroups.add(new ComponentGroupDTO(result.getInt("componentGroupId"), result.getString("name"), result.getString("standardLoanDuration"), result.getInt("status")));
            }
            return componentGroups.toArray(new ComponentGroupDTO[componentGroups.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int updateComponentGroups(ComponentGroupDTO componentGroup) {
        String sql = "UPDATE " + DATABASE_NAME + " set ";
        String sqlValues = "";

        if (componentGroup.getComponentGroupId() == 0)
            return -1;

        if (componentGroup.getName() != null)
            sqlValues += " name = ?";

        if (componentGroup.getStandardLoanDuration() != null) {
            if (!sqlValues.equals(""))
                sqlValues += ", standardLoanDuration = ?";
            else
                sqlValues += " standardLoanDuration = ?";
        }

        if (componentGroup.getStatus() != -1) {
            if (!sqlValues.equals(""))
                sqlValues += ", status = ?";
            else
                sqlValues += " status = ?";
        }
        sql += sqlValues;
        sql += " WHERE componentGroupId = ?";

        try {
            PreparedStatement stm = CONN.prepareStatement(sql);
            int param = 1;
            if (componentGroup.getName() != null)
                stm.setString(param++, componentGroup.getName());
            if (componentGroup.getStandardLoanDuration() != null)
                stm.setString(param++, componentGroup.getStandardLoanDuration());
            if (componentGroup.getStatus() != -1)
                stm.setInt(param++, componentGroup.getStatus());

            stm.setInt(param++, componentGroup.getComponentGroupId());
            if (!stm.execute()) {
                if (stm.getUpdateCount() == 0) {
                    return -2;
                } else {
                    return 1;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteComponentGroup(int componentGroupId) {
        try {
            String sql = "DELETE FROM " + DATABASE_NAME + " WHERE componentGroupId = ?";
            PreparedStatement stm = CONN.prepareStatement(sql);
            stm.setInt(1, componentGroupId);
            stm.execute();
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
