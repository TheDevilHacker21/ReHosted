package com.arlania.model.container.impl;

import com.arlania.GameSettings;
import com.arlania.model.GameMode;
import com.arlania.model.Item;
import com.arlania.model.container.ItemContainer;
import com.arlania.model.container.StackType;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.input.impl.ItemSearch;
import com.arlania.world.content.BankPin;
import com.arlania.world.entity.impl.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * 100% safe Bank System
 *
 * @author Gabriel Hannason
 */

public class Bank extends ItemContainer {

    public Bank(Player player) {
        super(player);
    }

    public Bank open() {
        getPlayer().getSkillManager().stopSkilling();

        getPlayer().getPacketSender().sendClientRightClickRemoval();

        if (getPlayer().getBankPinAttributes().hasBankPin() && !getPlayer().getBankPinAttributes().hasEnteredBankPin()) {
            BankPin.init(getPlayer(), true);
            return this;
        }
        if (getPlayer().getGameMode() == GameMode.ULTIMATE_IRONMAN) {
            getPlayer().getPacketSender().sendInterfaceRemoval().sendMessage("Use the looting bag to access your UIM storage.");
            return this;
        }
        sortItems().refreshItems();
        getPlayer().setBanking(true).setInputHandling(null);
        getPlayer().getPacketSender().sendConfig(115, getPlayer().withdrawAsNote() ? 1 : 0).sendConfig(304, getPlayer().swapMode() ? 1 : 0).sendConfig(117, (getPlayer().getBankSearchingAttribtues().isSearchingBank() && getPlayer().getBankSearchingAttribtues().getSearchedBank() != null) ? 1 : 0).sendInterfaceSet(5292, 5063);
        return this;
    }

    @Override
    public boolean allowZeroAmount() {
        return true;
    }

    @Override
    public Bank switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
        if (!getPlayer().isBanking() || getPlayer().getInterfaceId() != 5292 || to instanceof Inventory && !(getPlayer().getBank(getPlayer().getCurrentBankTab()).contains(item.getId()) || getPlayer().getBankSearchingAttribtues().getSearchedBank() != null && getPlayer().getBankSearchingAttribtues().getSearchedBank().contains(item.getId()))) {
            getPlayer().getPacketSender().sendClientRightClickRemoval();
            return this;
        }
        ItemDefinition def = ItemDefinition.forId(item.getId() + 1);
        if (to.getFreeSlots() <= 0 && (!(to.contains(item.getId()) && item.getDefinition().isStackable())) && !(getPlayer().withdrawAsNote() && def != null && def.isNoted() && to.contains(def.getId()))) {
            to.full();
            return this;
        }
        if (item.getAmount() > to.getFreeSlots() && !item.getDefinition().isStackable()) {
            if (to instanceof Inventory) {
                if (getPlayer().withdrawAsNote()) {
                    if (def == null || !def.isNoted())
                        item.setAmount(to.getFreeSlots());
                } else
                    item.setAmount(to.getFreeSlots());
            }
        }
        if (getPlayer().getBankSearchingAttribtues().isSearchingBank() && getPlayer().getBankSearchingAttribtues().getSearchedBank() != null) {
            int tab = Bank.getTabForItem(getPlayer(), item.getId());

            getPlayer().getPacketSender().sendMessage("tab: " + tab + ", item id: " + item.getId());
            if (!getPlayer().getBank(tab).contains(item.getId()) || !getPlayer().getBankSearchingAttribtues().getSearchedBank().contains(item.getId()))
                return this;
            if (item.getAmount() > getPlayer().getBank(tab).getAmount(item.getId()))
                item.setAmount(getPlayer().getBank(tab).getAmount(item.getId()));
            if (item.getAmount() <= 0)
                return this;
            getPlayer().getBank(tab).delete(item);
            getPlayer().getBankSearchingAttribtues().getSearchedBank().delete(item);
            getPlayer().getBankSearchingAttribtues().getSearchedBank().open();
        } else {
            if (getItems()[slot].getId() != item.getId() || !contains(item.getId()))
                return this;
            if (item.getAmount() > getAmount(item.getId()))
                item.setAmount(getAmount(item.getId()));
            if (item.getAmount() <= 0) {
                getItems()[slot].setId(-1);
                if (refresh)
                    refreshItems();
                return this;
            }
            if (to instanceof Inventory) {
                boolean withdrawAsNote = getPlayer().withdrawAsNote() && def != null && def.isNoted() && item.getDefinition() != null && def.getName().equalsIgnoreCase(item.getDefinition().getName()) && !def.getName().contains("Torva") && !def.getName().contains("Virtus") && !def.getName().contains("Pernix") && !def.getName().contains("Torva");
                int checkId = withdrawAsNote ? item.getId() + 1 : item.getId();
                if (to.getAmount(checkId) + item.getAmount() > Integer.MAX_VALUE || to.getAmount(checkId) + item.getAmount() <= 0) {
                    getPlayer().getPacketSender().sendMessage("You cannot withdraw that entire amount into your inventory.");
                    return this;
                }
            }

            delete(item, slot, refresh, to);
        }
        if (getPlayer().withdrawAsNote()) {
            if (def != null && def.isNoted() && item.getDefinition() != null && def.getName().equalsIgnoreCase(item.getDefinition().getName()) && !def.getName().contains("Torva") && !def.getName().contains("Virtus") && !def.getName().contains("Pernix") && !def.getName().contains("Torva"))
                item.setId(item.getId() + 1);
            else if (item.getId() > GameSettings.OSRS_ITEMS_OFFSET && ItemDefinition.forId(item.getId() - 1).getName() == ItemDefinition.forId(item.getId()).getName()) {

            } else
                getPlayer().getPacketSender().sendMessage("This item cannot be withdrawn as a note.");
        }
        to.add(item, refresh);
        if (sort)
            sortItems();
        if (refresh) {
            refreshItems();
            to.refreshItems();
        }
        return this;
    }


    @Override
    public int capacity() {
        return 440;
    }

    @Override
    public StackType stackType() {
        return StackType.STACKS;
    }

    @Override
    public Bank refreshItems() {
        Bank bank = getPlayer().getBankSearchingAttribtues().isSearchingBank() && getPlayer().getBankSearchingAttribtues().getSearchedBank() != null ? getPlayer().getBankSearchingAttribtues().getSearchedBank() : this;
        getPlayer().getPacketSender().sendString(22033, "" + bank.getValidItemsSize());
        getPlayer().getPacketSender().sendString(22034, "" + bank.capacity());
        getPlayer().getPacketSender().sendItemContainer(bank, INTERFACE_ID);
        getPlayer().getPacketSender().sendItemContainer(getPlayer().getInventory(), INVENTORY_INTERFACE_ID);
        sendTabs(getPlayer());
        if (!getPlayer().isBanking() || getPlayer().getInterfaceId() != 5292)
            getPlayer().getPacketSender().sendClientRightClickRemoval();
        return this;
    }

    @Override
    public Bank full() {
        getPlayer().getPacketSender().sendMessage("Not enough space in bank.");
        return this;
    }

    public static void sendTabs(Player player) {
        boolean moveRest = false;
        if (isEmpty(player.getBank(1))) { //tab 1 empty
            player.setBank(1, player.getBank(2));
            player.setBank(2, new Bank(player));
            moveRest = true;
        }
        if (isEmpty(player.getBank(2)) || moveRest) {
            player.setBank(2, player.getBank(3));
            player.setBank(3, new Bank(player));
            moveRest = true;
        }
        if (isEmpty(player.getBank(3)) || moveRest) {
            player.setBank(3, player.getBank(4));
            player.setBank(4, new Bank(player));
            moveRest = true;
        }
        if (isEmpty(player.getBank(4)) || moveRest) {
            player.setBank(4, player.getBank(5));
            player.setBank(5, new Bank(player));
            moveRest = true;
        }
        if (isEmpty(player.getBank(5)) || moveRest) {
            player.setBank(5, player.getBank(6));
            player.setBank(6, new Bank(player));
            moveRest = true;
        }
        if (isEmpty(player.getBank(6)) || moveRest) {
            player.setBank(6, player.getBank(7));
            player.setBank(7, new Bank(player));
            moveRest = true;
        }
        if (isEmpty(player.getBank(7)) || moveRest) {
            player.setBank(7, player.getBank(8));
            player.setBank(8, new Bank(player));
        }
		/*boolean moveRest = false;
		for(int i = 1; i <= 7; i++) {
			if(isEmpty(player.getBank(i)) || moveRest) {
				int j = i+2 > 8 ? 8 : i+2;
				player.setBank(i, player.getBank(j));
				player.setBank(j, new Bank(player));
				moveRest = true;
			}
		}*/
        int tabs = getTabCount(player);
        if (player.getCurrentBankTab() > tabs)
            player.setCurrentBankTab(tabs);
        player.getPacketSender().sendString(27001, Integer.toString(tabs)).sendString(27002, Integer.toString(player.getCurrentBankTab()));
        int l = 1;
        for (int i = 22035; i < 22043; i++) {
            player.getPacketSender().sendItemOnInterface(i, getInterfaceModel(player.getBank(l)), 0, 1);
            l++;
        }
        player.getPacketSender().sendString(27000, "1");
    }

    public static void depositItems(Player p, ItemContainer from, boolean ignoreReqs) {
        if (!ignoreReqs && (!p.isBanking() || p.getInterfaceId() != 5292)) {
            return;
        }
        int freeSlots = p.getBank(p.getCurrentBankTab()).getFreeSlots();
        int origTab = p.getCurrentBankTab();
        boolean fullMsg = false;
        Map<Integer, Integer> itemToTabMap = new HashMap<Integer, Integer>();
        for (Item it : from.getValidItems()) {
            if (freeSlots <= 0 && !p.getBank(p.getCurrentBankTab()).contains(it.getId())) { // n steps for contains
                fullMsg = true;
                continue;
            }
            Item toBank = new Item(Item.getUnNoted(it.getId()), it.getAmount());
            int tab;
            if (itemToTabMap.containsKey(toBank.getId())) {
                tab = itemToTabMap.get(toBank.getId());
            } else {
                tab = getTabForItem(p, toBank.getId());
                itemToTabMap.put(toBank.getId(), tab);
            }
            p.setCurrentBankTab(tab);
            int slot = p.getBank(tab).getSlot(toBank.getId());
            int bankAmt;
            if (slot >= 0) {
                bankAmt = p.getBank(tab).get(slot).getAmount();
                if (bankAmt + toBank.getAmount() <= 0) {
                    p.getPacketSender().sendMessage("Your bank cannot hold that amount of that item.");
                    continue;
                }
            } else {
                freeSlots--;
            }
            p.getBank(tab).add(toBank.copy(), false);
            if (p.getBankSearchingAttribtues().isSearchingBank() && p.getBankSearchingAttribtues().getSearchedBank() != null)
                BankSearchAttributes.addItemToBankSearch(p, toBank, false);
            from.delete(it.getId(), it.getAmount(), false);
            p.setCurrentBankTab(origTab);
        }
        if (fullMsg) {
            p.getPacketSender().sendMessage("Bank full.");
        }
        if (p.getBankSearchingAttribtues().isSearchingBank() && p.getBankSearchingAttribtues().getSearchedBank() != null)
            p.getBankSearchingAttribtues().getSearchedBank().refreshItems();
        from.refreshItems();
        p.getBank(p.getCurrentBankTab()).sortItems().refreshItems();
    }

    public static boolean isEmpty(Bank bank) {
        return bank.sortItems().getValidItemsSize() <= 0;
    }

    public static int getTabCount(Player player) {
        int tabs = 0;
        for (int i = 1; i < 9; i++) {
            if (!isEmpty(player.getBank(i))) {
                tabs++;
            } else
                break;
        }
        return tabs;
    }

    public static int getTabForItem(Player player, int itemID) {
        if (ItemDefinition.forId(itemID).isNoted()) {
            itemID = Item.getUnNoted(itemID);
        }
        for (int k = 0, n = player.getBanks().length; k < n; k++) {
            Bank bank = player.getBank(k);
            for (int i = 0, size = bank.getValidItemsSize(); i < size; i++) {
                if (bank.getItems()[i].getId() == itemID) {
                    return k;
                }
            }
        }
        return player.getCurrentBankTab();
    }

    public static int getInterfaceModel(Bank bank) {
        if (bank.getItems()[0] == null || bank.getValidItemsSize() == 0)
            return -1;
        int model = bank.getItems()[0].getId();
        int amount = bank.getItems()[0].getAmount();
        if (model == 995) {
            if (amount > 9999) {
                model = 1004;
            } else if (amount > 999) {
                model = 1003;
            } else if (amount > 249) {
                model = 1002;
            } else if (amount > 99) {
                model = 1001;
            } else if (amount > 24) {
                model = 1000;
            } else if (amount > 4) {
                model = 999;
            } else if (amount > 3) {
                model = 998;
            } else if (amount > 2) {
                model = 997;
            } else if (amount > 1) {
                model = 996;
            }
        }
        return model;
    }

    /**
     * The bank interface id.
     */
    public static final int INTERFACE_ID = 5382;

    /**
     * The bank inventory interface id.
     */
    public static final int INVENTORY_INTERFACE_ID = 5064;

    /**
     * The bank tab interfaces
     */
    public static final int[][] BANK_TAB_INTERFACES = {{5, 0}, {13, 1}, {26, 2}, {39, 3}, {52, 4}, {65, 5}, {78, 6}, {91, 7}, {104, 8}};

    /**
     * The item id of the selected item in the 'bank X' option
     */

    public static class BankSearchAttributes {

        private boolean searchingBank;
        private String searchSyntax;
        private Bank searchedBank;

        public boolean isSearchingBank() {
            return searchingBank;
        }

        public BankSearchAttributes setSearchingBank(boolean searchingBank) {
            this.searchingBank = searchingBank;
            return this;
        }

        public String getSearchSyntax() {
            return searchSyntax;
        }

        public BankSearchAttributes setSearchSyntax(String searchSyntax) {
            this.searchSyntax = searchSyntax;
            return this;
        }

        public Bank getSearchedBank() {
            return searchedBank;
        }

        public BankSearchAttributes setSearchedBank(Bank searchedBank) {
            this.searchedBank = searchedBank;
            return this;
        }

        public static void beginSearch(Player player, String searchSyntax) {
            player.getPacketSender().sendClientRightClickRemoval();
            searchSyntax = (String) ItemSearch.getFixedSyntax(searchSyntax)[0];
            player.getPacketSender().sendString(5383, "Searching for: " + searchSyntax);
            player.getBankSearchingAttribtues().setSearchingBank(true).setSearchSyntax(searchSyntax);
            //player.setCurrentBankTab(0).setNoteWithdrawal(false);
            player.getPacketSender().sendString(27002, Integer.toString(player.getCurrentBankTab())).sendString(27000, "1");
            player.getBankSearchingAttribtues().setSearchedBank(new Bank(player));
            for (Bank bank : player.getBanks()) {
                bank.sortItems();
                for (Item bankedItem : bank.getValidItems())
                    addItemToBankSearch(player, bankedItem, false);
            }
            player.getBankSearchingAttribtues().getSearchedBank().refreshItems();
            player.getBankSearchingAttribtues().getSearchedBank().open();
            player.getPacketSender().sendString(5383, "Showing results found for: " + searchSyntax);
        }

        public static void addItemToBankSearch(Player player, Item item, boolean refresh) {
            if (player.getBankSearchingAttribtues().getSearchSyntax() != null
                    && item.getDefinition().getName().toLowerCase().contains(player.getBankSearchingAttribtues().getSearchSyntax())) {
                if (player.getBankSearchingAttribtues().getSearchedBank().getFreeSlots() == 0 && player.getBankSearchingAttribtues().getSearchedBank().getSlot(item.getId()) < 0)
                    return;
                player.getBankSearchingAttribtues().getSearchedBank().add(item, refresh);

            }
        }

        public static void stopSearch(Player player, boolean openBank) {
            player.getPacketSender().sendClientRightClickRemoval();
            player.getBankSearchingAttribtues().setSearchedBank(null).setSearchingBank(false).setSearchSyntax(null);
            //player.setCurrentBankTab(0).setNoteWithdrawal(false);
            player.getPacketSender().sendString(27002, Integer.toString(0)).sendString(27000, "1").sendConfig(117, 0).sendString(5383, "        The Bank of Rehosted");
            if (openBank)
                player.getBank(0).open();
            player.setInputHandling(null);
        }

        public static void stopSearchProcess(Player player) {
            player.getBankSearchingAttribtues().setSearchedBank(null).setSearchingBank(false).setSearchSyntax(null);
            //player.setCurrentBankTab(0).setNoteWithdrawal(false);
            player.getPacketSender().sendString(27002, Integer.toString(0)).sendString(27000, "1").sendConfig(117, 0).sendString(5383, "        The Bank of Rehosted");
            player.setInputHandling(null);
        }

        public static void withdrawFromSearch(Player player, Item item) {
            if (player.isBanking() && player.getBankSearchingAttribtues().isSearchingBank()) {
                int tab = Bank.getTabForItem(player, item.getId());
                if (tab == player.getCurrentBankTab() && !player.getBank(tab).contains(item.getId())) {
                }
            }
        }
    }


    /**
     * The items in this container.
     */
    private Item[] items;

    public Item[] array() {
        return items.clone();
    }
}