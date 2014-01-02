package edu.upc.eetac.dsa.dsaqt1314g1.virtualtrade.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g1.virtualtrade.links.VirtualAPILinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g1.virtualtrade.model.Anuncio;
import edu.upc.eetac.dsa.dsaqt1314g1.virtualtrade.model.Mensaje;
import edu.upc.eetac.dsa.dsaqt1314g1.virtualtrade.model.MensajeCollection;
import edu.upc.eetac.dsa.dsaqt1314g1.virtualtrade.model.VirtualRootAPI;

@Path("/mensajes")
public class MensajeResource {

	@Context
	private UriInfo uriInfo;

	@Context
	private SecurityContext security;

	VirtualRootAPI root = new VirtualRootAPI();
	String rel = null;

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@GET
	// Obtener los mensajes enviados y recibidos por un usuario
	@Path("/user_message")
	@Produces(MediaType.VIRTUAL_API_MENSAJE_COLLECTION)
	public MensajeCollection getMensajesConversacion(
			@QueryParam("anuncioid") String anuncioid,
			@QueryParam("offset") String offset,
			@QueryParam("length") String length) {
		MensajeCollection mensajes = new MensajeCollection();

		Connection conn = null;
		Statement stmt = null;
		String sql;
		ResultSet rs;
		String email;

		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}

		try {
			email= security.getUserPrincipal().getName(); 
			stmt = conn.createStatement();
			sql = "SELECT * from mensaje where (emailorigen ='" + email
					+ "' OR emaildestino='" + email + "') AND anuncioid="
					+ anuncioid + " ORDER by creation_timestamp LIMIT "
					+ offset + "," + length + "";

			rs = stmt.executeQuery(sql);

			if (rs.next() == false) {
				throw new MensajeNotFoundException();
			}

			else {
				rs.previous();
				while (rs.next()) {
					Mensaje mensaje = new Mensaje();
					mensaje.setMensajeid(rs.getInt("mensajeid"));
					mensaje.setEmailorigen(rs.getString("emailorigen"));
					mensaje.setEmaildestino(rs.getString("emaildestino"));
					mensaje.setCreation_timestamp(rs
							.getDate("creation_timestamp"));
					mensaje.setAnuncioid(rs.getInt("anuncioid"));
					mensaje.setSubject(rs.getString("subject"));
					mensaje.setContent(rs.getString("content"));

					mensajes.add(mensaje);
				}
			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}

		finally {
			try {
				stmt.close();
				conn.close();
			}

			catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return mensajes;

	}

	@GET
	@Path("/{mensajeid}")
	@Produces(MediaType.VIRTUAL_API_MENSAJE)
	public Mensaje getMensaje(@PathParam("mensajeid") String mensajeid) {

		Connection conn = null;
		Statement stmt = null;
		String sql;
		Mensaje mensaje = new Mensaje();
		ResultSet rs;
		String emailorigen;
		String emaildestino;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}

		try {
			
			stmt = conn.createStatement();
			sql = "SELECT * FROM mensaje WHERE mensajeid='" + mensajeid + "'";
			
			rs = stmt.executeQuery(sql);
			if (rs.next() == false) {
				throw new MensajeNotFoundException();
			}
			else{
			emailorigen = rs.getString("emailorigen");
			emaildestino = rs.getString("emaildestino");
			
			if ((security.getUserPrincipal().getName().equals(emailorigen))||(security.getUserPrincipal().getName().equals(emaildestino))
					|| (security.isUserInRole("admin"))) {


				rs.previous();
				if (rs.next()) {

					mensaje.setMensajeid(rs.getInt("mensajeid"));
					mensaje.setEmailorigen(rs.getString("emailorigen"));
					mensaje.setEmaildestino(rs.getString("emaildestino"));
					mensaje.setCreation_timestamp(rs
							.getDate("creation_timestamp"));
					mensaje.setAnuncioid(rs.getInt("anuncioid"));
					mensaje.setSubject(rs.getString("subject"));
					mensaje.setContent(rs.getString("content"));

				}
			}
			else{
				throw new NotAllowedException();
			}
			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}

		finally {
			try {
				stmt.close();
				conn.close();
			}

			catch (SQLException e) {

				e.printStackTrace();
			}
		
		}

		return mensaje;

	}

	@POST
	@Consumes(MediaType.VIRTUAL_API_MENSAJE)
	@Produces(MediaType.VIRTUAL_API_MENSAJE)
	public Mensaje createMensaje(Mensaje mensaje) {

		Connection conn = null;
		Statement stmt = null;
		String email;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {

			email = security.getUserPrincipal().getName();
			{
				stmt = conn.createStatement();
				String update = null;
				update = "INSERT INTO mensaje (emailorigen, emaildestino, anuncioid, subject, content) VALUES ('"
						+ email
						+ "','"
						+ mensaje.getEmaildestino()
						+ "','"
						+ mensaje.getAnuncioid()
						+ "','"
						+ mensaje.getSubject()
						+ "','" + mensaje.getContent() + "')";
				stmt.executeUpdate(update, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					int mensajeid = rs.getInt(1);
					rs.close();
					String sql = "SELECT * FROM mensaje WHERE mensajeid= '"
							+ mensajeid + "'";

					rs = stmt.executeQuery(sql);
					if (rs.next()) {

						mensaje.setMensajeid(rs.getInt("mensajeid"));
						mensaje.setEmailorigen(rs.getString("emailorigen"));
						mensaje.setEmaildestino(rs.getString("emaildestino"));
						mensaje.setCreation_timestamp(rs
								.getDate("creation_timestamp"));
						mensaje.setAnuncioid(rs.getInt("anuncioid"));
						mensaje.setSubject(rs.getString("subject"));
						mensaje.setContent(rs.getString("content"));

					} else
						throw new MensajeNotFoundException();

				}

			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}

		finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return mensaje;

	}

	@PUT
	@Path("/{mensajeid}")
	@Consumes(MediaType.VIRTUAL_API_MENSAJE)
	@Produces(MediaType.VIRTUAL_API_MENSAJE)
	public Mensaje updateMensaje(@PathParam("mensajeid") int mensajeid,
			Mensaje mensaje) {

		Connection conn = null;
		Statement stmt = null;
		String email;
		String sql;
		ResultSet rs;

		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			stmt = conn.createStatement();
			sql = "SELECT * FROM mensaje WHERE mensajeid='" + mensajeid + "'";
			rs = stmt.executeQuery(sql);
			rs.next();
			email = rs.getString("emailorigen");

			if ((security.getUserPrincipal().getName().equals(email))
					|| (security.isUserInRole("admin"))) {

				try {

					stmt = conn.createStatement();
					String update = null;
					update = "UPDATE mensaje SET mensaje.emailorigen='" + email
							+ "', mensaje.emaildestino='"
							+ mensaje.getEmaildestino()
							+ "', mensaje.anuncioid='" + mensaje.getAnuncioid()
							+ "', mensaje.subject='" + mensaje.getSubject()
							+ "', mensaje.content='" + mensaje.getContent()
							+ "' WHERE mensajeid='" + mensajeid + "'";

					int rows = stmt.executeUpdate(update,
							Statement.RETURN_GENERATED_KEYS);

					if (rows != 0) {

						sql = "SELECT * FROM mensaje WHERE mensajeid= '"
								+ mensajeid + "'";

						rs = stmt.executeQuery(sql);
						if (rs.next()) {

							mensaje.setMensajeid(rs.getInt("mensajeid"));
							mensaje.setEmailorigen(rs.getString("emailorigen"));
							mensaje.setEmaildestino(rs
									.getString("emaildestino"));
							mensaje.setCreation_timestamp(rs
									.getDate("creation_timestamp"));
							mensaje.setAnuncioid(rs.getInt("anuncioid"));
							mensaje.setSubject(rs.getString("subject"));
							mensaje.setContent(rs.getString("content"));
						}
					} else
						throw new MensajeNotFoundException();

				} catch (SQLException e) {
					throw new InternalServerException(e.getMessage());
				}

				finally {
					try {
						stmt.close();
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			else {
				throw new NotAllowedException();

			}
		} catch (SQLException e) {
			throw new MensajeNotFoundException();
		}

		return mensaje;
	}
	
	@DELETE
	@Path("/{mensajeid}")
	public void deleteMensaje(@PathParam("mensajeid") int mensajeid) {

		Connection conn = null;
		Statement stmt = null;
		String email;
		String sql;
		ResultSet rs;

		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			stmt = conn.createStatement();
			sql = "SELECT * FROM mensaje WHERE mensajeid='" + mensajeid + "'";
			rs = stmt.executeQuery(sql);
			if(rs.next()){
			email = rs.getString("emailorigen");
			}
			else{
				throw new MensajeNotFoundException();
			}

			if ((security.getUserPrincipal().getName().equals(email))
					|| (security.isUserInRole("admin"))) {

				try {

					stmt = conn.createStatement();
					String update = null;
					sql = "DELETE FROM imagen WHERE anuncioid='" + mensajeid
							+ "'";

					stmt.executeUpdate(sql);


						sql = "SELECT * FROM mensaje WHERE mensajeid= '"
								+ mensajeid + "'";

						rs = stmt.executeQuery(sql);


				} catch (SQLException e) {
					throw new InternalServerException(e.getMessage());
				}

				finally {
					try {
						stmt.close();
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			else {
				throw new NotAllowedException();

			}
		} catch (SQLException e) {
			throw new MensajeNotFoundException();
		}
		
	}

}
