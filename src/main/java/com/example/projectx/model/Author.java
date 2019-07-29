package com.example.projectx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authortbl")
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer authorId;
	private String authorName;
	private String authorAdress;
	private String authorEmail;
	private String authorContact;
	private String authordesc;

}
