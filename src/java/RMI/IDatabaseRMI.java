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
    
    void setComponent(ComponentRMI component) throws java.rmi.RemoteException;
    
    // ComponetGroup
    
    ComponentGroupRMI getComponentGroup(int componentGroupId) throws java.rmi.RemoteException;
    
    void setComponentGroup(ComponentGroupRMI componentGroup) throws java.rmi.RemoteException;
    
    
    // Loan

    LoanRMI getLoan(int loanId) throws java.rmi.RemoteException;

    int deleteLoan(int loanId) throws java.rmi.RemoteException;
    
    
    // Student

    StudentRMI getStudent(String studentId) throws java.rmi.RemoteException;
    
}
