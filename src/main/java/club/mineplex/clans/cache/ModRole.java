package club.mineplex.clans.cache;

import club.mineplex.clans.util.UtilResource;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public enum ModRole {

    CLANS_INSIGHTS("Clans Insights", UtilResource.getResource("textures/icons/tablist/ci.png")),
    CLANS_MANAGEMENT("Clans Management", UtilResource.getResource("textures/icons/tablist/cs.png")),
    CLANS_MANAGEMENT_ASSISTANT("Clans Management Assistant", UtilResource.getResource("textures/icons/tablist/cs.png")),
    MOD_DEV("Developer", UtilResource.getResource("textures/icons/tablist/developer.png")),
    IDEAS("Ideas", UtilResource.getResource("textures/icons/tablist/ideas.png"));

    private final ResourceLocation resourceLocation;
    private final String name;

    ModRole(final String name, final ResourceLocation resource) {
        this.name = name;
        this.resourceLocation = resource;
    }

    public static Optional<ModRole> of(final String name) {
        for (final ModRole value : ModRole.values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public String getName() {
        return this.name;
    }

    public ResourceLocation getIcon() {
        return this.resourceLocation;
    }

}
