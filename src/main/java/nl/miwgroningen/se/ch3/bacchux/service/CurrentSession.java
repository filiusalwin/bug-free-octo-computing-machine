package nl.miwgroningen.se.ch3.bacchux.service;

import org.springframework.stereotype.Component;

@Component
public class CurrentSession {

    private boolean lockscreenEnabled = false;

    public boolean isLockscreenEnabled() {
        return lockscreenEnabled;
    }

    public void setLockscreenEnabled(boolean lockscreenEnabled) {
        this.lockscreenEnabled = lockscreenEnabled;
    }
}
