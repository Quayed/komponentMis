package DAL;

import DTO.ComponentGroupDTO;
import DTO.LoanDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        if (loan.getBarcode() == null || loan.getStudentId() == null || loan.getLoanDate() == null || loan.getDueDate() == null) {
            if (loan.getStudentId() == null)
                return -3;
            if (loan.getLoanDate() == null)
                return -4;

            return -1;
        }

        String sql = "INSERT INTO " + DATABASE_NAME + "(barcode, studentId, loanDate, dueDate";
        String sqlValues = "?, ?, ?, ?";


        if (loan.getDeliveryDate() != null && !loan.getDeliveryDate().equals("")) {
            sql += ", deliveryDate";
            sqlValues += ", ?";
        }

        if (loan.getDeliveredTo() != null && !loan.getDeliveredTo().equals("")) {
            sql += ", deliveredTo";
            sqlValues += ", ?";
        }

        if (loan.getMailCount() != -1){
            sql += ", mailCount";
            sqlValues += ", ?";
        }

        sql += ") VALUES(" + sqlValues + ")";

        try {
            int param = 1;
            PreparedStatement stm = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(param++, loan.getBarcode());

            stm.setString(param++, loan.getStudentId());

            stm.setDate(param++, new java.sql.Date(loan.getLoanDateAsDate().getTime()));

            stm.setDate(param++, new java.sql.Date(loan.getDueDateAsDate().getTime()));

            if (loan.getDeliveryDate() != null && !loan.getDeliveryDate().equals(""))
                stm.setDate(param++, new java.sql.Date(loan.getDeliveryDateAsDate().getTime()));

            if (loan.getDeliveredTo() != null && !loan.getDeliveredTo().equals(""))
                stm.setString(param++, loan.getDeliveredTo());

            if (loan.getMailCount() != -1)
                stm.setInt(param++, loan.getMailCount());

            stm.execute();

            if (stm.getUpdateCount() != 1) {
                return -2;
            } else {
                ResultSet generatedKeys = stm.getGeneratedKeys();
                if (generatedKeys.next())
                    return generatedKeys.getInt(1);
                else
                    return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public LoanDTO getLoan(int loanId) {
        try {
            String sql = "SELECT l.loanID, c.barcode, cg.componentGroupID, cg.name as componentGroupName, " +
                    "cg.standardLoanDuration, cg.status as componentGroupStatus, c.componentNumber, " +
                    "c.status as componentStatus, s.studentId, s.name as studentName, s.status as studentStatus, " +
                    "l.loanDate, l.dueDate, l.deliveryDate, l.deliveredTo, l.mailCount " +
                    "FROM Loan l " +
                    "LEFT JOIN Component c ON l.barcode = c.barcode " +
                    "LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId " +
                    "LEFT JOIN Student s ON l.studentId = s.studentId " +
                    "WHERE l.loanId = ?";
            PreparedStatement stm = CONN.prepareStatement(sql);
            stm.setInt(1, loanId);
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                LoanDTO loan = new LoanDTO();
                loan.setLoanId(result.getInt("loanId"));
                loan.setBarcode(result.getString("barcode"));
                loan.getComponent().setComponentGroup(new ComponentGroupDTO());
                loan.getComponent().getComponentGroup().setComponentGroupId(result.getInt("componentGroupId"));
                loan.getComponent().getComponentGroup().setName(result.getString("componentGroupName"));
                loan.getComponent().getComponentGroup().setStatus(result.getInt("componentGroupStatus"));
                loan.getComponent().getComponentGroup().setStandardLoanDuration(result.getString("standardLoanDuration"));
                loan.getComponent().setComponentNumber(result.getInt("componentNumber"));
                loan.setStudentId(result.getString("studentId"));
                loan.getStudent().setName(result.getString("studentName"));
                loan.getStudent().setStatus(result.getInt("studentStatus"));
                loan.setLoanDateFromDate(result.getDate("loanDate"));
                loan.setDueDateFromDate(result.getDate("dueDate"));
                loan.setDeliveryDateFromDate(result.getDate("deliveryDate"));
                loan.setDeliveredTo(result.getString("deliveredTo"));
                loan.setMailCount(result.getInt("mailCount"));
                return loan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public LoanDTO[] getLoans() {
        try {
            String sql = "SELECT l.loanID, c.barcode, cg.componentGroupID, cg.name as componentGroupName, " +
                    "cg.standardLoanDuration, cg.status as componentGroupStatus, c.componentNumber, " +
                    "c.status as componentStatus, l.studentId, " +
                    "l.loanDate, l.dueDate, l.deliveryDate, l.deliveredTo, l.mailCount " +
                    "FROM Loan l " +
                    "LEFT JOIN Component c ON l.barcode = c.barcode " +
                    "LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId;";
            ResultSet result = CONN.createStatement().executeQuery(sql);

            return addResultToLoan(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LoanDTO[] searchLoans(String keyword) {
        String sql = "SELECT l.loanID, c.barcode, cg.componentGroupID, cg.name as componentGroupName, " +
                "cg.standardLoanDuration, cg.status as componentGroupStatus, c.componentNumber, " +
                "c.status as componentStatus, s.studentId, s.name as studentName, s.status as studentStatus, " +
                "l.loanDate, l.dueDate, l.deliveryDate, l.deliveredTo, l.mailCount " +
                "FROM Loan l " +
                "LEFT JOIN Component c ON l.barcode = c.barcode " +
                "LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId " +
                "WHERE l.studentId LIKE ? OR "
                + "l.loanDate LIKE ? OR "
                + "l.dueDate LIKE ? OR "
                + "c.barcode LIKE ? OR "
                + "cg.name LIKE ?;";
        keyword = "%" + keyword + "%";
        try {
            PreparedStatement stm = CONN.prepareStatement(sql);
            stm.setString(1, keyword);
            stm.setString(2, keyword);
            stm.setString(3, keyword);
            stm.setString(4, keyword);
            stm.setString(5, keyword);
            ResultSet result = stm.executeQuery();

            return addResultToLoan(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LoanDTO[] getLoansForStudent(String studentId) {
        try {

            PreparedStatement stm = CONN.prepareStatement("SELECT l.loanID, c.barcode, cg.componentGroupID, cg.name as componentGroupName, " +
                    "cg.standardLoanDuration, cg.status as componentGroupStatus, c.componentNumber, " +
                    "c.status as componentStatus, l.studentId, " +
                    "l.loanDate, l.dueDate, l.deliveryDate, l.deliveredTo, l.mailCount " +
                    "FROM Loan l " +
                    "LEFT JOIN Component c ON l.barcode = c.barcode " +
                    "LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId " +
                    "WHERE l.studentId LIKE ?;");

            studentId = "%" + studentId + "%";

            stm.setString(1, studentId);
            ResultSet result = stm.executeQuery();

            return addResultToLoan(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public LoanDTO[] getLoansForBarcode(String barcode) {
        try {
            PreparedStatement stm = CONN.prepareStatement("SELECT l.loanID, c.barcode, cg.componentGroupID, cg.name as componentGroupName, " +
                    "cg.standardLoanDuration, cg.status as componentGroupStatus, c.componentNumber, " +
                    "c.status as componentStatus, l.studentId, " +
                    "l.loanDate, l.dueDate, l.deliveryDate, l.deliveredTo, l.mailCount " +
                    "FROM Loan l " +
                    "LEFT JOIN Component c ON l.barcode = c.barcode " +
                    "LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId " +
                    "WHERE c.barcode LIKE ?;");
            barcode = "%" + barcode + "%";

            stm.setString(1, barcode);
            ResultSet result = stm.executeQuery();

            return addResultToLoan(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getStudentIdForActiveLoan(String barcode){
        try {

            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM Loan l "
                    + "LEFT JOIN Component c ON l.barcode = c.barcode "
                    + "LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId "
                    + "WHERE l.barcode = ? AND l.deliveryDate IS NULL;");

            stm.setString(1, barcode);
            ResultSet result = stm.executeQuery();

            if(result.next()) {
                System.out.println(result.getString("studentId"));
                return result.getString("studentId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private LoanDTO[] addResultToLoan(ResultSet result) throws SQLException {
        ArrayList<LoanDTO> loans = new ArrayList<>();
        while (result.next()) {
            LoanDTO loan = new LoanDTO();
            loan.setLoanId(result.getInt("loanId"));
            loan.setBarcode(result.getString("barcode"));
            loan.getComponent().setComponentGroup(new ComponentGroupDTO());
            loan.getComponent().getComponentGroup().setComponentGroupId(result.getInt("componentGroupId"));
            loan.getComponent().getComponentGroup().setName(result.getString("componentGroupName"));
            loan.getComponent().getComponentGroup().setStatus(result.getInt("componentGroupStatus"));
            loan.getComponent().getComponentGroup().setStandardLoanDuration(result.getString("standardLoanDuration"));
            loan.getComponent().setComponentNumber(result.getInt("componentNumber"));
            loan.getComponent().setStatus(result.getInt("componentStatus"));
            loan.setStudentId(result.getString("studentId"));
            loan.setLoanDateFromDate(result.getDate("loanDate"));
            loan.setDueDateFromDate(result.getDate("dueDate"));
            loan.setDeliveryDateFromDate(result.getDate("deliveryDate"));
            loan.setDeliveredTo(result.getString("deliveredTo"));
            loan.setMailCount(result.getInt("mailCount"));
            loans.add(loan);
        }

        // Check if something was actually found
        if (loans.size() == 0)
            return null;

        return loans.toArray(new LoanDTO[loans.size()]);
    }

    @Override
    public int updateLoan(LoanDTO loan) {
        if (loan.getLoanId() == 0 || (loan.getBarcode() == null && loan.getStudentId() != null && loan.getLoanDate() == null
                && loan.getDueDate() == null && loan.getDeliveryDate() == null && loan.getDeliveredTo() == null && loan.getMailCount() == -1)) {
            return -1;
        }

        String sql = "UPDATE " + DATABASE_NAME + " set ";
        String sqlValues = "";

        if (loan.getBarcode() != null) {
            sqlValues += "barcode = ?";
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

        if (loan.getMailCount() != -1){
            if (sqlValues.equals(""))
                sqlValues += "mailCount = ?";
            else
                sqlValues += ", mailCount = ?";
        }

        sql += sqlValues;
        sql += " WHERE loanId = ?";

        try {
            PreparedStatement stm = CONN.prepareStatement(sql);
            int param = 1;

            if (loan.getBarcode() != null)
                stm.setString(param++, loan.getBarcode());
            if (loan.getStudentId() != null)
                stm.setString(param++, loan.getStudentId());
            if (loan.getLoanDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getLoanDateAsDate().getTime()));
            if (loan.getDueDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getDueDateAsDate().getTime()));
            if (loan.getDeliveryDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getDeliveryDateAsDate().getTime()));
            if (loan.getDeliveredTo() != null)
                stm.setString(param++, loan.getDeliveredTo());
            if (loan.getMailCount() != -1)
                stm.setInt(param++, loan.getMailCount());

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
































