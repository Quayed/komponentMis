package DTO;

import java.io.Serializable;

/**
 * Created by mathias on 21/03/16.
 */
public class ComponentGroupDTO implements Serializable {
    private int componentGroupId;
    private String name;
    private String standardLoanDuration;
    private int status;

    public ComponentGroupDTO() {
        status = -1;
    }

    public ComponentGroupDTO(int componentGroupId, String name, String standardLoanDuration, int status) {
        this.componentGroupId = componentGroupId;
        this.name = name;
        this.standardLoanDuration = standardLoanDuration;
        this.status = status;
    }

    public int getComponentGroupId() {
        return componentGroupId;
    }

    public void setComponentGroupId(int componentGroupId) {
        this.componentGroupId = componentGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandardLoanDuration() {
        return standardLoanDuration;
    }

    public void setStandardLoanDuration(String standardLoanDuration) {
        this.standardLoanDuration = standardLoanDuration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
