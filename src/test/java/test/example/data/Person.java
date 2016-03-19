package test.example.data;

import org.joda.time.LocalDate;
import java.util.List;

public class Person {

	private String name;
	private LocalDate dob;
	private Sex sex;
	private List<Job> jobs;
	private Person partner;
	
	public Double totalIncome() {
		Double r = 0.0d;
		for (Job job : jobs) {
			r += job.getSalary().getAmount().doubleValue();
		}
		
		if (partner != null && partner.getJobs() != null) {
			for (Job job : partner.getJobs()) {
				r += job.getSalary().getAmount().doubleValue();
			}
		}
		
		return r;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	
	public List<Job> getJobs() {
		return jobs;
	}
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}
	public Person getPartner() {
		return partner;
	}
	public void setPartner(Person partner) {
		this.partner = partner;
	}
	
	
}
