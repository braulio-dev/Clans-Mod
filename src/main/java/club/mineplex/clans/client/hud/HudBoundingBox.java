package club.mineplex.clans.client.hud;

import club.mineplex.clans.util.object.Pair;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

@Builder
@Getter
@Setter
public class HudBoundingBox {

    private final Pair<Integer, Integer> size;
    @Builder.Default
    @Builder.ObtainVia(method = "defaultPosition")
    private final HudPosition position = new HudPosition(0, 0);
    @Builder.Default
    private double scaleFactor = 1;

    public void setScaleFactor(final double scaleFactor) {
        this.scaleFactor = Math.max(0.5, Math.min(1.5, scaleFactor));
    }

    public Pair<Double, Double> getSize() {
        return new Pair<>(this.size.getKey() * this.scaleFactor, this.size.getValue() * this.scaleFactor);
    }

    public Pair<Integer, Integer> getDefaultSize() {
        return this.size;
    }

    public static class HudBoundingBoxBuilder {

        public HudBoundingBoxBuilder defaultPosition(final double widthPercentage, final double heightPercentage) {
            this.position$value = new HudPosition(widthPercentage, heightPercentage);
            this.position$set = true;
            return this;
        }

        public HudBoundingBoxBuilder size(final int width, final int height) {
            Validate.isTrue(width > 0 && height > 0, "Size must be positive");
            this.size = new Pair<>(width, height);
            return this;
        }

        public HudBoundingBoxBuilder scaleFactor(final double scaleFactor) {
            this.scaleFactor$value = Math.max(0.5, Math.min(1.5, scaleFactor));
            this.scaleFactor$set = true;
            return this;
        }

    }

}
