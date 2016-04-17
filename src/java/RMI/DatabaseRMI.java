/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DAL.ComponentDAO;
import DAL.ComponentGroupDAO;
import DAL.LoanDAO;
import DAL.LoanDTO;
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
    private Connection conn;
   
    public DatabaseRMI(Connection conn) throws RemoteException {
        this(1190, conn);
    }

    public DatabaseRMI(int port, Connection conn) throws RemoteException {       
        super(port);
        this.conn = conn;
        componentDAO = new ComponentDAO(conn);
        componentGroupDAO = new ComponentGroupDAO(conn);
        loanDAO = new LoanDAO(conn);
        studentDAO = new StudentDAO(conn);
    }
  
    @Override
    public ComponentRMI getComponent(int componentId) throws RemoteException {        
        component = 
                new ComponentRMI(componentDAO.getComponent(componentId).getComponentId(), 
                componentDAO.getComponent(componentId).getBarcode(), 
                componentDAO.getComponent(componentId).getComponentGroupId(), 
                componentDAO.getComponent(componentId).getComponentNumber(), 
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
        componentGroup = 
                new ComponentGroupRMI(componentGroupDAO.getComponentGroup(componentGroupId).getComponentGroupId(), 
                componentGroupDAO.getComponentGroup(componentGroupId).getName(), 
                componentGroupDAO.getComponentGroup(componentGroupId).getStandardLoanDuration(), 
                componentGroupDAO.getComponentGroup(componentGroupId).getStatus());
        return componentGroup;
    }

    @Override
    public void setComponentGroup(ComponentGroupRMI componentGroup) throws RemoteException {
        componentGroupDAO.getComponentGroup(componentGroup.getComponentGroupId()).setComponentGroupId(componentGroup.getComponentGroupId());
        componentGroupDAO.getComponentGroup(componentGroup.getComponentGroupId()).setName(componentGroup.getName());
        componentGroupDAO.getComponentGroup(componentGroup.getComponentGroupId()).setStandardLoanDuration(componentGroup.getStandardLoanDuration());
        componentGroupDAO.getComponentGroup(componentGroup.getComponentGroupId()).setStatus(componentGroup.getStatus());
    }

    @Override
    public LoanRMI getLoan(int loanId) throws RemoteException {
        loan = 
                new LoanRMI(loanDAO.getLoan(loanId).getLoanId(), 
                loanDAO.getLoan(loanId).getComponentId(), 
                loanDAO.getLoan(loanId).getStudentId(), 
                loanDAO.getLoan(loanId).getLoanDate(), 
                loanDAO.getLoan(loanId).getDueDate(), 
                loanDAO.getLoan(loanId).getDeliveryDate(), 
                loanDAO.getLoan(loanId).getDeliveredTo());
        return loan;
    }
 
    @Override
    public void createLoan(LoanRMI loanRMI) throws RemoteException {
        LoanDTO newLoan = 
                new LoanDTO(loanRMI.getLoanId(), 
                loanRMI.getComponentId(), 
                loanRMI.getStudentId(), 
                loanRMI.getLoanDate(), 
                loanRMI.getDueDate(), 
                loanRMI.getDeliveryDate(), 
                loanRMI.getDeliveredTo());
        loanDAO.createLoan(newLoan);
    }   
    
    @Override
    public void setLoan(LoanRMI loanRMI) throws RemoteException {
        loanDAO.getLoan(loanRMI.getLoanId()).setComponentId(loanRMI.getComponentId());
        loanDAO.getLoan(loanRMI.getLoanId()).setDeliveredTo(loanRMI.getDeliveredTo());
        loanDAO.getLoan(loanRMI.getLoanId()).setDeliveryDate(loanRMI.getDeliveryDate());
        loanDAO.getLoan(loanRMI.getLoanId()).setDueDate(loanRMI.getDueDate());
        loanDAO.getLoan(loanRMI.getLoanId()).setLoanDate(loanRMI.getLoanDate());
        loanDAO.getLoan(loanRMI.getLoanId()).setLoanId(loanRMI.getLoanId());
        loanDAO.getLoan(loanRMI.getLoanId()).setStudentId(loanRMI.getStudentId());
    }
    
    @Override
    public int deleteLoan(int loanId) throws RemoteException {
        return loanDAO.deleteLoan(loanId);      
    }

    @Override
    public StudentRMI getStudent(String studentId) throws RemoteException {
        student = 
                new StudentRMI(studentDAO.getStudent(studentId).getStudentId(), 
                studentDAO.getStudent(studentId).getName(), 
                studentDAO.getStudent(studentId).getStatus());
        return student;
    }

    @Override
    public void setStudent(StudentRMI studentRMI) throws RemoteException {
        studentDAO.getStudent(studentRMI.getStudentId()).setName(studentRMI.getName());
        studentDAO.getStudent(studentRMI.getStudentId()).setStatus(studentRMI.getStatus());
        studentDAO.getStudent(studentRMI.getStudentId()).setStudentId(studentRMI.getStudentId());
    }
}
