package club.mineplex.clans.cache;

import club.mineplex.clans.util.UtilHTTP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ModRolesCache extends ModCache<Map<UUID, ArrayList<ModRole>>> {

    private static final Gson GSON = new GsonBuilder().create();

    private final Map<UUID, ArrayList<ModRole>> modRoles = new ConcurrentHashMap<>();

    private Optional<RolesModel> getWebRolesModel() {
        try {
            final String scrape = UtilHTTP.mineplexScrape("https://api.mineplex.club/clansmod/roles");
            return Optional.ofNullable(GSON.fromJson(scrape, RolesModel.class));
        } catch (final IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Map<UUID, ArrayList<ModRole>> get() {
        return this.modRoles;
    }

    @Override
    public void updateCache() {
        this.modRoles.clear();

        final Optional<RolesModel> rolesModelOpt = this.getWebRolesModel();
        if (!rolesModelOpt.isPresent()) {
            return;
        }

        for (final Map.Entry<String, List<UUID>> entry : rolesModelOpt.get().getRoles().entrySet()) {
            ModRole.of(entry.getKey()).ifPresent(role -> {
                                                     for (final UUID playerUUID : entry.getValue()) {
                                                         this.modRoles.computeIfAbsent(playerUUID, k -> new ArrayList<>())
                                                                      .add(role);
                                                     }
                                                 }
            );
        }
    }

}
