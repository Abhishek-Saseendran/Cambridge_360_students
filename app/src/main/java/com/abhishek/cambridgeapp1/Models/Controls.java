package com.abhishek.cambridgeapp1.Models;

public class Controls {

    private boolean canEdit;
    private boolean enableFeedback;

    public Controls() {
    }

    public Controls(boolean canEdit, boolean enableFeedback) {
        this.canEdit = canEdit;
        this.enableFeedback = enableFeedback;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isEnableFeedback() {
        return enableFeedback;
    }

    public void setEnableFeedback(boolean enableFeedback) {
        this.enableFeedback = enableFeedback;
    }
}
