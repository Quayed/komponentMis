/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

/**
 *
 * @author hippomormor
 */
public class ComponentRMI {

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
}
