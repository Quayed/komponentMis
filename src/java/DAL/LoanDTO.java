package DAL;

import java.util.Date;

/**
 * Created by mathias on 21/03/16.
 */
public class LoanDTO {
    private int loanId;
    private int componentId;
    private String studentId;
    private Date loanDate;
    private Date dueDate;
    private Date deliveryDate;
    private String deliveredTo;


    public LoanDTO(int laonId, int componentId, String studentId, Date loanDate, Date dueDate, Date deliveryDate, String deliveredTo) {
        this.loanId = laonId;
        this.componentId = componentId;
        this.studentId = studentId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.deliveryDate = deliveryDate;
        this.deliveredTo = deliveredTo;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
    
    public Date getDueDate(){
        return dueDate;
    }

    public void setDueDate(Date dueDate){
        this.dueDate = dueDate;
    }
    
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveredTo() {
        return deliveredTo;
    }

    public void setDeliveredTo(String deliveredTo) {
        this.deliveredTo = deliveredTo;
    }
}
