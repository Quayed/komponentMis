package DAL;

import DTO.LoanDTO;

/**
 * Created by mathias on 21/03/16.
 */
public interface ILoanDAO {
    int createLoan(LoanDTO loan);

    LoanDTO getLoan(int loanId);

    LoanDTO[] getLoans();

    int updateLoan(LoanDTO loan);

    int deleteLoan(int loanId);
}
