/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DTO.ComponentDTO;
import DTO.ComponentGroupDTO;
import DTO.LoanDTO;
import DTO.StudentDTO;
import java.math.BigInteger;
import java.rmi.RemoteException;


/**
 *
 * @author hippomormor
 */
public interface IDatabaseRMI extends java.rmi.Remote {
    
    // Token-stuff
    
    int getNewID() throws java.rmi.RemoteException;
    
    BigInteger exchangeTokens(BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    BigInteger exchangeKeys(BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    // Component
        
    ComponentDTO getComponent(String barcode, BigInteger keyToken, int ID) throws RemoteException;
    
    ComponentDTO[] getComponents(BigInteger keyToken, int ID) throws RemoteException;
    
    // ComponentGroup
    
    ComponentGroupDTO getComponentGroup(int componentGroupId, BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    ComponentGroupDTO[] getComponentGroups(BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    // Loan
    
    int createLoan(LoanDTO loanDTO, BigInteger keyToken, int ID) throws java.rmi.RemoteException;

    LoanDTO getLoan(int loanId, BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    LoanDTO[] getLoans(BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    LoanDTO[] searchLoans(String keyword, BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    LoanDTO[] getLoansForStudent(String studentId, BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    int updateLoan(LoanDTO loan, BigInteger keyToken, int ID) throws java.rmi.RemoteException;

    int deleteLoan(int loanId, BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    // Student

    StudentDTO getStudent(String studentId, BigInteger keyToken, int ID) throws java.rmi.RemoteException;
    
    StudentDTO[] getStudents(BigInteger keyToken, int ID) throws java.rmi.RemoteException;
}
