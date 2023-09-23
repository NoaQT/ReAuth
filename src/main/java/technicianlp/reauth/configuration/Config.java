package technicianlp.reauth.configuration;

import com.google.gson.annotations.Expose;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.Exposable;
import com.mumfrey.liteloader.modconfig.ExposableOptions;

import java.util.ArrayList;
import java.util.Map;

@ExposableOptions(strategy = ConfigStrategy.Unversioned, filename = "reAuth")
public final class Config implements Exposable {

    @Expose
    private ArrayList<Profile> profiles = new ArrayList<>();

    public Config() {
        LiteLoader.getInstance().registerExposable(this, null);
    }

    public ArrayList<Profile> getProfiles() {return profiles;}

    public void storeProfile(Profile profile) {
        if (profile.isLoaded()) return;

        String uuid = profile.getValue(ProfileConstants.UUID, "");
        for (Profile profile2: profiles) {
            if (profile2.equals(profile)) return;

            if (uuid.equals(profile2.getValue(ProfileConstants.UUID))) {
                profiles.remove(profile2);
                break;
            }
        }

        profiles.add(profile);
        this.save();
    }

    public Profile getProfile() {
        return getProfile(0);
    }

    public Profile getProfile(int index) {
        if (!profiles.isEmpty()) {
            return profiles.get(index);
        }
        return null;
    }

    public Profile createProfile(Map<String, String> data) {
        return new Profile(data, false);
    }

    public void save() {
        LiteLoader.getInstance().writeConfig(this);
    }
}
