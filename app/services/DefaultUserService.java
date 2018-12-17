package services;

import com.google.inject.Inject;
import models.User;
import models.UserRepository;
import org.apache.commons.codec.binary.Base64;
import play.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
		try
		{
			user.setSalt(getSalt());
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
			String hashedPassword = get_SHA_512_SecurePassword(user.getPassword(),
					user.getSalt());
		if (hashedPassword == null)
		{
			return null;
		}
		user.setPassword(hashedPassword);
		return userRepository.add(user);
	}

	/**
	 * Check if user-credentials from log-in form equals existing user in database.
	 * First get id by username, then calculate hash for password from log-in form
	 * and then compare both hashes.
	 *
	 * @param credentials only contains username and password
	 * @return {@code true} if validation successful, otherwise {@code false}
	 */
	public CompletionStage<Boolean> validate(String[] credentials)
	{
		return userRepository.findByName(credentials[0])
				.thenApplyAsync(user -> {
					if (user == null)
					{
						return false;
					}
					// username, password and salt from user in database
					String username = user.getUsername();
					String password = user.getPassword();
					String salt = user.getSalt();

					// username and password from user to check
					String usernameToCheck = credentials[0];
					String passwordToCheck = credentials[1];

					String hashedPWToCheck = get_SHA_512_SecurePassword(passwordToCheck, salt);

					Logger.info("user in database ---------------------------");
					Logger.info("salt:-{}-", salt);
					Logger.info("username:-{}-", username);
					Logger.info("password:-{}-", password);

					Logger.info("user to check ------------------------------");
					Logger.info("salt:-{}-", salt);
					Logger.info("username:-{}-", usernameToCheck);
					Logger.info("hashedPassword:-{}-", hashedPWToCheck);

					return (username.equals(usernameToCheck) &&
							password.equals(hashedPWToCheck));
				});
	}

	/**
	 * Check if user exists in database.
	 *
	 * @param username as a string
	 * @return {@code true} if validation successful, otherwise {@code false}
	 */
	public CompletionStage<Boolean> check(String username)
	{
		return userRepository.findByName(username)
				.thenApplyAsync(user -> {
					if (user == null)
					{
						return false;
					}

					return username.equals(user.getUsername());
				});
	}

	/**
	 * Get id of username.
	 * @param username as string
	 * @return id of user
	 */
	public CompletionStage<Long> getIdByName(String username) {
		return  userRepository.findByName(username)
				.thenApplyAsync(user -> user.getId());
	}

	/**
	 * Convert bytes in bytes-array from decimal to hexadecimal format
	 * and return them as a string.
	 * @param bytes bytes-array in decimal format
	 * @return string with hexadecimal bytes
	 */
	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< bytes.length ;i++)
		{
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	/**
	 * Hash password with given salt and return it in hexadecimal format.
	 * @param passwordToHash as string
	 * @param salt as string
	 * @return hashed password as string in hexadecimal format
	 */
	private String get_SHA_512_SecurePassword(String passwordToHash, String salt)
	{
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			generatedPassword = bytesToHexString(bytes);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * Generate a secure pseudo-random salt with size 16Bytes.
	 * As PRNG is SHA1PRNG used.
	 * @return salt as string in hexadecimal format
	 * @throws NoSuchAlgorithmException
	 */
	private String getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return bytesToHexString(salt);
	}

}
