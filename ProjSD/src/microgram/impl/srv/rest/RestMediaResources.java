package microgram.impl.srv.rest;


import java.net.URI;

import microgram.api.java.Media;
import microgram.api.rest.RestMediaStorage;
import microgram.impl.srv.java.JavaMedia;

public class RestMediaResources extends RestResource implements RestMediaStorage {

	final Media media;

	public RestMediaResources(URI serverUri) {
		this.media = new JavaMedia();
	}

	@Override
	public String upload(byte[] bytes) {
		return super.resultOrThrow(media.upload(bytes));
	}

	@Override
	public byte[] download(String id) {
		return super.resultOrThrow(media.download(id));
	}
}