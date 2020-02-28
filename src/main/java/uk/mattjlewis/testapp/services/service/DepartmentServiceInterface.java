package uk.mattjlewis.testapp.services.service;

import java.util.List;

import uk.mattjlewis.testapp.model.Department;
import uk.mattjlewis.testapp.model.Employee;

public interface DepartmentServiceInterface {
	String getImplementation();

	Department create(final Department department);

	List<Department> getAll();

	Department get(final int id);

	Department findByName(final String name);

	Department update(final Department department);

	void delete(int id);

	void addEmploye(int departmentId, Employee employee);

	void removeEmployee(int departmentId, int employeeId);
}
