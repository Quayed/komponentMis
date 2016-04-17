/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.rmi.RemoteException;
import java.util.Date;

/**
 *
 * @author hippomormor
 */
public interface ILoanRMI {
    
    public int getLoanId();

    public void setLoanId(int id);

    public int getComponentId();

    public void setComponentId(int componentId);

    public String getStudentId();

    public void setStudentId(String studentId);

    public Date getLoanDate();

    public void setLoanDate(Date loanDate);

    public Date getDueDate();

    public void setDueDate(Date dueDate);
    
    public Date getDeliveryDate();

    public void setDeliveryDate(Date deliveryDate);

    public String getDeliveredTo();

    public void setDeliveredTo(String deliveredTo);
    
    public int deleteLoan();
}
