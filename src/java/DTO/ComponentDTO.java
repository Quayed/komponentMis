package DTO;

import java.io.Serializable;

/**
 * Created by mathias on 21/03/16.
 */
public class ComponentDTO implements Serializable {
    private int componentId;
    private int componentGroupId;
    private int componentNumber;
    private String barcode;
    private int status;

    public ComponentDTO() {

    }

    public ComponentDTO(int componentId, int componentGroupId, int componentNumber, String barcode, int status) {
        this.componentId = componentId;
        this.componentGroupId = componentGroupId;
        this.componentNumber = componentNumber;
        this.barcode = barcode;
        this.status = status;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int id) {
        this.componentId = id;
    }

    public int getComponentGroupId() {
        return componentGroupId;
    }

    public void setComponentGroupId(int componentGroupId) {
        this.componentGroupId = componentGroupId;
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
}
