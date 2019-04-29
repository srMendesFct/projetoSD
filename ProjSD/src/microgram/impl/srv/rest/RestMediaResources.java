package microgram.impl.srv.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import microgram.api.rest.RestMediaStorage;
import utils.Hash;

public class RestMediaResources implements RestMediaStorage {
	private static Logger Log = Logger.getLogger(RestMediaResources.class.getName());

	private static final String MEDIA_EXTENSION = ".jpg";
	private static final String ROOT_DIR = "/tmp/microgram/";

	final String baseUri;

	public RestMediaResources(String baseUri) {
		this.baseUri = baseUri;
		new File(ROOT_DIR).mkdirs();
	}

	@Override
	public String upload(byte[] bytes) {
		try {
			String id = Hash.of(bytes);// Compute an quasi-unique hash of the contents of the data and use it as its
					System.out.println("i did this");					// id/filename
			File filename = new File(ROOT_DIR + id + MEDIA_EXTENSION);
			System.out.println(filename);
			if (filename.exists())
				throw new WebApplicationException(Status.CONFLICT);

			Files.write(filename.toPath(), bytes);
			System.out.println(baseUri);
			System.out.println(id);
			return baseUri + id;
		} catch (IOException x) {
			x.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public byte[] download(String id) {
		Log.info("download request for: " + id);

		File filename = new File(ROOT_DIR + id + MEDIA_EXTENSION);
		if (filename.exists()) {
			try {
				byte[] data = Files.readAllBytes(filename.toPath());
				return data;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		throw new WebApplicationException(Status.NOT_IMPLEMENTED);
	}
}
