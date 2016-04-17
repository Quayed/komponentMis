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
    
    IComponentRMI getComponent(int componentId) throws java.rmi.RemoteException;
    
    
    // ComponetGroup
    
    IComponentGroupRMI getComponentGroup(int componentGroupId) throws java.rmi.RemoteException;
    
    
    // Loan

    ILoanRMI getLoan(int loanId) throws java.rmi.RemoteException;

    int deleteLoan(int loanId) throws java.rmi.RemoteException;
    
    
    // Student

    IStudentRMI getStudent(String studentId) throws java.rmi.RemoteException;
    
}
