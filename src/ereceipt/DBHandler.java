package ereceipt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.time.LocalDate;

import static javafx.application.Platform.exit;

class DBHandler {
    private Connection con;
    private Alert alert;

    DBHandler() {
        try {
            String dbURL = "jdbc:sqlite:ereceipt.db3";
            con = DriverManager.getConnection(dbURL);
        } catch (SQLException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());

            alert.showAndWait();
            exit();
        }
    }

    //================================================================================
    // Handle Lessees Table
    //================================================================================

    boolean newLessee(Lessee data) {
        String sql = String.format("INSERT INTO LESSEES VALUES(null,'%s','%s','%s','%s','%s','%s','%s','%s','%d','%s', '%s', '%s')",
                data.getName(),
                data.getAddress(),
                data.getCity(),
                data.getPostalCode(),
                data.getMobile(),
                data.getHomeTelephone(),
                data.getWorkTelephone(),
                data.getEmail(),
                data.getApartment().getId(),
                data.getContractFromSQL(),
                data.getContractUntilSQL(),
                data.getRent());
        return executeStatement(sql);
    }

    private boolean executeStatement(String sql) {
        try {
            Statement sta = con.createStatement();
            sta.execute(sql);

            sta.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    void getLessees(ObservableList<Lessee> list) {
        try {
            list.clear();
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery("SELECT * FROM LESSEES");

            while (res.next()) {
                list.add(new Lessee(
                        res.getString("NAME"),
                        res.getString("ADDRESS"),
                        res.getString("CITY"),
                        res.getString("POSTAL_CODE"),
                        res.getString("MOBILE"),
                        res.getString("HOME_TELEPHONE"),
                        res.getString("WORK_TELEPHONE"),
                        res.getString("EMAIL"),
                        getApartment(res.getInt("APARTMENT_ID")),
                        LocalDate.parse(res.getString("CONTRACT_FROM")),
                        LocalDate.parse(res.getString("CONTRACT_UNTIL")),
                        res.getInt("RENT"),
                        res.getInt("ID")));
            }
            res.close();
            sta.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    boolean setLessee(Lessee data) {
        try {
            Statement sta = con.createStatement();
            sta.execute(String.format(
                    "UPDATE LESSEES SET NAME = '%s', ADDRESS = '%s', CITY = '%s', POSTAL_CODE = '%s', MOBILE = '%s', HOME_TELEPHONE = '%s', WORK_TELEPHONE = '%s', EMAIL = '%s', APARTMENT_ID = '%d', CONTRACT_FROM = '%s', CONTRACT_UNTIL = '%s', RENT = '%s' WHERE ID = '%d'",
                    data.getName(),
                    data.getAddress(),
                    data.getCity(),
                    data.getPostalCode(),
                    data.getMobile(),
                    data.getHomeTelephone(),
                    data.getWorkTelephone(),
                    data.getEmail(),
                    data.getApartment().getId(),
                    data.getContractFromSQL(),
                    data.getContractUntilSQL(),
                    data.getRent(),
                    data.getId()));

            sta.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    void deleteLessee(String id) {
        try {
            Statement sta = con.createStatement();
            sta.execute(String.format("DELETE FROM LESSEES WHERE ID = '%s'", id));

            sta.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Lessee getLessee(Integer id) {
        Lessee result = null;
        try {
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery(String.format("SELECT * FROM LESSEES WHERE ID = '%d'", id));

            while (res.next()) {
                result = new Lessee(
                        res.getString("NAME"),
                        res.getString("ADDRESS"),
                        res.getString("CITY"),
                        res.getString("POSTAL_CODE"),
                        res.getString("MOBILE"),
                        res.getString("HOME_TELEPHONE"),
                        res.getString("WORK_TELEPHONE"),
                        res.getString("EMAIL"),
                        getApartment(res.getInt("APARTMENT_ID")),
                        LocalDate.parse(res.getString("CONTRACT_FROM")),
                        LocalDate.parse(res.getString("CONTRACT_UNTIL")),
                        res.getInt("RENT"),
                        res.getInt("ID"));
            }
            res.close();
            sta.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    ObservableList<Lessee> getActiveLesseesForApartment(Apartment apartment, String from, String until) {
        ObservableList<Lessee> result = FXCollections.observableArrayList();
        try {
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery(String.format(
                    "SELECT * FROM LESSEES WHERE APARTMENT_ID = '%d' AND ((CONTRACT_UNTIL >= date('%s') OR CONTRACT_UNTIL >= date('%s')) AND (CONTRACT_FROM <= date('%s') OR CONTRACT_FROM <= date('%s')))",
                    apartment.getId(),
                    until,
                    from,
                    from,
                    until));

            while (res.next()) {
                result.add(new Lessee(
                        res.getString("NAME"),
                        res.getString("ADDRESS"),
                        res.getString("CITY"),
                        res.getString("POSTAL_CODE"),
                        res.getString("MOBILE"),
                        res.getString("HOME_TELEPHONE"),
                        res.getString("WORK_TELEPHONE"),
                        res.getString("EMAIL"),
                        getApartment(res.getInt("APARTMENT_ID")),
                        LocalDate.parse(res.getString("CONTRACT_FROM")),
                        LocalDate.parse(res.getString("CONTRACT_UNTIL")),
                        res.getInt("RENT"),
                        res.getInt("ID")));
            }
            res.close();
            sta.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    //================================================================================
    // Handle Apartments Table
    //================================================================================

    boolean newApartment(Apartment data) {
        try {
            Statement sta = con.createStatement();
            sta.execute(String.format("INSERT INTO APARTMENTS VALUES(null,'%s','%s', '%s', '%s')",
                    data.getName(),
                    data.getAddress(),
                    data.getCity(),
                    data.getFloor()));

            sta.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    void getApartments(ObservableList<Apartment> list) {
        try {
            list.clear();
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery("SELECT * FROM APARTMENTS");

            while (res.next()) {
                list.add(new Apartment(
                        res.getString("NAME"),
                        res.getString("ADDRESS"),
                        res.getString("CITY"),
                        res.getString("FLOOR"),
                        res.getInt("ID"))
                );
            }
            res.close();
            sta.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Apartment getApartment(Integer id) {
        Apartment result = null;
        try {
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery(String.format("SELECT * FROM APARTMENTS WHERE ID = '%d'", id));

            while (res.next()) {
                result = new Apartment(
                        res.getString("NAME"),
                        res.getString("ADDRESS"),
                        res.getString("CITY"),
                        res.getString("FLOOR"),
                        res.getInt("ID")
                );
            }
            res.close();
            sta.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    boolean setApartment(Apartment data) {
        try {
            Statement sta = con.createStatement();
            sta.execute(String.format("UPDATE APARTMENTS SET NAME = '%s', ADDRESS = '%s', CITY = '%s', FLOOR = '%s' WHERE ID = '%d'",
                    data.getName(),
                    data.getAddress(),
                    data.getCity(),
                    data.getFloor(),
                    data.getId()));

            sta.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    boolean deleteApartment(String id) {
        try {
            Statement sta = con.createStatement();
            sta.execute(String.format("DELETE FROM APARTMENTS WHERE ID = '%s'", id));

            sta.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //================================================================================
    // Handle Receipts Table
    //================================================================================

    boolean newReceipt(Receipt data) {
        String sql = String.format("INSERT INTO RECEIPTS VALUES(null,'%d','%s','%s','%d','%d', '%s')",
                data.getApartment().getId(),
                data.getDateSQL(),
                data.getAmount(),
                data.getMonths(),
                data.getLessee().getId(),
                data.getYear());

        return executeStatement(sql);
    }

    void getReceipts(ObservableList<Receipt> list) {
        try {
            list.clear();
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery("SELECT * FROM RECEIPTS");

            while (res.next()) {
                Receipt invoice = new Receipt(
                        getLessee(res.getInt("LESSEE_ID")),
                        getApartment(res.getInt("APARTMENT_ID")),
                        LocalDate.parse(res.getString("DATE")),
                        res.getDouble("AMOUNT"),
                        res.getInt("MONTHS"),
                        res.getString("YEAR"),
                        res.getInt("ID"));

                list.add(invoice);
            }
            res.close();
            sta.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    boolean setReceipt(Receipt data) {
        try {
            Statement sta = con.createStatement();
            sta.execute(String.format(
                    "UPDATE RECEIPTS SET DATE = '%s',APARTMENT_ID = '%d',AMOUNT = '%s',MONTHS = '%d',LESSEE_ID = '%d', YEAR = '%s' WHERE ID = '%d'",
                    data.getDateSQL(),
                    data.getApartment().getId(),
                    data.getAmount(),
                    data.getMonths(),
                    data.getLessee().getId(),
                    data.getYear(),
                    data.getId()));

            sta.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    void deleteReceipt(String id) {
        try {
            Statement sta = con.createStatement();
            sta.execute(String.format("DELETE FROM RECEIPTS WHERE ID = '%s'", id));

            sta.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //================================================================================
    // Handle Backup and Restore of database
    //================================================================================

    String backUpDatabase(String file) {
        try {
            //Store the SQL file
            Statement sta = con.createStatement();
            if (!sta.execute("SCRIPT SIMPLE DROP TO '" + file + "'")) {
                System.out.println("Error in Backup");
            }

            sta.close();
        } catch (SQLException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("BackUp Procedure Error");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());

            alert.showAndWait();
        }
        return "FINISHED";
    }

    String restoreDatabase(String file) {
        try {
            //Restore the SQL file
            Statement sta = con.createStatement();
            sta.execute("RUNSCRIPT FROM '" + file + "'");

            sta.close();
        } catch (SQLException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Restore Procedure Error");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());

            alert.showAndWait();
        }
        return "FINISHED";
    }
}
