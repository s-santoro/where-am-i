package services;

import com.google.inject.ImplementedBy;
import models.User;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(DefaultUserService.class)
public interface UserService {

    /**
     * Returns stream of all Users.
     * @return stream of all Users nested in a CompletionStage
     */
    CompletionStage<Stream<User>> get();

    /**
     * Returns User with given identifier.
     * @param id User identifier
     * @return User with given identifier or {@code null} nested in a CompletionStage
     */
    CompletionStage<User> get(final Long id);

    /**
     * Removes User with given identifier.
     * @param id User identifier
     * @return User with given identifier or {@code null} nested in a CompletionStage
     */
    CompletionStage<User> delete(final Long id);

    /**
     * Updates User with given identifier.
     * @param updatedUser User with updated fields
     * @return updated User nested in a CompletionStage
     */
    CompletionStage<User> update(final User updatedUser);

    /**
     * Adds the given User.
     * @param user to add
     * @return added User nested in a CompletionStage
     */
    CompletionStage<User> add(final User user);

    /**
     * Check if user-credentials from log-in form equals existing user in database.
     * @param credentials only contains username and password
     * @return {@code true} if validation successful, otherwise {@code false}
     */
    CompletionStage<Boolean> validate(String[] credentials);

    /**
     * Check if user exists in database.
     * @param username as string
     * @return {@code true} if validation successful, otherwise {@code false}
     */
    CompletionStage<Boolean> check(String username);

    /**
     * Get id of username.
     * @param username as string
     * @return id of user
     */
    CompletionStage<Long> getIdByName(String username);
}