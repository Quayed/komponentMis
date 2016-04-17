/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.util.Date;


/**
 *
 * @author hippomormor
 */
public class LoanRMI{
    
    private int loanId;
    private int componentId;
    private String studentId;
    private Date loanDate;
    private Date dueDate;
    private Date deliveryDate;
    private String deliveredTo;

    public LoanRMI() {
    }

    public LoanRMI(int loanId, int componentId, String studentId, Date loanDate, Date dueDate, Date deliveryDate, String deliveredTo) {
        this.loanId = loanId;
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

    public int getComponentId() {
        return componentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public Date getLoanDate() {
        return loanDate;
    }  

    public Date getDueDate() {
        return dueDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveredTo() {
        return deliveredTo;
    }
}
