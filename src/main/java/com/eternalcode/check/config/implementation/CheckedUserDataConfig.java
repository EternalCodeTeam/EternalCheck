package com.eternalcode.check.config.implementation;

import com.eternalcode.check.config.ReloadableConfig;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

public class CheckedUserDataConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "data" + File.separator + "checkedusers.data");
    }

    public int checkedUsers = 0;

    public int getCheckedUsers() {
        return this.checkedUsers;
    }

    public void addCheckedUser() {
        this.checkedUsers += 1;
    }
}
