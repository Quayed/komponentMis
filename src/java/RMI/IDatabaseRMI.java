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
public interface IDatabaseRMI extends java.rmi.Remote {
    
    // Component
    
    ComponentRMI getComponent(int componentId) throws java.rmi.RemoteException;
    
    void setComponent(ComponentRMI componentRMI) throws java.rmi.RemoteException;
    
    
    // ComponentGroup
    
    ComponentGroupRMI getComponentGroup(int componentGroupId) throws java.rmi.RemoteException;
    
    void setComponentGroup(ComponentGroupRMI componentGroupRMI) throws java.rmi.RemoteException;
    
    
    // Loan
    
    void createLoan(LoanRMI loanRMI) throws java.rmi.RemoteException;

    LoanRMI getLoan(int loanId) throws java.rmi.RemoteException;
    
    void setLoan(LoanRMI loanRMI) throws java.rmi.RemoteException;

    int deleteLoan(int loanId) throws java.rmi.RemoteException;
    
    
    // Student

    StudentRMI getStudent(String studentId) throws java.rmi.RemoteException;
    
    void setStudent(StudentRMI studentRMI) throws java.rmi.RemoteException;

    StudentRMI getTest() throws java.rmi.RemoteException;
}
