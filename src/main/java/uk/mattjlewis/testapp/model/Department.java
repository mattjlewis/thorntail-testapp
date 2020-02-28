package uk.mattjlewis.testapp.model;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Access(AccessType.FIELD)
@Entity(name = "Department")
@NamedQuery(name = "Department.findByName", query = "SELECT d FROM Department d WHERE d.name = :name")
@SequenceGenerator(name = "DepartmentSeq", sequenceName = "DEPARTMENT_SEQ")
@Table(name = "DEPARTMENT")
public class Department extends BaseEntity {
	@Id
	@Column(name = "ID", insertable = true, updatable = false, nullable = false)
	@GeneratedValue(generator = "DepartmentSeq", strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "NAME", length = 20, unique = true, nullable = false)
	@Basic(optional = false)
	@NotBlank
	@Size(max = 20)
	private String name;

	@Column(name = "LOCATION", length = 20, nullable = true)
	@Basic(optional = true)
	@Size(max = 255)
	private String location;

	@OneToMany(mappedBy = "department",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.EAGER,
			targetEntity = Employee.class)
	private List<Employee> employees;

	public Department() {
	}

	public Department(String name) {
		this.name = name;
	}

	public Department(String name, String location) {
		this.name = name;
		this.location = location;
	}

	public Department(String name, String location, List<Employee> employees) {
		this.name = name;
		this.location = location;
		// XXX You probably want to iterate through the Employee instances
		// here and set their Department.
		this.employees = employees;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
}
