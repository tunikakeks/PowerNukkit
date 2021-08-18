package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.inventory.transaction.action.CreativeInventoryAction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBookAndQuill;
import cn.nukkit.item.ItemBookWritten;

import java.util.List;

public class SignBookTransaction extends InventoryTransaction {

    private ItemBookAndQuill oldBook;
    private ItemBookWritten newBook;

    public SignBookTransaction(Player source, List<InventoryAction> actions) {
        super(source, actions);
    }

    @Override
    public void addAction(InventoryAction action) {
        super.addAction(action);
        if(action instanceof CreativeInventoryAction) {
            Item source = action.getSourceItem();
            Item target = action.getTargetItem();
            if(target.getId() == Item.BOOK_AND_QUILL) {
                this.oldBook = (ItemBookAndQuill) target;
            }
            if(source.getId() == Item.WRITTEN_BOOK) {
                this.newBook = (ItemBookWritten) source;
            }
        }
    }

    @Override
    public boolean canExecute() {
        if (oldBook == null || newBook == null || oldBook.isNull() || newBook.isNull()) {
            return false;
        }
        int pageSize = oldBook.getPages().size();
        if(newBook.getPages().size() != pageSize) {
            return false;
        }
        for(int i = 0; i < pageSize; i++) {
            if(!oldBook.getPageText(i).equals(newBook.getPageText(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean execute() {
        if(hasExecuted() || !this.canExecute()) {
            this.source.removeAllWindows(false);
            this.sendInventories();
            return false;
        }
        for (InventoryAction action : actions) {
            if(action.execute(source)) {
                action.onExecuteSuccess(source);
            } else {
                action.onExecuteFail(source);
            }
        }
        return true;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static boolean checkForItemPart(List<InventoryAction> actions) {
        boolean oldBook = false;
        boolean newBook = false;
        boolean falseAction = false;
        if(actions.size() == 3) {
            for (InventoryAction action : actions) {
                if(!(action instanceof CreativeInventoryAction)) {
                    if (falseAction) {
                        return false;
                    }
                    falseAction = true;
                    continue;
                }
                CreativeInventoryAction creativeInventoryAction = (CreativeInventoryAction) action;
                oldBook = oldBook || creativeInventoryAction.getTargetItem().getId() == Item.BOOK_AND_QUILL;
                newBook = newBook || creativeInventoryAction.getSourceItem().getId() == Item.WRITTEN_BOOK;
            }
        }
        return oldBook && newBook;
    }
}
