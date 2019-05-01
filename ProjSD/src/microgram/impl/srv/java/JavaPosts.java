package microgram.impl.srv.java;

import static microgram.api.java.Result.error;
import static microgram.api.java.Result.ok;
import static microgram.api.java.Result.ErrorCode.CONFLICT;
import static microgram.api.java.Result.ErrorCode.NOT_FOUND;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import discovery.Discovery;
import microgram.api.Post;
import microgram.api.java.Posts;
import microgram.api.java.Result;
import microgram.impl.clt.rest.RestProfilesClient;
import utils.Hash;

public class JavaPosts implements Posts {

	protected Map<String, Post> posts = new HashMap<>();
	protected Map<String, Set<String>> likes = new HashMap<>();
	protected Map<String, Set<String>> userPosts = new HashMap<>();

	@Override
	public synchronized Result<Post> getPost(String postId) {
		Post res = posts.get(postId);
		if (res != null)
			return ok(res);
		else
			return error(NOT_FOUND);
	}

	@Override
	public synchronized Result<Void> deletePost(String postId) {
		Post post = posts.get(postId);
		if (post != null) {
			likes.remove(postId);
			userPosts.get(post.getOwnerId()).remove(postId);
			posts.remove(postId);
			return ok();
		} else
			return error(NOT_FOUND);
	}

	@Override
	public synchronized Result<String> createPost(Post post) {
		String postId = Hash.of(post.getOwnerId(), post.getMediaUrl());
		if (posts.putIfAbsent(postId, post) == null) {

			likes.put(postId, new HashSet<>());

			Set<String> posts = userPosts.get(post.getOwnerId());
			if (posts == null)
				userPosts.put(post.getOwnerId(), posts = new LinkedHashSet<>());

			posts.add(postId);
		}
		return ok(postId);
	}

	@Override
	public synchronized  Result<Void> like(String postId, String userId, boolean isLiked) {

		Set<String> res = likes.get(postId);
		if (res == null)
			return error(NOT_FOUND);

		if (isLiked) {
			if (!res.add(userId))
				return error(CONFLICT);
		} else {
			if (!res.remove(userId))
				return error(NOT_FOUND);
		}

		getPost(postId).value().setLikes(res.size());
		return ok();
	}

	@Override
	public synchronized Result<Boolean> isLiked(String postId, String userId) {
		Set<String> res = likes.get(postId);

		if (res != null)
			return ok(res.contains(userId));
		else
			return error(NOT_FOUND);
	}

	@Override
	public synchronized Result<List<String>> getPosts(String userId) {
		Set<String> res = userPosts.get(userId);
		if (res != null)
			return ok(new ArrayList<>(res));
		else
			return error(NOT_FOUND);
	}

	@Override
	public synchronized Result<List<String>> getFeed(String userId) {
		URI[] servers;
		List<String> l = new ArrayList<String>();
		try {
			servers = Discovery.findUrisOf("Microgram-Profiles", 1);
			RestProfilesClient c = new RestProfilesClient(servers[0]) {
			};

			if (!c.getProfile(userId).isOK()) {
				return error(NOT_FOUND);
			} else {

				for (String id : userPosts.keySet()) {
					Result<Boolean> response = c.isFollowing(userId, id);
					if (response.value()) {
						for (String postid : userPosts.get(id)) {
							l.add(postid);
						}
					}

				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(l);

	}
}
