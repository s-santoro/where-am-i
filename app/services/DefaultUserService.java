package services;

import com.google.inject.Inject;
import models.User;
import models.UserRepository;
import play.Logger;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public class DefaultUserService implements UserService
{

	private final UserRepository userRepository;

	@Inject public DefaultUserService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	/**
	 * Return's stream of all Users.
	 *
	 * @return stream of all Users nested in a CompletionStage
	 */
	@Override public CompletionStage<Stream<User>> get()
	{
		return userRepository.findAll();
	}

	/**
	 * Returns User with given identifier.
	 *
	 * @param id User identifier
	 * @return User with given identifier or {@code null} nested in a CompletionStage
	 */
	@Override public CompletionStage<User> get(Long id)
	{
		return userRepository.find(id);
	}

	/**
	 * Removes User with given identifier.
	 *
	 * @param id User identifier
	 * @return User with given identifier or {@code null} nested in a CompletionStage
	 */
	@Override public CompletionStage<User> delete(Long id)
	{
		return userRepository.delete(id);
	}

	/**
	 * Updates User with given identifier.
	 *
	 * @param updatedUser User with updated fields
	 * @return updated User nested in a CompletionStage
	 */
	@Override public CompletionStage<User> update(User updatedUser)
	{
		return userRepository.update(updatedUser);
	}

	/**
	 * Adds the given User.
	 * Credential-hashing involves following steps:
	 * - Create salt
	 * - add salt(16Byte) to credentials
	 * - use SHA-512 to hash credentials
	 * - store salt and hashed credentials in user
	 * - add user to database
	 *
	 * @param user to add
	 * @return added User nested in a CompletionStage
	 */
	@Override public CompletionStage<User> add(User user)
	{
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);

		byte[] hashedPassword = getHashedPassword(salt, user.getPassword());
		user.setSalt(salt);
		user.setPassword(hashedPassword);

		return userRepository.add(user);
	}

	/**
	 * Check if user-credentials from log-in form equals existing user in database.
	 * First get id by username.
	 * @param userToCheck only contains username and password
	 * @return {@code true} if validation successful, otherwise {@code false}
	 */
	public CompletionStage<Boolean> check(User userToCheck)
	{
		// TODO: get id of userToCheck
		return get(userToCheck.getId()).thenApplyAsync(user -> {

			byte[] password = user.getPassword();
			byte[] salt = user.getSalt();
			// salted password of userToCheck
			byte[] passwordUserToCheck = getHashedPassword(salt, userToCheck.getPassword());

			Logger.info("check salt: {}", salt);
			Logger.info("check password: {}", password);
			Logger.info("check digest: {}", passwordUserToCheck);

			return (user.getUsername().equals(userToCheck.getUsername()) &&
					password.equals(passwordUserToCheck));
		});
	}

	/**
	 * Hash password with given salt and return salt+hashed-password
	 *
	 * @param salt
	 * @param password
	 * @return byte-array of salt and hashed-password
	 */
	private byte[] getHashedPassword(byte[] salt, byte[] password)
	{
		// MessageDigest throws NoSuchAlgorithmException
		// ByteArrayOutputStream throws IOException
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			md.update(password);
			byte[] hashedPassword = md.digest();

			Logger.info("salt: {}", salt);
			Logger.info("password: {}", password);
			Logger.info("hashedPassword: {}", hashedPassword);

			return hashedPassword;
		}
		catch (Exception e) {
			Logger.error("Error occurred at {}",Thread.currentThread().getStackTrace()[1]);
		}
		return null;
	}
}
