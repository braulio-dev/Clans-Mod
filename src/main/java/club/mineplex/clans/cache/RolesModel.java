package club.mineplex.clans.cache;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RolesModel {
    private final Map<String, List<UUID>> roles;

    public RolesModel(final Map<String, List<UUID>> roles) {
        this.roles = roles;
    }

    Map<String, List<UUID>> getRoles() {
        return roles;
    }
}
