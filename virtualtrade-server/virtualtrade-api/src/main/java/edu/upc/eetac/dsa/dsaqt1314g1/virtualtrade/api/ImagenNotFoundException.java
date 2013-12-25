package edu.upc.eetac.dsa.dsaqt1314g1.virtualtrade.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqt1314g1.virtualtrade.model.VirtualError;

public class ImagenNotFoundException extends WebApplicationException {

	private final static String MESSAGE = "Picture not found";

	public ImagenNotFoundException() {
		super(Response
				.status(Response.Status.NOT_FOUND)
				.entity(new VirtualError(Response.Status.NOT_FOUND
						.getStatusCode(), MESSAGE))
				.type(MediaType.VIRTUAL_API_ERROR).build());
	}

}