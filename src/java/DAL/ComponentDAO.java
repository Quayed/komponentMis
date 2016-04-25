package DAL;

import DTO.ComponentDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        if (component.getComponentNumber() == 0 || component.getComponentGroupId() == 0 || component.getBarcode() == null)
            return -1;

        String sql = "INSERT INTO " + DATABASE_NAME + 
                " (componentGroupId, componentNumber, barcode, status) "
                + "VALUES(?, ?, ?, ?);";

        System.out.println(sql);
        
        try {
            int param = 1;
            PreparedStatement stm = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setInt(param++, component.getComponentGroupId());

            stm.setInt(param++, component.getComponentNumber());

            stm.setString(param++, component.getBarcode());

            stm.setInt(param++, component.getStatus());
    
            stm.execute();

            
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    @Override
    public ComponentDTO getComponent(String barcode) {
        try {
            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE barcode = ?");
            stm.setString(1, barcode);
            ResultSet result = stm.executeQuery();
            while (result.next())
                return new ComponentDTO(result.getInt("componentGroupId"), result.getInt("componentNumber"), result.getString("barcode"), result.getInt("status"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ComponentDTO[] getComponents() {
        try {
            ResultSet result = CONN.createStatement().executeQuery("SELECT * FROM " + DATABASE_NAME + " c "
                            + "LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId;");
            ArrayList<ComponentDTO> components = new ArrayList<>();
            while (result.next()){
                ComponentDTO component = new ComponentDTO(result.getInt("componentGroupId"), result.getInt("componentNumber"), result.getString("barcode"), result.getInt("status"));
                component.getComponentGroup().setName(result.getString("name"));
                components.add(component);
                
            }
            return components.toArray(new ComponentDTO[components.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override 
    public int updateComponent(ComponentDTO component){
        if(component.getBarcode() == null){
            return -1;
        }else{
            String barcode = component.getBarcode();
            component.setBarcode(null);
            return updateComponent(barcode, component);
        }
    }
    
    @Override
    public int updateComponent(String barcode, ComponentDTO component) {
        if (barcode== null || (component.getComponentNumber() == -1 && component.getComponentGroupId() == -1 && component.getStatus() == -1 && component.getBarcode() == null))
            return -1;

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

        try {
            PreparedStatement stm = CONN.prepareStatement(sql);
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
        }

        return -1;
    }

    @Override
    public int deleteComponent(String barcode) {

        try {
            String sql = "DELETE FROM " + DATABASE_NAME + " WHERE barcode = ?";
            PreparedStatement stm = CONN.prepareStatement(sql);
            stm.setString(1, barcode);
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
