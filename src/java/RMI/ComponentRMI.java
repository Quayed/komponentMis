/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.io.Serializable;

/**
 *
 * @author hippomormor
 */
public class ComponentRMI implements Serializable{

    private int componentId;
    private String barcode;
    private int componentGroupId;
    private int componentNumber;
    private int status;

    public ComponentRMI() {
    }
    
    public ComponentRMI(int componentId, String barcode, int componentGroupId, int componentNumber, int status) {
        this.componentId = componentId;
        this.barcode = barcode;
        this.componentGroupId = componentGroupId;
        this.componentNumber = componentNumber;
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public int getComponentGroupId() {
        return componentGroupId;
    }

    public int getComponentNumber() {
        return componentNumber;
    }

    public int getStatus() {
        return status;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setComponentGroupId(int componentGroupId) {
        this.componentGroupId = componentGroupId;
    }

    public void setComponentNumber(int componentNumber) {
        this.componentNumber = componentNumber;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
