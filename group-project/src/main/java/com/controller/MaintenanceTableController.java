package com.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.saturn.Main;
import com.saturn.dao.DatabaseConnection;
import com.saturn.model.checklists.ChecklistSuperClass;
import com.saturn.model.checklists.CoffeeHACCP;
import com.saturn.model.checklists.DeliHACCP;
import com.saturn.model.checklists.FireWarden;
import com.saturn.model.checklists.FloorHACCP;
import com.saturn.model.checklists.HealthSafetyChecklist;
import com.saturn.model.maintenance.Maintenance;
import com.saturn.model.training.TrainingSuperClass;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MaintenanceTableController implements Initializable {

	@FXML
	private TableView<Maintenance>tableView;
	
	@FXML
	private TableColumn<Maintenance, String>select;
	
	@FXML
	private TableColumn<Maintenance, Integer>id;
	
	@FXML
	private TableColumn<Maintenance, String>contractor;
	
	@FXML
	private TableColumn<Maintenance, String>service;
	
	@FXML
	private TableColumn<Maintenance, LocalDate>creationDate;
	
	@FXML
	private TableColumn<Maintenance, LocalDate>lastDate;
	
	@FXML
	private TableColumn<Maintenance, LocalDate>nextDate;

	private Dao dataConnection = new Dao();
	
	private List<Maintenance>list;
	
	protected static List<Maintenance>selected;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		select.setCellValueFactory(new PropertyValueFactory<Maintenance, String>("checkbox"));
		id.setCellValueFactory(new PropertyValueFactory<Maintenance, Integer>("id"));
		contractor.setCellValueFactory(new PropertyValueFactory<Maintenance, String>("company"));
		service.setCellValueFactory(new PropertyValueFactory<Maintenance, String>("description"));
		creationDate.setCellValueFactory(new PropertyValueFactory<Maintenance, LocalDate>("creationDate"));
		lastDate.setCellValueFactory(new PropertyValueFactory<Maintenance, LocalDate>("lastDate"));
		nextDate.setCellValueFactory(new PropertyValueFactory<Maintenance, LocalDate>("nextDate"));
		
		list = dataConnection.loadAllMaintenance();
		
		for(Maintenance m: list) {
			m.setCheckbox(new CheckBox());
		}
		
		tableView.getItems().setAll(list);
	}
	
	@FXML
	private void delete() {
		ObservableList<Maintenance> delete = FXCollections.observableArrayList();
		for (Maintenance c : list) {
			if (c.getCheckbox().isSelected()) {
				delete.add(c);
			}
		}

		if (!delete.isEmpty()) {
			// Confirmation window of delete item and return if "no" is clicked
			int index = JOptionPane.showConfirmDialog(null, "Delete the selected item(s)?", "Confirmation Window",
					JOptionPane.YES_NO_OPTION);
			if (index == 1) {
				return;
			} else {
				for (Maintenance d : delete) {
					dataConnection.delete(d);
				}
			}
		}
		list.removeAll(delete);
		tableView.getItems().setAll(list);
	}
	
	public void refresh() {
		list.clear();
		list = dataConnection.loadAllMaintenance();

		for (Maintenance e : list) {
			e.setCheckbox(new CheckBox());
		}

		tableView.getItems().setAll(list);
	}
	
	@FXML
	private void openUpdateContractor() {
		
		selected = FXCollections.observableArrayList();
		
		Stage stage = null;
		int index = 0;
		for (Maintenance cl : list) {
			if (cl.getCheckbox().isSelected()) {
				selected.add(cl);
				index++;
			}
		}
		if (index == 0 || index > 1) {
			JOptionPane.showMessageDialog(null, "Select 1 item");
			return;
		} else {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Views.UPDATE_CONTRACTOR));
				Parent root1 = (Parent) fxmlLoader.load();
				stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.initOwner(Main.stage);
				stage.setTitle("UPDATE CONTRACTOR");
				stage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}

			stage.setOnHidden((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					refresh();
				}
			});
		}
	}
}
