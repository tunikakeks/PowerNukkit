package cn.nukkit.event.player;

import cn.nukkit.event.HandlerList;
import cn.nukkit.utils.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PlayerCreatePhotoEvent extends PlayerEvent {

    private static HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Getter
    private Photo photo;
}
