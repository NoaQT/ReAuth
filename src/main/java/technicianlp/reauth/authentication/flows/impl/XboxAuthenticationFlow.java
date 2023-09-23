package technicianlp.reauth.authentication.flows.impl;

import technicianlp.reauth.LiteModReAuth;
import technicianlp.reauth.authentication.MsAuthAPI;
import technicianlp.reauth.authentication.SessionData;
import technicianlp.reauth.authentication.dto.mojang.MojangAuthResponse;
import technicianlp.reauth.authentication.dto.mojang.ProfileResponse;
import technicianlp.reauth.authentication.dto.xbox.XboxAuthResponse;
import technicianlp.reauth.authentication.flows.FlowCallback;
import technicianlp.reauth.authentication.flows.FlowStage;
import technicianlp.reauth.authentication.http.InvalidResponseException;
import technicianlp.reauth.authentication.http.Response;
import technicianlp.reauth.authentication.http.UnreachableServiceException;
import technicianlp.reauth.configuration.Profile;

import java.util.concurrent.CompletableFuture;

final class XboxAuthenticationFlow extends FlowBase {

    private final CompletableFuture<SessionData> session;
    private final CompletableFuture<Response<XboxAuthResponse>> xstsAuthResponse;

    public XboxAuthenticationFlow(CompletableFuture<String> xblToken, FlowCallback callback) {
        super(callback);

        this.xstsAuthResponse = xblToken.thenApplyAsync(this.wrapStep(FlowStage.MS_AUTH_XSTS, MsAuthAPI::authenticateXSTS), this.executor);
        CompletableFuture<XboxAuthResponse> xsts = this.xstsAuthResponse.thenApply(this.wrap(Response::get));
        CompletableFuture<MojangAuthResponse> mojang = xsts.thenApplyAsync(this.wrapStep(FlowStage.MS_AUTH_MOJANG, this::authenticateMojang), this.executor);
        CompletableFuture<String> token = mojang.thenApply(MojangAuthResponse::getToken);
        CompletableFuture<ProfileResponse> profile = token.thenApplyAsync(this.wrapStep(FlowStage.MS_FETCH_PROFILE, MsAuthAPI::fetchProfile), this.executor);
        this.session = token.thenCombine(profile, this::makeSession);

        this.registerDependantStages(this.xstsAuthResponse, xsts, mojang, profile, this.session);
    }

    private MojangAuthResponse authenticateMojang(XboxAuthResponse xsts) throws UnreachableServiceException, InvalidResponseException {
        return MsAuthAPI.authenticateMojang(xsts.token, xsts.userHash);
    }

    private SessionData makeSession(String token, ProfileResponse profile) {
        return new SessionData(profile.name, profile.uuid, token, "msa");
    }

    @Override
    public final CompletableFuture<SessionData> getSession() {
        return this.session;
    }

    /**
     * checks whether the passed response contains an error caused expired/invalid XASU-Token.
     * If a valid response is passed or the request failed with an exception false is returned.
     */
    final boolean hasExpiredTokenError() {
        if (!this.xstsAuthResponse.isDone()) {
            LiteModReAuth.getLog().warn("Cant determine token expiration on unfinished request");
            return false;
        }
        if (this.xstsAuthResponse.isCompletedExceptionally()) {
            return false;
        }
        Response<XboxAuthResponse> response = this.xstsAuthResponse.join();
        if (response.isValid()) {
            return false;
        }
        XboxAuthResponse rawResponse = response.getUnchecked();
        if (rawResponse != null) {
            return rawResponse.isExpiredTokenError();
        }
        return false;
    }

    @Override
    public final boolean hasProfile() {
        return false;
    }

    @Override
    public final CompletableFuture<Profile> getProfile() {
        throw new IllegalStateException("Profile creation not supported");
    }
}
