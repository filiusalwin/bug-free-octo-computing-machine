package nl.miwgroningen.se.ch3.bacchux.utils;

public class CurrencyFormatter {
    private static final int CENTS_PER_EURO = 100;

    public static String formatCurrency(Integer amount) {
        return String.format("€%.2f", (double) amount / CENTS_PER_EURO);
    }
}
