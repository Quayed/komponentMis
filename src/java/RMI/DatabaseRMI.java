/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DAL.ComponentDAO;
import DAL.ComponentDTO;
import DAL.ComponentGroupDAO;
import DAL.ComponentGroupDTO;
import DAL.LoanDAO;
import DAL.LoanDTO;
import DAL.StudentDAO;
import DAL.StudentDTO;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;

/**
 *
 * @author hippomormor
 */
public class DatabaseRMI extends UnicastRemoteObject implements IDatabaseRMI {

    private ComponentDAO component;
    private ComponentGroupDAO componentgroup;
    private LoanDAO loan;
    private StudentDAO student;
    
    public DatabaseRMI(Connection conn) throws RemoteException {
        init(conn);
    }

    public DatabaseRMI(int port, Connection conn) throws RemoteException {
        super(port);
        init(conn);     
    }
    
    private void init(Connection conn) {
       component = new ComponentDAO(conn);
       componentgroup = new ComponentGroupDAO(conn);
       loan = new LoanDAO(conn);
       student = new StudentDAO(conn);
    }

    @Override
    public ComponentDTO getComponent(int componentId) throws RemoteException {
        return component.getComponent(componentId);
    }

    @Override
    public ComponentDTO[] getComponents() throws RemoteException {
        return component.getComponents();
    }

    @Override
    public int updateComponent(ComponentDTO componentDTO) throws RemoteException {
        return component.updateComponent(componentDTO);
    }

    @Override
    public ComponentGroupDTO getComponentGroup(int componentGroupId) throws RemoteException {
        return componentgroup.getComponentGroup(componentGroupId);
    }

    @Override
    public ComponentGroupDTO[] getComponentGroups() throws RemoteException {
        return componentgroup.getComponentGroups();
    }

    @Override
    public int updateComponentGroups(ComponentGroupDTO componentGroupDTO) throws RemoteException {
        return componentgroup.updateComponentGroups(componentGroupDTO);
    }

    @Override
    public int createLoan(LoanDTO loanDTO) throws RemoteException {
        return loan.createLoan(loanDTO);
    }

    @Override
    public LoanDTO getLoan(int loanId) throws RemoteException {
        return loan.getLoan(loanId);
    }

    @Override
    public LoanDTO[] getLoans() throws RemoteException {
        return loan.getLoans();
    }

    @Override
    public int updateLoan(LoanDTO loanDTO) throws RemoteException {
        return loan.updateLoan(loanDTO);
    }

    @Override
    public int deleteLoan(int loanId) throws RemoteException {
        return loan.deleteLoan(loanId);
    }

    @Override
    public StudentDTO getStudent(String studentId) throws RemoteException {
        return student.getStudent(studentId);
    }

    @Override
    public StudentDTO[] getStudents() throws RemoteException {
        return student.getStudents();
    } 
}
