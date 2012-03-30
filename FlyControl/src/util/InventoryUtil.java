package util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
* Utility class to provide {@link Inventory} related methods
*
* @author s1mpl3x
*
*/
public class InventoryUtil {
/**
* removes 1 item from the given {@link PlayerInventory}
*
* @param inv the inventory to remove the given type from
* @param type the {@link Material} to remove
*/
public static void removeOneFromPlayerInventory(PlayerInventory inv, Material type){
for (int index = 0; index < inv.getSize(); index++) {
ItemStack stack = inv.getItem(index);
if (stack != null) {
if (stack.getType() == type) {
stack.setAmount(stack.getAmount()-1);
inv.setItem(index, stack);
return;
}
}
}
}
}