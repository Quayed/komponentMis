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
public class ComponentGroupRMI implements Serializable{

    private int componentGroupId;
    private String name;
    private String standardLoanDuration ;
    private int status;

    public ComponentGroupRMI() {
    }

    public ComponentGroupRMI(int componentGroupId, String name, String standardLoanDuration, int status) {
        this.componentGroupId = componentGroupId;
        this.name = name;
        this.standardLoanDuration = standardLoanDuration;
        this.status = status;
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

    public void setComponentGroupId(int componentGroupId) {
        this.componentGroupId = componentGroupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStandardLoanDuration(String standardLoanDuration) {
        this.standardLoanDuration = standardLoanDuration;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
