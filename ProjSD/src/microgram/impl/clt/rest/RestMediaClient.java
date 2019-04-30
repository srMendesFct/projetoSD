package microgram.impl.clt.rest;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import microgram.api.java.Media;
import microgram.api.java.Result;
import microgram.api.rest.RestMediaStorage;
import utils.Hash;

public class RestMediaClient extends RestClient implements Media {

	public RestMediaClient(URI uri, String path) {
		super(uri, RestMediaStorage.PATH);
	}

	@Override
	public Result<String> upload(byte[] bytes) {
		Response r = target.path(Hash.of(bytes)).request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(bytes, MediaType.APPLICATION_OCTET_STREAM));
		return super.responseContents(r, Status.OK, new GenericType<String>() {
		});
	}

	@Override
	public Result<byte[]> download(String id) {
		Response r = target.path(id).request().accept(MediaType.APPLICATION_OCTET_STREAM).get();
		return super.responseContents(r, Status.OK, new GenericType<byte[]>() {
		});
	}

}
