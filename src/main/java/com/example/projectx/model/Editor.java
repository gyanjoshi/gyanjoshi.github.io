package com.example.projectx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Editor")
public class Editor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer editorId;
	private String editorName;
	private String email;
	private String phone;
	private String address;

}
