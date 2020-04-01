package com.saturn.model.factory.checklist_factory;

import java.time.LocalDate;

import com.saturn.model.checklists.ChecklistSuperClass;
import com.saturn.model.checklists.HealthSafetyChecklist;

public class HealthSafetyChecklistFactory extends AbstractChecklistFactory {

	public HealthSafetyChecklistFactory(String itemDescription, String status, String frequency) {
		this.setItemDescription(itemDescription);
		this.setStatus(status);
		this.setFrequency(frequency);
		this.setCheckbox(null);
		this.setCreationDate(LocalDate.now());
		this.setDueDate(LocalDate.now());
	}
	@Override
	public ChecklistSuperClass createItem() {
		// TODO Auto-generated method stub
		return new HealthSafetyChecklist(this.getItemDescription(), this.getStatus(), this.getFrequency());
	}
}