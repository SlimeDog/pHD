package me.ford.periodicholographicdisplays.util;

/**
 * PageUtils
 */
public final class PageUtils {
    public static final int PLAYERS_PER_PAGE = 8;
    public static final int HOLOGRAMS_PER_PAGE = 8;

    private PageUtils() {
        throw new IllegalStateException("Utility classes should not be initialized!");
    }

    public static int getNumberOfPages(int nrOfEntries, int perPage) {
        int nrOfPages = nrOfEntries / perPage;
        if (nrOfEntries % perPage != 0)
            nrOfPages++;
        return nrOfPages;
    }

    public static int getStartNumber(int nrOfEntries, int perPage, int page) {
        int startNr = (page - 1) * perPage + 1;
        if (startNr == 1 && nrOfEntries == 0)
            return startNr;
        if (startNr <= 0 || startNr > nrOfEntries)
            throw new IllegalArgumentException("Page number too high or too low");
        return startNr;
    }

    public static int getEndNumber(int nrOfEntries, int perPage, int page) {
        int endNr = page * perPage;
        if (endNr > nrOfEntries + perPage || endNr <= 0)
            throw new IllegalArgumentException("Page number too high or too low");
        if (endNr > nrOfEntries)
            endNr = nrOfEntries;
        return endNr;
    }

    public static PageInfo getPageInfo(int nrOfEntries, int perPage, int page) {
        int nrOfPages = getNumberOfPages(nrOfEntries, perPage);
        if (nrOfPages == 0)
            nrOfPages++;
        if (page <= 0 || page > nrOfPages)
            throw new IllegalArgumentException(
                    String.format("Expected page number between 1 and %d. Got %d.", nrOfPages, page));
        int startNr = getStartNumber(nrOfEntries, perPage, page);
        int endNr = getEndNumber(nrOfEntries, perPage, page);
        return new PageInfo(page, startNr, endNr, nrOfPages);
    }

    public static class PageInfo {
        private final int page;
        private final int startNr;
        private final int endNr;
        private final int nrOfPages;

        public PageInfo(int page, int startNr, int endNr, int nrOfPages) {
            this.page = page;
            this.startNr = startNr;
            this.endNr = endNr;
            this.nrOfPages = nrOfPages;
        }

        public int getPage() {
            return page;
        }

        public int getStartNumber() {
            return startNr;
        }

        public int getEndNumber() {
            return endNr;
        }

        public int getNumberOfPages() {
            return nrOfPages;
        }

    }

}