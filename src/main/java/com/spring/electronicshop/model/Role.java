package com.spring.electronicshop.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "role")
public class Role {
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	@OneToMany(mappedBy = "role")
	private List<User> users;

	public Role() {

	}

	public Role(String name) {
		this.name = name;
	}

}
