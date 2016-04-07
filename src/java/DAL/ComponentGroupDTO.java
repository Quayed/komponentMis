package DAL;

/**
 * Created by mathias on 21/03/16.
 */
public class ComponentGroupDTO {
    private int componentGroupId;
    private String name;
    private String standardLoanDuration;
    private int status;
    
    public ComponentGroupDTO() {

    }

    public ComponentGroupDTO(int componentGroupId, String name, String standardLoanDuration, int status){
        this.componentGroupId = componentGroupId;
        this.name = name;
        this.standardLoanDuration = standardLoanDuration;
        this.status = status;
    }

    public int getId() {
        return componentGroupId;
    }

    public void setId(int componentGroupId) {
        this.componentGroupId = componentGroupId;
    }

    public String getNavn() {
        return name;
    }

    public void setNavn(String name) {
        this.name = name;
    }

    public String getStandardLoanDuration() {
        return standardLoanDuration;
    }

    public void setStandardLoanDuration(String standardLoanDuration) {
        this.standardLoanDuration = standardLoanDuration;
    }
    
    public int getStatus(){
        return status;
    }
    
    public void setStatus(int status){
        this.status = status;
    }
}
