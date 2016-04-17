/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI.server;

import DAL.LoanDAO;
import RMI.ILoanRMI;
import java.sql.Connection;
import java.util.Date;

/**
 *
 * @author hippomormor
 */
public class LoanRMI implements ILoanRMI{
    
    private final int loanId;
    private final LoanDAO loan;

    public LoanRMI(Connection conn, int id) {
        loanId = id;
        loan = new LoanDAO(conn);
    }

    @Override
    public int getLoanId() {
        return loan.getLoan(loanId).getLoanId();
    }

    @Override
    public void setLoanId(int id) {
        loan.getLoan(loanId).setLoanId(id);
    }

    @Override
    public int getComponentId() {
        return loan.getLoan(loanId).getComponentId();
    }

    @Override
    public void setComponentId(int componentId) {
        loan.getLoan(loanId).setComponentId(componentId);
    }

    @Override
    public String getStudentId() {
        return loan.getLoan(loanId).getStudentId();
    }

    @Override
    public void setStudentId(String studentId) {
        loan.getLoan(loanId).setStudentId(studentId);
    }

    @Override
    public Date getLoanDate() {
        return loan.getLoan(loanId).getLoanDate();
    }

    @Override
    public void setLoanDate(Date loanDate) {
        loan.getLoan(loanId).setLoanDate(loanDate);
    }

    @Override
    public Date getDueDate() {
        return loan.getLoan(loanId).getDueDate();
    }

    @Override
    public void setDueDate(Date dueDate) {
        loan.getLoan(loanId).setDueDate(dueDate);
    }

    @Override
    public Date getDeliveryDate() {
        return loan.getLoan(loanId).getDeliveryDate();
    }

    @Override
    public void setDeliveryDate(Date deliveryDate) {
        loan.getLoan(loanId).setDeliveryDate(deliveryDate);
    }

    @Override
    public String getDeliveredTo() {
        return loan.getLoan(loanId).getDeliveredTo();
    }

    @Override
    public void setDeliveredTo(String deliveredTo) {
        loan.getLoan(loanId).setDeliveredTo(deliveredTo);
    }

    @Override
    public int deleteLoan() {
        return loan.deleteLoan(loanId);
    }
}
