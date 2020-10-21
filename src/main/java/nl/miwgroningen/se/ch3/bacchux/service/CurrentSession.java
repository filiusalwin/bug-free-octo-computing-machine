package nl.miwgroningen.se.ch3.bacchux.service;

import org.springframework.stereotype.Component;

@Component
public class CurrentSession {

    private boolean lockscreenEnabled = false;

    private String previousUrl = "";

    public String getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(String previousUrl) {
        this.previousUrl = previousUrl;
    }

    public boolean isLockscreenEnabled() {
        return lockscreenEnabled;
    }

    public void setLockscreenEnabled(boolean lockscreenEnabled) {
        this.lockscreenEnabled = lockscreenEnabled;
    }
}
