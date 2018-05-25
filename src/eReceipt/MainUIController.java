package eReceipt;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class MainUIController {
    private static final String EMAIL_REGEX = "^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,6})$";
    private final ObservableList<Apartment> apartments = FXCollections.observableArrayList();
    private final ObservableList<Receipt> receipts = FXCollections.observableArrayList();
    private final ObservableList<Lessee> lessees = FXCollections.observableArrayList();
    private final DBHandler db = new DBHandler();
    private final ObservableList<String> period = FXCollections.observableArrayList(
            "12 Months",
            "11 Months",
            "10 Months",
            "9 Months",
            "8 Months",
            "7 Months",
            "6 Months",
            "5 Months",
            "4 Months",
            "3 Months",
            "2 Months",
            "1 Months"
    );
    public TabPane mainPane;
    public TableColumn<Receipt, Integer> idColumn;
    public TableColumn<Receipt, Lessee> lesseeColumn;
    public TableColumn<Receipt, LocalDate> dateColumn;
    public TableColumn<Receipt, Double> rentColumn;
    public TableColumn<Receipt, Integer> periodColumn;
    public TableColumn<Receipt, String> yearColumn;
    public TableColumn<Receipt, Apartment> apartmentColumn;
    public TableView<Receipt> tblReceipts;
    public ProgressBar bar;
    public TextField apartmentName;
    public TextField apartmentAddress;
    public TextField apartmentCity;
    public TextField apartmentFloor;
    public Button btnClear;
    public Label status;
    public Button btnDelete;
    public Button btnShowReceipt;
    public DatePicker date;
    public TextField amount;
    public ListView<Apartment> lstApartments;
    public ComboBox<Apartment> cmbApartments;
    public ComboBox<Lessee> cmbLessee;
    public ChoiceBox<String> cbxMonths;
    public ChoiceBox<String> cbxYear;
    public TextField lesseeName;
    public ComboBox<Apartment> lesseeApartment;
    public TextField lesseePostalCode;
    public TextField lesseeCity;
    public TextField lesseeMobile;
    public TextField lesseeAddress;
    public TextField lesseeHomeTelephone;
    public TextField lesseeWorkTelephone;
    public TextField lesseeEmail;
    public DatePicker lesseeContractFrom;
    public DatePicker lesseeContractUntil;
    public TextField lesseeRent;
    public ListView<Lessee> lstLessees;
    private ObservableList<String> years = FXCollections.observableArrayList();

    public void initialize() {
        bar.setVisible(false);
        btnClear.setDisable(true);
        btnDelete.setDisable(true);
        btnShowReceipt.setDisable(true);

        // Read Objects from database
        db.getApartments(apartments);
        db.getLessees(lessees);
        db.getReceipts(receipts);

        // Bind Table columns to specific properties of Receipt Class
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        lesseeColumn.setCellValueFactory(cellData -> cellData.getValue().lesseeProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        rentColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        periodColumn.setCellValueFactory(cellData -> cellData.getValue().monthsProperty().asObject());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty());
        apartmentColumn.setCellValueFactory(cellData -> cellData.getValue().apartmentProperty());

        // Update UI with read Objects from database
        tblReceipts.setItems(receipts);
        lstApartments.setItems(apartments);
        lstLessees.setItems(lessees);
        cmbApartments.setItems(apartments);
        lesseeApartment.setItems(apartments);
        cbxMonths.setItems(period);
        cbxMonths.setValue(period.get(0));
        years.add(Year.now().minusYears(1).toString());
        years.add(Year.now().toString());
        cbxYear.setItems(years);
        cbxYear.setValue(years.get(0));

        // Listeners and Cell Factories for Tables and ComboBox UI Elements
        lstLessees.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Lessee> call(ListView<Lessee> p) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Lessee t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        Callback<ListView<Apartment>, ListCell<Apartment>> apartmentCallback = new Callback<>() {
            @Override
            public ListCell<Apartment> call(ListView<Apartment> p) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Apartment t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        };

        lstApartments.setCellFactory(apartmentCallback);
        cmbApartments.setCellFactory(apartmentCallback);
        lesseeApartment.setCellFactory(apartmentCallback);

        String pattern = "dd-MM-yyyy";
        date.setPromptText(pattern);
        date.setValue(LocalDate.now());
        date.setConverter(
                new StringConverter<>() {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                    @Override
                    public String toString(LocalDate date
                    ) {
                        if (date != null) {
                            return dateFormatter.format(date);
                        } else {
                            return "";
                        }
                    }

                    @Override
                    public LocalDate fromString(String string
                    ) {
                        if (string != null && !string.isEmpty()) {
                            return LocalDate.parse(string, dateFormatter);
                        } else {
                            return null;
                        }
                    }
                });

        lesseeContractFrom.setPromptText(pattern);
        lesseeContractFrom.setConverter(
                new StringConverter<>() {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                    @Override
                    public String toString(LocalDate date
                    ) {
                        if (date != null) {
                            return dateFormatter.format(date);
                        } else {
                            return "";
                        }
                    }

                    @Override
                    public LocalDate fromString(String string
                    ) {
                        if (string != null && !string.isEmpty()) {
                            return LocalDate.parse(string, dateFormatter);
                        } else {
                            return null;
                        }
                    }
                });

        lesseeContractUntil.setPromptText(pattern);
        lesseeContractUntil.setConverter(
                new StringConverter<>() {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                    @Override
                    public String toString(LocalDate date
                    ) {
                        if (date != null) {
                            return dateFormatter.format(date);
                        } else {
                            return "";
                        }
                    }

                    @Override
                    public LocalDate fromString(String string
                    ) {
                        if (string != null && !string.isEmpty()) {
                            return LocalDate.parse(string, dateFormatter);
                        } else {
                            return null;
                        }
                    }
                });

        // Set correct mask for textfields
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}")) {
                amount.setText(oldValue);
            }
        });

        lesseeRent.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}")) {
                lesseeRent.setText(oldValue);
            }
        });

        lesseePostalCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,5}")) {
                lesseePostalCode.setText(oldValue);
            }
        });

        lesseeMobile.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}")) {
                lesseeMobile.setText(oldValue);
            }
        });

        lesseeHomeTelephone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}")) {
                lesseeHomeTelephone.setText(oldValue);
            }
        });

        lesseeWorkTelephone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}")) {
                lesseeWorkTelephone.setText(oldValue);
            }
        });

        cmbLessee.valueProperty().addListener((ov, oldValue, newValue) -> {
            cmbLessee.getStyleClass().remove("required");
            // If we change lessee we need to update the period combo box
            if (oldValue != newValue) {
                // Set correct value to period combo box
                LocalDate from = LocalDate.of(Year.parse(cbxYear.getValue()).getValue(), Month.JANUARY, 1);
                LocalDate until = LocalDate.of(Year.parse(cbxYear.getValue()).getValue(), Month.DECEMBER, 31);
                Lessee activeLessee = cmbLessee.getValue();
                long months;
                if (activeLessee != null) {
                    months = getMonths(from, until, activeLessee);
                    cbxMonths.setValue(Long.toString(months) + " Months");
                    amount.setText(Integer.toString(activeLessee.getRent()));
                }
            }
        });

        cbxYear.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                // Set correct value to period combo box
                LocalDate from = LocalDate.of(Year.parse(newValue).getValue(), Month.JANUARY, 1);
                LocalDate until = LocalDate.of(Year.parse(newValue).getValue(), Month.DECEMBER, 31);
                Lessee activeLessee = cmbLessee.getValue();
                long months;
                if (activeLessee != null) {
                    months = getMonths(from, until, activeLessee);
                    cbxMonths.setValue(Long.toString(months) + " Months");
                    amount.setText(Integer.toString(activeLessee.getRent()));
                }
            }
        }));

        apartmentAddress.textProperty().addListener((ov, oldValue, newValue) -> {
            apartmentAddress.getStyleClass().remove("required");
            apartmentAddress.setText(upperCase(newValue));
        });

        apartmentName.textProperty().addListener((ov, oldValue, newValue) -> {
            apartmentName.getStyleClass().remove("required");
            apartmentName.setText(upperCase(newValue));
        });

        apartmentCity.textProperty().addListener((ov, oldValue, newValue) -> {
            apartmentCity.getStyleClass().remove("required");
            apartmentCity.setText(upperCase(newValue));
        });

        apartmentFloor.textProperty().addListener((ov, oldValue, newValue) -> {
            apartmentFloor.getStyleClass().remove("required");
            apartmentFloor.setText(upperCase(newValue));
        });

        lesseeName.textProperty().addListener((ov, oldValue, newValue) -> {
            lesseeName.getStyleClass().remove("required");
            lesseeName.setText(upperCase(newValue));
        });

        cmbApartments.valueProperty().addListener((observable, oldValue, newValue) -> {
            cmbApartments.getStyleClass().remove("required");
            if (oldValue != newValue) {
                Apartment selectedApartment = cmbApartments.getSelectionModel().getSelectedItem();
                if (selectedApartment != null) {
                    // Change only if nothing is selected in Receipts table
                    if (tblReceipts.getSelectionModel().getSelectedItem() == null) {
                        // Find active lessees of previous year for selected apartment
                        LocalDate from = LocalDate.of(Year.parse(cbxYear.getValue()).getValue(), Month.JANUARY, 1);
                        LocalDate until = LocalDate.of(Year.parse(cbxYear.getValue()).getValue(), Month.DECEMBER, 31);
                        ObservableList<Lessee> activeLessees = db.getActiveLesseesForApartment(selectedApartment,
                                from.format(DateTimeFormatter.ISO_DATE),
                                until.format(DateTimeFormatter.ISO_DATE));
                        if (!activeLessees.isEmpty()) {
                            // Populate the lessees combo box
                            cmbLessee.setItems(activeLessees);

                            // Select the first lessee and update the period combo box
                            cmbLessee.getSelectionModel().selectFirst();

                            // Set correct value to period combo box
                            Lessee activeLessee = cmbLessee.getValue();
                            long months;

                            months = getMonths(from, until, activeLessee);
                            cbxMonths.setValue(Long.toString(months) + " Months");
                            cmbLessee.setValue(activeLessee);
                            amount.setText(Integer.toString(activeLessee.getRent()));
                        } else {
                            cbxMonths.setValue("12 Months");
                            cmbLessee.getItems().clear();
                            cmbLessee.setValue(null);
                        }
                    }
                }
            }
        });

        lesseeAddress.textProperty().addListener((ov, oldValue, newValue) -> lesseeAddress.setText(upperCase(newValue)));
        lesseeCity.textProperty().addListener((ov, oldValue, newValue) -> lesseeCity.setText(upperCase(newValue)));
        lesseeMobile.textProperty().addListener((ov, oldValue, newValue) -> lesseeMobile.getStyleClass().remove("required"));
        lesseeEmail.textProperty().addListener((ov, oldValue, newValue) -> lesseeEmail.getStyleClass().remove("required"));
        lesseeRent.textProperty().addListener((ov, oldValue, newValue) -> lesseeRent.getStyleClass().remove("required"));
        lesseeApartment.valueProperty().addListener((observable, oldValue, newValue) -> lesseeApartment.getStyleClass().remove("required"));
        date.valueProperty().addListener((observable, oldValue, newValue) -> date.getStyleClass().remove("required"));
        lesseeContractFrom.valueProperty().addListener((observable, oldValue, newValue) -> lesseeContractFrom.getStyleClass().remove("required"));
        lesseeContractUntil.valueProperty().addListener((observable, oldValue, newValue) -> lesseeContractUntil.getStyleClass().remove("required"));
        amount.textProperty().addListener((ov, oldValue, newValue) -> amount.getStyleClass().remove("required"));
    }

    private long getMonths(LocalDate from, LocalDate until, Lessee activeLessee) {
        long months;
        if (activeLessee.getContractFrom().isAfter(from)) {
            if (activeLessee.getContractUntil().isBefore(until)) {
                months = ChronoUnit.MONTHS.between(activeLessee.getContractFrom(), activeLessee.getContractUntil().plusDays(1));
            } else {
                months = ChronoUnit.MONTHS.between(activeLessee.getContractFrom(), until.plusDays(1));
            }
        } else {
            if (activeLessee.getContractUntil().isBefore(until)) {
                months = ChronoUnit.MONTHS.between(from, activeLessee.getContractUntil().plusDays(1));
            } else {
                months = ChronoUnit.MONTHS.between(from, until.plusDays(1));

            }
        }
        return months;
    }

    public void importDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Backup File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("SQLite Files", "*.db"));
        File selectedFile
                = fileChooser.showOpenDialog(status.getScene().getWindow());
        String backupFile;

        if (selectedFile == null) {
            showMessage("No Backup File selected", Type.ERROR);
            return;
        } else {
            backupFile = selectedFile.getAbsolutePath();
        }

        showMessage("Restore of internal Database started.", Type.INFO);

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                return db.restoreDatabase(backupFile);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                updateMessage("Done!");
                // Read Objects from database
                db.getApartments(apartments);
                db.getLessees(lessees);
                db.getReceipts(receipts);
                tblReceipts.setItems(receipts);
                lstApartments.setItems(apartments);
                lstLessees.setItems(lessees);
                clearApartment();
                clearReceipt();
                clearLessee();
                showMessage("Restore of file " + backupFile + " finished.", Type.INFO);
            }
        };

        bar.visibleProperty().unbind();
        bar.visibleProperty().bind(task.runningProperty());
        new Thread(task).start();
    }

    //================================================================================
    // UI Event Handlers
    //================================================================================

    public void exportDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Backup File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("SQLite Files", "*.db"));
        File selectedFile
                = fileChooser.showSaveDialog(status.getScene().getWindow());
        String backupFile;

        if (selectedFile == null) {
            showMessage("No Backup File selected", Type.ERROR);
            return;
        } else {
            backupFile = selectedFile.getAbsolutePath();
        }

        showMessage("BackUp of internal Database started.", Type.INFO);

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                return db.backUpDatabase(backupFile);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                updateMessage("Done!");
                showMessage("BackUp finished at " + backupFile, Type.INFO);
            }
        };

        bar.visibleProperty().unbind();
        bar.visibleProperty().bind(task.runningProperty());
        new Thread(task).start();
    }

    public void selectApartment() {
        Apartment selectedApartment = lstApartments.getSelectionModel().getSelectedItem();
        if (selectedApartment != null) {
            btnClear.setDisable(false);
            btnDelete.setDisable(false);

            apartmentName.setText(selectedApartment.getName());
            apartmentAddress.setText(selectedApartment.getAddress());
            apartmentCity.setText(selectedApartment.getCity());
            apartmentFloor.setText(selectedApartment.getFloor());
        }
    }

    public void selectLessee() {
        Lessee selectedLessee = lstLessees.getSelectionModel().getSelectedItem();
        if (selectedLessee != null) {
            btnClear.setDisable(false);
            btnDelete.setDisable(false);

            lesseeName.setText(selectedLessee.getName());
            lesseeAddress.setText(selectedLessee.getAddress());
            lesseePostalCode.setText(selectedLessee.getPostalCode());
            lesseeCity.setText(selectedLessee.getCity());
            lesseeMobile.setText(selectedLessee.getMobile());
            lesseeHomeTelephone.setText(selectedLessee.getHomeTelephone());
            lesseeWorkTelephone.setText(selectedLessee.getWorkTelephone());
            lesseeEmail.setText(selectedLessee.getEmail());
            lesseeApartment.setValue(selectedLessee.getApartment());
            lesseeContractFrom.setValue(selectedLessee.getContractFrom());
            lesseeContractUntil.setValue(selectedLessee.getContractUntil());
            lesseeRent.setText(Integer.toString(selectedLessee.getRent()));
        }
    }

    public void selectReceipt() {
        Receipt selectedReceipt = tblReceipts.getSelectionModel().getSelectedItem();
        if (selectedReceipt != null) {
            btnClear.setDisable(false);
            btnDelete.setDisable(false);
            btnShowReceipt.setDisable(false);

            cmbLessee.setValue(selectedReceipt.getLessee());
            amount.setText(selectedReceipt.getAmount().toString());
            date.setValue(selectedReceipt.getLocalDate());
            cmbApartments.setValue(selectedReceipt.getApartment());
            cbxMonths.setValue(selectedReceipt.getMonths().toString() + " Months");
            cbxYear.setValue(selectedReceipt.getYear());
        }
    }

    public void tabChanged() {
        switch (mainPane.getSelectionModel().getSelectedItem().getText()) {
            case "Apartments":
                btnShowReceipt.setDisable(true);
                if (lstApartments.getSelectionModel().getSelectedItem() == null) {
                    clearApartment();
                } else {
                    btnClear.setDisable(false);
                    btnDelete.setDisable(false);
                }
                break;
            case "Receipts":
                if (tblReceipts.getSelectionModel().getSelectedItem() == null) {
                    clearReceipt();
                } else {
                    btnClear.setDisable(false);
                    btnDelete.setDisable(false);
                    btnShowReceipt.setDisable(false);
                }
                break;
            case "Lessees":
                btnShowReceipt.setDisable(true);
                if (lstLessees.getSelectionModel().getSelectedItem() == null) {
                    clearLessee();
                } else {
                    btnClear.setDisable(false);
                    btnDelete.setDisable(false);
                }
                break;
        }
    }

    public void clear() {
        switch (mainPane.getSelectionModel().getSelectedItem().getText()) {
            case "Apartments":
                clearApartment();
                break;
            case "Receipts":
                clearReceipt();
                break;
            case "Lessees":
                clearLessee();
                break;
        }
    }

    public void delete() {
        switch (mainPane.getSelectionModel().getSelectedItem().getText()) {
            case "Apartments":
                deleteApartment();
                break;
            case "Receipts":
                deleteReceipt();
                break;
            case "Lessees":
                deleteLessee();
                break;
        }
    }

    public void save() {
        switch (mainPane.getSelectionModel().getSelectedItem().getText()) {
            case "Apartments":
                saveApartment();
                break;
            case "Receipts":
                saveReceipt();
                break;
            case "Lessees":
                saveLessee();
                break;
        }
    }

    public void generateReceipt() {
        Receipt receipt = tblReceipts.getSelectionModel().getSelectedItem();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Receipt File");
        fileChooser.setInitialFileName(receipt.getLessee().getName() + "-" + receipt.getYear());
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Downloads"));
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("ODT Files", "*.odt"));
        File selectedFile
                = fileChooser.showSaveDialog(status.getScene().getWindow());
        String receiptFile;

        if (selectedFile == null) {
            showMessage("No Receipt File selected", Type.ERROR);
            return;
        } else {
            receiptFile = selectedFile.getAbsolutePath();
        }
        showMessage("Receipt generation started.", Type.INFO);

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
                    DocumentTemplate template = documentTemplateFactory.getTemplate(getClass().getResourceAsStream("/receipt.odt"));
                    HashMap<String, String> data = new HashMap<>();
                    data.put("NR", Integer.toString(receipt.getId()));
                    data.put("DATE", receipt.getDate());
                    data.put("AMOUNT", String.format("%.2f", (receipt.getAmount() * receipt.getMonths())));
                    data.put("FLAT_ADDRESS", receipt.getApartment().getAddress());
                    data.put("FLAT_CITY", receipt.getApartment().getCity());
                    data.put("LESSEE_NAME", receipt.getLessee().getName());
                    data.put("YEAR", receipt.getYear());
                    data.put("FLAT_FLOOR", receipt.getApartment().getFloor());
                    template.createDocument(data, new FileOutputStream(receiptFile));
                } catch (IOException | DocumentTemplateException ex) {
                    ex.printStackTrace();
                }

                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + receiptFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                updateMessage("Done!");
                showMessage("Receipt generated at " + receiptFile, Type.INFO);
            }
        };

        bar.visibleProperty().unbind();
        bar.visibleProperty().bind(task.runningProperty());
        new Thread(task).start();
    }

    private void clearApartment() {
        apartmentName.setText("");
        apartmentAddress.setText("");
        apartmentCity.setText("");
        apartmentFloor.setText("");
        btnClear.setDisable(true);
        btnDelete.setDisable(true);
        lstApartments.getSelectionModel().clearSelection();
    }

    //================================================================================
    // UI related methods
    //================================================================================

    private void clearLessee() {
        lesseeName.setText("");
        lesseeAddress.setText("");
        lesseeCity.setText("");
        lesseePostalCode.setText("");
        lesseeMobile.setText("");
        lesseeHomeTelephone.setText("");
        lesseeWorkTelephone.setText("");
        lesseeEmail.setText("");
        lesseeApartment.setValue(null);
        lesseeContractFrom.setValue(null);
        lesseeContractUntil.setValue(null);
        lesseeRent.setText("");
        btnClear.setDisable(true);
        btnDelete.setDisable(true);
        lstLessees.getSelectionModel().clearSelection();
    }

    private void clearReceipt() {
        cmbLessee.getItems().clear();
        cmbLessee.setValue(null);
        amount.setText("");
        cbxMonths.setValue(period.get(0));
        if (years.size() != 0) {
            cbxYear.setValue(years.get(0));
        }
        date.setValue(LocalDate.now());
        cmbApartments.setValue(null);
        btnClear.setDisable(true);
        btnDelete.setDisable(true);
        btnShowReceipt.setDisable(true);
        tblReceipts.getSelectionModel().clearSelection();
    }

    private void saveApartment() {
        String name = apartmentName.getText();
        String address = apartmentAddress.getText();
        String city = apartmentCity.getText();
        String floor = apartmentFloor.getText();

        if (name.isEmpty() || address.isEmpty() || city.isEmpty() || floor.isEmpty()) {
            showMessage("Empty fields are not allowed", Type.ERROR);
            if (name.isEmpty()) {
                apartmentName.getStyleClass().add("required");
            }
            if (address.isEmpty()) {
                apartmentAddress.getStyleClass().add("required");
            }
            if (city.isEmpty()) {
                apartmentCity.getStyleClass().add("required");
            }
            if (floor.isEmpty()) {
                apartmentFloor.getStyleClass().add("required");
            }
        } else {
            if (lstApartments.getSelectionModel().isEmpty()) {
                Apartment data = new Apartment(
                        name,
                        address,
                        city,
                        floor,
                        -1
                );
                if (db.newApartment(data)) {
                    showMessage("New apartment added successfully", Type.SUCCESS);
                } else {
                    showMessage("Failed to add new apartment", Type.ERROR);
                }
            } else {
                Apartment selectedApartment = lstApartments.getSelectionModel().getSelectedItem();
                Apartment data = new Apartment(
                        name,
                        address,
                        city,
                        floor,
                        selectedApartment.getId()
                );
                if (db.setApartment(data)) {
                    showMessage("Apartment data edited successfully", Type.SUCCESS);
                } else {
                    showMessage("Failed to edit data of apartment", Type.ERROR);
                }
            }

            db.getApartments(apartments);
            clearApartment();
        }
    }

    private void saveLessee() {
        String name = lesseeName.getText();
        String mobile = lesseeMobile.getText();
        String rent = lesseeRent.getText();
        String email = lesseeEmail.getText();
        Apartment apartment = lesseeApartment.getValue();

        if (name.isEmpty()
                || mobile.isEmpty()
                || apartment == null
                || lesseeContractFrom.getValue() == null
                || lesseeContractUntil.getValue() == null
                || rent.isEmpty()) {
            showMessage("Empty fields are not allowed", Type.ERROR);
            if (name.isEmpty()) {
                lesseeName.getStyleClass().add("required");
            }
            if (mobile.isEmpty()) {
                lesseeMobile.getStyleClass().add("required");
            }
            if (apartment == null) {
                lesseeApartment.getStyleClass().add("required");
            }
            if (lesseeContractFrom.getValue() == null) {
                lesseeContractFrom.getStyleClass().add("required");
            }
            if (lesseeContractUntil.getValue() == null) {
                lesseeContractUntil.getStyleClass().add("required");
            }
            if (rent.isEmpty()) {
                lesseeRent.getStyleClass().add("required");
            }
        } else {
            if (!email.matches(EMAIL_REGEX)) {
                showMessage("Email is not valid", Type.ERROR);
                lesseeEmail.getStyleClass().add("required");
                return;
            }
            if (lstLessees.getSelectionModel().isEmpty()) {
                Lessee data = new Lessee(
                        name,
                        lesseeAddress.getText(),
                        lesseeCity.getText(),
                        lesseePostalCode.getText(),
                        mobile, lesseeHomeTelephone.getText(),
                        lesseeWorkTelephone.getText(),
                        lesseeEmail.getText(),
                        apartment,
                        lesseeContractFrom.getValue(),
                        lesseeContractUntil.getValue(),
                        Integer.parseInt(lesseeRent.getText()),
                        -1);
                if (db.newLessee(data)) {
                    showMessage("New lessee added successfully", Type.SUCCESS);
                } else {
                    showMessage("Failed to add new lessee", Type.ERROR);
                }
            } else {
                Lessee selectedLessee = lstLessees.getSelectionModel().getSelectedItem();
                Lessee data = new Lessee(
                        name,
                        lesseeAddress.getText(),
                        lesseeCity.getText(),
                        lesseePostalCode.getText(),
                        mobile, lesseeHomeTelephone.getText(),
                        lesseeWorkTelephone.getText(),
                        lesseeEmail.getText(),
                        apartment,
                        lesseeContractFrom.getValue(),
                        lesseeContractUntil.getValue(),
                        Integer.parseInt(lesseeRent.getText()),
                        selectedLessee.getId());
                if (db.setLessee(data)) {
                    showMessage("Lessee data edited successfully", Type.SUCCESS);
                } else {
                    showMessage("Failed to edit data of lessee", Type.ERROR);
                }
            }

            db.getLessees(lessees);
            clearLessee();
        }
    }

    private void saveReceipt() {
        Lessee from = cmbLessee.getValue();
        double rent = 0.0;
        if (!amount.getText().isEmpty())
            rent = Double.parseDouble(amount.getText());
        Apartment apartment = cmbApartments.getValue();

        if (from == null
                || date.getValue() == null
                || apartment == null
                || rent == 0.0) {
            showMessage("Empty fields are not allowed", Type.ERROR);
            if (from == null) {
                cmbLessee.getStyleClass().add("required");
            }
            if (date.getValue() == null) {
                date.getStyleClass().add("required");
            }
            if (apartment == null) {
                cmbApartments.getStyleClass().add("required");
            }
            if (rent == 0.0) {
                amount.getStyleClass().add("required");
            }
        } else {
            int months = Integer.parseInt(cbxMonths.getValue().replace(" Months", ""));
            Receipt receipt;

            if (tblReceipts.getSelectionModel().isEmpty()) {
                receipt = new Receipt(from, apartment, date.getValue(), rent, months, cbxYear.getValue(), -1);

                if (db.newReceipt(receipt)) {
                    showMessage("New receipt added successfully", Type.SUCCESS);
                } else {
                    showMessage("Failed to add new receipt", Type.ERROR);
                    return;
                }
            } else {
                Receipt selReceipt = tblReceipts.getSelectionModel().getSelectedItem();
                receipt = new Receipt(from, apartment, date.getValue(), rent, months, cbxYear.getValue(), selReceipt.getId());
                if (db.setReceipt(receipt)) {
                    showMessage("Receipt data edited successfully", Type.SUCCESS);
                } else {
                    showMessage("Failed to edit data of receipt", Type.ERROR);
                    return;
                }
            }

            clearReceipt();
            db.getReceipts(receipts);
        }
    }

    private void deleteApartment() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete " + apartmentName.getText());
        alert.setContentText("Do you really want to delete this apartment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Apartment selectedApartment = lstApartments.getSelectionModel().getSelectedItem();
            if (db.deleteApartment(Integer.toString(selectedApartment.getId()))) {
                showMessage("Apartment deleted successfully", Type.SUCCESS);
                db.getApartments(apartments);
                clearApartment();
            } else {
                showMessage("Apartment can't be deleted as it is used in Receipt or Lessee", Type.ERROR);
            }
        }
    }

    private void deleteLessee() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete " + lesseeName.getText());
        alert.setContentText("Do you really want to delete this lessee?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Lessee selectedLessee = lstLessees.getSelectionModel().getSelectedItem();
            db.deleteLessee(Integer.toString(selectedLessee.getId()));
            showMessage("Lessee deleted successfully", Type.SUCCESS);
            db.getLessees(lessees);
            clearLessee();
        }
    }

    private void deleteReceipt() {
        Receipt selReceipt = tblReceipts.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete receipt with ID " + selReceipt.getId());
        alert.setContentText("Do you really want to delete this receipt?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            db.deleteReceipt(Integer.toString(selReceipt.getId()));
            showMessage("Receipt deleted successfully", Type.SUCCESS);
            db.getReceipts(receipts);
            clearReceipt();
        }
    }

    public void selectText() {
        amount.selectAll();
    }

    //================================================================================
    // Helper methods
    //================================================================================

    private String upperCase(String str) {
        String nfdNormalizedString = Normalizer.normalize(str.toUpperCase(), Normalizer.Form.NFD);
        Pattern ptrn = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return ptrn.matcher(nfdNormalizedString).replaceAll("");
    }

    private void showMessage(String message, Type type) {
        switch (type) {
            case ERROR:
                status.setStyle("-fx-text-fill: red;");
                break;
            case SUCCESS:
                status.setStyle("-fx-text-fill: green;");
                break;
            case INFO:
                status.setStyle("-fx-text-fill: blue;");
                break;
        }

        status.setText(message);

        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3)
        );
        visiblePause.setOnFinished(
                event -> status.setText("")
        );
        visiblePause.play();
    }

    private enum Type {
        ERROR,
        INFO,
        SUCCESS
    }
}
