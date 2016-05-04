package DAL;

import DTO.ComponentDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mathias on 21/03/16.
 */
public class ComponentDAO implements IComponentDAO {
    private final Connection CONN;
    private final String DATABASE_NAME = "Component";
    private final int MIN_BARCODE = 100000000;
    private final int MAX_BARCODE = 999999999;

    public ComponentDAO(Connection conn) {
        this.CONN = conn;
    }

    @Override
    public int createComponent(ComponentDTO component) {
        if (component.getComponentNumber() == -1 || component.getComponentGroupId() == -1)
            return -1;

        // Check that status is valid
        if (component.getStatus() != -1 && component.getStatus() != 0 && component.getStatus() != 1)
            return -4;

        String sql = "INSERT INTO " + DATABASE_NAME +
                " (componentGroupId, componentNumber, barcode, status) "
                + "VALUES(?, ?, ?, ?);";

        PreparedStatement stm = null;

        try {
            int param = 1;
            stm = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setInt(param++, component.getComponentGroupId());

            stm.setInt(param++, component.getComponentNumber());

            String barcode;
            if (component.getBarcode() != null && component.getBarcode().equals("") && component.getBarcode().matches("^\\d+$"))
                barcode = component.getBarcode();
            else
                barcode = generateBarcode();

            stm.setString(param++, barcode);

            if (component.getStatus() == -1)
                stm.setInt(param++, 0);
            else
                stm.setInt(param++, component.getStatus());

            stm.execute();

            return Integer.parseInt(barcode);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    private String generateBarcode() {
        ComponentDTO component = null;
        String barcode = "";
        do {
            int barcodeInt = new Random().nextInt(MAX_BARCODE - MIN_BARCODE) + MIN_BARCODE;
            barcode = Integer.toString(barcodeInt);
            component = getComponent(barcode);
        } while (component != null);

        return barcode;
    }

    @Override
    public ComponentDTO getComponent(String barcode) {
        PreparedStatement stm = null;
        ResultSet result = null;
        try {
            stm = CONN.prepareStatement("SELECT *, c.status as componentStatus, cg.status as componentGroupStatus FROM Component c " +
                    "LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId WHERE c.barcode = ?;");
            stm.setString(1, barcode);
            result = stm.executeQuery();
            while (result.next()) {
                ComponentDTO component = new ComponentDTO(result.getInt("componentGroupId"), result.getInt("componentNumber"), result.getString("barcode"), result.getInt("componentStatus"));
                component.getComponentGroup().setName(result.getString("name"));
                component.getComponentGroup().setStandardLoanDuration("standardLoanDuration");
                component.getComponentGroup().setStatus(result.getInt("componentGroupStatus"));
                return component;
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (result != null)
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    @Override
    public ComponentDTO[] getComponentsFromGroup(int componentGroupId) {
        PreparedStatement stm = null;
        ResultSet result = null;
        try {
            stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE componentGroupId = ?;");

            stm.setInt(1, componentGroupId);
            result = stm.executeQuery();
            ArrayList<ComponentDTO> components = new ArrayList<>();
            while (result.next()) {
                ComponentDTO component = new ComponentDTO(result.getInt("componentGroupId"), result.getInt("componentNumber"), result.getString("barcode"), result.getInt("status"));
                components.add(component);
            }
            result.close();
            return components.toArray(new ComponentDTO[components.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (result != null)
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    @Override
    public ComponentDTO[] getComponents() {
        Statement stm = null;
        ResultSet result = null;
        try {
            stm = CONN.createStatement();
            result = stm.executeQuery("SELECT *, c.status as componentStatus, cg.status as componentGroupStatus FROM Component c " +
                    "LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId;");
            ArrayList<ComponentDTO> components = new ArrayList<>();
            while (result.next()) {
                ComponentDTO component = new ComponentDTO(result.getInt("componentGroupId"), result.getInt("componentNumber"), result.getString("barcode"), result.getInt("componentStatus"));
                component.getComponentGroup().setName(result.getString("name"));
                components.add(component);

            }
            result.close();
            return components.toArray(new ComponentDTO[components.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (result != null)
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    @Override
    public int updateComponent(ComponentDTO component) {
        if (component.getBarcode() == null) {
            return -1;
        } else {
            String barcode = component.getBarcode();
            component.setBarcode(null);
            return updateComponent(barcode, component);
        }
    }

    @Override
    public int updateComponent(String barcode, ComponentDTO component) {
        if (barcode == null || (component.getComponentNumber() == -1 && component.getComponentGroupId() == -1 && component.getStatus() == -1 && component.getBarcode() == null))
            return -1;

        // Check that status is valid
        if (component.getStatus() != -1 && component.getStatus() != 0 && component.getStatus() != 1)
            return -4;

        String sql = "UPDATE " + DATABASE_NAME + " set ";
        String sqlValues = "";

        if (component.getComponentGroupId() != -1)
            sqlValues += "componentGroupId = ?";

        if (component.getComponentNumber() != -1) {
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

        if (sqlValues.equals(""))
            sqlValues += "status = ?";
        else
            sqlValues += ", status = ?";

        sql += sqlValues + " WHERE barcode = ?";

        PreparedStatement stm = null;
        try {
            stm = CONN.prepareStatement(sql);
            int param = 1;
            if (component.getComponentNumber() != -1)
                stm.setInt(param++, component.getComponentNumber());
            if (component.getComponentGroupId() != -1)
                stm.setInt(param++, component.getComponentGroupId());
            if (component.getBarcode() != null)
                stm.setString(param++, component.getBarcode());

            stm.setInt(param++, component.getStatus());

            stm.setString(param++, barcode);

            stm.execute();

            if (stm.getUpdateCount() == 1)
                return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return -1;
    }

    @Override
    public int deleteComponent(String barcode) {

        PreparedStatement stm = null;
        try {
            String sql = "DELETE FROM " + DATABASE_NAME + " WHERE barcode = ?";
            stm = CONN.prepareStatement(sql);
            stm.setString(1, barcode);
            stm.execute();
            if (stm.getUpdateCount() == 1) {
                return 1;
            } else {
                return -2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return -1;

    }
}
