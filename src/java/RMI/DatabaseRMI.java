/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DAL.ComponentDAO;
import RMI.IDatabaseRMI;
import DAL.LoanDAO;
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
    private LoanRMI loan;
    private LoanDAO loanDAO;
    private StudentRMI student;
    private final Connection conn;
   
    public DatabaseRMI(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    public DatabaseRMI(int port, Connection conn) throws RemoteException {
        super(port);
        this.conn = conn;     
    }
  
    @Override
    public ComponentRMI getComponent(int componentId) throws RemoteException {
        componentDAO = new ComponentDAO(conn);
        component = new ComponentRMI(componentDAO.getComponent(componentId).getComponentId(),componentDAO.getComponent(componentId).getBarcode(),
                        componentDAO.getComponent(componentId).getComponentGroupId(), componentDAO.getComponent(componentId).getComponentNumber(), 
                        componentDAO.getComponent(componentId).getStatus()); 
        return component;
    }

    @Override
    public void setComponent(ComponentRMI component) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
