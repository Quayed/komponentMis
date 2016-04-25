package DTO;

import java.io.Serializable;

/**
 * Created by mathias on 21/03/16.
 */
public class ComponentDTO implements Serializable {
    private ComponentGroupDTO componentGroup;
    private int componentGroupId;
    private int componentNumber;
    private String barcode;
    private int status;

    public ComponentDTO() {
        componentGroupId = -1;
        componentNumber = -1;
        status = -1;
    }

    public ComponentDTO(int componentGroupId, int componentNumber, String barcode, int status) {
        this.componentGroup = new ComponentGroupDTO();
        this.componentGroup.setComponentGroupId(componentGroupId);
        this.componentNumber = componentNumber;
        this.barcode = barcode;
        this.status = status;
    }

    public int getComponentGroupId() {
        if (componentGroup == null)
            return -1;
        return componentGroup.getComponentGroupId();
    }

    public void setComponentGroupId(int componentGroupId) {
        if(componentGroup == null)
            componentGroup = new ComponentGroupDTO();
        componentGroup.setComponentGroupId(componentGroupId);
        componentGroupId = componentGroupId;
    }

    public int getComponentNumber() {
        return componentNumber;
    }

    public void setComponentNumber(int componentNumber) {
        this.componentNumber = componentNumber;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public void setComponentGroup(ComponentGroupDTO componentGroup){
        this.componentGroup = componentGroup;
    }
    
    public ComponentGroupDTO getComponentGroup(){
        return this.componentGroup;
    }
}
