package DAL;

import DTO.LoanDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by mathias on 21/03/16.
 */
public class LoanDAO implements ILoanDAO {
    private final Connection CONN;
    private final String DATABASE_NAME = "Loan";

    public LoanDAO(Connection conn) {
        this.CONN = conn;
    }

    @Override
    public int createLoan(LoanDTO loan) {
        if(loan.getComponentId() == 0 || loan.getStudentId() == null || loan.getLoanDate() == null || loan.getDueDate() == null)
            return -1;
        
        String sql = "INSERT INTO " + DATABASE_NAME + "(componentId, studentId, loanDate, dueDate";
        String sqlValues = "?, ?, ?, ?";


        if (loan.getDeliveryDate() != null) {
            sql += ", deliveryDate";
            sqlValues = ", ?";
        }

        if (loan.getDeliveredTo() != null) {
            sql += ", deliveredTo";
            sqlValues += ", ?";
        }

        sql += ") VALUES(" + sqlValues + ")";

        try {
            int param = 1;
            PreparedStatement stm = CONN.prepareStatement(sql);

            stm.setInt(param++, loan.getComponentId());

            stm.setString(param++, loan.getStudentId());

            stm.setDate(param++, new java.sql.Date(loan.getLoanDate().getTime()));

            stm.setDate(param++, new java.sql.Date(loan.getDueDate().getTime()));

            if (loan.getDeliveryDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getDeliveryDate().getTime()));

            if (loan.getDeliveredTo() != null)
                stm.setString(param++, loan.getDeliveredTo());

            stm.execute();

            if (stm.getUpdateCount() != 1) {
                return -2;
            } else {
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public LoanDTO getLoan(int loanId) {
        try {
            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE loanId = ?");
            stm.setInt(1, loanId);
            ResultSet result = stm.executeQuery();
            while (result.next())
                return new LoanDTO(result.getInt("loanId"), result.getInt("componentId"), result.getString("studentId"),
                        result.getDate("loanDate"), result.getDate("dueDate"), result.getDate("deliveryDate"), result.getString("deliveredTo"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public LoanDTO[] getLoans() {
        try {
            ResultSet result = CONN.createStatement().executeQuery("SELECT * FROM " + DATABASE_NAME);
            ArrayList<LoanDTO> loans = new ArrayList<>();
            while (result.next())
                loans.add(new LoanDTO(result.getInt("loanId"), result.getInt("componentId"), result.getString("studentId"),
                        result.getDate("loanDate"), result.getDate("dueDate"), result.getDate("deliveryDate"), result.getString("deliveredTo")));

            // Check if something was actually found
            if(loans.size() == 0)
                return null;

            return loans.toArray(new LoanDTO[loans.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public LoanDTO[] searchLoans(String keyword) {
        // TODO DESIGN MYSQL QUERY FOR THIS
    }

    @Override
    public LoanDTO[] getLoansForStudent(String studentId) {
        try{
            ArrayList<LoanDTO> loans = new ArrayList<>();
            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE studentId LIKE '%?%'");
            stm.setString(1, studentId);
            ResultSet result = stm.executeQuery();
            while(result.next())
                loans.add(new LoanDTO(result.getInt("loanId"), result.getInt("componentId"), result.getString("studentId"),
                        result.getDate("loanDate"), result.getDate("dueDate"), result.getDate("deliveryDate"), result.getString("deliveredTo")));

            // Check if something was actually found
            if(loans.size() == 0)
                return null;

            return loans.toArray(new LoanDTO[loans.size()]);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public int updateLoan(LoanDTO loan) {
        if (loan.getLoanId() == 0 || (loan.getComponentId() == 0 && loan.getStudentId() != null && loan.getLoanDate() == null
                && loan.getDueDate() == null && loan.getDeliveryDate() == null && loan.getDeliveredTo() == null)) {
            return -1;
        }

        String sql = "UPDATE " + DATABASE_NAME + " set ";
        String sqlValues = "";

        if (loan.getComponentId() != 0) {
            sqlValues += "componentId = ?";
        }

        if (loan.getStudentId() != null) {
            if (sqlValues.equals("")) {
                sqlValues += "studentId = ?";
            } else {
                sqlValues += ", studentId = ?";
            }
        }

        if (loan.getLoanDate() != null) {
            if (sqlValues.equals(""))
                sqlValues += "loanDate = ?";
            else
                sqlValues += ", loanDate = ?";
        }

        if (loan.getDueDate() != null) {
            if (sqlValues.equals(""))
                sqlValues += "dueDate = ?";
            else
                sqlValues += ", dueDate = ?";
        }

        if (loan.getDeliveryDate() != null) {
            if (sqlValues.equals(""))
                sqlValues += "deliveryDate = ?";
            else
                sqlValues += ", deliveryDate = ?";
        }

        if (loan.getDeliveredTo() != null) {
            if (sqlValues.equals(""))
                sqlValues += "deliveredTo = ?";
            else
                sqlValues += ", deliveredTo = ?";
        }


        sql += sqlValues;
        sql += " WHERE loanId = ?";

        try {
            PreparedStatement stm = CONN.prepareStatement(sql);
            int param = 1;

            if (loan.getComponentId() != 0)
                stm.setInt(param++, loan.getComponentId());
            if (loan.getStudentId() != null)
                stm.setString(param++, loan.getStudentId());
            if (loan.getLoanDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getLoanDate().getTime()));
            if (loan.getDueDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getDueDate().getTime()));
            if (loan.getDeliveryDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getDeliveryDate().getTime()));
            if (loan.getDeliveredTo() != null)
                stm.setString(param++, loan.getDeliveredTo());

            stm.setInt(param++, loan.getLoanId());

            stm.execute();

            if (stm.getUpdateCount() == 1)
                return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteLoan(int loanId) {
        try {
            String sql = "DELETE FROM " + DATABASE_NAME + "WHERE loanId = ?";
            PreparedStatement stm = CONN.prepareStatement(sql);
            stm.setInt(1, loanId);
            if (stm.getUpdateCount() == 1) {
                return 1;
            } else {
                return -2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
































