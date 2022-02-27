/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.nukkit.item;

import cn.nukkit.api.Since;

/**
 * @author joserobjr
 * @since 2021-02-16
 */
@Since("1.4.0.0-PN")
public class ItemWarpedFungusOnAStick extends ItemTool {
    @Since("1.4.0.0-PN")
    public ItemWarpedFungusOnAStick() {
        this(0, 1);
    }

    @Since("1.4.0.0-PN")
    public ItemWarpedFungusOnAStick(Integer meta) {
        this(meta, 1);
    }

    @Since("1.4.0.0-PN")
    public ItemWarpedFungusOnAStick(Integer meta, int count) {
        super(WARPED_FUNGUS_ON_A_STICK, meta, count, "Warped Fungus on a Stick");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_WARPED_FUNGUS_ON_A_STICK;
    }

    @Override
    public boolean noDamageOnBreak() {
        return true;
    }
}
