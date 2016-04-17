/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI.server;

import RMI.server.ComponentGroupRMI;
import RMI.server.ComponentRMI;
import RMI.IDatabaseRMI;
import RMI.server.LoanRMI;
import RMI.server.StudentRMI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;

/**
 *
 * @author hippomormor
 */
public class DatabaseRMI extends UnicastRemoteObject implements IDatabaseRMI {
    
    private ComponentRMI component;
    private ComponentGroupRMI componentGroup;
    private LoanRMI loan;
    private StudentRMI student;
    private Connection conn;
   
    public DatabaseRMI(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    public DatabaseRMI(int port, Connection conn) throws RemoteException {
        super(port);
        this.conn = conn;     
    }
  
    @Override
    public ComponentRMI getComponent(int componentId) throws RemoteException {  
        component = new ComponentRMI(conn, componentId);
        return component;
    }

    @Override
    public LoanRMI getLoan(int loanId) throws RemoteException {
        loan = new LoanRMI(conn, loanId);
        return loan;
    }

    @Override
    public int deleteLoan(int loanId) throws RemoteException {
        loan = new LoanRMI(conn, loanId);
        return loan.deleteLoan();
    }
        
    @Override
    public StudentRMI getStudent(String studentId) throws RemoteException {
        student = new StudentRMI(conn, studentId);
        return student;
    }

    @Override
    public ComponentGroupRMI getComponentGroup(int componentGroupId) throws RemoteException {
        componentGroup = new ComponentGroupRMI(conn, componentGroupId);
        return componentGroup;
    }
}
