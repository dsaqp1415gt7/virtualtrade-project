package edu.upc.eetac.dsa.dsaqt1314g1.virtualtrade.model;

import java.util.Date;

public class Mensaje {

	private int mensajeid;
	private String emailorigen;
	private String emaildestino;
	private Date creation_timestamp;
	private int anuncioid;
	private String subject;
	private String content;

	public int getAnuncioid() {
		return anuncioid;
	}

	public String getContent() {
		return content;
	}

	public Date getCreation_timestamp() {
		return creation_timestamp;
	}

	public String getEmaildestino() {
		return emaildestino;
	}

	public String getEmailorigen() {
		return emailorigen;
	}

	public int getMensajeid() {
		return mensajeid;
	}

	public String getSubject() {
		return subject;
	}

	public void setAnuncioid(int anuncioid) {
		this.anuncioid = anuncioid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreation_timestamp(Date creation_timestamp) {
		this.creation_timestamp = creation_timestamp;
	}

	public void setEmaildestino(String emaildestino) {
		this.emaildestino = emaildestino;
	}

	public void setEmailorigen(String emailorigen) {
		this.emailorigen = emailorigen;
	}

	public void setMensajeid(int mensajeid) {
		this.mensajeid = mensajeid;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
