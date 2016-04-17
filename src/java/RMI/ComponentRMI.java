/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DAL.ComponentDAO;
import java.sql.Connection;

/**
 *
 * @author hippomormor
 */
public class ComponentRMI implements IComponentRMI{

    private final ComponentDAO component;
    private final int componentId;

    public ComponentRMI(Connection conn, int id) {
        componentId = id;
        component = new ComponentDAO(conn);
    }

    @Override
    public String getBarcode() {
        return component.getComponent(componentId).getBarcode();
    }

    @Override
    public int getComponentGroupId() {
        return component.getComponent(componentId).getComponentGroupId();
    }

    @Override
    public int getComponentNumber() {
        return component.getComponent(componentId).getComponentNumber();
    }

    @Override
    public int getStatus() {
        return component.getComponent(componentId).getStatus();
    }

    @Override
    public int getComponentId() {
        return component.getComponent(componentId).getComponentId();
    }
}
