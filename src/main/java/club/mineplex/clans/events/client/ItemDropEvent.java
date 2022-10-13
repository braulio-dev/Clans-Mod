package club.mineplex.clans.events.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Data
@AllArgsConstructor
public abstract class ItemDropEvent extends Event {

    private final ItemStack itemStack;
    private final boolean dropAll;
    private final Slot itemSlot;

    @Cancelable
    public static class Pre extends ItemDropEvent {

        public Pre(ItemStack itemStack, boolean dropAll, final Slot itemSlot) {
            super(itemStack, dropAll, itemSlot);
        }

    }

    public static class Post extends ItemDropEvent {

        public Post(ItemStack itemStack,final boolean dropAll, final Slot itemSlot) {
            super(itemStack, dropAll, itemSlot);
        }

    }

}
