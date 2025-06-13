package ru.qusli.whitelist;

public final class Whitelist {
    private final int[] _whitelist;

    public Whitelist(String whitelist) {
        _whitelist = this._prepareWhitelist(whitelist);
    }

    public boolean userInWhitelist(long userId) {
        for (int id : this._whitelist) {
            if (userId == id) {
                return true;
            }
        }

        return false;
    }

    private int[] _prepareWhitelist(String whitelist) {
        String[] _whitelist = whitelist.split(",");
        int[] result = new int[_whitelist.length];

        for (int i = 0; i < _whitelist.length; ++i) {
            if (!_whitelist[i].isEmpty()) {
                result[i] = Integer.parseInt(_whitelist[i]);
            }
        }

        return result;
    }
}
