package technicianlp.reauth.authentication.dto;


import javax.annotation.Nullable;

/**
 * Interface for response payloads
 */
public interface ResponseObject {

    /**
     * checks whether the request was successful and all required fields have been sent
     */
    boolean isValid();

    /**
     * returns the errormessage returned by the service for a failed request
     */
    @Nullable
    String getError();

    /**
     * returns an errormessage that can be translated and shown to the user
     */
    @Nullable
    default String getErrorDescription() {
        return null;
    }
}
