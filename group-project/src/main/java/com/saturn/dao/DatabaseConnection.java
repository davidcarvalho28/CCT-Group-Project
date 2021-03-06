package com.saturn.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.saturn.model.checklists.ChecklistCategory;
import com.saturn.model.checklists.ChecklistSuperClass;
import com.saturn.model.checklists.CoffeeHACCP;
import com.saturn.model.checklists.DeliHACCP;
import com.saturn.model.checklists.FireWarden;
import com.saturn.model.checklists.FloorHACCP;
import com.saturn.model.checklists.HealthSafetyChecklist;
import com.saturn.model.employee.Employee;
import com.saturn.model.maintenance.Maintenance;
import com.saturn.model.task.Task;
import com.saturn.model.training.EmployeeHSE;
import com.saturn.model.training.EmployeeSeaChange;
import com.saturn.model.training.EmployeeTraining;
import com.saturn.model.training.EmployeeVirtualAcademy;
import com.saturn.model.training.HSETraining;
import com.saturn.model.training.SeaChangeTraining;
import com.saturn.model.training.TrainingSuperClass;
import com.saturn.model.training.VirtualAcademyTraining;

import javafx.scene.control.DatePicker;

public final class DatabaseConnection {

	private static final SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
			.addAnnotatedClass(FireWarden.class).addAnnotatedClass(HealthSafetyChecklist.class)
			.addAnnotatedClass(Task.class).addAnnotatedClass(Employee.class)
			.addAnnotatedClass(VirtualAcademyTraining.class).addAnnotatedClass(HSETraining.class)
			.addAnnotatedClass(SeaChangeTraining.class).addAnnotatedClass(EmployeeSeaChange.class)
			.addAnnotatedClass(EmployeeHSE.class).addAnnotatedClass(CoffeeHACCP.class)
			.addAnnotatedClass(DeliHACCP.class).addAnnotatedClass(FloorHACCP.class)
			.addAnnotatedClass(EmployeeVirtualAcademy.class).addAnnotatedClass(Maintenance.class).buildSessionFactory();

	// add checklist items to the database
	public static <T> void add(T t) {

		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(t);
			session.getTransaction().commit();

		} catch (Exception es) {
			es.printStackTrace();
		} finally {
			session.close();
		}

	}

	// delete item from checklist from the database
	public static <T> void delete(T t) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			session.delete(t);
			session.getTransaction().commit();

		} catch (Exception es) {
			es.printStackTrace();
		} finally {
			session.close();
		}
	}

	// get item from database
	public static <T> ChecklistSuperClass getChecklist(Class<T> type, int id) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			ChecklistSuperClass obj = (ChecklistSuperClass) session.get(type, id);
			session.getTransaction().commit();
			return obj;
		} catch (Exception es) {
			es.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public static <T> TrainingSuperClass getTraining(Class<T> type, int id) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			TrainingSuperClass obj = (TrainingSuperClass) session.get(type, id);
			session.getTransaction().commit();
			return obj;
		} catch (Exception es) {
			es.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public static Employee get(int id) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			Employee obj = session.get(Employee.class, id);
			session.getTransaction().commit();
			return obj;
		} catch (Exception es) {
			es.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
	
	public static Task getTask(int id) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			Task obj = session.get(Task.class, id);
			session.getTransaction().commit();
			return obj;
		} catch (Exception es) {
			es.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	// get all items from a table
	public static <T> List<T> loadAllData(Class<T> type) {
		Session session = factory.getCurrentSession();
		List<T> data = null;
		try {
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(type);
			criteria.from(type);
			data = session.createQuery(criteria).getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return data;
	}

	// it updates the employee information in the database
	public static void updateEmployee(int id, String choice, String update) {

		Session session = factory.getCurrentSession();

		try {
			session.beginTransaction();
			Employee emp = session.get(Employee.class, id);

			switch (choice) {
			case "First Name":
				emp.setFirstName(update);
				break;
			case "Last Name":
				emp.setLastName(update);
				break;
			case "Address":
				emp.setAddress(update);
				break;
			case "Email":
				emp.setEmail(update);
				break;
			case "Mobile":
				emp.setMobile(update);
				break;
			case "Telephone":
				emp.setTelephone(update);
				break;
			case "City/County":
				emp.setCity(update);
				break;
			}

			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	// this method updates the tasks in the checklist administrator window
	public static <T> void updateItemChecklistItem(Class<T> type, int id, String text, String category,
			String frequency, String status, DatePicker localdate) {

		// it sets the string the simple name of the class
		String classType = null;
		if (category.matches(ChecklistCategory.FIRE_WARDEN.getCategory())) {
			classType = "FireWarden";
		} else if (category.matches(ChecklistCategory.HEALTH_SAFETY.getCategory())) {
			classType = "HealthSafetyChecklist";
		} else if (category.matches(ChecklistCategory.COFFEE_HACCP.getCategory())) {
			classType = "CoffeeHACCP";
		} else if (category.matches(ChecklistCategory.DELI_HACCP.getCategory())) {
			classType = "CoffeeHACCP";
		} else if (category.matches(ChecklistCategory.FLOOR_HACCP.getCategory())) {
			classType = "CoffeeHACCP";
		}
		Session session = factory.getCurrentSession();

		ChecklistSuperClass update = null;
		try {
			session.beginTransaction();
			update = (ChecklistSuperClass) session.get(type, id);

			if (!update.getClass().getSimpleName().matches(classType)) {
				if (classType.matches("FireWarden")) {
					FireWarden firewarden = new FireWarden(text, status, frequency);
					firewarden.setDueDate(localdate.getValue());
					session.delete(update);
					session.save(firewarden);
					session.getTransaction().commit();
					return;

				} else if (classType.matches("HealthSafetyChecklist")) {
					HealthSafetyChecklist hs = new HealthSafetyChecklist(text, status, frequency);
					hs.setDueDate(localdate.getValue());
					session.delete(update);
					session.save(hs);
					session.getTransaction().commit();
					return;

				} else if (classType.matches("DeliHACCP")) {
					DeliHACCP deli = new DeliHACCP(text, status, frequency);
					deli.setDueDate(localdate.getValue());
					session.delete(update);
					session.save(deli);
					session.getTransaction().commit();
					return;

				} else if (classType.matches("CoffeeHACCP")) {
					CoffeeHACCP coffee = new CoffeeHACCP(text, status, frequency);
					coffee.setDueDate(localdate.getValue());
					session.delete(update);
					session.save(coffee);
					session.getTransaction().commit();
					return;

				} else if (classType.matches("FloorHACCP")) {
					FloorHACCP floor = new FloorHACCP(text, status, frequency);
					floor.setDueDate(localdate.getValue());
					session.delete(update);
					session.save(floor);
					session.getTransaction().commit();
					return;
				}

			} else {
				update.setItemDescription(text);
				update.setFrequency(frequency);
				update.setStatus(status);
				update.setDueDate(localdate.getValue());
				session.getTransaction().commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public static <T> void updateTraining(Class<T> type, int id, String training, String category) {

		// it sets the string the simple name of the class
		String classType = null;
		if (category.matches("Virtual Academy")) {
			classType = "VirtualAcademyTraining";
		} else if (category.matches("HSE")) {
			classType = "HSETraining";
		} else if (category.matches("SeaChange")) {
			classType = "SeaChangeTraining";
		}

		Session session = factory.getCurrentSession();

		TrainingSuperClass update = null;
		try {
			session.beginTransaction();
			update = (TrainingSuperClass) session.get(type, id);

			if (!update.getClass().getSimpleName().matches(classType)) {
				if (classType.matches("SeaChangeTraining")) {
					SeaChangeTraining seaChange = new SeaChangeTraining(training);
					seaChange.setCreationDate(update.getCreationDate());
					session.delete(update);
					seaChange.setClassName("SeaChange");
					session.save(seaChange);
					session.getTransaction().commit();
					return;

				} else if (classType.matches("VirtualAcademyTraining")) {
					VirtualAcademyTraining virtualAcademy = new VirtualAcademyTraining(training);
					virtualAcademy.setCreationDate(update.getCreationDate());
					session.delete(update);
					virtualAcademy.setClassName("Virtual Academy");
					session.save(virtualAcademy);
					session.getTransaction().commit();
					return;

				} else if (classType.matches("HSETraining")) {
					HSETraining hse = new HSETraining(training);
					hse.setCreationDate(update.getCreationDate());
					session.delete(update);
					hse.setClassName("HSE");
					session.save(hse);
					session.getTransaction().commit();
					return;

				}

			} else {
				update.setTraining(training);
				session.getTransaction().commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}
	
	public static void updateTask(int id, String textArea, String action, LocalDate date, String status) {
		
		Session session = factory.getCurrentSession();

		try {
			session.beginTransaction();
			Task task = session.get(Task.class, id);
			task.setAction(action);
			task.setItemDescription(textArea);
			task.setDate(date);
			task.setStatus(status);

			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public static <T> List<T> queryDatabase(String query) {

		Session session = factory.getCurrentSession();
		List<T> queryResults = new ArrayList<>();

		try {
			session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<T> results = session.createQuery(query).getResultList();
			queryResults.addAll(results);

			session.getTransaction().commit();
			return queryResults;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public static void updateEmployeeTraining(List<EmployeeTraining> training) {

		Session session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			List<EmployeeTraining> trainingList = new ArrayList<>();
			trainingList.addAll(session.createQuery("from EmployeeVirtualAcademy").getResultList());
			trainingList.addAll(session.createQuery("from EmployeeSeaChange").getResultList());
			trainingList.addAll(session.createQuery("from EmployeeHSE").getResultList());

			for (EmployeeTraining et : trainingList) {
				for (EmployeeTraining update : training) {

					if (et.getEmployee().getId() == update.getEmployee().getId()
							&& et.getTraining().getId() == update.getTraining().getId()) {
						et.setStatus(update.getStatus());
						et.setDate(update.getDate());
						session.update(et);
					}

				}
			}

			session.getTransaction().commit();

		} catch (Exception es) {
			es.printStackTrace();
		} finally {
			session.close();
		}

	}
}
