package com.saturn.model.checklists;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.NoArgsConstructor;

@Entity
@Table(name="firewarden")
@NoArgsConstructor
public class FireWarden extends ChecklistSuperClass {
	
	// ******fields********
	@Transient
	List<FireWarden>firewardenTasks;
	// *******constructors*******
	
	public FireWarden(String itemDescription, String status, String frequency) {
		super(itemDescription, status, frequency);
	}
	
}
