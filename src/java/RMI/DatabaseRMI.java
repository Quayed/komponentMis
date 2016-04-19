/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DAL.ComponentDAO;
import DAL.ComponentGroupDAO;
import DAL.LoanDAO;
import DTO.LoanDTO;
import DAL.StudentDAO;
import DTO.ComponentDTO;
import DTO.ComponentGroupDTO;
import DTO.StudentDTO;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import security.TokenHandler;

/**
 *
 * @author hippomormor
 */
public class DatabaseRMI extends UnicastRemoteObject implements IDatabaseRMI {
    
    private ComponentDAO componentDAO;
    private ComponentGroupDAO componentGroupDAO;
    private LoanDAO loanDAO;
    private StudentDAO studentDAO;
    private TokenHandler tokenhandler;
   
    public DatabaseRMI(Connection conn, String user, String pass) throws RemoteException {
        this(1190, conn, user, pass);
    }

    public DatabaseRMI(int port, Connection conn, String user, String pass) throws RemoteException {       
        super(port);
        componentDAO = new ComponentDAO(conn);
        componentGroupDAO = new ComponentGroupDAO(conn);
        loanDAO = new LoanDAO(conn);
        studentDAO = new StudentDAO(conn);
        tokenhandler = new TokenHandler(user, pass);
    }
  
    @Override
    public ComponentDTO getComponent(int componentId, int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return null;        
        return componentDAO.getComponent(componentId);
    }
    
    @Override
    public ComponentDTO getComponent(String barcode, int publicToken) throws RemoteException { 
        if (!tokenhandler.checkToken(publicToken))
            return null;        
        return componentDAO.getComponent(barcode);
    }    

    @Override
    public ComponentDTO[] getComponents(int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return null;        
        return componentDAO.getComponents();
    }
    
    @Override
    public ComponentGroupDTO getComponentGroup(int componentGroupId, int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return null;
        return componentGroupDAO.getComponentGroup(componentGroupId);          
    }
    
    @Override
    public ComponentGroupDTO[] getComponentGroups(int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return null;        
        return componentGroupDAO.getComponentGroups();
    }

    @Override
    public LoanDTO getLoan(int loanId, int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return null;       
        return loanDAO.getLoan(loanId);
    }

    @Override
    public LoanDTO[] getLoans(int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return null;        
        return loanDAO.getLoans();
    }
    
    @Override
    public int createLoan(LoanDTO loanDTO, int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return -1;
        return loanDAO.createLoan(loanDTO);
    }  
    
    public int updateLoan(LoanDTO loanDTO, int publicToken) {
        if (!tokenhandler.checkToken(publicToken))
            return -1;        
        return loanDAO.updateLoan(loanDTO);
    }
    
    @Override
    public int deleteLoan(int loanId, int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return -1;        
        return loanDAO.deleteLoan(loanId);
    }

    @Override
    public StudentDTO getStudent(String studentId, int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return null;       
        return studentDAO.getStudent(studentId);
    }

    @Override
    public StudentDTO[] getStudents(int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return null;       
        return studentDAO.getStudents();
    }    
    
    @Override
    public int generateToken(int randomToken) throws RemoteException {
        tokenhandler.generateToken(randomToken);
        return tokenhandler.getPublicToken();
    }

    @Override
    public StudentDTO getTest(int publicToken) throws RemoteException {
        if (!tokenhandler.checkToken(publicToken))
            return null;
        return new StudentDTO("testId", "testName", 0);
    }
}
