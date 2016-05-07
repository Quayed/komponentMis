
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
 * @author Christian Genter
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
        
        // Make data acces objects
        componentDAO = new ComponentDAO(conn);
        componentGroupDAO = new ComponentGroupDAO(conn);
        loanDAO = new LoanDAO(conn);
        studentDAO = new StudentDAO(conn);
        
        // Make Token-handler
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
        
        // First time loaned check
        LoanDTO[] loans = loanDAO.getLoansForBarcode(loanDTO.getBarcode());
        if (loans != null) { 
            boolean isLoaned = false;
            for (LoanDTO loan : loans) {
                if (loan.getDeliveryDate() == null || loan.getDeliveryDate().equals(""))  {
                    isLoaned = true;
                    break;
                }
            }
            
            // Check if any loan currently active
            if (isLoaned) { 
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
        
        // Check if loan is allready delivered
        LoanDTO[] loans = loanDAO.getLoansForBarcode(loanDTO.getBarcode());
        if (loans != null) {
            
            for (LoanDTO loan : loans) {

                if (loan.getDeliveryDate() == null || loan.getDeliveryDate().equals("")) {
                    return loanDAO.updateLoan(loanDTO);
                }
            }
        }
        return -5;
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

    // Method to do the Diffie-Hellman key-exchange algorithm over RMI
    @Override
    public BigInteger exchangeTokens(BigInteger publicToken, int ID) throws RemoteException {
        tokenhandler.generateKey(publicToken, ID);
        return tokenhandler.getPublicToken();
    }

    // Method to check key integrity between client & host over RMI
    @Override
    public BigInteger exchangeKeys(BigInteger keyToken, int ID) throws RemoteException {
        if (!tokenhandler.checkKey(keyToken, ID)) {
            System.out.println("Key-tokens not matching!");
        }
        return tokenhandler.getKeyToken(ID);
    }

    // Method to get a new client ID - used when a client connects/reconnects. ID is then asociated with the unique key in an array in TokenHandler 
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
