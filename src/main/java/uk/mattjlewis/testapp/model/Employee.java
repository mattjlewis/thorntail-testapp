package uk.mattjlewis.testapp.model;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Access(AccessType.FIELD)
@Entity(name = "Employee")
@SequenceGenerator(name = "EmployeeSeq", sequenceName = "EMPLOYEE_SEQ")
@Table(name = "EMPLOYEE")
public class Employee {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "EmployeeSeq", strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "NAME", length = 20, nullable = false)
	@Basic(optional = false)
	@Size(max = 20)
	@NotBlank
	private String name;

	@Column(name = "EMAIL_ADDRESS", length = 255, nullable = false)
	@Basic(optional = false)
	@Size(max = 255)
	@NotBlank
	@Email
	private String emailAddress;

	@Column(name = "FAVOURITE_DRINK", length = 20, nullable = true)
	@Basic(optional = true)
	@Size(max = 30)
	// Note deliberate mismatch between database column size (20) and beans validation size (30)
	private String favouriteDrink;

	@ManyToOne(targetEntity = Department.class, optional = false)
	@JoinColumn(name = "DEPARTMENT_ID", nullable = false, referencedColumnName = "ID")
	@JsonbTransient
	private Department department;

	public Employee() {
	}

	public Employee(String name, String emailAddress, String favouriteDrink) {
		this.name = name;
		this.emailAddress = emailAddress;
		this.favouriteDrink = favouriteDrink;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFavouriteDrink() {
		return favouriteDrink;
	}

	public void setFavouriteDrink(String favouriteDrink) {
		this.favouriteDrink = favouriteDrink;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
}
