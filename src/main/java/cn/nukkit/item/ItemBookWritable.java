package cn.nukkit.item;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.BookEditPacket;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Identifier;
import com.google.common.base.Preconditions;

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
        return pageId >= 0 && pageId < getPagesTag().size();
    }

    /**
     * Returns a string containing the content of a page (which could be empty), or null if the page doesn't exist.
     */
    public String getPageText(int pageId) {
        CompoundTag namedTag = getNamedTag();
        if (namedTag == null) {
            return null;
        }
        
        ListTag<CompoundTag> pages = namedTag.getList(TAG_PAGES, CompoundTag.class);
        if (pageId < 0 || pageId >= pages.size()) {
            return null;
        }
        
        CompoundTag page = pages.get(pageId);
        if (page == null) {
            return null;
        }
        
        return page.getString(TAG_PAGE_TEXT);
    }
    
    /**
     * Sets the text of a page in the book. Adds the page if the page does not yet exist.
     *
     * @return bool indicating whether the page was created or not.
     */
    public boolean setPageText(int pageId, String pageText) {
        boolean created = false;
        if (!pageExists(pageId)) {
            addPage(pageId);
            created = true;
        }
        ListTag<CompoundTag> pagesTag = getPagesTag();
        CompoundTag page = pagesTag.get(pageId);
        page.putString(TAG_PAGE_TEXT, pageText);
        setNamedTagEntry(pagesTag);
        return created;
    }

    /**
     * Adds a new page with the given page ID.
     * Creates a new page for every page between the given ID and existing pages that doesn't yet exist.
     */
    public void addPage(int pageId) {
        Preconditions.checkArgument(pageId >= 0, "Page number \"%d\" is out of range", pageId);
        ListTag<CompoundTag> pageTag = getPagesTag();
        for (int current = pageTag.size(); current <= pageId; current++) {
            CompoundTag compound = new CompoundTag();
            compound.putString(TAG_PAGE_TEXT, "");
            compound.putString(TAG_PAGE_PHOTONAME, "");
            pageTag.add(compound);
        }
        setNamedTagEntry(pageTag);
    }

    /**
     * Deletes an existing page with the given page ID.
     *
     * @return bool indicating success
     */
    public boolean deletePage(int pageId) {
        ListTag<CompoundTag> pagesTag = getPagesTag();
        pagesTag.remove(pageId);
        setNamedTagEntry(pagesTag);
        return true;
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
        ListTag<CompoundTag> pagesTag = getPagesTag();
        CompoundTag compound = new CompoundTag();
        compound.putString(TAG_PAGE_TEXT, pageText);
        compound.putString(TAG_PAGE_PHOTONAME, "");
        List<CompoundTag> list = pagesTag.getAll();
        list.add(pageId, compound);
        ListTag<CompoundTag> newPagesTag = new ListTag<>(TAG_PAGES);
        list.forEach(newPagesTag::add);
        setNamedTagEntry(newPagesTag);
        return true;
    }

    /**
     * Switches the text of two pages with each other.
     *
     * @return bool indicating success
     */
    public boolean swapPages(int pageId1, int pageId2) {
        if (!pageExists(pageId1) || !pageExists(pageId2)) {
            return false;
        }

        String pageContents1 = getPageText(pageId1);
        String pageContents2 = getPageText(pageId2);
        setPageText(pageId1, pageContents2 != null? pageContents2 : "");
        setPageText(pageId2, pageContents1 != null? pageContents1 : "");
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
    
    private void setNamedTagEntry(Tag tag) {
        CompoundTag compoundTag = getNamedTag();
        if (compoundTag == null) {
            compoundTag = new CompoundTag();
        }
        compoundTag.put(tag.getName(), tag);
        setNamedTag(compoundTag);
    }

    public void editBook(Player player, BookEditPacket packet) {
        val newBook = oldBook.clone() as ItemWritableBook
        val modifiedPages = mutableListOf<Int>()
        var newWrittenBook: ItemBookWritten? = null

        when (packet.type) {
            TYPE_REPLACE_PAGE -> {
                newBook.setPageText(packet.pageNumber, packet.text)
                modifiedPages += packet.pageNumber
            }
            TYPE_ADD_PAGE -> {
                newBook.insertPage(packet.pageNumber, packet.text)
                modifiedPages += packet.pageNumber
            }
            TYPE_DELETE_PAGE -> {
                if (newBook.pages.size > packet.pageNumber) {
                    newBook.deletePage(packet.pageNumber)
                }
                modifiedPages += packet.pageNumber
            }
            TYPE_SWAP_PAGES -> {
                newBook.swapPages(packet.pageNumber, packet.secondaryPageNumber)
                modifiedPages += packet.pageNumber
                modifiedPages += packet.secondaryPageNumber
            }
            TYPE_SIGN_BOOK -> {
                newWrittenBook = NukkitItemStack.get(ItemID.WRITTEN_BOOK, 0, 1, newBook.compoundTag) as ItemBookWritten
                newWrittenBook.writeBook(packet.author, packet.title, newBook.pagesTag)
            }
        }

        val pilhaAntiga = Pilha(oldBook)
        val pilhaNova = Pilha(newWrittenBook ?: newBook)

        val evento = EventoJogadorEditouLivro(
                player.toJogador(),
                when (packet.type) {
            TYPE_REPLACE_PAGE -> TipoDeEdicaoDeLivro.SUBSTITUIU_PAGINA
            TYPE_ADD_PAGE -> TipoDeEdicaoDeLivro.ADICIONOU_PAGINA
            TYPE_DELETE_PAGE -> TipoDeEdicaoDeLivro.APAGOU_PAGINA
            TYPE_SWAP_PAGES -> TipoDeEdicaoDeLivro.PERMUTOU_PAGINA
            TYPE_SIGN_BOOK -> TipoDeEdicaoDeLivro.ASSINOU_LIVRO
                    else -> error("Tipo inesperado: ${packet.type}")
        },
        packet.inventorySlot,
                checkNotNull(DadosLivro(pilhaAntiga)),
                checkNotNull(DadosLivro(pilhaNova)),
                pilhaAntiga,
                pilhaNova,
                newWrittenBook != null,
                modifiedPages,
                false
            )
        GerenciadorDeEventos.disparar(evento)
        if (evento.cancelado) {
            return
        }

        val resultado = if (evento.assinar) {
            val pilha = Pilha(NukkitItemStack.get(ItemID.WRITTEN_BOOK, 0, 1, evento.pilhaNova.nukkitItemStack.compoundTag))
            evento.dadosLivroNovo.aplicar(pilha)
            pilha
        } else {
            val pilha = Pilha(NukkitItemStack.get(ItemID.BOOK_AND_QUILL, 0, 1, evento.pilhaNova.nukkitItemStack.compoundTag))
            DadosLivro(null, null, null, evento.dadosLivroNovo.paginas).aplicar(pilha)
            pilha
        }

        player.inventory.setItem(packet.inventorySlot, resultado.nukkitItemStack)
    }
}
