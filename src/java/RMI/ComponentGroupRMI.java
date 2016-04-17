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
public class ComponentGroupRMI {

    private int componentGroupId;
    private int componentId;
    private String name;
    private String standardLoanDuration ;
    private int status;

    public ComponentGroupRMI() {
    }

    public ComponentGroupRMI(int componentGroupId, int componentId, String name, String standardLoanDuration, int status) {
        this.componentGroupId = componentGroupId;
        this.componentId = componentId;
        this.name = name;
        this.standardLoanDuration = standardLoanDuration;
        this.status = status;
    }
    
    public int getComponentId() {
        return componentId;
    }  
    
    public int getComponentGroupId() {
        return componentGroupId;
    }

    public String getName() {
        return name;
    }

    public String getStandardLoanDuration() {
        return standardLoanDuration;
    }

    public int getStatus() {
        return status;
    }
    
}
