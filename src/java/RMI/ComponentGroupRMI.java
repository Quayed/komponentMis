/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DAL.ComponentGroupDAO;
import java.sql.Connection;

/**
 *
 * @author hippomormor
 */
public class ComponentGroupRMI implements IComponentGroupRMI {

    private final ComponentGroupDAO componentGroup;
    private final int componentGroupId;

    public ComponentGroupRMI(Connection conn, int id) {
        componentGroupId = id;
        componentGroup = new ComponentGroupDAO(conn);
    }
    
    @Override
    public int getComponentGroupId() {
        return componentGroup.getComponentGroup(componentGroupId).getComponentGroupId();
    }

    @Override
    public String getName() {
        return componentGroup.getComponentGroup(componentGroupId).getName();
    }

    @Override
    public String getStandardLoanDuration() {
        return componentGroup.getComponentGroup(componentGroupId).getStandardLoanDuration();
    }

    @Override
    public int getStatus() {
        return componentGroup.getComponentGroup(componentGroupId).getStatus();
    }
    
}
