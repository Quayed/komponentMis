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
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import security.TokenHandlerServer;

/**
 *
 * @author hippomormor
 */
public class DatabaseRMI extends UnicastRemoteObject implements IDatabaseRMI {

    private ComponentDAO componentDAO;
    private ComponentGroupDAO componentGroupDAO;
    private LoanDAO loanDAO;
    private StudentDAO studentDAO;
    private TokenHandlerServer tokenhandler;

    public DatabaseRMI(Connection conn, String user, String pass) throws RemoteException {
        this(1099, conn, user, pass);
    }

    public DatabaseRMI(int port, Connection conn, String user, String pass) throws RemoteException {
        super(port);
        componentDAO = new ComponentDAO(conn);
        componentGroupDAO = new ComponentGroupDAO(conn);
        loanDAO = new LoanDAO(conn);
        studentDAO = new StudentDAO(conn);
        tokenhandler = new TokenHandlerServer(user, pass);
    }

    @Override
    public ComponentDTO getComponent(String barcode, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return componentDAO.getComponent(barcode);
    }

    @Override
    public ComponentDTO[] getComponents(BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return componentDAO.getComponents();
    }

    @Override
    public ComponentGroupDTO getComponentGroup(int componentGroupId, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return componentGroupDAO.getComponentGroup(componentGroupId);
    }

    @Override
    public ComponentGroupDTO[] getComponentGroups(BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return componentGroupDAO.getComponentGroups();
    }

    @Override
    public LoanDTO getLoan(int loanId, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return loanDAO.getLoan(loanId);
    }

    @Override
    public LoanDTO[] getLoans(BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return loanDAO.getLoans();
    }

    @Override
    public int createLoan(LoanDTO loanDTO, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        LoanDTO[] loans = loanDAO.getLoansForBarcode(loanDTO.getBarcode());
        if (loans != null) { // first time loaned check
            boolean isLoaned = false;
            for (LoanDTO loan : loans) {
                if (loan.getDeliveryDate() == "") {
                    isLoaned = true;
                    break;
                }
            }
            if (isLoaned) { // check if any loan currently active
                return -5;
            }
        }
        return loanDAO.createLoan(loanDTO);
    }

    @Override
    public int updateLoan(LoanDTO loanDTO, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return loanDAO.updateLoan(loanDTO);
    }

    @Override
    public int deleteLoan(int loanId, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return loanDAO.deleteLoan(loanId);
    }

    @Override
    public LoanDTO[] searchLoans(String keyword, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return loanDAO.searchLoans(keyword);
    }

    @Override
    public LoanDTO[] getLoansForStudent(String studentId, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return loanDAO.getLoansForStudent(studentId);
    }

    @Override
    public LoanDTO[] getLoansForBarcode(String barcode, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return loanDAO.getLoansForBarcode(barcode);
    }

    @Override
    public StudentDTO getStudent(String studentId, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return studentDAO.getStudent(studentId);
    }

    @Override
    public StudentDTO[] getStudents(BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return studentDAO.getStudents();
    }

    @Override
    public BigInteger exchangeTokens(BigInteger publicToken, int ID) throws RemoteException {
        tokenhandler.generateKey(publicToken, ID);
        return tokenhandler.getPublicToken();
    }

    @Override
    public BigInteger exchangeKeys(BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            System.out.println("Key-tokens not matching!");
        }
        return tokenhandler.getKeyToken(ID);
    }

    @Override
    public int getNewID() throws RemoteException {
        return tokenhandler.getNewID();
    }

    @Override
    public int updateComponent(ComponentDTO component, BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            throw new RemoteException();
        }
        return componentDAO.updateComponent(component);
    }
}
