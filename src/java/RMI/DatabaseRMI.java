/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DAL.ComponentDAO;
import DAL.ComponentGroupDAO;
import DAL.LoanDAO;
import DAL.StudentDAO;
import RMI.IDatabaseRMI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;

/**
 *
 * @author hippomormor
 */
public class DatabaseRMI extends UnicastRemoteObject implements IDatabaseRMI {
    
    private ComponentRMI component;
    private ComponentDAO componentDAO;
    private ComponentGroupRMI componentGroup;
    private ComponentGroupDAO componentGroupDAO;
    private LoanRMI loan;
    private LoanDAO loanDAO;
    private StudentRMI student;
    private StudentDAO studentDAO;
   
    public DatabaseRMI(Connection conn) throws RemoteException {
        this(1190, conn);
    }

    public DatabaseRMI(int port, Connection conn) throws RemoteException {       
        super(port);
        componentDAO = new ComponentDAO(conn);
        componentGroupDAO = new ComponentGroupDAO(conn);
        loanDAO = new LoanDAO(conn);
        studentDAO = new StudentDAO(conn);
    }
  
    @Override
    public ComponentRMI getComponent(int componentId) throws RemoteException {        
        component = new ComponentRMI(componentDAO.getComponent(componentId).getComponentId(),componentDAO.getComponent(componentId).getBarcode(),
                        componentDAO.getComponent(componentId).getComponentGroupId(), componentDAO.getComponent(componentId).getComponentNumber(), 
                        componentDAO.getComponent(componentId).getStatus()); 
        return component;
    }

    @Override
    public void setComponent(ComponentRMI component) throws RemoteException {
        componentDAO.getComponent(component.getComponentId()).setBarcode(component.getBarcode());
        componentDAO.getComponent(component.getComponentId()).setComponentGroupId(component.getComponentGroupId());
        componentDAO.getComponent(component.getComponentId()).setComponentId(component.getComponentId());
        componentDAO.getComponent(component.getComponentId()).setComponentNumber(component.getComponentNumber());
        componentDAO.getComponent(component.getComponentId()).setStatus(component.getStatus());
    }

    @Override
    public ComponentGroupRMI getComponentGroup(int componentGroupId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setComponentGroup(ComponentGroupRMI componentGroup) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteLoan(int loanId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LoanRMI getLoan(int loanId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StudentRMI getStudent(String studentId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLoan(LoanRMI loanRMI) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStudent(StudentRMI studentRMI) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createLoan(LoanRMI loanRMI) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
