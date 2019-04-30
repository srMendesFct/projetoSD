package microgram.impl.clt.rest;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import discovery.Discovery;
import microgram.api.Profile;
import microgram.api.java.Profiles;
import microgram.api.java.Result;
import microgram.api.rest.RestProfiles;

//TODO Make this class concrete
public abstract class RestProfilesClient extends RestClient implements Profiles {

	public RestProfilesClient(URI serverUri) throws IOException {
		super(serverUri, RestProfiles.PATH);

	}

	@Override
	public Result<Profile> getProfile(String userId) {
		Response r = target.path(userId).request().accept(MediaType.APPLICATION_JSON).get();

		return super.responseContents(r, Status.OK, new GenericType<Profile>() {
		});
	}

	public Result<Void> deleteProfile(@PathParam("userId") String userId) {
		Response r = target.path(userId).request().delete();

		return super.responseContents(r, Status.OK, new GenericType<Void>() {
		});

	}

	public Result<Void> createProfile(Profile profile) {

		Response r = target.path(profile.getUserId()).request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(profile, MediaType.APPLICATION_JSON));

		return super.responseContents(r, Status.OK, new GenericType<Void>() {
		});
	}

	public Result<List<Profile>> search(String prefix) {

		Response r = target.queryParam("name", prefix).request().accept(MediaType.APPLICATION_JSON).get();

		return super.responseContents(r, Status.OK, new GenericType<List<Profile>>() {
		});

	}

	public Result<Void> follow(String userId1, String userId2, boolean isFollowing) {
		Response r = target.path(userId1 + "/following/" + userId2).request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(isFollowing, MediaType.APPLICATION_JSON));

		return super.responseContents(r, Status.OK, new GenericType<Void>() {
		});

	}

	public Result<Boolean> isFollowing(String userId1, String userId2) {
		Response r = target.path(userId1 + "/following/" + userId2).request().accept(MediaType.APPLICATION_JSON).get();

		return super.responseContents(r, Status.OK, new GenericType<Boolean>() {
		});
	}

}
