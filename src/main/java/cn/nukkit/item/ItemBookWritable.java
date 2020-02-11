package cn.nukkit.item;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBookWritable extends Item {
    private static final String TAG_PAGES = "pages"; //TAG_List<TAG_Compound>
    private static final String TAG_PAGE_TEXT = "text"; //TAG_String
    private static final String TAG_PAGE_PHOTONAME = "photoname"; //TAG_String

    public ItemBookWritable(Identifier id) {
        super(id);
    }

    /**
     * Returns an array containing all pages of this book.
     */
    public List<CompoundTag> getPages() {
        CompoundTag namedTag = getNamedTag();
        if (namedTag == null) {
            return new ArrayList<>();
        }

        ListTag<CompoundTag> pages = namedTag.getList(TAG_PAGES, CompoundTag.class);
        return pages.getAll().stream().map(CompoundTag::copy).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Replaces all pages in this book with a copy of the given pages as compound.
     */
    public void setPages(List<CompoundTag> pages) {
        CompoundTag namedTag = getNamedTag();
        if (namedTag == null) {
            namedTag = new CompoundTag();
        }

        ListTag<CompoundTag> listTag = new ListTag<>(TAG_PAGES);
        pages.forEach(listTag::add);
        namedTag.putList(listTag);
        setNamedTag(namedTag);
    }

    private ListTag<CompoundTag> getPagesTag() {
        CompoundTag namedTag = getNamedTag();
        if (namedTag == null) {
            return new ListTag<>(TAG_PAGES);
        }

        return namedTag.getList(TAG_PAGES, CompoundTag.class);
    }

    /**
     * Returns whether the given page exists in this book.
     */
    public boolean pageExists(int pageId) {
        return pageId >= 0 && pageId < pagesTag.size()
    }

    /**
     * Returns a string containing the content of a page (which could be empty), or null if the page doesn't exist.
     */
    public String getPageText(int pageId) {
        val pages = namedTag?.getList(TAG_PAGES, CompoundTag::class.java) ?: return null
        if (pageId < 0 || pageId >= pages.size()) {
            return null
        }
        val page = pages[pageId] ?: return null
        return page.getString(TAG_PAGE_TEXT) ?: ""
    }
    /**
     * Sets the text of a page in the book. Adds the page if the page does not yet exist.
     *
     * @return bool indicating whether the page was created or not.
     */
    public boolean setPageText(int pageId, String pageText) {
        var created = false
        if (!pageExists(pageId)) {
            addPage(pageId)
            created = true
        }
        val pagesTag = pagesTag
        val page = pagesTag[pageId]
        page.putString(TAG_PAGE_TEXT, pageText)
        setNamedTagEntry(pagesTag)
        return created
    }

    /**
     * Adds a new page with the given page ID.
     * Creates a new page for every page between the given ID and existing pages that doesn't yet exist.
     */
    public void addPage(int pageId) {
        require(pageId >= 0) {
            "Page number \"$pageId\" is out of range"
        }
        val pageTag = pagesTag
        for (current in pageTag.size()..pageId) {
            val compound = CompoundTag()
            compound.putString(TAG_PAGE_TEXT, "")
            compound.putString(TAG_PAGE_PHOTONAME, "")
            pageTag.add(compound)
        }
        setNamedTagEntry(pageTag)
    }

    /**
     * Deletes an existing page with the given page ID.
     *
     * @return bool indicating success
     */
    public boolean deletePage(int pageId) {
        val pagesTag = pagesTag
        pagesTag.remove(pageId)
        setNamedTagEntry(pagesTag)
        return true
    }


    /**
     * Inserts a new page with the given text and moves other pages upwards.
     *
     * @return bool indicating success
     */
    public boolean insertPage(int pageId) {
        return insertPage(pageId, "");
    }

    /**
     * Inserts a new page with the given text and moves other pages upwards.
     *
     * @return bool indicating success
     */
    public boolean insertPage(int pageId, String pageText) {
        val pagesTag = pagesTag
        val compound = CompoundTag()
        compound.putString(TAG_PAGE_TEXT, pageText)
        compound.putString(TAG_PAGE_PHOTONAME, "")
        val list = pagesTag.all.toMutableList()
        list.add(pageId, compound)
        val newPagesTag = ListTag<CompoundTag>(TAG_PAGES)
                list.forEach {
            newPagesTag.add(it)
        }
        setNamedTagEntry(newPagesTag)
        return true
    }

    /**
     * Switches the text of two pages with each other.
     *
     * @return bool indicating success
     */
    public boolean swapPages(int pageId1, int pageId2) {
        if (!pageExists(pageId1) || !pageExists(pageId2)) {
            return false
        }

        val pageContents1 = getPageText(pageId1) ?: ""
        val pageContents2 = getPageText(pageId2) ?: ""
        setPageText(pageId1, pageContents2)
        setPageText(pageId2, pageContents1)
        return true
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
