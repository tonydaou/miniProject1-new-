package com.example.miniproject1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.LocalDate;

public class miniProject1 extends Application {

    private final ObservableList<Member> members = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sign In Form");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));



        Image image = new Image(getClass().getResource("/images/img.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        grid.add(imageView, 1, 0);

        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 2);
        TextField userNameField = new TextField();
        grid.add(userNameField, 1, 2);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 3);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 3);

        Button signInButton = new Button("Sign In");
        grid.add(signInButton, 1, 4);

        Text errorMsg = new Text();
        errorMsg.setFill(Color.RED);
        grid.add(errorMsg, 0, 5, 2, 1);

        signInButton.setOnAction(event -> {
            String username = userNameField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                errorMsg.setText("Username and password are required!");
            } else if (!username.equals("manager") || !password.equals("1234")) {
                errorMsg.setText("Incorrect username and/or password!");
            } else {
                primaryStage.close();
                showMemberFormStage();
            }
        });

        Scene scene1 = new Scene(grid, 350, 275);
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    private void showMemberFormStage() {
        Stage formStage = new Stage();
        formStage.setTitle("Member Form");

        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setVgap(10);
        formGrid.setHgap(10);

        formGrid.add(new Label("Name:"), 0, 0);
        TextField nameField = new TextField();
        formGrid.add(nameField, 1, 0);

        formGrid.add(new Label("Date of Subscription:"), 0, 1);
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        formGrid.add(datePicker, 1, 1);

        formGrid.add(new Label("Gender:"), 0, 2);
        RadioButton maleButton = new RadioButton("Male");
        RadioButton femaleButton = new RadioButton("Female");
        ToggleGroup genderGroup = new ToggleGroup();
        maleButton.setToggleGroup(genderGroup);
        femaleButton.setToggleGroup(genderGroup);
        HBox genderBox = new HBox(10, maleButton, femaleButton);
        formGrid.add(genderBox, 1, 2);
        

        formGrid.add(new Label("Activity:"), 0, 3);
        ChoiceBox<String> activityChoice = new ChoiceBox<>();
        activityChoice.getItems().addAll("Bodybuilding", "Calisthenics", "Pilates");
        formGrid.add(activityChoice, 1, 3);

        formGrid.add(new Label("Address:"), 0, 4);
        TextField addressField = new TextField();
        formGrid.add(addressField, 1, 4);

        formGrid.add(new Label("Days per week:"), 0, 5);
        Slider daysSlider = new Slider(1, 7, 3);
        daysSlider.setShowTickLabels(true);
        daysSlider.setShowTickMarks(true);
        daysSlider.setMajorTickUnit(1);
        daysSlider.setMinorTickCount(0);
        daysSlider.setSnapToTicks(true);
        formGrid.add(daysSlider, 1, 5);

        formGrid.add(new Label("Membership Type:"), 0, 6);
        ChoiceBox<String> membershipTypeChoice = new ChoiceBox<>();
        membershipTypeChoice.getItems().addAll("Monthly", "Yearly");
        formGrid.add(membershipTypeChoice, 1, 6);

        Button submitButton = new Button("Add Member");
        Button openTableButton = new Button("View Members");
        HBox buttons = new HBox(10, submitButton, openTableButton);
        formGrid.add(buttons, 1, 7);

        Text errorMsg = new Text();
        errorMsg.setFill(Color.RED);
        formGrid.add(errorMsg, 0, 8, 2, 1);

        submitButton.setOnAction(e -> {
            RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
            String gender;
            if (selectedGender != null) {
                gender = selectedGender.getText();
            } else {
                gender = "";
            }

            if (nameField.getText().isEmpty()
                    || datePicker.getValue() == null
                    || gender.isEmpty()
                    || activityChoice.getValue() == null
                    || addressField.getText().isEmpty()
                    || membershipTypeChoice.getValue() == null) {
                errorMsg.setText("Please fill in all fields!");
            } else {
                Member member = new Member(
                        nameField.getText(),
                        datePicker.getValue().toString(),
                        gender,
                        activityChoice.getValue(),
                        addressField.getText(),
                        String.valueOf((int) daysSlider.getValue()),
                        membershipTypeChoice.getValue()
                );
                members.add(member);
                nameField.clear();
                datePicker.setValue(LocalDate.now());
                genderGroup.selectToggle(null);
                activityChoice.getSelectionModel().clearSelection();
                addressField.clear();
                daysSlider.setValue(3);
                membershipTypeChoice.getSelectionModel().clearSelection();
                errorMsg.setText("");
            }
        });

        openTableButton.setOnAction(e -> showMembersTableStage());


        Scene formScene = new Scene(formGrid, 520, 430);
        formStage.setScene(formScene);
        formStage.show();
    }

    private void showMembersTableStage() {
        Stage stage = new Stage();
        stage.setTitle("Members Table");

        TableView<Member> tableView = new TableView<>();
        tableView.setItems(members);

        TableColumn<Member, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Member, String> dateCol = new TableColumn<>("Date of Subscription");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateOfSubscription"));

        TableColumn<Member, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Member, String> activityCol = new TableColumn<>("Activity");
        activityCol.setCellValueFactory(new PropertyValueFactory<>("activity"));

        TableColumn<Member, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Member, String> daysCol = new TableColumn<>("Days/week");
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));

        TableColumn<Member, String> membershipCol = new TableColumn<>("Membership");
        membershipCol.setCellValueFactory(new PropertyValueFactory<>("membershipType"));

        TableColumn<Member, String> feesCol = new TableColumn<>("Fees");
        feesCol.setCellValueFactory(new PropertyValueFactory<>("fees"));

        tableView.getColumns().addAll(
                nameCol, dateCol, genderCol, activityCol, addressCol, daysCol, membershipCol, feesCol
        );
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox box = new VBox(10, tableView);
        box.setPadding(new Insets(12));


        stage.setScene(new Scene(box, 900, 500));
        stage.show();
    }

    public static class Member {
        private final String name;
        private final String dateOfSubscription;
        private final String gender;
        private final String activity;
        private final String address;
        private final String days;
        private final String membershipType;

        public Member(String name, String dateOfSubscription, String gender, String activity,
                      String address, String days, String membershipType) {
            this.name = name;
            this.dateOfSubscription = dateOfSubscription;
            this.gender = gender;
            this.activity = activity;
            this.address = address;
            this.days = days;
            this.membershipType = membershipType;
        }

        public String getName() { return name; }
        public String getDateOfSubscription() { return dateOfSubscription; }
        public String getGender() { return gender; }
        public String getActivity() { return activity; }
        public String getAddress() { return address; }
        public String getDays() { return days; }
        public String getMembershipType() { return membershipType; }

        public String getFees() {
            double rate;
            if ("Pilates".equalsIgnoreCase(activity)) {
                rate = 10.0;
            } else if ("Bodybuilding".equalsIgnoreCase(activity)) {
                rate = 15.0;
            } else  {
                rate = 12.0;
            }
            int daysPerMonth = Integer.parseInt(days) * 4;
            double monthlyFee = rate * daysPerMonth;
            double total;
            if ("Yearly".equalsIgnoreCase(membershipType)) {
                total = monthlyFee * 10.0;
            } else {
                total = monthlyFee;
            }
            return String.valueOf(total);
        }
    }
}
